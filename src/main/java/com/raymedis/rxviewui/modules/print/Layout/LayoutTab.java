package com.raymedis.rxviewui.modules.print.Layout;

import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintData;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintImageData;
import com.raymedis.rxviewui.modules.print.printPage.PrintPageHandler;
import com.raymedis.rxviewui.service.print.PrintService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LayoutTab {

    private String id;

    private GridPane gridPaneMain;
    private double gridPaneWidth;
    private double gridPaneHeight;

    private StackPane canvasStackPane;
    private GridPane imageDisplayGrid;
    private GridPane overlayGridPane;

    private Canvas drawingCanvas;
    private Pane textPane;
    private Pane canvasPane;



    /*private ViewBox imageviewbox;*/
    private Image displayImage;

    private VBox topLeftOverlayBox;
    private VBox topRightOverlayBox;

    private VBox bottomLeftOverlayBox;
    private VBox bottomRightOverlayBox;

    public PatientPrintImageData patientPrintImageData;

    private HandleLayoutImageProcessing handleLayoutImageProcessing;

    private GraphicsContext gc;


    private final Logger logger = LoggerFactory.getLogger(LayoutTab.class);


    public LayoutTab() {

    }

    public LayoutTab(double gridPaneWidth,double gridPaneHeight){
        this.gridPaneWidth=gridPaneWidth;
        this.gridPaneHeight=gridPaneHeight;

        //logger.info("w * h {} {}",gridPaneWidth,gridPaneHeight);

        init();
    }


    public void init() {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(gridPaneWidth, gridPaneHeight);
        gridPane.setMinSize(gridPaneWidth, gridPaneHeight);
        gridPane.setMaxSize(gridPaneWidth, gridPaneHeight);



        gridPane.setId("vBox");

        // Initialize the drawing canvas with appropriate size
        drawingCanvas = new Canvas(gridPaneWidth, gridPaneHeight);



        canvasStackPane = new StackPane();
        canvasStackPane.setPrefSize(gridPaneWidth, gridPaneHeight);
        canvasStackPane.setMinSize(gridPaneWidth, gridPaneHeight);
        canvasStackPane.setMaxSize(gridPaneWidth, gridPaneHeight);

        imageDisplayGrid = generateImageDisplayGrid();
        overlayGridPane = generateOverlayGridPane();
        overlayGridPane.setMouseTransparent(true);

        // Add the canvas to your stack pane
        canvasStackPane.getChildren().addAll(imageDisplayGrid, overlayGridPane);
        gridPane.getChildren().add(canvasStackPane);

        this.id = UUID.randomUUID().toString();
        this.gridPaneMain = gridPane;

        // Set up drag and drop handlers for the canvas
        canvasStackPane.addEventFilter(DragEvent.DRAG_OVER, this::handleDragOver);
        canvasStackPane.setOnDragDropped(this::handleDragDropped);



    }

    public void setupDragAndDrop(PatientPrintImageData patientPrintImageData) {
        canvasStackPane.setOnDragDetected(event -> {
            // Start drag and drop operation
            Dragboard db = canvasStackPane.startDragAndDrop(TransferMode.COPY);

            // Create clipboard content
            ClipboardContent content = new ClipboardContent();

            // Store a unique identifier for the bodyPartNode
            if(patientPrintImageData!=null){
                content.putString(patientPrintImageData.getImageSessionId());

                // Set visual representation during drag
                ImageView imageView = new ImageView(matToImage(patientPrintImageData.getImageMat()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                // Create scaled image for drag view
                WritableImage scaledImage = new WritableImage((int) imageView.getFitWidth(), (int) imageView.getFitHeight());
                imageView.snapshot(null, scaledImage);
                db.setDragView(scaledImage);

                // Store the bodyPartNode in a static map or application context
                PrintService.DragAndDropContext.setDraggedItem(patientPrintImageData);

                db.setContent(content);
            }

            event.consume();
        });
    }

    public void handleDragOver(DragEvent event) {
        // Accept drag over if we have a valid dragged item
        if (PrintService.DragAndDropContext.getDraggedItem() instanceof PatientPrintImageData) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    public void handleDragDropped(DragEvent event) {
        boolean success = false;

        Object draggedItem = PrintService.DragAndDropContext.getDraggedItem();

        if (draggedItem instanceof PatientPrintImageData patientPrintImageData) {
            LayoutTabHandler layoutTabHandler = PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler();
            layoutTabHandler.setCurrentPatientPrintImageData(getPatientPrintImageData());


            setPatientPrintImageData(patientPrintImageData);
            layoutTabHandler.reloadLayout(this);
            PrintService.getInstance().reloadDisableButtons();
           // PrintService.getInstance().setLayoutForPage(true,false);
            success = true;
        }


        PrintService.DragAndDropContext.clearDraggedItem();

        event.setDropCompleted(success);
        event.consume();
    }

    public PatientPrintImageData getPatientPrintImageData() {
        return patientPrintImageData;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    private Rect rect = new Rect();
    public double cropMultiple;
    public double scaleX = 1;
    public double scaleY = 1;
    public double orgScaleX=0;
    public double orgScaleY=0;
    public void setPatientPrintImageData(PatientPrintImageData patientPrintImageData) {

        this.patientPrintImageData = patientPrintImageData;

        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());

        this.topLeftOverlayBox.getChildren().clear();
        this.topRightOverlayBox.getChildren().clear();
        this.bottomLeftOverlayBox.getChildren().clear();
        this.bottomRightOverlayBox.getChildren().clear();


        if(patientPrintImageData!=null){
            this.handleLayoutImageProcessing = new HandleLayoutImageProcessing(this);
            patientPrintImageData.setId(this.id);
            setupDragAndDrop(this.patientPrintImageData);

            Mat org = patientPrintImageData.getImageMat();

            logger.info("org width {} height {}", org.width(), org.height());

            cropMultiple = (double) org.width() /patientPrintImageData.getEditedMat().width();

            rect.x = (int)(patientPrintImageData.getRectX()*cropMultiple);
            rect.y=(int)(patientPrintImageData.getRectY()*cropMultiple);
            rect.width = (int)(patientPrintImageData.getRectWidth()*cropMultiple);
            rect.height = (int)(patientPrintImageData.getRectHeight()*cropMultiple);

            if(rect.width> org.width()){
                rect.width = org.width();
            }
            if(rect.height> org.height()){
                rect.height = org.height();
            }


            logger.info("rect x {} y {} width {} height {}", rect.x, rect.y, rect.width, rect.height);
            logger.info("org width {} height {}", org.width(), org.height());


            Mat croppedMat = new Mat(org, rect);
            Mat croppedMatOld = new Mat(patientPrintImageData.getEditedMat(),new Rect((int)patientPrintImageData.getRectX(), (int)patientPrintImageData.getRectY(), (int)patientPrintImageData.getRectWidth(), (int)patientPrintImageData.getRectHeight()));


            double width = croppedMatOld.width();
            double height = croppedMatOld.height();

            if(width>=height){
                height = (1024*height)/width;
                width = 1024;
            }else{
                width = (1024*width)/height;
                height = 1024;
            }

            double imageToolMultiple = croppedMat.width()/width;

            applyExportValues(imageToolMultiple);


            double ratio = gridPaneWidth / gridPaneHeight;
            double targetRatio = (double) rect.width /rect.height;


            scaleX = 1;
            scaleY = 1;

            if (targetRatio < ratio) {
                scaleX = gridPaneHeight / rect.height;
                scaleY = gridPaneHeight / rect.height;
            } else if (targetRatio > ratio) {
                scaleX = gridPaneWidth / rect.width;
                scaleY = gridPaneWidth / rect.width;
            }
            else if (targetRatio == ratio) {
                scaleX = gridPaneWidth / rect.width;
                scaleY = gridPaneWidth / rect.width;
            }

            orgScaleX = scaleX;
            orgScaleY = scaleY;


             width = croppedMatOld.width()*scaleX;
             height = croppedMatOld.height()*scaleY;

            if(width>=height){
                height = (1024*height)/width;
                width = 1024;
            }else{
                width = (1024*width)/height;
                height = 1024;
            }

             imageToolMultiple = croppedMat.width()*scaleX/width;

            applyExportValues(imageToolMultiple);

            handleLayoutImageProcessing.getPrintMainDrawTool().setMainImageMat(org);

            handleLayoutImageProcessing.getPrintCanvasMainChange().setMainImage(ImageConversionService.getInstance().matToImage(org));
            handleLayoutImageProcessing.getPrintCanvasMainChange().setDisplayImage(ImageConversionService.getInstance().matToImage(org));

            handleLayoutImageProcessing.getPrintMainDrawTool().applyAllImageParams(patientPrintImageData);
            handleLayoutImageProcessing.getPrintCanvasMainChange().applyAllDrawingTools();

            double xOffset = ((drawingCanvas.getWidth()-handleLayoutImageProcessing.getPrintCanvasMainChange().displayImage.getWidth())/2);
            double yOffset = ((drawingCanvas.getHeight()-handleLayoutImageProcessing.getPrintCanvasMainChange().displayImage.getHeight())/2);

            patientPrintImageData.setImageLeft(xOffset);
            patientPrintImageData.setImageTop(yOffset);

            applyPrintOverLay();

            PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().setSelectLayout(this);

        }
    }


    public void applyExportValues(double multiple){
        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setMultiple(multiple);

        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setLineWidth(3 * multiple);
        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setEllipseRadiusX(10 * multiple);
        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setEllipseRadiusY(10 * multiple);
        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setLabelFontSize(14 * multiple);
        handleLayoutImageProcessing.getPrintDynamicCanvasElementsResize().setCustomFontIconSize(200*multiple);
    }

    public void applyImageFrameResize(double width, double height) {

        textPane.setPrefHeight(height);
        textPane.setPrefWidth(width);
        textPane.setMinHeight(height);
        textPane.setMinWidth(width);
        textPane.setMaxHeight(height);
        textPane.setMaxWidth(width);


        drawingCanvas.setHeight(height);
        drawingCanvas.setWidth(width);

        drawingCanvas.setLayoutX((gridPaneWidth-drawingCanvas.getWidth() ) / 2);
        drawingCanvas.setLayoutY((gridPaneHeight-drawingCanvas.getHeight() ) / 2);

    }

    public Image matToImage(Mat mat) {
        if (mat == null) {
            throw new IllegalArgumentException("Input Mat is null.");
        }

        if (mat.empty()) {
            throw new IllegalArgumentException("Input Mat is empty.");
        }


        // Convert Mat to Byte Array
        MatOfByte matOfByte = new MatOfByte();
        if (!Imgcodecs.imencode(".png", mat, matOfByte)) {
            throw new RuntimeException("Error encoding Mat to PNG.");
        }

        byte[] byteArray = matOfByte.toArray();

        // Convert Byte Array to Image
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return new Image(inputStream);
    }


    public List<Label> overlayLabels = new ArrayList<>();

    public void applyPrintOverLay() {
        this.topLeftOverlayBox.getChildren().clear();
        this.topRightOverlayBox.getChildren().clear();
        this.bottomLeftOverlayBox.getChildren().clear();
        this.bottomRightOverlayBox.getChildren().clear();
        this.overlayLabels.clear();

        PrintPageHandler printPageHandler = PrintPageHandler.getInstance();
        PatientPrintData patientPrintData = printPageHandler.getPatientPrintData();

        for(PrintOverlayEntity overlay : printPageHandler.getPrintOverlayEntityList()){
            String contentVal = "";

            switch (overlay.getItemContent().toLowerCase()) {
                case "patient id":
                    contentVal = patientPrintData.getPatientId();
                    break;
                case "patient name":
                    contentVal = patientPrintData.getPatientName();
                    break;
                case "birth date":
                    contentVal = patientPrintData.getBirthDate().toString();
                    break;
                case "sex":
                    contentVal = patientPrintData.getSex();
                    break;
                case "age":
                    contentVal = patientPrintData.getAge() + "";
                    break;
                case "gray value":
                    //contentVal = patientPrintData.getGrayValue() + "";
                    break;
                case "histogram":
                    //contentVal = patientPrintData.getHistogram();
                    break;
                case "dose kvp":
                    //contentVal = patientPrintData.getDoseKvp() + "";
                    break;
                case "dose ma":
                    //contentVal = patientPrintData.getDoseMa() + "";
                    break;
                case "dose ms":
                    //contentVal = patientPrintData.getDoseMs() + "";
                    break;
                case "dose mas":
                    // contentVal = patientPrintData.getDoseMas() + "";
                    break;
                case "exposure index":
                    // contentVal = patientPrintData.getExposureIndex() + "";
                    break;
                case "exposure indec(ugy)":
                    //contentVal = patientPrintData.getExposureIndecUgy() + "";
                    break;
                case "deviation index":
                    //contentVal = patientPrintData.getDeviationIndex() + "";
                    break;
                case "target exposure index":
                    //contentVal = patientPrintData.getTargetExposureIndex() + "";
                    break;
                case "target exposure index(ugy)":
                    // contentVal = patientPrintData.getTargetExposureIndexUgy() + "";
                    break;
                case "dap(dgy.cm^2)":
                    //contentVal = patientPrintData.getDapDgyCm2() + "";
                    break;
                case "dap(mgy.cm^2)":
                    // contentVal = patientPrintData.getDapMgyCm2() + "";
                    break;
                case "dap(ugy.m^2)":
                    //contentVal = patientPrintData.getDapUgyM2() + "";
                    break;
                case "exposure date":
                    contentVal = patientPrintImageData.getExposureDateTime().toString();
                    break;
                case "exposure time":
                    // contentVal = patientPrintData.getExposureTime().toString();
                    break;
                case "performing physcian":
                    contentVal = patientPrintData.getPerformingPhysician();
                    break;
                case "refering physcian":
                    contentVal = patientPrintData.getReferringPhysician();
                    break;
                case "series number - instance number":
                    //contentVal = patientPrintData.getSeriesNumber() + " - " + patientPrintData.getInstanceNumber();
                    break;
                case "bodypart projection":
                    contentVal = patientPrintImageData.getBodyPartName() + " " + patientPrintImageData.getBodyPartPosition();
                    break;
                case "study date", "study time":
                    contentVal = patientPrintData.getStudyDateTime()+"";
                    break;
                case "screen zoom":
                    //contentVal = patientPrintData.getScreenZoom() + "";
                    break;
                case "pixel zoom":
                    // contentVal = patientPrintData.getPixelZoom() + "";
                    break;
                case "accession number":
                    contentVal = patientPrintData.getAccessionNum();
                    break;
                case "laterality":
                    // contentVal = patientPrintData.getLaterality();
                    break;
                case "modality":
                    contentVal = patientPrintData.getModality();
                    break;
                case "height":
                    contentVal = patientPrintData.getHeight() + "";
                    break;
                case "weight":
                    contentVal = patientPrintData.getWeight() + "";
                    break;
                case "institutional name":
                    contentVal = printPageHandler.getSystemInfo().getInstitutionName();
                    break;
                case "institutional adress":
                    contentVal = printPageHandler.getSystemInfo().getInstitutionAddress();
                    break;
                case "institutional dept name":
                    contentVal = printPageHandler.getSystemInfo().getDepartment();
                    break;
                case "manufacturer":
                    contentVal = printPageHandler.getSystemInfo().getManufacturer();
                    break;
                case "manufacturer model name":
                    contentVal = printPageHandler.getSystemInfo().getModelName();
                    break;
                case "station name":
                    contentVal = printPageHandler.getSystemInfo().getSerialNumber();
                    break;
                case "other patient id":
                    //contentVal = patientPrintData.getOtherPatientId();
                    break;
                case "image orientation":
                    //contentVal = patientPrintData.getImageOrientation();
                    break;
                default:
                    logger.info("Invalid Value: {}", overlay.getItemContent().toLowerCase());
                    contentVal = "";
                    break;
            }

            if(contentVal.isEmpty() || contentVal.equals("0.0") || contentVal.equals("0")){
                continue;
            }

            String content = overlay.getItemContent() + " : " + contentVal;


            //double fontSize  = 32 * (gridPaneMain.getPrefWidth()/2048);

            double fontSize = 32;

            String fontFamily = "Century Gothic";

            Label label = new Label(content);
            label.setStyle(
                    "-fx-font-family: '" + fontFamily + "';" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: " + fontSize + "px;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 0;"
            );

            overlayLabels.add(label);


            switch (overlay.getPositionType()){
                case PositionType.TOP_LEFT:
                    this.topLeftOverlayBox.getChildren().add(label);
                    break;
                case PositionType.TOP_RIGHT:
                    this.topRightOverlayBox.getChildren().add(label);
                    break;
                case PositionType.BOTTOM_LEFT:
                    this.bottomLeftOverlayBox.getChildren().add(label);
                    break;
                case PositionType.BOTTOM_RIGHT:
                    this.bottomRightOverlayBox.getChildren().add(label);
                    break;
                default:
                    logger.info("");
            }
        }

    }

    public Image getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(Image displayImage) {
        this.displayImage = displayImage;
    }



    private GridPane generateImageDisplayGrid() {
        imageDisplayGrid = new GridPane();
        imageDisplayGrid.setPrefSize(gridPaneWidth, gridPaneHeight);
        imageDisplayGrid.setMinSize(gridPaneWidth, gridPaneHeight);
        imageDisplayGrid.setMaxSize(gridPaneWidth, gridPaneHeight);
        imageDisplayGrid.setAlignment(Pos.CENTER);

        canvasPane = new Pane();
        canvasPane.setPrefSize(gridPaneWidth, gridPaneHeight);
        canvasPane.setMinSize(gridPaneWidth, gridPaneHeight);
        canvasPane.setMaxSize(gridPaneWidth, gridPaneHeight);

        drawingCanvas.setWidth(gridPaneWidth);
        drawingCanvas.setHeight(gridPaneHeight);

        this.gc = drawingCanvas.getGraphicsContext2D();
        canvasPane.getChildren().add(drawingCanvas);

        textPane = new Pane();
        textPane.setPrefSize(gridPaneWidth, gridPaneHeight);
        textPane.setMinSize(gridPaneWidth, gridPaneHeight);
        textPane.setMaxSize(gridPaneWidth, gridPaneHeight);
        textPane.setMouseTransparent(true); // allow clicks to pass through textPane

        imageDisplayGrid.getChildren().addAll(canvasPane, textPane);

        return imageDisplayGrid;
    }


    private GridPane generateOverlayGridPane() {
        GridPane overlayGridPane = new GridPane();
        overlayGridPane.setAlignment(Pos.CENTER); // GridPane alignment

        ViewBox viewBox = new ViewBox();
        viewBox.setAlignment(Pos.CENTER); // ViewBox alignment

        // Set sizes using your existing variables
        viewBox.setPrefSize(gridPaneWidth, gridPaneHeight);
        viewBox.setMinSize(gridPaneWidth, gridPaneHeight);
        viewBox.setMaxSize(gridPaneWidth, gridPaneHeight);

        VBox vBox = new VBox();
        vBox.setPrefSize(gridPaneWidth, gridPaneHeight);
        vBox.setMinSize(gridPaneWidth, gridPaneHeight);
        vBox.setMaxSize(gridPaneWidth, gridPaneHeight);

        // Top HBox
        HBox topHBox = new HBox();
        topHBox.setPrefSize(gridPaneWidth, gridPaneHeight/2);

        // Top-Left VBox
        topLeftOverlayBox = new VBox();
        topLeftOverlayBox.setPadding(new Insets(15, 0, 0, 15)); // Left & Top padding
        topLeftOverlayBox.setPrefSize(gridPaneWidth/2, gridPaneHeight/2);

        // Top-Right VBox
        topRightOverlayBox = new VBox();
        topRightOverlayBox.setAlignment(Pos.TOP_RIGHT);
        topRightOverlayBox.setPadding(new Insets(15, 15, 0, 0)); // Right & Top padding
        topRightOverlayBox.setPrefSize(gridPaneWidth/2, gridPaneHeight/2);

        topHBox.getChildren().addAll(topLeftOverlayBox, topRightOverlayBox);

        // Bottom HBox
        HBox bottomHBox = new HBox();
        bottomHBox.setPrefSize(gridPaneWidth, gridPaneHeight/2);

        // Bottom-Left VBox
        bottomLeftOverlayBox = new VBox();
        bottomLeftOverlayBox.setAlignment(Pos.BOTTOM_LEFT);
        bottomLeftOverlayBox.setPadding(new Insets(0, 0, 15, 15)); // Left & Bottom padding
        bottomLeftOverlayBox.setPrefSize(gridPaneWidth/2, gridPaneHeight/2);

        // Bottom-Right VBox
        bottomRightOverlayBox = new VBox();
        bottomRightOverlayBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightOverlayBox.setPadding(new Insets(0, 15, 15, 0)); // Right & Bottom padding
        bottomRightOverlayBox.setPrefSize(gridPaneWidth/2, gridPaneHeight/2);

        bottomHBox.getChildren().addAll(bottomLeftOverlayBox, bottomRightOverlayBox);

        vBox.getChildren().addAll(topHBox, bottomHBox);
        viewBox.getChildren().add(vBox);
        overlayGridPane.getChildren().add(viewBox);

        return overlayGridPane;
    }

    public LayoutTab(String id, GridPane gridPaneMain) {
        this.id = id;
        this.gridPaneMain = gridPaneMain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GridPane getGridPaneMain() {
        return gridPaneMain;
    }

    public void setGridPaneMain(GridPane gridPaneMain) {
        this.gridPaneMain = gridPaneMain;
    }

    public double getGridPaneWidth() {
        return gridPaneWidth;
    }

    public void setGridPaneWidth(double gridPaneWidth) {
        this.gridPaneWidth = gridPaneWidth;
    }

    public double getGridPaneHeight() {
        return gridPaneHeight;
    }

    public void setGridPaneHeight(double gridPaneHeight) {
        this.gridPaneHeight = gridPaneHeight;
    }

    public StackPane getCanvasStackPane() {
        return canvasStackPane;
    }

    public void setCanvasStackPane(StackPane canvasStackPane) {
        this.canvasStackPane = canvasStackPane;
    }

    public GridPane getImageDisplayGrid() {
        return imageDisplayGrid;
    }

    public void setImageDisplayGrid(GridPane imageDisplayGrid) {
        this.imageDisplayGrid = imageDisplayGrid;
    }

    public GridPane getOverlayGridPane() {
        return overlayGridPane;
    }

    public void setOverlayGridPane(GridPane overlayGridPane) {
        this.overlayGridPane = overlayGridPane;
    }

    public Canvas getDrawingCanvas() {
        return drawingCanvas;
    }

    public void setDrawingCanvas(Canvas drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
        this.gc = drawingCanvas.getGraphicsContext2D();
    }

    public Pane getTextPane() {
        return textPane;
    }

    public void setTextPane(Pane textPane) {
        this.textPane = textPane;
    }

    public VBox getTopLeftOverlayBox() {
        return topLeftOverlayBox;
    }

    public void setTopLeftOverlayBox(VBox topLeftOverlayBox) {
        this.topLeftOverlayBox = topLeftOverlayBox;
    }

    public VBox getTopRightOverlayBox() {
        return topRightOverlayBox;
    }

    public void setTopRightOverlayBox(VBox topRightOverlayBox) {
        this.topRightOverlayBox = topRightOverlayBox;
    }

    public VBox getBottomLeftOverlayBox() {
        return bottomLeftOverlayBox;
    }

    public void setBottomLeftOverlayBox(VBox bottomLeftOverlayBox) {
        this.bottomLeftOverlayBox = bottomLeftOverlayBox;
    }

    public VBox getBottomRightOverlayBox() {
        return bottomRightOverlayBox;
    }

    public void setBottomRightOverlayBox(VBox bottomRightOverlayBox) {
        this.bottomRightOverlayBox = bottomRightOverlayBox;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public HandleLayoutImageProcessing getHandleLayoutImageProcessing() {
        return handleLayoutImageProcessing;
    }

    public void setHandleLayoutImageProcessing(HandleLayoutImageProcessing handleLayoutImageProcessing) {
        this.handleLayoutImageProcessing = handleLayoutImageProcessing;
    }


    public Pane getCanvasPane() {
        return canvasPane;
    }

    public void setCanvasPane(Pane canvasPane) {
        this.canvasPane = canvasPane;
    }

 /*   public ViewBox getImageviewbox() {
        return imageviewbox;
    }

    public void setImageviewbox(ViewBox imageviewbox) {
        this.imageviewbox = imageviewbox;
    }*/

}
