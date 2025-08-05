package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;


import com.raymedis.rxviewui.modules.print.Layout.LayoutTab;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintDynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintImageTools;
import com.raymedis.rxviewui.modules.print.printInput.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintCanvasMainChange {

    public boolean isAngleDrawComplete = true;
    public boolean isDistanceDrawComplete = true;
    public boolean isTextDrawComplete = true;
    public boolean isEllipseDrawComplete = true;
    public boolean isCropLayoutComplete = true;

    public PrintMainDrawTool printMainDrawTool;

    private final Logger logger = LoggerFactory.getLogger(PrintCanvasMainChange.class);
    public PrintDynamicCanvasElementsResize printDynamicCanvasElementsResize;

    private Canvas drawingCanvas;

    private Pane overlayPane;

    public ObservableList<Ellipse> ellipses = FXCollections.observableArrayList();

    public ObservableList<Line> lines = FXCollections.observableArrayList();

    public ObservableList<Rectangle> rectangles = FXCollections.observableArrayList();

    public ObservableList<Ellipse> ellipseDrawEllipses = FXCollections.observableArrayList();

    public ObservableList<PrintDegreeLabel> degreeLabels = FXCollections.observableArrayList();

    public ObservableList<PrintDistanceLabel> distanceLabels = FXCollections.observableArrayList();

    public ObservableList<PrintTextLabel> textLabels = FXCollections.observableArrayList();

    public PrintCustomFontIcon customFontIcon;

    public Image mainImage = null;

    public Image displayImage = null;


    private ImageConversionService imageConversionService = new ImageConversionService();

    private ImageProcessService imageProcessService = new ImageProcessService();



    public void clearCanvas(){
        ellipses.clear();
        lines.clear();
        rectangles.clear();
        ellipseDrawEllipses.clear();
        degreeLabels.clear();
        distanceLabels.clear();
        textLabels.clear();
        customFontIcon = null;

        mainImage = null;
        displayImage = null;

        redraw();
    }



    public void init(Pane textPane){
        overlayPane = textPane;
        setTextMouse();
    }

    public void setTextMouse(){
        overlayPane.setMouseTransparent(true);
        //System.out.println("Mouse set true");
    }

    public void removeTextMouse(){
        overlayPane.setMouseTransparent(false);
        //System.out.println("Mouse set false");
    }

    public Mat croppedMat;
    public void redraw() {

        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());

        gc.setFill(Color.RED);
        gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());


        if(displayImage!=null) {
            croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(displayImage), layoutTab.getRect());
            Imgproc.resize(croppedMat, croppedMat, new Size(layoutTab.getRect().width*layoutTab.scaleX, layoutTab.getRect().height*layoutTab.scaleY), 0, 0, Imgproc.INTER_AREA);
            gc.drawImage(ImageConversionService.getInstance().matToImage(croppedMat), printMainDrawTool.getImageLeft(), printMainDrawTool.getImageTop());
        }

        for (Line line : lines) {
            gc.setLineDashes();
            gc.setStroke(Color.ORANGERED);
            gc.setLineWidth(printDynamicCanvasElementsResize.getLineWidth());  // Set line thickness here
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        }

        for (Ellipse ellipse : ellipses) {
            gc.setFill(Color.ORANGERED);
            gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(),
                    ellipse.getCenterY() - ellipse.getRadiusY(),
                    ellipse.getRadiusX() * 2,
                    ellipse.getRadiusY() * 2);
        }

        for (PrintDegreeLabel label : degreeLabels) {
            double angleDeg = label.getAngle();
            double px = label.getX();
            double py = label.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        for (PrintDistanceLabel label : distanceLabels) {
            double distanceInmm = label.getDistance();
            double px = label.getX();
            double py = label.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f mm", distanceInmm), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f mm", distanceInmm), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        for(PrintTextLabel textLabel : textLabels){
            String text = textLabel.getText();
            double px = textLabel.getX();
            double py = textLabel.getY();

            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(text, px, py+ 4*printDynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(text, px, py+ 4*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        if(customFontIcon != null){
            gc.drawImage(customFontIcon.getCurrentImage(), customFontIcon.getX(), customFontIcon.getY());
        }

    }

    public void redrawPrintAnnotations() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());


        if(displayImage!=null) {
            croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(displayImage), layoutTab.getRect());

            Imgproc.resize(croppedMat, croppedMat, new Size(layoutTab.getRect().width*layoutTab.scaleX, layoutTab.getRect().height*layoutTab.scaleY), 0, 0, Imgproc.INTER_AREA);
            gc.drawImage(ImageConversionService.getInstance().matToImage(croppedMat), printMainDrawTool.getImageLeft(), printMainDrawTool.getImageTop());
        }

        for (Line line : lines) {
            gc.setLineDashes();
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(printDynamicCanvasElementsResize.getLineWidth());  // Set line thickness here
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        }

        for (Ellipse ellipse : ellipses) {
            gc.setFill(Color.WHITE);
            gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(),
                    ellipse.getCenterY() - ellipse.getRadiusY(),
                    ellipse.getRadiusX() * 2,
                    ellipse.getRadiusY() * 2);
        }

        for (PrintDegreeLabel label : degreeLabels) {
            double angleDeg = label.getAngle();
            double px = label.getX();
            double py = label.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        for (PrintDistanceLabel label : distanceLabels) {
            double distanceInmm = label.getDistance();
            double px = label.getX();
            double py = label.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f mm", distanceInmm), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f mm", distanceInmm), px, py+ 2*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        for(PrintTextLabel textLabel : textLabels){
            String text = textLabel.getText();
            double px = textLabel.getX();
            double py = textLabel.getY();

            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(text, px, py+ 4*printDynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(text, px, py+ 4*printDynamicCanvasElementsResize.getLabelFontSize());
        }

        if(customFontIcon != null){
            gc.drawImage(customFontIcon.getCurrentImage(), customFontIcon.getX(), customFontIcon.getY());
        }





    }


    public void disableImageTools(){
        printMainDrawTool.isImageToolEnable = false;
        clearCanvas();

        printMainDrawTool.reset();
        printMainDrawTool.isHFlip = false;
        printMainDrawTool.isVFlip = false;
        printMainDrawTool.isInvert = false;

        printMainDrawTool.brightnessBefore = 0;
        printMainDrawTool.contrastBefore = 1;
        printMainDrawTool.imageLeftBefore = 0;
        printMainDrawTool.imageTopBefore=0;
        printMainDrawTool.zoomFactorBefore = 1;

        printMainDrawTool.brightness = 0;
        printMainDrawTool.contrast = 1;
        printMainDrawTool.zoomFactor = 1;
        printMainDrawTool.angleFactor = 0;

        printMainDrawTool.imageLeft = 0;
        printMainDrawTool.imageTop = 0;

       // printMainDrawTool.canvasViewBox.setRotate(0);

        PrintImageTools.getInstance().resetUi();
    }


    private LayoutTab layoutTab;

    public LayoutTab getLayoutTab() {
        return layoutTab;
    }

    public void setLayoutTab(LayoutTab layoutTab) {
        this.layoutTab = layoutTab;
        this.drawingCanvas = layoutTab.getDrawingCanvas();
        this.overlayPane=layoutTab.getOverlayGridPane();
    }


    public void addEllipse(Ellipse ellipse){
        if(!ellipses.contains(ellipse)){
            ellipses.add(ellipse);
        }

    }

    public void addLine(Line line){
        lines.add(line);
    }

    public void addRectangle(Rectangle rectangle){
        if(!rectangles.contains(rectangle)){
            rectangles.add(rectangle);
        }

    }

    public void addEllipseDraw(Ellipse ellipse) {
        if(!ellipseDrawEllipses.contains(ellipse)){
            ellipseDrawEllipses.add(ellipse);
        }
    }


    public void addDegreeLabel(PrintDegreeLabel degreeLabel){
        degreeLabels.add(degreeLabel);
    }

    public void addDistanceLabel(PrintDistanceLabel distanceLabel){
        distanceLabels.add(distanceLabel);
    }

    public void addTextLabel(PrintTextLabel textLabel){
        textLabels.add(textLabel);
    }

    public Ellipse isEllipse(Point2D point){
        for (Ellipse ellipse : ellipses) {
            if (isPointInEllipse(point, ellipse)) {
                return ellipse;
            }
        }
        return null;
    }

    public Ellipse isEllipseDraw(Point2D point) {
        for (Ellipse ellipse : ellipseDrawEllipses) {
            if (isPointInEllipse(point, ellipse)) {
                return ellipse;
            }
        }

        return null;
    }

    private boolean isPointInEllipse(Point2D point, Ellipse ellipse) {
        if(ellipse==null){
            return false;
        }
        double dx = point.getX() - ellipse.getCenterX();
        double dy = point.getY() - ellipse.getCenterY();
        return (dx * dx) / (ellipse.getRadiusX() * ellipse.getRadiusX()) +
                (dy * dy) / (ellipse.getRadiusY() * ellipse.getRadiusY()) <= 1;
    }

    public void removeAllEllipse(ObservableList<Ellipse> theEllipses){
        ellipses.removeAll(theEllipses);
    }

    public void removeAllLines(ObservableList<Line> theLines){
        lines.removeAll(theLines);
    }

    public void removeLine(Line theLines){
        lines.remove(theLines);
    }

    public void removeEllipse(Ellipse ellipse){
        ellipses.remove(ellipse);
    }

    public void removeRectangle(Rectangle rectangle){
        rectangles.remove(rectangle);
    }

    public void removeEllipseDraw(Ellipse ellipse) {
        ellipseDrawEllipses.remove(ellipse);
    }

    public void removeDegreeLabel(PrintDegreeLabel degreeLabel){
        degreeLabels.remove(degreeLabel);
    }

    public void removeDistanceLabel(PrintDistanceLabel distanceLabel){
        distanceLabels.remove(distanceLabel);
    }

    public void removeTextLabel(PrintTextLabel textLabel){
        textLabels.remove(textLabel);
    }

    public Rectangle isRectangle(Point2D clickPoint) {
        for (Rectangle rectangle : rectangles) {
            if(rectangle!=null && clickPoint!=null){
                if (rectangle.contains(clickPoint)) {
                    return rectangle;
                }
            }else {
                rectangles.remove(rectangle);
            }
        }
        return null;
    }

    public Line isLine(Point2D clickPoint) {
        for (Line line : lines) {
            if (line.contains(clickPoint)) {
                return line;
            }
        }

        return null;
    }

    public PrintTextLabel isTextLabel(Point2D clickPoint) {
        for (PrintTextLabel textLabels : textLabels) {
            if (textLabels.contains(clickPoint.getX(), clickPoint.getY())) {
                return textLabels;
            }
        }

        return null;
    }

    public PrintCustomFontIcon isCustomFontIcon(Point2D clickPoint) {
        if (customFontIcon != null && customFontIcon.contains(clickPoint.getX(), clickPoint.getY())) {
            return customFontIcon;
        }

        return null;
    }

    public void removeCustomFontIcon() {
        customFontIcon = null;
    }

    public PrintMainDrawTool getPrintMainDrawTool() {
        return printMainDrawTool;
    }

    public void setPrintMainDrawTool(PrintMainDrawTool printMainDrawTool) {
        this.printMainDrawTool = printMainDrawTool;
    }

    public Canvas getDrawingCanvas() {
        return drawingCanvas;
    }

    public void setDrawingCanvas(Canvas drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
    }

    public Pane getOverlayPane() {
        return overlayPane;
    }

    public void setOverlayPane(Pane overlayPane) {
        this.overlayPane = overlayPane;
    }

    public ObservableList<Ellipse> getEllipses() {
        return ellipses;
    }

    public void setEllipses(ObservableList<Ellipse> ellipses) {
        this.ellipses = ellipses;
    }

    public ObservableList<Line> getLines() {
        return lines;
    }

    public void setLines(ObservableList<Line> lines) {
        this.lines = lines;
    }

    public ObservableList<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(ObservableList<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public ObservableList<Ellipse> getEllipseDrawEllipses() {
        return ellipseDrawEllipses;
    }

    public void setEllipseDrawEllipses(ObservableList<Ellipse> ellipseDrawEllipses) {
        this.ellipseDrawEllipses = ellipseDrawEllipses;
    }

    public ObservableList<PrintDegreeLabel> getDegreeLabels() {
        return degreeLabels;
    }

    public void setDegreeLabels(ObservableList<PrintDegreeLabel> degreeLabels) {
        this.degreeLabels = degreeLabels;
    }

    public ObservableList<PrintDistanceLabel> getDistanceLabels() {
        return distanceLabels;
    }

    public void setDistanceLabels(ObservableList<PrintDistanceLabel> distanceLabels) {
        this.distanceLabels = distanceLabels;
    }

    public ObservableList<PrintTextLabel> getTextLabels() {
        return textLabels;
    }

    public void setTextLabels(ObservableList<PrintTextLabel> textLabels) {
        this.textLabels = textLabels;
    }

    public PrintCustomFontIcon getCustomFontIcon() {
        return customFontIcon;
    }

    public void setCustomFontIcon(PrintCustomFontIcon customFontIcon) {
        this.customFontIcon = customFontIcon;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    public Image getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(Image displayImage) {
        this.displayImage = displayImage;
    }

    public ImageConversionService getImageConversionService() {
        return imageConversionService;
    }

    public void setImageConversionService(ImageConversionService imageConversionService) {
        this.imageConversionService = imageConversionService;
    }

    public ImageProcessService getImageProcessService() {
        return imageProcessService;
    }

    public void setImageProcessService(ImageProcessService imageProcessService) {
        this.imageProcessService = imageProcessService;
    }



    public void applyAllDrawingTools(){
        PatientPrintImageData patientPrintImageData = layoutTab.getPatientPrintImageData();

        //logger.info("printDynamicCanvasElementsResize.getMultiple() {}",printDynamicCanvasElementsResize.getMultiple());

        if(patientPrintImageData.getPatientPrintAngleDrawWrappers()!=null){
            for(PatientPrintAngleDrawWrapper angleDrawWrapper : patientPrintImageData.getPatientPrintAngleDrawWrappers()){
                PrintAngleDraw angle = new PrintAngleDraw();
                angle.setCanvas(drawingCanvas);
                angle.setGc(drawingCanvas.getGraphicsContext2D());
                angle.setClickCount(3);
                angle.setSelectedEllipse(null);

                ObservableList<Ellipse> ellipseList = FXCollections.observableArrayList();
                ObservableList<Line> lineList = FXCollections.observableArrayList();

                isAngleDrawComplete=true;

                for (PatientPrintEllipseWrapper ellipseWrapper : angleDrawWrapper.getEllipsesWrapperList()){
                    Ellipse ellipse = new Ellipse(printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getCenterX(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getCenterY(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getRadiusX(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getRadiusY());
                    ellipse.setFill(Color.BLUE);
                    ellipses.add(ellipse);
                    ellipseList.add(ellipse);
                }

                for (PatientPrintLineWrapper lineWrapper : angleDrawWrapper.getLineWrapperList()){
                    Line line = new Line(printDynamicCanvasElementsResize.getMultiple()*lineWrapper.getStartX(),printDynamicCanvasElementsResize.getMultiple()*lineWrapper.getStartY(),printDynamicCanvasElementsResize.getMultiple()*lineWrapper.getEndX(),printDynamicCanvasElementsResize.getMultiple()*lineWrapper.getEndY());
                    lines.add(line);
                    lineList.add(line);
                }

                PatientPrintLabelWrapper labelWrapper = angleDrawWrapper.getLabelWrapper();
                PrintDegreeLabel degreeLabel = new PrintDegreeLabel(labelWrapper.getValue(), (printDynamicCanvasElementsResize.getMultiple()*labelWrapper.getX()), (printDynamicCanvasElementsResize.getMultiple()*labelWrapper.getY()), Color.rgb(255, 235, 42));
                addDegreeLabel(degreeLabel);

                angle.setEllipses(ellipseList);
                angle.setLines(lineList);
                angle.setDegreeLabel(degreeLabel);

                printMainDrawTool.angleDrawList.add(angle);
                printMainDrawTool.allDrawTools.add(angle);

            }
        }

        if(patientPrintImageData.getPatientPrintDistanceDrawWrappers()!=null){
            for(PatientPrintDistanceDrawWrapper distanceDrawWrapper : patientPrintImageData.getPatientPrintDistanceDrawWrappers()){

                PrintDistanceDraw distanceDraw = new PrintDistanceDraw();
                distanceDraw.setCanvas(drawingCanvas);
                distanceDraw.setGc(drawingCanvas.getGraphicsContext2D());
                distanceDraw.setClickCount(2);
                distanceDraw.setSelectedEllipse(null);

                ObservableList<Ellipse> ellipseList = FXCollections.observableArrayList();

                isDistanceDrawComplete=true;


                for (PatientPrintEllipseWrapper ellipseWrapper : distanceDrawWrapper.getEllipses()){
                    Ellipse ellipse = new Ellipse(printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getCenterX(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getCenterY(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getRadiusX(),printDynamicCanvasElementsResize.getMultiple()*ellipseWrapper.getRadiusY());
                    ellipse.setFill(Color.BLUE);
                    ellipses.add(ellipse);
                    ellipseList.add(ellipse);
                }

                Line line = new Line(printDynamicCanvasElementsResize.getMultiple()*distanceDrawWrapper.getLines().getStartX(),printDynamicCanvasElementsResize.getMultiple()*distanceDrawWrapper.getLines().getStartY(),printDynamicCanvasElementsResize.getMultiple()*distanceDrawWrapper.getLines().getEndX(),printDynamicCanvasElementsResize.getMultiple()*distanceDrawWrapper.getLines().getEndY());
                lines.add(line);

                PatientPrintLabelWrapper labelWrapper = distanceDrawWrapper.getLabelWrapper();
                PrintDistanceLabel distanceLabel = new PrintDistanceLabel(labelWrapper.getValue(), printDynamicCanvasElementsResize.getMultiple()*labelWrapper.getX(), printDynamicCanvasElementsResize.getMultiple()*labelWrapper.getY(), Color.rgb(255, 235, 42));
                addDistanceLabel(distanceLabel);

                distanceDraw.setEllipses(ellipseList);
                distanceDraw.setLine(line);
                distanceDraw.setPrintDistanceLabel(distanceLabel);

                printMainDrawTool.distanceDrawList.add(distanceDraw);
                printMainDrawTool.allDrawTools.add(distanceDraw);
            }
        }

        if(patientPrintImageData.getText()!=null){
            printMainDrawTool.leftRightDraw = new PrintLeftRightDraw(patientPrintImageData.getText(),drawingCanvas,this);
            printMainDrawTool.leftRightDraw.getRlIcon().setX(printDynamicCanvasElementsResize.getMultiple()*patientPrintImageData.getOffsetX());
            printMainDrawTool.leftRightDraw.getRlIcon().setY(printDynamicCanvasElementsResize.getMultiple()*patientPrintImageData.getOffsetY());
            printMainDrawTool.leftRightDraw.redraw();
        }

        if(patientPrintImageData.getPatientPrintTextDrawWrappers()!=null){
            for(PatientPrintTextDrawWrapper textDrawWrapper:patientPrintImageData.getPatientPrintTextDrawWrappers()){
                PrintTextDraw textDraw = new PrintTextDraw();
                textDraw.setPrintCanvasMainChange(this);
                textDraw.setRoot(overlayPane);
                textDraw.setCanvas(drawingCanvas);
                PrintTextLabel textLabel = new PrintTextLabel(textDrawWrapper.getText(), (textDrawWrapper.getX()),
                        (textDrawWrapper.getY()), Color.valueOf("#66FF00"),printDynamicCanvasElementsResize);
                textDraw.setTextLabel(textLabel);

                textDraw.drawTexts();

                printMainDrawTool.textDrawList.add(textDraw);
                printMainDrawTool.allDrawTools.add(textDraw);
                textLabels.add(textLabel);
            }
        }


        //layoutTab.applyImageFrameResize(layoutTab.getRect().width,layoutTab.getRect().height);
        layoutTab.applyImageFrameResize(layoutTab.getRect().width*layoutTab.scaleX, layoutTab.getRect().height*layoutTab.scaleY);

        redraw();



    }


}