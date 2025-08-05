package com.raymedis.rxviewui.service.print;

import com.dicomprintmodule.DicomCommunication;
import com.dicomprintmodule.DicomPrintManager;
import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.dicom.*;
import com.raymedis.rxviewui.modules.print.Layout.LayoutCreator;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTab;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintImageTools;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintData;
import com.raymedis.rxviewui.modules.print.printInput.PatientPrintImageData;
import com.raymedis.rxviewui.modules.print.printMat.PrintImageCordData;
import com.raymedis.rxviewui.modules.print.printMat.PrintPageToImageCordData;
import com.raymedis.rxviewui.modules.print.printMat.PrintPageToMat;
import com.raymedis.rxviewui.modules.print.printPage.PrintPage;
import com.raymedis.rxviewui.modules.print.printPage.PrintPageHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class PrintService {

    private final static PrintService instance = new PrintService();
    public static PrintService getInstance(){
        return  instance;
    }


    private double width;
    private double height;

    public final static PrintPageHandler printPageHandler = PrintPageHandler.getInstance();
    public final LinkedHashMap<String,Button> bodyPartMap = new LinkedHashMap<>();
    public final LinkedHashMap<String ,JFXButton> pageMap = new LinkedHashMap<>();

    private DicomStorageEntity dicomStorageEntity;
    private SystemInfoEntity systemInfoEntity;
    private final DicomStorageService dicomStorageService = DicomStorageService.getInstance();
    private final SystemInfoService systemInfoService = SystemInfoService.getInstance();
    private final DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();
    private String stationAeTitle = "";

    public void loadData() throws SQLException {

        dicomStorageEntity =dicomStorageService.findSelected();
        ArrayList<SystemInfoEntity> systemInfoEntityList = systemInfoService.getSystemInfo();

        ArrayList<DicomGeneralEntity> dicomGeneralEntityArrayList = dicomGeneralService.findAll();
        if(dicomGeneralEntityArrayList.size()>1){
            stationAeTitle = dicomGeneralEntityArrayList.getFirst().getStationAETitle();
            logger.info("stationAeTitle : {}",stationAeTitle);
        }


        if(systemInfoEntityList.isEmpty()){
            logger.info("studyEntity is null");
        }else{
            systemInfoEntity = systemInfoEntityList.getFirst();
        }
    }

    public final LinkedHashMap<String,Button> disabledButtonsMap = new LinkedHashMap<>();

    private PatientPrintData currentPatientPrintData;

    private final Logger logger = LoggerFactory.getLogger(PrintService.class);

    public void landScapeClick(){

         width = PrintController.getInstance().imageContainerViewBox.getPrefWidth();
         height = PrintController.getInstance().imageContainerViewBox.getPrefHeight();

        //logger.info("old width * old height : {} * {}",width,height);

         if(height>width){
             PrintController.getInstance().imageContainerViewBox.setPrefSize(height,width);
             PrintController.getInstance().imageContainerViewBox.setMaxSize(height,width);
             PrintController.getInstance().imageContainerViewBox.setMinSize(height,width);
         }

         setLayoutForPage(true,false);
    }

    public void portraitClick() {

        width = PrintController.getInstance().imageContainerViewBox.getPrefWidth();
        height = PrintController.getInstance().imageContainerViewBox.getPrefHeight();

       //logger.info("old width * old height : {} * {}",width,height);

        if(width>height){
            PrintController.getInstance().imageContainerViewBox.setPrefSize(height,width);
            PrintController.getInstance().imageContainerViewBox.setMaxSize(height,width);
            PrintController.getInstance().imageContainerViewBox.setMinSize(height,width);
        }


       setLayoutForPage(true,false);

    }



    public void loadStudyData(PatientPrintData patientPrintData){

        //patientPrintData.getPatientPrintImageDataList().forEach(System.out::println);

        if(patientPrintData.getPatientPrintImageDataList() == null){
            bodyPartMap.clear();
            return;
        }


        this.currentPatientPrintData = patientPrintData;
        printPageHandler.setPatientPrintData(patientPrintData);

        PrintController.getInstance().bodyPartsContainer.getChildren().clear();
        PrintController.getInstance().pageContainer.getChildren().clear();

        disabledButtonsMap.clear();
        pageMap.clear();
        for(PrintPage p : printPageHandler.getAllTabs()){
            printPageHandler.deletePage(p.getId());
        }

        createPrintPage();


        bodyPartMap.clear();
        //List<TabNode> allTabs = patientPrintData.getBodyPartHandler().getAllTabs();
        List<PatientPrintImageData> allTabs = patientPrintData.getPatientPrintImageDataList();

        for (PatientPrintImageData patientPrintImageData : allTabs) {
            if(patientPrintImageData.isExposed()){
                bodyPartMap.put(
                        patientPrintImageData.getImageSessionId(),
                        createBodyPartTabButton(patientPrintImageData)
                );
            }
        }


        if(!bodyPartMap.isEmpty()){
            createBodyPartTabs();
        }

    }

    public void createPrintPage() {
        PrintPage printPage = printPageHandler.createPage();

        if(printPageHandler.getCurrentPrintPage() != null) {
            printPageHandler.getCurrentPrintPage().getLayoutTabHandler().saveAllLayouts();
        }

        printPageHandler.setCurrentPrintPage(printPage);

        JFXButton pageTabButton = createPageTabButton(printPage);
        pageMap.put(printPage.getId(),pageTabButton);

        createPrintPagesUi();
        PrintImageTools.getInstance().resetUi();
        PrintImageTools.getInstance().disableTools(true);
    }

    public void createPrintPagesUi(){
        PrintController.getInstance().pageContainer.getChildren().clear();

        for (JFXButton b : pageMap.values()){
            PrintController.getInstance().pageContainer.getChildren().add(b);
        }

        PrintPage currentPage = printPageHandler.getCurrentPrintPage();
        handlePageTabButtonAction(pageMap.get(currentPage.getId()),currentPage);
    }

    private JFXButton createPageTabButton(PrintPage printPage){
        // Button creation code remains the same
        JFXButton selectedBodyPartButton = new JFXButton();
        selectedBodyPartButton.setId("unSelectedPageLayoutButton");
        selectedBodyPartButton.setContentDisplay(ContentDisplay.TOP);
        String pageText = "Page_"  + (pageMap.size()+1);
        //System.out.println(pageText);
        selectedBodyPartButton.setText(pageText);
        selectedBodyPartButton.setAlignment(Pos.CENTER);
        selectedBodyPartButton.setPrefSize(120, 120);
        selectedBodyPartButton.setMinSize(120, 120);
        selectedBodyPartButton.setMaxSize(120, 120);

        VBox vBox = new VBox();
        vBox.setMinSize(90,90);
        vBox.setPrefSize(90,90);
        vBox.setMaxSize(90,90);

        vBox.setId("pageVbox");
        selectedBodyPartButton.setGraphic(vBox);

        // Set action using separate method
        selectedBodyPartButton.setOnAction(event -> {
            //System.out.println(PrintController.getInstance().layoutCode);
            PrintPage currentPrintPage = printPageHandler.getCurrentPrintPage();
            currentPrintPage.setLayoutCode(PrintController.getInstance().layoutCode);
            handlePageTabButtonAction(selectedBodyPartButton,printPage);
        });
        return selectedBodyPartButton;
    }

    private void handlePageTabButtonAction(JFXButton button,PrintPage printPage) {
        for (JFXButton b:pageMap.values()){
            if(b!=button){
                b.setId("unSelectedPageLayoutButton");
            }else{
                b.setId("selectedPageLayoutButton");
            }
        }


        printPageHandler.setCurrentPage(printPage.getId());
        PrintController.getInstance().layoutCode=printPage.getLayoutCode();

        PrintController.getInstance().handleLayoutClick(PrintController.getInstance().layoutsButtonMap.get(PrintController.getInstance().layoutCode));
        setLayoutForPage(true,false);

    }

    public void deletePageClick() {
        if(printPageHandler.getCurrentPrintPage()!=null){
            String currentPageId = printPageHandler.getCurrentPrintPage().getId();

            printPageHandler.deletePage(currentPageId);
            pageMap.remove(currentPageId);

            reloadDisableButtons();
        }

        if (pageMap.isEmpty()) {
            createPrintPage();
        }

        String firstKey = pageMap.keySet().iterator().next();
        printPageHandler.setCurrentPage(firstKey);
        createPrintPagesUi();

    }

    private void createBodyPartTabs(){
        PrintController.getInstance().bodyPartsContainer.getChildren().clear();


        PrintController.getInstance().bodyPartsContainer.setSpacing(10);
        for (Map.Entry<String, Button> entry : bodyPartMap.entrySet()){
            entry.getValue().setDisable(disabledButtonsMap.containsKey(entry.getKey()) || disabledButtonsMap.containsValue(entry.getValue()));
            PrintController.getInstance().bodyPartsContainer.getChildren().add(entry.getValue());
        }
    }

    private Button createBodyPartTabButton(PatientPrintImageData patientPrintImageData) {
        // Button creation code remains the same
        Button selectedBodyPartButton = new Button(patientPrintImageData.getBodyPartName() + " " + patientPrintImageData.getBodyPartPosition());
        selectedBodyPartButton.setId("unSelectedBodyPartsButton");
        selectedBodyPartButton.setContentDisplay(ContentDisplay.TOP);
        selectedBodyPartButton.setAlignment(Pos.CENTER);
        selectedBodyPartButton.setPrefSize(180, 180);
        selectedBodyPartButton.setMinSize(180, 180);
        selectedBodyPartButton.setMaxSize(180, 180);

        double ratio = (double) 150 / 150;

        Mat mat = patientPrintImageData.getThumbNail();

        double targetRatio = (double) mat.width() /mat.height();

        double scaleX = 1;
        double scaleY = 1;

        if (targetRatio < ratio) {
            scaleX = (double) 150 / mat.height();
            scaleY = (double) 150 / mat.height();
        } else if (targetRatio > ratio) {
            scaleX = (double)150 / mat.width();
            scaleY = (double)150 / mat.width();
        }
        else {
            scaleX = (double)150 / mat.width();
            scaleY = (double)150 / mat.width();
        }


        Imgproc.resize(mat, mat, new Size(mat.width()*scaleX, mat.height()*scaleY), 0, 0, Imgproc.INTER_AREA);


        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        imageView.setImage(matToImage(mat));

        setupDragAndDrop(selectedBodyPartButton,patientPrintImageData);

        // Set action using separate method
        //selectedBodyPartButton.setOnAction(event -> handleBodyPartTabButtonAction(patientPrintImageData));

        selectedBodyPartButton.setGraphic(imageView);
        return selectedBodyPartButton;
    }

    private void setupDragAndDrop(Button button, PatientPrintImageData patientPrintImageData) {
        button.setOnDragDetected(event -> {
            // Start drag and drop operation
            Dragboard db = button.startDragAndDrop(TransferMode.COPY);

            // Create clipboard content
            ClipboardContent content = new ClipboardContent();

            // Store a unique identifier for the bodyPartNode
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
            DragAndDropContext.setDraggedItem(patientPrintImageData);

            db.setContent(content);
            event.consume();
        });
    }


    private static Image matToImage(Mat mat) {
        // Check if Mat is valid
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

    private final LayoutCreator layoutCreator = new LayoutCreator();

    public void setLayoutForPage(boolean page, boolean layout){
        if(printPageHandler.getCurrentPrintPage()!=null){

            printPageHandler.getCurrentPrintPage().getLayoutTabHandler().saveAllLayouts();

            LinkedList<PatientPrintImageData> printImageDataLinkedList = new LinkedList<>();

            PrintPage currentPrintPage = printPageHandler.getCurrentPrintPage();
            currentPrintPage.setLayoutCode(PrintController.getInstance().layoutCode);
            List<LayoutTab> oldTabs = currentPrintPage.getLayoutTabHandler().getAllLayoutTabs();

            for (LayoutTab layoutTab : oldTabs) {
                if(page) {
                    printImageDataLinkedList.add(layoutTab.getPatientPrintImageData());
                } else if (layout) {
                    if(layoutTab.getPatientPrintImageData()!=null){
                        printImageDataLinkedList.add(layoutTab.getPatientPrintImageData());
                        layoutTab.setPatientPrintImageData(null);
                    }
                }
            }


            currentPrintPage.getLayoutTabHandler().clear();


            GridPane gridPane = layoutCreator.createLayoutFormat(
                    PrintController.getInstance().layoutCode,
                    PrintController.getInstance().imageContainerViewBox.getPrefWidth(),
                    PrintController.getInstance().imageContainerViewBox.getPrefHeight()
            );

            PrintController.getInstance().pageGridPane = gridPane;

            List<LayoutTab> newTabs = currentPrintPage.getLayoutTabHandler().getAllLayoutTabs();

            PrintController.getInstance().imageContainerViewBox.getChildren().clear();
            PrintController.getInstance().imageContainerViewBox.getChildren().add(gridPane);





            for (int i = 0; i < Math.min(newTabs.size(),printImageDataLinkedList.size()); i++) {
                newTabs.get(i).setPatientPrintImageData(printImageDataLinkedList.get(i));
            }

            reloadDisableButtons();

        }
    }


    public void reloadDisableButtons(){
        disabledButtonsMap.clear();
        createBodyPartTabs();

        for(PrintPage p : printPageHandler.getAllTabs()){
            p.getLayoutTabHandler().getAllLayoutTabs().forEach(layoutTab -> {
                if(layoutTab.getPatientPrintImageData()!=null && bodyPartMap.containsKey(layoutTab.getPatientPrintImageData().getImageSessionId())) {
                    bodyPartMap.get(layoutTab.getPatientPrintImageData().getImageSessionId()).setDisable(true);
                    disabledButtonsMap.put(layoutTab.getPatientPrintImageData().getImageSessionId(),bodyPartMap.get(layoutTab.getPatientPrintImageData().getImageSessionId()));
                }
            });
        }

    }



    int i=0;
    public void printClick() throws SQLException {

       try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        for (PrintPage p : printPageHandler.getAllTabs()){

            handlePageTabButtonAction(pageMap.get(p.getId()),p);

            printPageHandler.getCurrentPrintPage().getLayoutTabHandler().setSelectLayout(null);

            List<PrintImageCordData> printImageCordDataList = PrintPageToImageCordData.getInstance().createImageCordData(p);
            Mat imagesMat = PrintPageToMat.createMat(PrintController.getInstance().imageContainerViewBox.getPrefWidth(), PrintController.getInstance().imageContainerViewBox.getPrefHeight(), printImageCordDataList);
            // Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\imagesMat" + i + ".png", imagesMat);

            WritableImage informationImage = PrintPageToImageCordData.getInstance().getInformationMat(p);
            Mat informationoMat = ImageConversionService.writableImageToMat(informationImage);
            // Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\informationoMat" + i + ".png", informationoMat);

            if(imagesMat.depth() != informationoMat.depth()){
                logger.info("Type or Depth Issue in informationMat : {} : {}", CvType.typeToString(imagesMat.type()), CvType.typeToString(informationoMat.type()));

                if (imagesMat.depth() == CvType.CV_8U) {
                    informationoMat.convertTo(informationoMat, CvType.CV_8UC4);
                } else if (imagesMat.depth() == CvType.CV_16U) {
                    informationoMat.convertTo(informationoMat, CvType.CV_16UC4,256.0);
                }
            }


            WritableImage annotationsImage = PrintPageToImageCordData.getInstance().getAnnotationMat(p);
            Mat annotationsMat = ImageConversionService.writableImageToMat(annotationsImage);
            //Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\annotationsMat" + i + ".png", annotationsMat);

            if(imagesMat.depth() != annotationsMat.depth()){
                logger.info("Type or Depth Issue in annotationsMat : {} : {}", CvType.typeToString(imagesMat.type()), CvType.typeToString(annotationsMat.type()));

                if (imagesMat.depth() == CvType.CV_8U) {
                    annotationsMat.convertTo(annotationsMat, CvType.CV_8UC4);
                } else if (imagesMat.depth() == CvType.CV_16U) {
                    annotationsMat.convertTo(annotationsMat, CvType.CV_16UC4,256.0);
                }
            }

            Mat outputMat = new Mat(imagesMat.size(), imagesMat.type(), new Scalar(0, 0, 65535, 0));
            convertToBGRA(imagesMat);


            Mat alphaMask = new Mat();
            Core.extractChannel(imagesMat, alphaMask, 3);
            Mat mask8U = new Mat();
            alphaMask.convertTo(mask8U, CvType.CV_8UC1, 1.0 / 256.0);
            imagesMat.copyTo(outputMat, mask8U);

            convertToBGRA(annotationsMat);

            alphaMask = new Mat();
            Core.extractChannel(annotationsMat, alphaMask, 3);
            mask8U = new Mat();
            alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
            annotationsMat.copyTo(outputMat, mask8U);

            alphaMask = new Mat();
            Core.extractChannel(informationoMat, alphaMask, 3);
            mask8U = new Mat();
            alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
            informationoMat.copyTo(outputMat, mask8U);

            logger.info("outputMat type : {}", CvType.typeToString(outputMat.type()));

            List<Mat> channels = new ArrayList<>();
            Core.split(outputMat, channels);

            List<Mat> bgrChannels = channels.subList(0, 3);
            Core.merge(bgrChannels, outputMat);

            //logger.info("outputMat type: {}", CvType.typeToString(outputMat.type()));
            Imgcodecs.imwrite("D:\\temp\\finalOutPut" + i++ + ".png", outputMat);

            //change to 1 CHANNEL
            Imgproc.cvtColor(outputMat, outputMat, Imgproc.COLOR_RGB2GRAY);

            logger.info("changed outputMat type: {}", CvType.typeToString(outputMat.type()));

            LayoutTab layoutTab = p.getLayoutTabHandler().getAllLayoutTabs().getFirst();
            try {
                String dicomImagePath = createDicomFile(layoutTab.getPatientPrintImageData(), List.of(outputMat),p);

                String scpHostAddress = PrintController.getInstance().defaultPrint.getIpAddress();
                String port = String.valueOf(PrintController.getInstance().defaultPrint.getPort());
                String aeTitle = PrintController.getInstance().defaultPrint.getAeTitle();
                String size = PrintController.getInstance().sizeComboBox.getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");
                String orientation = PrintController.getInstance().orientation;


                DicomCommunication dicomCommunication = new DicomCommunication(
                        "READY",
                        scpHostAddress,
                        port,
                        aeTitle,
                        stationAeTitle,
                        "STANDARD\\1,1",
                        orientation,
                        size,
                        dicomImagePath);

                logger.info("Java return : {}", DicomPrintManager.getInstance().printDicom(dicomCommunication));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            informationoMat.release();
            annotationsMat.release();
            imagesMat.release();


        }

        /*printPageHandler.getCurrentPrintPage().getLayoutTabHandler().setSelectLayout(null);

        List<PrintImageCordData> printImageCordDataList = PrintPageToImageCordData.getInstance().createImageCordData(printPageHandler.getCurrentPrintPage());
        Mat imagesMat = PrintPageToMat.createMat(PrintController.getInstance().imageContainerViewBox.getPrefWidth(), PrintController.getInstance().imageContainerViewBox.getPrefHeight(), printImageCordDataList);

        WritableImage informationImage = PrintPageToImageCordData.getInstance().getInformationMat(printPageHandler.getCurrentPrintPage());
        Mat informationoMat = ImageConversionService.writableImageToMat(informationImage);

        if(imagesMat.depth() != informationoMat.depth()){
            logger.info("Type or Depth Issue : {} : {}", CvType.typeToString(imagesMat.type()), CvType.typeToString(informationoMat.type()));

            if (imagesMat.depth() == CvType.CV_8U) {
                informationoMat.convertTo(informationoMat, CvType.CV_8UC4);
            } else if (imagesMat.depth() == CvType.CV_16U) {
                informationoMat.convertTo(informationoMat, CvType.CV_16UC4,256.0);
            }
        }


        WritableImage annotationsImage = PrintPageToImageCordData.getInstance().getAnnotationMat(printPageHandler.getCurrentPrintPage());
        Mat annotationsMat = ImageConversionService.writableImageToMat(annotationsImage);

        if(imagesMat.depth() != annotationsMat.depth()){
            logger.info("Type or Depth Issue : {} : {}", CvType.typeToString(imagesMat.type()), CvType.typeToString(annotationsMat.type()));

            if (imagesMat.depth() == CvType.CV_8U) {
                annotationsMat.convertTo(annotationsMat, CvType.CV_8UC4);
            } else if (imagesMat.depth() == CvType.CV_16U) {
                annotationsMat.convertTo(annotationsMat, CvType.CV_16UC4,256.0);
            }
        }

        Mat outputMat = new Mat(imagesMat.size(), imagesMat.type(), new Scalar(0, 0, 65535, 0));
        convertToBGRA(imagesMat);


        Mat alphaMask = new Mat();
        Core.extractChannel(imagesMat, alphaMask, 3);
        Mat mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8UC1, 1.0 / 256.0);
        imagesMat.copyTo(outputMat, mask8U);

        convertToBGRA(annotationsMat);

        alphaMask = new Mat();
        Core.extractChannel(annotationsMat, alphaMask, 3);
        mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
        annotationsMat.copyTo(outputMat, mask8U);

        alphaMask = new Mat();
        Core.extractChannel(informationoMat, alphaMask, 3);
        mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
        informationoMat.copyTo(outputMat, mask8U);

        logger.info("outputMat type : {}", CvType.typeToString(outputMat.type()));

        List<Mat> channels = new ArrayList<>();
        Core.split(outputMat, channels);

        List<Mat> bgrChannels = channels.subList(0, 3);
        Core.merge(bgrChannels, outputMat);

        logger.info("outputMat type: {}", CvType.typeToString(outputMat.type()));
        Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\finalOutPut.png", outputMat);

        //change from 1 CHANNEL
        Imgproc.cvtColor(outputMat, outputMat, Imgproc.COLOR_RGB2GRAY);

        logger.info("changed outputMat type: {}", CvType.typeToString(outputMat.type()));

        LayoutTab layoutTab = printPageHandler.getCurrentPrintPage().getLayoutTabHandler().getAllLayoutTabs().getFirst();
        try {
            createDicomFile(layoutTab.getPatientPrintImageData(), List.of(outputMat));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }


    private String createDicomFile(PatientPrintImageData patientPrintImageData, List<Mat> imageList,PrintPage p) throws Exception {
        String outputDicomPath = buildAndCreateOutputPath(p);

        DicomMetadata metadata = buildDicomMetadata(patientPrintImageData);
        DicomCreator dicomCreator = new DicomCreator(imageList, outputDicomPath, metadata);

        cleanupResources(imageList);
       /* updateBodyPartUIds(patientPrintImageData, dicomCreator);*/

        return outputDicomPath;
    }

   /* private void updateBodyPartUIds(PatientPrintImageData patientPrintImageData, DicomCreator dicomCreator) {
        String seriesInstanceUID = dicomCreator.getSeriesInstanceUID() == null ? "": dicomCreator.getSeriesInstanceUID();
        String sOPInstanceUID = dicomCreator.getsOPInstanceUID() == null ? "": dicomCreator.getsOPInstanceUID();
        String studyInstanceUID = dicomCreator.getStudyInstanceUID() == null ? "": dicomCreator.getStudyInstanceUID();

        patientPrintImageData.setSeriesUid(seriesInstanceUID);
        patientPrintImageData.setInstanceUid(sOPInstanceUID);
        patientPrintImageData.setSeriesUid(studyInstanceUID);
    }*/

    private DicomMetadata buildDicomMetadata(PatientPrintImageData patientPrintImageData) {

        DicomMetadata dicomMetadata = new DicomMetadata();

        String patientNameText = currentPatientPrintData.getPatientName().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientName(patientNameText);

        String patientIdText =  currentPatientPrintData.getPatientId().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientId(patientIdText);

        String patientDOB = String.format("%04d%02d%02d", currentPatientPrintData.getBirthDate().getYear(),  currentPatientPrintData.getBirthDate().getMonthValue(),  currentPatientPrintData.getBirthDate().getDayOfMonth());
        dicomMetadata.setPatientDOB(patientDOB);

        String patientSex =  currentPatientPrintData.getSex().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientSex(patientSex);

        String accessionNumberText =  currentPatientPrintData.getAccessionNum().trim().replaceAll(" +", "^");
        dicomMetadata.setAccessionNumber(accessionNumberText);

        LocalDateTime exposureDateTime = patientPrintImageData.getExposureDateTime();

        if (exposureDateTime == null) {
            logger.warn("ExposureDateTime is null. Using current time as default.");
            exposureDateTime = LocalDateTime.now();
        }

        Date studyDateTime = Date.from(
                exposureDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
        dicomMetadata.setStudyDateTime(studyDateTime);


        Date acquisitionDateTime = Date.from(
                exposureDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
        dicomMetadata.setAcquisitionDateTime(acquisitionDateTime);


        String referringPhysician = currentPatientPrintData.getReferringPhysician().trim().replaceAll(" +", "^");
        dicomMetadata.setReferringPhysician(referringPhysician);


        String kVp = String.valueOf(patientPrintImageData.getKV());
        dicomMetadata.setkVp(kVp);

        String xrayTubeCurrent = String.valueOf(patientPrintImageData.getmA());
        dicomMetadata.setXrayTubeCurrent(xrayTubeCurrent);

        String dap = String.valueOf(patientPrintImageData.getmAs());
        dicomMetadata.setAreaDoseProduct(dap);

        String viewPosition = String.valueOf(patientPrintImageData.getBodyPartPosition());
        dicomMetadata.setViewPosition(viewPosition);

        String studyId = String.valueOf(currentPatientPrintData.getStudyId());
        dicomMetadata.setStudyId(studyId);

        String seriesNumber = String.valueOf(patientPrintImageData.getSeriesId());
        dicomMetadata.setSeriesNumber(seriesNumber);

        String instanceNumber = String.valueOf(patientPrintImageData.getInstanceId());
        dicomMetadata.setInstanceNumber(instanceNumber);

        String modality = String.valueOf(currentPatientPrintData.getModality());
        dicomMetadata.setModality(modality);

        String sopClass = "1.2.840.10008.5.1.4.1.1.1.1"; // radiography SOP Class UID
        dicomMetadata.setSopClass(sopClass);

        String hospitalNameText = systemInfoEntity.getInstitutionName().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalName(hospitalNameText);

        String hospitalAddressText = systemInfoEntity.getInstitutionAddress().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalAddress(hospitalAddressText);

        String studyDescription = String.valueOf(currentPatientPrintData.getStudyDescription());
        dicomMetadata.setStudyDescription(studyDescription);

        String manufacturer = String.valueOf(systemInfoEntity.getManufacturer());
        dicomMetadata.setManufacturer(manufacturer);

        String bodyPartName = String.valueOf(patientPrintImageData.getBodyPartName());
        dicomMetadata.setBodyPart(bodyPartName);

        String procedureCodeSequence = String.valueOf(currentPatientPrintData.getProcedureCode());
        dicomMetadata.setProcedureCode(procedureCodeSequence);

        boolean burnedInAnnotation = dicomStorageEntity.isBurnedInAnnotation();
        dicomMetadata.setBurnedInAnnotation(burnedInAnnotation);


        return dicomMetadata;
    }

    private void cleanupResources(List<Mat> imageList) {
        for (Mat mat :imageList){
            mat.release();
        }
    }

    public String buildAndCreateOutputPath(PrintPage printPage) {
        String outputDicomPath = DicomService.rootDirectory + "/dcmOutput";
        Path path = Paths.get(outputDicomPath);

        // Create the directory if it doesn't exist
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            else{
                logger.info("path exits");
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }


        outputDicomPath = outputDicomPath + "/" + currentPatientPrintData.getPatientName() + printPage.getId()  + ".dcm";
        logger.info(outputDicomPath);
        return outputDicomPath;
    }


    private void convertToBGRA(Mat mat) {
        if (mat.channels() != 4) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
        }
    }

    public void cancelClick() {
        /*PrintController.getInstance().mainViewBoxParent.setPrefSize(2560, 2560);
        PrintController.getInstance().mainViewBoxParent.setMinSize(2560, 2560);
        PrintController.getInstance().mainViewBoxParent.setMaxSize(2560, 2560);*/

    }


    //a utility class to store dragged objects
    public class DragAndDropContext {
        private static Map<String, Object> draggedItems = new HashMap<>();
        private static Object currentDraggedItem;

        public static void setDraggedItem(Object item) {
            currentDraggedItem = item;
        }

        public static Object getDraggedItem() {
            return currentDraggedItem;
        }

        public static void clearDraggedItem() {
            currentDraggedItem = null;
        }
    }



}
