package com.raymedis.rxviewui.service.study;

import com.jfoenix.controls.JFXButton;
import com.ramedis.zioxa.utils.RawImageLoader;
import com.raymedis.rxviewui.controller.Footer;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.MainDrawTool;
import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.ImageDrawWrapperHandling;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.database.tables.bodyPartStudy_table.BodyPartStudyEntity;
import com.raymedis.rxviewui.database.tables.bodyPartStudy_table.BodyPartStudyService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListEntity;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListService;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.*;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;

import com.raymedis.rxviewui.service.database.DatabaseController;
import com.raymedis.rxviewui.service.fpdSdks.controller.FpdController;
import com.raymedis.rxviewui.service.fpdSdks.service.FpsStatus;
import com.raymedis.rxviewui.service.login.LoginController;
import com.raymedis.rxviewui.service.preProcessing.PreProcess;
import com.raymedis.rxviewui.service.serialization.KryoSerializer;

import com.zioxa.nativeSdkModule.sdk.innocare.FpdStatusMode;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class StudyService {

    public final static PatientStudyHandler patientStudyHandler = new PatientStudyHandler();
    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    public static StudyService instance = new StudyService();
    public static StudyService getInstance() {
        return instance;
    }
    private final BodyPartStudyService bodyPartStudyService = BodyPartStudyService.getInstance();
    private final PatientLocalListService patientLocalListService = PatientLocalListService.getInstance();

    private final static ImageConversionService imageConversionService = ImageConversionService.getInstance();

    private final LinkedHashMap<String,JFXButton> studyTabMap = new LinkedHashMap<>();
    private final LinkedHashMap<String,JFXButton> bodyPartMap = new LinkedHashMap<>();
    public final static Logger logger = LoggerFactory.getLogger(StudyService.class);
    public static boolean imageStatus=false;


    public void createStudyTabs(){
        if(patientStudyHandler.getCurrentStudy() == null){
            StudyMainController.getInstance().tabPane.getChildren().clear();
            studyTabMap.clear();

            StudyMainController.getInstance().bodyPartTabPane.getChildren().clear();
            bodyPartMap.clear();

            StudyBodyPartsController.getInstance().loadStudyBodyParts();
            NavConnect.getInstance().navigateToRegisterMain();

            return;
        }

        StudyMainController.getInstance().tabPane.getChildren().clear();

        studyTabMap.clear();
        List<TabNode> allTabs = patientStudyHandler.getAllTabs();
        for (TabNode t : allTabs) {
            PatientStudyNode tab = (PatientStudyNode) t;
            studyTabMap.put(tab.getStudySessionId(),
                    createStudyTabButton(tab.getStudySessionId(),
                            tab.getPatientStudy().getPatientInfo().getPatientName(),
                            tab.getPatientStudy().getPatientInfo().getPatientId()));

        }

        if (!studyTabMap.isEmpty()) {
            StudyMainController.getInstance().tabPane.setSpacing(10);
            for (Map.Entry<String,JFXButton> entry : studyTabMap.entrySet()){
                StudyMainController.getInstance().tabPane.getChildren().add(entry.getValue());
            }
        }

        handleStudyTabButtonClick();
        createBodyPartTabs();
    }

    private JFXButton createStudyTabButton(String sessionId,String patientName,String patientId) {
        JFXButton tabButton = new JFXButton();
        tabButton.setContentDisplay(ContentDisplay.RIGHT);

        tabButton.setTextFill(Color.WHITE);
        tabButton.setMaxHeight(65.0);
        tabButton.setMaxWidth(220.0);
        tabButton.setMinHeight(65.0);
        tabButton.setMinWidth(220.0);
        tabButton.setPrefHeight(65.0);
        tabButton.setPrefWidth(220.0);
        tabButton.setId("unSelectedTabButton");

        HBox hbox = new HBox();
        hbox.setMaxHeight(65.0);
        hbox.setMaxWidth(200.0);
        hbox.setMinHeight(65.0);
        hbox.setMinWidth(200.0);
        hbox.setPrefHeight(65.0);
        hbox.setPrefWidth(200.0);
        hbox.setAlignment(Pos.CENTER);

        Label tabLabel = new Label(patientId + "/" + patientName);
        tabLabel.setAlignment(Pos.CENTER);

        tabLabel.setMaxHeight(65.0);
        tabLabel.setMaxWidth(175.0);
        tabLabel.setMinHeight(65.0);
        tabLabel.setMinWidth(175.0);
        tabLabel.setPrefHeight(65.0);
        tabLabel.setPrefWidth(175.0);


        JFXButton closeButton = new JFXButton();
        closeButton.setContentDisplay(ContentDisplay.CENTER);
        closeButton.setMaxHeight(25.0);
        closeButton.setMaxWidth(25.0);
        closeButton.setMinHeight(25.0);
        closeButton.setMinWidth(25.0);
        closeButton.setPrefHeight(25.0);
        closeButton.setPrefWidth(25.0);
        closeButton.setId("selectedCloseTabIcon");

        FontIcon closeIcon = new FontIcon("mdi2c-close-circle");
        closeIcon.setIconColor(Color.WHITE);
        closeButton.setGraphic(closeIcon);

        hbox.getChildren().addAll(tabLabel, closeButton);
        tabButton.setGraphic(hbox);

        tabButton.getStyleClass().addAll("unSelectedTabButton", "fontStyle");

        AnchorPane.setRightAnchor(tabButton, 0.0);

        tabButton.setOnAction(event -> {
            if(!StudyMainController.isEdit && !DatabaseController.isEdit){
                patientStudyHandler.selectPatientStudy(sessionId);
                createStudyTabs();
                createBodyPartTabs();
            }
        });

        closeButton.setOnAction(event -> {
            if(!StudyMainController.isEdit  && !DatabaseController.isEdit){
                handleStudyCloseTabButtonClick(sessionId);
                event.consume();
            }
        });

        return tabButton;
    }

    private void handleStudyTabButtonClick() {
        for (Node node : StudyMainController.getInstance().tabPane.getChildren()) {
            if (node instanceof JFXButton button) {
                button.setId("unSelectedTabButton");
            }
        }

        studyTabMap.get(patientStudyHandler.getCurrentStudy().getStudySessionId()).setId("selectedTabButton");
        StudyBodyPartsController.getInstance().loadStudyBodyParts();

    }

    private void handleStudyCloseTabButtonClick(String sessionId){
        patientStudyHandler.removeTab(sessionId);
        createStudyTabs();
    }

    public void createBodyPartTabs(){
        if(patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart() == null){
            StudyMainController.getInstance().bodyPartTabPane.getChildren().clear();
            bodyPartMap.clear();
            return;
        }

        StudyMainController.getInstance().bodyPartTabPane.getChildren().clear();
        bodyPartMap.clear();

        List<TabNode> allTabs = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getAllTabs();

        for (TabNode allTab : allTabs) {
            BodyPartNode tab = (BodyPartNode) allTab;
            bodyPartMap.put(
                    tab.getImageSessionId(),
                    createBodyPartTabButton(tab)
            );
        }


        if (!bodyPartMap.isEmpty()) {
            StudyMainController.getInstance().bodyPartTabPane.setSpacing(10);
            for (Map.Entry<String,JFXButton> entry : bodyPartMap.entrySet()){
                StudyMainController.getInstance().bodyPartTabPane.getChildren().add(entry.getValue());
            }
        }

        handleBodyPartTabButtonClick();
    }


    public void createBodyPartTabs2(){
        if(patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart() == null){
            StudyMainController.getInstance().bodyPartTabPane.getChildren().clear();
            bodyPartMap.clear();
            return;
        }

        StudyMainController.getInstance().bodyPartTabPane.getChildren().clear();
        bodyPartMap.clear();

        List<TabNode> allTabs = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getAllTabs();

        for (TabNode allTab : allTabs) {
            BodyPartNode tab = (BodyPartNode) allTab;
            bodyPartMap.put(
                    tab.getImageSessionId(),
                    createBodyPartTabButton(tab)
            );
        }


        if (!bodyPartMap.isEmpty()) {
            StudyMainController.getInstance().bodyPartTabPane.setSpacing(10);
            for (Map.Entry<String,JFXButton> entry : bodyPartMap.entrySet()){
                StudyMainController.getInstance().bodyPartTabPane.getChildren().add(entry.getValue());
            }
        }
    }

    public JFXButton createBodyPartTabButton(BodyPartNode tab) {
        // Button creation code remains the same
        JFXButton selectedBodyPartButton = new JFXButton(tab.getBodyPart().getBodyPartName() + " " + tab.getBodyPart().getBodyPartPosition());
        selectedBodyPartButton.setId("unSelectedBodyPartsButton");
        selectedBodyPartButton.setContentDisplay(ContentDisplay.TOP);
        selectedBodyPartButton.setAlignment(Pos.CENTER);
        selectedBodyPartButton.setPrefSize(120, 120);
        selectedBodyPartButton.setMinSize(120, 120);
        selectedBodyPartButton.setMaxSize(120, 120);

        Mat mat = tab.getBodyPart().getThumbNail();


        Imgproc.resize(mat,mat,new Size(85,85), 0, 0, Imgproc.INTER_AREA);
        //Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\thumbnail_"+ tab.getBodyPart().getBodyPartName() + " " + tab.getBodyPart().getBodyPartPosition() + ".png", mat);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(85);
        imageView.setFitHeight(85);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        imageView.setImage(ImageConversionService.getInstance().matToImage(mat));


        // Set action using separate method
        selectedBodyPartButton.setOnAction(event -> handleBodyPartTabButtonAction(tab));

        selectedBodyPartButton.setGraphic(imageView);
        return selectedBodyPartButton;
    }

    public void handleBodyPartTabButtonAction(BodyPartNode tab) {
        if(patientStudyHandler.getCurrentStudy()==null || patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart()==null){
            return;
        }

        BodyPartNode currentBodyPartNode = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart();
        if (currentBodyPartNode.getBodyPart() != null) {
            if (currentBodyPartNode.getBodyPart().isExposed()) {
                CanvasMainChange.mainDrawTool.reset();
                ImageDrawWrapperHandling imageDrawWrapperHandling = new ImageDrawWrapperHandling(currentBodyPartNode.getBodyPart());
                imageDrawWrapperHandling.saveAllImageParams();
                //currentBodyPartNode.getBodyPart().setEditedMat(CanvasMainChange.croppedMat);
                CanvasMainChange.mainDrawTool.saveImageParams(currentBodyPartNode.getBodyPart());
                imageDrawWrapperHandling.setPatientBodyPart(currentBodyPartNode.getBodyPart());
            }
        }
        patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().setCurrentBodyPart(tab.getImageSessionId());
        handleBodyPartTabButtonClick();
    }

    public static Image matToImage(Mat mat) {
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

    public void handleBodyPartTabButtonClick() {
        createBodyPartTabs2();
        StudyMainController.getInstance().newStudyProjectionUi();
        StudyMainController.getInstance().editProjection.setDisable(true);
        BodyPartHandler bodyPartHandler = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler();
        JFXButton currentSelectedBodyPartButton = bodyPartMap.get(bodyPartHandler.getCurrentBodyPart().getImageSessionId());

        for (Node node : StudyMainController.getInstance().bodyPartTabPane.getChildren()) {
            if (node instanceof JFXButton button) {
                button.setId("unSelectedBodyPartsButton");
            }
        }

        List<TabNode> allTabs = bodyPartHandler.getAllTabs();
        for (TabNode allTab : allTabs) {
            BodyPartNode tab = (BodyPartNode) allTab;

            if(tab.getBodyPart().isRejected()){
                bodyPartMap.get(tab.getImageSessionId()).setId("rejectedUnselectedBodyPartButton");
            }else if(tab.getBodyPart().isExposed()){
                bodyPartMap.get(tab.getImageSessionId()).setId("exposedUnselectedBodyPartButton");
            }
        }

        imageStatus=false;
        currentBodyPart = bodyPartHandler.getCurrentBodyPart().getBodyPart();
        if(currentBodyPart.isRejected()){
            Platform.runLater(()-> {
                StudyMainController.getInstance().applyStudyOverLay();
                currentSelectedBodyPartButton.setId("rejectedSelectedBodyPartButton");
                //StudyMainController.getInstance().consoleOffClick();
                if(currentBodyPart.getEditedMat()!=null){
                    MainDrawTool.cropLayout.reset(currentBodyPart.getEditedMat().cols(),currentBodyPart.getEditedMat().rows());
                    setMainCanvasImage(currentBodyPart.getEditedMat());
                    StudyMainController.getInstance().exposedStudyProjection(currentBodyPart);
                }
            });
        }
        else if (currentBodyPart.isExposed()) {
            isSimulationMode=false;
            Platform.runLater(()-> {
                StudyMainController.getInstance().applyStudyOverLay();
                currentSelectedBodyPartButton.setId("exposedSelectedBodyPartButton");
                //StudyMainController.getInstance().consoleOffClick();
                if(currentBodyPart.getEditedMat()!=null){
                    MainDrawTool.cropLayout.reset(currentBodyPart.getEditedMat().cols(),currentBodyPart.getEditedMat().rows());
                    setMainCanvasImage(currentBodyPart.getEditedMat());
                    StudyMainController.getInstance().exposedStudyProjection(currentBodyPart);
                }
            });
        }
        else {
            currentSelectedBodyPartButton.setId("selectedBodyPartsButton");
            StudyMainController.getInstance().newStudyProjectionUi();
            currentBodyPart = bodyPartHandler.getCurrentBodyPart().getBodyPart();
            if(!isSimulationMode){
                simulationMode();
            }
            //operationMode(currentBodyPart);
            //StudyMainController.getInstance().consoleOnClick();
            //StudyMainController.getInstance().setConsoleXrayValues(currentBodyPart);
            StudyMainController.getInstance().loadConsoleValues(currentBodyPart);
        }

    }


    private boolean isSimulationMode = false; // Set this based on your application logic
    private volatile PatientBodyPart currentBodyPart;
    private void simulationMode(){
        isSimulationMode = true;

        new Thread(()->{
            //LoginController.isInitialized
            if(true)
            {
                //LoginController.isConnected
                if(true)
                {
                   /* InnoCareService.innoCareNative.cancelOperationMode();
                    InnoCareService.innoCareNative.triggerAedOneShot();
                    InxFpdOperStatusJava fpsStatus = InnoCareService.innoCareNative.getFpdStatus();*/
                    FpdStatusMode mode = FpdStatusMode.AED_ONE_SHOT_MODE;//FpdStatusMode.fromValue(fpsStatus.getMode() & 0xFF);
                    //logger.info("fpd status {}",mode);
                    if (mode.equals(FpdStatusMode.AED_ONE_SHOT_MODE)) {
                        StudyMainController.getInstance().aedReadyLabel.setId("aedSelectedReadyIcon");
                        StudyMainController.getInstance().exposeButton.setDisable(false);
                        while (!imageStatus) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                logger.error("Thread interrupted", e);
                                return;
                            }
                        }

                        /*

                        ->get the image
                        ->serialize and save image
                        ->deserialize the image and use


                        String imagePath = "project base folder + output folder + patient id";
                        InnoCareService.innoCareNative.getFpdImageToFile(imagePath);
                        InnoCareService.innoCareNative.getCorrectedImageToFile(imagePath);

                        */

                     //start stopwatch


                        PreProcess preProcess  = PreProcess.getInstance();
                        Mat inputMat = null;
                        try {
                            logger.info("current bodyPart {}",currentBodyPart.getBodyPartName().toLowerCase() + " " + currentBodyPart.getBodyPartPosition());



                            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\Styles\\Images\\input\\"  + currentBodyPart.getBodyPartName().toLowerCase()+ "\\1.raw";

                            //logger.info("input path : {}", path);
                            inputMat = RawImageLoader.loadRawImage(path, 2500, 3052);


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }



                        Mat processedOrg = preProcess.process(inputMat,currentBodyPart.getBodyPartName().toLowerCase());
                        //System.out.printf("D:\\Downloads\\output\\%s\\1.png \n", currentBodyPart.getBodyPartName().toLowerCase());
                        //Imgcodecs.imwrite("D:\\Downloads\\output\\"+ currentBodyPart.getBodyPartName().toLowerCase() + "\\1.png", processedOrg);


                        /*String imagePath = "D:\\Work\\RxView_Java\\RxView\\ProcessedImages\\knee16.png";
                        String imageUrl = Paths.get(imagePath).toUri().toString();
                        Image image = new Image(imageUrl);*/



                        logger.info("Image received Found in FPD");

                        if(currentBodyPart.getSeriesId().equals("1")){
                            patientStudyHandler.getCurrentStudy().getPatientStudy().setExposedDateTime(LocalDateTime.now());
                        }

                        StudyMainController.getInstance().aedReadyLabel.setId("aedUnselectedReadyIcon");
                        StudyMainController.getInstance().exposeButton.setDisable(true);

                        //Thread.sleep(500);

                        Mat org = processedOrg.clone();
                        currentBodyPart.setImageMat(org);

                        Mat mat = imageFrameResize(processedOrg);
                        MainDrawTool.cropLayout.reset(mat.cols(),mat.rows());

                        currentBodyPart.setEditedMat(processedOrg);

                        Mat thumNailMat = processedOrg.clone();
                        thumNailMat = ImageProcessService.getInstance().resizeImageToSquareShape(thumNailMat);
                        Imgproc.resize(thumNailMat,thumNailMat,new Size(150,150), 0, 0, Imgproc.INTER_AREA);

                        currentBodyPart.setThumbNail(thumNailMat);

                        XrayParams xrayParams = new XrayParams();
                        xrayParams.setKV(StudyMainController.getInstance().kvValue);
                        xrayParams.setMs(StudyMainController.getInstance().msValue);
                        xrayParams.setmAs(StudyMainController.getInstance().masValue);
                        xrayParams.setmA(StudyMainController.getInstance().maValue);

                        currentBodyPart.setXrayParams(xrayParams);

                        CanvasMainChange.mainDrawTool.mainImageMat = processedOrg;
                        Platform.runLater(this::exposeSelectedBodyPart);

                        StudyMainController.getInstance().bodyPartTabPane.setDisable(false);
                        StudyMainController.getInstance().tabPane.setDisable(false);
                        NavConnect.getInstance().disableNav(false);

                    }
                    else{
                        logger.info("failed to trigger aed one shot mode");
                    }
                }
                else{
                    logger.info("fpd not connected");
                }
            }
            else{
                logger.info("fpd not initialized");
            }
        }).start();
    }


    private FpdController fpdController = FpdController.getInstance();


    private void operationMode(PatientBodyPart currentBodyPart){
        new Thread(()->{

            if(LoginController.isInitialized)
            {
                if(LoginController.isConnected)
                {
                    boolean isCancel = fpdController.cancelOperationMode();
                    boolean isTrigger = fpdController.triggerAedOneShot();

                    FpsStatus mode = fpdController.getFpdStatus();
                    logger.info("fpd status {}",mode);
                    if (mode.equals(FpsStatus.AED_ONE_SHOT_MODE)) {
                        StudyMainController.getInstance().aedReadyLabel.setId("aedSelectedReadyIcon");
                        StudyMainController.getInstance().exposeButton.setDisable(false);
                        while (!imageStatus) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                logger.error("Thread interrupted", e);
                                return;
                            }
                        }

                        String imagePath = "project base folder + output folder + patient id";

                        fpdController.getFpdImageToFile(imagePath);
                        fpdController.getCorrectedImageToFile(imagePath);

                        PreProcess preProcess = PreProcess.getInstance();
                        Mat inputMat = null;
                        try {
                            inputMat = RawImageLoader.loadRawImage("D:\\Downloads\\input\\test\\ankle\\2.raw", 2500, 3052);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Mat processedOrg = preProcess.process(inputMat,"abdomen");
                        Imgcodecs.imwrite("D:\\Downloads\\output\\ankle\\1.png", processedOrg);

                        logger.info("Image received Found in FPD");

                        if(currentBodyPart.getSeriesId().equals("1")) {
                            patientStudyHandler.getCurrentStudy().getPatientStudy().setExposedDateTime(LocalDateTime.now());
                        }

                        StudyMainController.getInstance().aedReadyLabel.setId("aedUnselectedReadyIcon");
                        StudyMainController.getInstance().exposeButton.setDisable(true);

                        try {
                            Thread.sleep(500);

                            Mat mat = imageFrameResize(processedOrg);
                            MainDrawTool.cropLayout.reset(mat.cols(),mat.rows());

                            logger.info("org : {}",CvType.typeToString(processedOrg.type()));

                            currentBodyPart.setImageMat(processedOrg);
                            currentBodyPart.setEditedMat(processedOrg);

                            Mat thumNailMat = processedOrg.clone();
                            thumNailMat = ImageProcessService.getInstance().resizeImageToSquareShape(thumNailMat);
                            Imgproc.resize(thumNailMat,thumNailMat,new Size(150,150), 0, 0, Imgproc.INTER_AREA);

                            currentBodyPart.setThumbNail(thumNailMat);

                            XrayParams xrayParams = new XrayParams();
                            xrayParams.setKV(StudyMainController.getInstance().kvValue);
                            xrayParams.setMs(StudyMainController.getInstance().msValue);
                            xrayParams.setmAs(StudyMainController.getInstance().masValue);
                            xrayParams.setmA(StudyMainController.getInstance().maValue);

                            currentBodyPart.setXrayParams(xrayParams);
                            CanvasMainChange.mainDrawTool.mainImageMat = processedOrg;
                            Platform.runLater(this::exposeSelectedBodyPart);

                        }
                        catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        logger.info("failed to trigger aed one shot mode");
                    }
                }
                else{
                    logger.info("fpd not connected");
                }
            }
            else{
                logger.info("fpd not initialized");
            }
        }).start();
    }

    public static Mat imageFrameResize(Image image){

        double width = image.getWidth();
        double height = image.getHeight();

        if(width>=height){
            height = (1024*height)/width;
            width = 1024;
        }else{
            width = (1024*width)/height;
            height = 1024;
        }

        //System.out.println("new : "+ width + " * " + height);

        applyImageFrameResize(width, height);

        Mat mat = imageConversionService.imageToMat(image);
        Imgproc.resize(mat, mat, new Size(width,height));
        return mat;
    }

    public static Mat imageFrameResize(Mat image){
        double width = image.width();
        double height = image.height();

        if(width>=height){
            height = (1024*height)/width;
            width = 1024;
        }else{
            width = (1024*width)/height;
            height = 1024;
        }

        //System.out.println("new : "+ width + " * " + height);

        applyImageFrameResize(width, height);

        Imgproc.resize(image, image, new Size(width,height));
        return image;
    }


    public GridPane resizeInformationLayout(double size) {

        double x = ((size*DynamicCanvasElementsResize.getInfoLabelFontSize()))/StudyMainController.getInstance().overlayGridPane.getWidth();


        GridPane gridPane = new GridPane();

        gridPane.setPrefWidth(size);
        gridPane.setPrefHeight(size);

        gridPane.setMaxWidth(size);
        gridPane.setMaxHeight(size);

        gridPane.setMinWidth(size);
        gridPane.setMinHeight(size);

        gridPane.getChildren().add(StudyMainController.getInstance().overlayGridPane);

//        StudyMainController.getInstance().overlayGridPane.setPrefWidth(size);
//        StudyMainController.getInstance().overlayGridPane.setPrefHeight(size);
//
//        StudyMainController.getInstance().overlayGridPane.setMaxWidth(size);
//        StudyMainController.getInstance().overlayGridPane.setMaxHeight(size);
//
//        StudyMainController.getInstance().overlayGridPane.setMinWidth(size);
//        StudyMainController.getInstance().overlayGridPane.setMinHeight(size);


        logger.info("resizeInformationLayout {}",size);

        DynamicCanvasElementsResize.setInfoLabelFontSize(x);

        StudyMainController.getInstance().applyStudyOverLay();

        return gridPane;
    }

    private final static Rectangle clip = new Rectangle();
    public static void applyImageFrameResize(double width,double height){
        StudyMainController.getInstance().canvasViewBox.setMaxSize(width,height);
        StudyMainController.getInstance().drawingCanvas.setHeight(height);
        StudyMainController.getInstance().drawingCanvas.setWidth(width);
        StudyMainController.getInstance().textPane.setMaxSize(width,height);

        StudyMainController.getInstance().canvasPane.setPrefHeight(height);
        StudyMainController.getInstance().canvasPane.setPrefWidth(width);

        StudyMainController.getInstance().canvasPane.setMinHeight(height);
        StudyMainController.getInstance().canvasPane.setMinWidth(width);

        StudyMainController.getInstance().canvasPane.setMaxHeight(height);
        StudyMainController.getInstance().canvasPane.setMaxWidth(width);

        StudyMainController.getInstance().canvasViewBox.setMinSize(width,height);
        StudyMainController.getInstance().textPane.setMinSize(width,height);

        StudyMainController.getInstance().canvasViewBox.setPrefSize(width,height);
        StudyMainController.getInstance().textPane.setPrefSize(width,height);

        clip.setWidth(width);
        clip.setHeight(height);
        MainDrawTool.canvasGroup.setClip(clip);
    }

    public void setMainCanvasImage(Mat mat){

        logger.info("mat size {} * {}", mat.cols(), mat.rows());


        Image temp = imageConversionService.matToImage(mat);
        Mat resizedMat = StudyService.imageFrameResize(temp);

        dynamicExportValues(resizedMat);
        MainDrawTool.enableImageTools();
        CanvasMainChange.mainImage = temp;
        CanvasMainChange.displayImage = temp;
        CanvasMainChange.mainDrawTool.mainImageMat = mat;

        Platform.runLater(()->StudyMainController.getInstance().applyStudyOverLay());
        CanvasMainChange.mainDrawTool.reset();
        CanvasMainChange.mainDrawTool.imageProcess();
        CanvasMainChange.redraw();
    }

    public static void dynamicExportValues(Mat mat){
        double width = mat.rows();
        double height = mat.cols();

        if(width>=height){
            width = 1024;
        } else{
            width = (1024*width)/height;
        }

        double multiple = mat.rows()/width;

        logger.info("study mat.rows() * width : {} {}", mat.rows(), width);


        applyExportValues(multiple);
    }

    public static void applyExportValues(double multiple){
        DynamicCanvasElementsResize.setMultiple(multiple);

        DynamicCanvasElementsResize.setLineWidth(2 * multiple);
        DynamicCanvasElementsResize.setEllipseRadiusX(8*multiple);
        DynamicCanvasElementsResize.setEllipseRadiusY(8*multiple);
        DynamicCanvasElementsResize.setLabelFontSize(14*multiple);
        DynamicCanvasElementsResize.setRectWidth(90*multiple);
        DynamicCanvasElementsResize.setRectHeight(30*multiple);
        DynamicCanvasElementsResize.setCustomFontIconSize(200*multiple);

        DynamicCanvasElementsResize.setInfoLabelFontSize(45);


        //logger.info("study DynamicCanvasElementsResize values set with multiple: {}",  DynamicCanvasElementsResize.getMultiple());
       /* logger.info("study Line Width: {}, Ellipse Radius X: {}, Ellipse Radius Y: {}, Label Font Size: {}, Rect Width: {}, Rect Height: {}, Custom Font Icon Size: {}",
                DynamicCanvasElementsResize.getLineWidth(),
                DynamicCanvasElementsResize.getEllipseRadiusX(),
                DynamicCanvasElementsResize.getEllipseRadiusY(),
                DynamicCanvasElementsResize.getLabelFontSize(),
                DynamicCanvasElementsResize.getRectWidth(),
                DynamicCanvasElementsResize.getRectHeight(),
                DynamicCanvasElementsResize.getCustomFontIconSize());*/

    }



    public void exposeSelectedBodyPart() {
        if (patientStudyHandler == null || patientStudyHandler.getCurrentStudy() == null) {
            logger.error("Error: No study found!");
            return;
        }

        PatientStudy patientStudy = patientStudyHandler.getCurrentStudy().getPatientStudy();
        if (patientStudy == null) {
            logger.error("Error: Patient study is null!");
            return;
        }

        BodyPartHandler bodyPartHandler = patientStudy.getBodyPartHandler();
        if (bodyPartHandler.getCurrentBodyPart() == null || bodyPartHandler.getCurrentBodyPart().getBodyPart() == null) {
            logger.error("Error: No body part selected!");
            return;
        }

        if (!bodyPartHandler.getCurrentBodyPart().getBodyPart().isExposed()) {
            bodyPartHandler.getCurrentBodyPart().getBodyPart().setExposed(true);
            bodyPartHandler.getCurrentBodyPart().getBodyPart().setRejected(false);
            LocalDateTime exposedTime = LocalDateTime.now();
            bodyPartHandler.getCurrentBodyPart().getBodyPart().setExposureDateTime(exposedTime);
            //patientStudy.setExposedDateTime(exposedTime);
        }

        createBodyPartTabs();
    }

    public void deleteSelectedBodyPartButtonClick(){
        if(patientStudyHandler.getCurrentStudy()==null || patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart()==null){
            return;
        }

        BodyPartHandler bodyPartHandler = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler();

        if(bodyPartHandler.getCurrentBodyPart().getBodyPart().isRejected()){
            return;
        }

        if(bodyPartHandler.getCurrentBodyPart().getBodyPart().isExposed()){
            bodyPartHandler.getCurrentBodyPart().getBodyPart().setRejected(true);
        } else{
            bodyPartHandler.removeTab(
                    bodyPartHandler.getCurrentBodyPart().
                            getImageSessionId());
        }

        createBodyPartTabs();
    }

    public void saveAllStudyTabs(List<TabNode> saveStudiesList) throws IOException {

        CanvasMainChange.mainDrawTool.reset();

        for (TabNode tab: saveStudiesList){
            PatientStudy patientStudy = ((PatientStudyNode) tab).getPatientStudy();

            if(patientStudy==null){
                return;
            }

            if (patientStudy.getExposedDateTime() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Exposures To Save...");
                alert.setHeaderText(null);
                alert.setContentText("No exposures taken in the study... Please take an exposure.");
                alert.show();
                return;
            }


            //changed from edit image to main image mat
            patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart().setEditedMat(CanvasMainChange.mainDrawTool.mainImageMat);

            Mat org = patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart().getImageMat();
            logger.info("org mat size {} * {}",org.cols(),org.rows());

            PatientInfo patientInfo = patientStudy.getPatientInfo();
            PatientBodyPart patientBodyPart = patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart();

            if(patientBodyPart.isExposed()){
                new ImageDrawWrapperHandling(patientBodyPart).saveAllImageParams();
            }

            CanvasMainChange.mainDrawTool.saveImageParams(patientBodyPart);
            PatientStudyEntity patientStudyEntity = new PatientStudyEntity();
            patientStudyEntity.setPatientId(patientInfo.getPatientId());
            patientStudyEntity.setPatientName(patientInfo.getPatientName());
            patientStudyEntity.setSex(patientInfo.getSex());
            patientStudyEntity.setAge(patientInfo.getAge());
            patientStudyEntity.setDob(patientInfo.getBirthDate().toLocalDate());
            patientStudyEntity.setHeight(patientInfo.getHeight());
            patientStudyEntity.setWeight(patientInfo.getWeight());
            patientStudyEntity.setPatientSize(patientInfo.getPatientSize());
            patientStudyEntity.setPatientInstituteResidence(patientInfo.getPatientInstituteResidence());

            patientStudyEntity.setAccessionNumber(patientStudy.getAccessionNum());
            patientStudyEntity.setModality(patientStudy.getModality());
            patientStudyEntity.setRequestProcedurePriority(patientStudy.getRequestProcedurePriority());
            patientStudyEntity.setAdditionalPatientHistory(patientStudy.getAdditionalPatientHistory());
            patientStudyEntity.setAdmittingDiagnosisDescription(patientStudy.getAdmittingDiagnosisDescription());
            patientStudyEntity.setStudyDescription(patientStudy.getStudyDescription());
            patientStudyEntity.setStudyProcedure(patientStudy.getProcedureCode());
            patientStudyEntity.setStudyDateTime(patientStudy.getStudyDateTime());
            patientStudyEntity.setRegisterDateTime(patientStudy.getRegisterDateTime());
            patientStudyEntity.setScheduledDateTime(patientStudy.getScheduledDateTime());
            patientStudyEntity.setExposureDateTime(patientBodyPart.getExposureDateTime());
            patientStudyEntity.setPatientComments(patientStudy.getPatientComments());

            String studyID = patientStudy.getStudyId();
            if(studyID==null || studyID.isEmpty()){
                try {
                    studyID =  IdGenerator.generateStudyId();
                    //logger.info(studyID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                patientStudy.setStudyId(studyID);
            }

            //logger.info(studyID);
            patientStudyEntity.setStudyId(studyID);

            patientStudyEntity.setReadingPhysician(patientStudy.getReadingPhysician());
            patientStudyEntity.setReferringPhysician(patientStudy.getReferringPhysician());
            patientStudyEntity.setPerformingPhysician(patientStudy.getPerformingPhysician());

            String institution ="";
            try {
                institution = SystemInfoService.getInstance().getSystemInfo().getFirst().getInstitutionName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            patientStudyEntity.setInstitution(institution);
            patientStudyEntity.setStudyUid(patientStudy.getStudyUid());
            patientStudyEntity.setInstanceId(patientBodyPart.getInstanceId());
            patientStudyEntity.setInstanceUid(patientBodyPart.getInstanceUid());
            patientStudyEntity.setSeriesId(patientBodyPart.getSeriesId());
            patientStudyEntity.setSeriesUid(patientBodyPart.getSeriesUid());
            patientStudyEntity.setIsRejected(patientStudy.isRejected());
            patientStudyEntity.setIsPrinted(false);
            patientStudyEntity.setIsDicomUploaded(false);
            patientStudyEntity.setIsBackedUp(false);
            patientStudyEntity.setIsCleaned(false);


            List<File> outputfiles = createStudyFile(patientStudy);
            try {
                if(patientStudyService.findPatientByStudyId(patientStudy.getStudyId())==null){
                    logger.info("Saving new patient study with ID: {}", patientStudyEntity.getStudyId());
                    patientStudyService.savePatient(patientStudyEntity);
                }
                else{
                    logger.info("Updating existing patient study with ID: {}", patientStudyEntity.getStudyId());
                    patientStudyService.updatePatient(patientStudy.getStudyId(), patientStudyEntity);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            PatientStudyNode studyNode = (PatientStudyNode) tab;
            String studySessionId = studyNode.getStudySessionId();
            PatientLocalListEntity patientLocalListEntity = patientLocalListService.findByStudySessionId(studySessionId);
            if(patientLocalListEntity!=null){
                patientLocalListEntity.setStudyId(patientStudyEntity.getStudyId());
                try {
                    patientLocalListService.update(patientLocalListEntity.getId(),patientLocalListEntity);
                    //logger.info("saving : {}"  , patientLocalListEntity.getStudyId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                //logger.info("local list not exist .");
            }

            KryoSerializer.getInstance().serializeToFile(patientStudy,outputfiles.get(0));


            saveCurrentStudyBodyParts(patientStudy,outputfiles.get(1));
            patientStudyHandler.removeTab(patientStudyHandler.getCurrentStudy().getStudySessionId());
            if(patientStudyHandler.getCurrentStudy() == null){
                NavConnect.getInstance().navigateToRegisterMain();
            }

            createStudyTabs();

        }

    }

    public void saveForPrint(TabNode patientStudyNode) throws IOException {
        PatientStudy patientStudy = ((PatientStudyNode) patientStudyNode).getPatientStudy();

        if(patientStudy==null){
            return;
        }

        if (patientStudy.getExposedDateTime() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Exposures To Save...");
            alert.setHeaderText(null);
            alert.setContentText("No exposures taken in the study... Please take an exposure.");
            alert.show();

            return;
        }

        patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart().setEditedMat(CanvasMainChange.mainDrawTool.mainImageMat);


        PatientInfo patientInfo = patientStudy.getPatientInfo();
        PatientBodyPart patientBodyPart = patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart();
        if(patientBodyPart.isExposed()){
            new ImageDrawWrapperHandling(patientBodyPart).saveAllImageParams();
        }

        CanvasMainChange.mainDrawTool.saveImageParams(patientBodyPart);
        PatientStudyEntity patientStudyEntity = new PatientStudyEntity();
        patientStudyEntity.setPatientId(patientInfo.getPatientId());
        patientStudyEntity.setPatientName(patientInfo.getPatientName());
        patientStudyEntity.setSex(patientInfo.getSex());
        patientStudyEntity.setAge(patientInfo.getAge());
        patientStudyEntity.setDob(patientInfo.getBirthDate().toLocalDate());
        patientStudyEntity.setHeight(patientInfo.getHeight());
        patientStudyEntity.setWeight(patientInfo.getWeight());
        patientStudyEntity.setPatientSize(patientInfo.getPatientSize());
        patientStudyEntity.setPatientInstituteResidence(patientInfo.getPatientInstituteResidence());

        patientStudyEntity.setAccessionNumber(patientStudy.getAccessionNum());
        patientStudyEntity.setModality(patientStudy.getModality());
        patientStudyEntity.setRequestProcedurePriority(patientStudy.getRequestProcedurePriority());
        patientStudyEntity.setAdditionalPatientHistory(patientStudy.getAdditionalPatientHistory());
        patientStudyEntity.setAdmittingDiagnosisDescription(patientStudy.getAdmittingDiagnosisDescription());
        patientStudyEntity.setStudyDescription(patientStudy.getStudyDescription());
        patientStudyEntity.setStudyProcedure(patientStudy.getProcedureCode());
        patientStudyEntity.setStudyDateTime(patientStudy.getStudyDateTime());
        patientStudyEntity.setRegisterDateTime(patientStudy.getRegisterDateTime());
        patientStudyEntity.setScheduledDateTime(patientStudy.getScheduledDateTime());
        patientStudyEntity.setExposureDateTime(patientBodyPart.getExposureDateTime());
        patientStudyEntity.setPatientComments(patientStudy.getPatientComments());

        String studyID = patientStudy.getStudyId();
        if(studyID==null || studyID.isEmpty()){
            try {
                studyID =  IdGenerator.generateStudyId();
                //logger.info(studyID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            patientStudy.setStudyId(studyID);
        }

        //logger.info(studyID);
        patientStudyEntity.setStudyId(studyID);

        patientStudyEntity.setReadingPhysician(patientStudy.getReadingPhysician());
        patientStudyEntity.setReferringPhysician(patientStudy.getReferringPhysician());
        patientStudyEntity.setPerformingPhysician(patientStudy.getPerformingPhysician());

        String institution ="";
        try {
            institution = SystemInfoService.getInstance().getSystemInfo().getFirst().getInstitutionName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        patientStudyEntity.setInstitution(institution);
        patientStudyEntity.setStudyUid(patientStudy.getStudyUid());
        patientStudyEntity.setInstanceId(patientBodyPart.getInstanceId());
        patientStudyEntity.setInstanceUid(patientBodyPart.getInstanceUid());
        patientStudyEntity.setSeriesId(patientBodyPart.getSeriesId());
        patientStudyEntity.setSeriesUid(patientBodyPart.getSeriesUid());
        patientStudyEntity.setIsRejected(patientStudy.isRejected());
        patientStudyEntity.setIsPrinted(false);
        patientStudyEntity.setIsDicomUploaded(false);
        patientStudyEntity.setIsBackedUp(false);
        patientStudyEntity.setIsCleaned(false);


        List<File> outputfiles = createStudyFile(patientStudy);
        try {
            if(patientStudyService.findPatientByStudyId(patientStudy.getStudyId())==null){
                patientStudyService.savePatient(patientStudyEntity);
            }else{
                patientStudyService.updatePatient(patientStudy.getStudyId(), patientStudyEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        PatientStudyNode studyNode = (PatientStudyNode) patientStudyNode;
        String studySessionId = studyNode.getStudySessionId();
        PatientLocalListEntity patientLocalListEntity = patientLocalListService.findByStudySessionId(studySessionId);
        if(patientLocalListEntity!=null){
            patientLocalListEntity.setStudyId(patientStudyEntity.getStudyId());
            try {
                patientLocalListService.update(patientLocalListEntity.getId(),patientLocalListEntity);
                //logger.info("saving : {}"  , patientLocalListEntity.getStudyId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //logger.info("local list not exist .");
        }

        KryoSerializer.getInstance().serializeToFile(patientStudy,outputfiles.get(0));

        saveCurrentStudyBodyParts(patientStudy, outputfiles.get(1));
    }


    private void saveCurrentStudyBodyParts(PatientStudy patientStudy,File outputfile){
        BodyPartHandler bodyPartsHandler = patientStudy.getBodyPartHandler();


        List<StudyThumbNails> thumbNailsList = new ArrayList<>();
        for (TabNode tab : bodyPartsHandler.getAllTabs()){
            PatientBodyPart bodyPart = ((BodyPartNode)tab).getBodyPart();

            String studyId = patientStudy.getStudyId();
            String bodyPartName = bodyPart.getBodyPartName();
            String position = bodyPart.getBodyPartPosition();
            String seriesID = bodyPart.getSeriesId();
            String seriesUid = bodyPart.getSeriesUid();
            String instanceId = bodyPart.getInstanceId();
            String instanceUid = bodyPart.getInstanceUid();
            boolean isRejected = bodyPart.isRejected();
            boolean isExposed = bodyPart.isExposed();

            double windowWidth = 1;
            double windowLevel = 0;

            if(bodyPart.getImagingParams()!=null){
                 windowWidth = bodyPart.getImagingParams().getWindowWidth();
                 windowLevel = bodyPart.getImagingParams().getWindowLevel();
            }


            double kv = bodyPart.getXrayParams().getKV();
            double ma = bodyPart.getXrayParams().getmA();
            double mas = bodyPart.getXrayParams().getmAs();
            double ms = bodyPart.getXrayParams().getMs();
            LocalDateTime exposedDate = bodyPart.getExposureDateTime();

            BodyPartStudyEntity bodyPartStudyEntity = new BodyPartStudyEntity();
            bodyPartStudyEntity.setStudyId(studyId);
            bodyPartStudyEntity.setBodyPartName(bodyPartName);
            bodyPartStudyEntity.setPosition(position);
            bodyPartStudyEntity.setSeriesId(seriesID);
            bodyPartStudyEntity.setSeriesUid(seriesUid);
            bodyPartStudyEntity.setInstanceId(instanceId);
            bodyPartStudyEntity.setInstanceUid(instanceUid);
            bodyPartStudyEntity.setIsRejected(isRejected);
            bodyPartStudyEntity.setIsExposed(isExposed);
            bodyPartStudyEntity.setWindowWidth(windowWidth);
            bodyPartStudyEntity.setWindowLevel(windowLevel);
            bodyPartStudyEntity.setKv(kv);
            bodyPartStudyEntity.setMa(ma);
            bodyPartStudyEntity.setMas(mas);
            bodyPartStudyEntity.setMs(ms);
            bodyPartStudyEntity.setExposedDate(exposedDate);

           StudyThumbNails studyThumbNails = new StudyThumbNails(UUID.randomUUID().toString(),bodyPartName+ " " + position ,bodyPart.getThumbNail());
           thumbNailsList.add(studyThumbNails);

            try {
                bodyPartStudyService.save(bodyPartStudyEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


        try {
            KryoSerializer.getInstance().serializeMatToFile(thumbNailsList,outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<File> createStudyFile(PatientStudy patientStudy) {
        String baseDir = System.getProperty("user.dir");
        String patientId = patientStudy.getPatientInfo().getPatientId();
        String directoryPath = String.format("%s/output/%s", baseDir, patientId);

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = patientStudy.getStudyId() + ".rxv";
        String fileName2 = patientStudy.getStudyId() + "_thumbnails.rxv";

        List<File> files = new ArrayList<>();

        files.add(new File(directory, fileName));
        files.add(new File(directory, fileName2));

        return files;
    }


    public void editSelectedBodyPart() {
        if(patientStudyHandler.getCurrentStudy()==null || patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart()==null){
            return;
        }

        PatientBodyPart patientBodyPart = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart().getBodyPart();

        //navigate to bodyParts
        //StudyMainController.getInstance().bodyClick();


        try {
            Screen secondaryScreen = Screen.getScreens().getFirst();
            Rectangle2D screenBounds = secondaryScreen.getVisualBounds();

            double scaleX = 1;
            double scaleY = 1;

            double parentHeight = screenBounds.getHeight();
            double parentWidth = screenBounds.getWidth();

            double ratio = parentWidth / parentHeight;
            double targetRatio = (double) 700 / 400;

            if (targetRatio < ratio) {
                scaleX = parentHeight / 400;
                scaleY = parentHeight / 400;
            } else if (targetRatio > ratio) {
                scaleX = parentWidth / 700;
                scaleY = parentWidth / 700;
            } else {
                scaleX = parentWidth / 700;
                scaleY = parentWidth / 700;
            }

            double newWidth = 700 * scaleX * 0.4;
            double newHeight = 400 * scaleY * 0.4;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StudyProjectionEditPage.fxml"));
            Parent root1 = fxmlLoader.load();

            // Apply a clip to make the stage rounded
            double radius = 50; // Adjust this value to control curvature
            Rectangle clip = new Rectangle(newWidth, newHeight);
            clip.setArcWidth(radius);
            clip.setArcHeight(radius);
            root1.setClip(clip);

            // Set the root node background to transparent
            root1.setStyle("-fx-background-color: transparent;");

            // Create the scene and set its fill to transparent
            Scene scene = new Scene(root1, newWidth, newHeight);
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent
            stage.setTitle("Edit Window");
            stage.setScene(scene);
            stage.show();

            // Center the stage on the secondary screen
            stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2);

        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}