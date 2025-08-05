package com.raymedis.rxviewui.service.study;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageTools;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.ImageDrawWrapperHandling;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.StudyOverlayService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.dicom.DicomService;
import com.raymedis.rxviewui.modules.serialPortCom.SerialPortCommunicationService;
import com.raymedis.rxviewui.modules.serialPortCom.SerialPortManager;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.PatientStudyNode;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.export.ExportWindowController;
import com.raymedis.rxviewui.service.print.PrintController;
import com.raymedis.rxviewui.service.print.PrintInputCreator;
import com.raymedis.rxviewui.service.print.PrintService;
import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudyMainController {

    private static final StudyMainController instance = new StudyMainController();
    public ViewBox overlayViewBox;
    public Pane canvasPane;

    public static StudyMainController getInstance(){
        return instance;
    }

    public HBox tabPane;

    public VBox bodyPartTabPane;
    public VBox BodyToolsContainer;
    public VBox consoleToolBox;

    public JFXButton bodyButton;
    public JFXButton toolsButton;
    public JFXButton selectButton;
    public JFXButton zoomButton;
    public JFXButton panButton;
    public JFXButton brightnessButton;
    public JFXButton contrastButton;
    public JFXButton cropButton;
    public JFXButton flipButton;
    public JFXButton inversionButton;
    public JFXButton cwButton;
    public JFXButton ccwButton;
    public JFXButton fitButton;
    public JFXButton distanceButton;
    public JFXButton angleButton;
    public JFXButton rightLabelButton;
    public JFXButton leftLabelButton;
    public JFXButton hideButton;
    public JFXButton resetButton;
    public JFXButton deleteButton;
    public JFXButton textButton;
    public JFXButton aec1Button;
    public JFXButton aec2Button;
    public JFXButton aec3Button;
    public JFXButton exposeButton;
    public JFXButton consoleOnButton;
    public JFXButton consoleOffButton;
    public JFXButton aedReadyButton;
    public StackPane canvasStackPane;
    public JFXButton editProjection;
    public JFXButton bodyPartDeleteButton;
    public JFXButton kvIncButton;
    public JFXButton kvDecButton;
    public JFXButton maDecButton;
    public JFXButton maIncButton;
    public JFXButton masDecButton;
    public JFXButton masIncButton;
    public JFXButton msDecButton;
    public JFXButton msIncButton;
    public Canvas drawingCanvas;
    public Pane textPane;
    public VBox toolsBox;

    public FontIcon aedReadyLabel;

    public Label kvValueLabel;
    public Label msValueLabel;
    public Label maValueLabel;
    public Label masValueLabel;

    public Parent bodyParent;
    public Parent toolsParent;
    public ViewBox canvasViewBox;

    public VBox topLeftOverlayBox;
    public VBox topRightOverlayBox;
    public VBox bottomLeftOverlayBox;
    public VBox bottomRightOverlayBox;

    public JFXButton exportButton;
    public JFXButton printButton;
    public static JFXButton editButton;
    public JFXButton emailButton;
    public JFXButton rejectStudyButton;

    public List<JFXButton> buttonList;
    public GridPane overlayGridPane;

    public JFXToggleButton lbdButton;
    public JFXToggleButton masButton;

    public FontIcon faultIcon;

    //fetch Values and intervals from database
    double kvValue;
    double maValue;
    double masValue;
    double msValue;

    private static final Logger logger = LoggerFactory.getLogger(StudyMainController.class);
    private final ImageTools imageTools = ImageTools.getInstance();
    private final StudyOverlayService studyOverlayService = StudyOverlayService.getInstance();
    private final List<TabNode> saveStudyList = new ArrayList<>();
    private ArrayList<PrintOverlayEntity> studyOverlayList = null;
    private SystemInfoEntity systemInfo =null;
    private final static DicomService dicomService = DicomService.getInstance();
    public static boolean isEdit = false;
    private final StepService stepService = StepService.getInstance();



    public void applyStudyOverLay() {
        topLeftOverlayBox.getChildren().clear();
        topRightOverlayBox.getChildren().clear();
        bottomLeftOverlayBox.getChildren().clear();
        bottomRightOverlayBox.getChildren().clear();


       if(StudyService.patientStudyHandler.getCurrentStudy()!=null){
           PatientStudy study = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy();
           for(PrintOverlayEntity overlay : studyOverlayList){

               String contentVal = "";

               switch (overlay.getItemContent().toLowerCase()) {
                   case "patient id":
                       contentVal = study.getPatientInfo().getPatientId();
                       break;
                   case "patient name":
                       contentVal = study.getPatientInfo().getPatientName();
                       break;
                   case "birth date":
                       contentVal = study.getPatientInfo().getBirthDate().toString();
                       break;
                   case "sex":
                       contentVal = study.getPatientInfo().getSex();
                       break;
                   case "age":
                       contentVal = study.getPatientInfo().getAge() + "";
                       break;
                   case "gray value":
                       //contentVal = study.getGrayValue() + "";
                       break;
                   case "histogram":
                       //contentVal = study.getHistogram();
                       break;
                   case "dose kvp":
                       //contentVal = study.getDoseKvp() + "";
                       break;
                   case "dose ma":
                       //contentVal = study.getDoseMa() + "";
                       break;
                   case "dose ms":
                       //contentVal = study.getDoseMs() + "";
                       break;
                   case "dose mas":
                       // contentVal = study.getDoseMas() + "";
                       break;
                   case "exposure index":
                       // contentVal = study.getExposureIndex() + "";
                       break;
                   case "exposure indec(ugy)":
                       //contentVal = study.getExposureIndecUgy() + "";
                       break;
                   case "deviation index":
                       //contentVal = study.getDeviationIndex() + "";
                       break;
                   case "target exposure index":
                       //contentVal = study.getTargetExposureIndex() + "";
                       break;
                   case "target exposure index(ugy)":
                       // contentVal = study.getTargetExposureIndexUgy() + "";
                       break;
                   case "dap(dgy.cm^2)":
                       //contentVal = study.getDapDgyCm2() + "";
                       break;
                   case "dap(mgy.cm^2)":
                       // contentVal = study.getDapMgyCm2() + "";
                       break;
                   case "dap(ugy.m^2)":
                       //contentVal = study.getDapUgyM2() + "";
                       break;
                   case "exposure date":
                       //contentVal = study.getExposureDate().toString();
                       break;
                   case "exposure time":
                       // contentVal = study.getExposureTime().toString();
                       break;
                   case "performing physcian":
                       contentVal = study.getPerformingPhysician();
                       break;
                   case "refering physcian":
                       contentVal = study.getReferringPhysician();
                       break;
                   case "series number - instance number":
                       //contentVal = study.getSeriesNumber() + " - " + study.getInstanceNumber();
                       break;
                   case "bodypart projection":
                       contentVal = study.getBodyPartHandler().getCurrentBodyPart().getBodyPart().getBodyPartName() + " " + study.getBodyPartHandler().getCurrentBodyPart().getBodyPart().getBodyPartPosition();
                       break;
                   case "study date", "study time":
                       contentVal = study.getStudyDateTime()+"";
                       break;
                   case "screen zoom":
                       //contentVal = study.getScreenZoom() + "";
                       break;
                   case "pixel zoom":
                       // contentVal = study.getPixelZoom() + "";
                       break;
                   case "accession number":
                       contentVal = study.getAccessionNum();
                       break;
                   case "laterality":
                       // contentVal = study.getLaterality();
                       break;
                   case "modality":
                       contentVal = study.getModality();
                       break;
                   case "height":
                       contentVal = study.getPatientInfo().getHeight() + "";
                       break;
                   case "weight":
                       contentVal = study.getPatientInfo().getWeight() + "";
                       break;
                   case "institutional name":
                       contentVal = systemInfo.getInstitutionName();
                       break;
                   case "institutional adress":
                       contentVal = systemInfo.getInstitutionAddress();
                       break;
                   case "institutional dept name":
                       contentVal = systemInfo.getDepartment();
                       break;
                   case "manufacturer":
                       contentVal = systemInfo.getManufacturer();
                       break;
                   case "manufacturer model name":
                       contentVal = systemInfo.getModelName();
                       break;
                   case "station name":
                       contentVal = systemInfo.getSerialNumber();
                       break;
                   case "other patient id":
                       //contentVal = study.getPatientInfo().getOtherPatientId();
                       break;
                   case "image orientation":
                       //contentVal = study.getImageOrientation();
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
               double fontSize = DynamicCanvasElementsResize.getInfoLabelFontSize();

               String fontFamily = "Century Gothic";

               Label label = new Label(content);
               label.setStyle(
                       "-fx-font-family: '" + fontFamily + "';" +
                               "-fx-font-weight: bold;" +
                               "-fx-font-size: " + fontSize + "px;" +
                               "-fx-text-fill: white;" +
                               "-fx-padding: 0;"
               );



               switch (overlay.getPositionType()) {
                   case PositionType.TOP_LEFT:
                       topLeftOverlayBox.getChildren().add(label);
                       break;
                   case PositionType.TOP_RIGHT:
                       topRightOverlayBox.getChildren().add(label);
                       break;
                   case PositionType.BOTTOM_LEFT:
                       bottomLeftOverlayBox.getChildren().add(label);
                       break;
                   case PositionType.BOTTOM_RIGHT:
                       bottomRightOverlayBox.getChildren().add(label);
                       break;
                   default:
                       logger.info("");
               }
           }
       }
    }


    public void studyLoad(){
        toolsButton.setDisable(true);

        bodyClick();
        setupImageToolsBtn();

        kvValueLabel.textProperty().bindBidirectional(SerialPortManager.getInstance().kv, new javafx.util.converter.NumberStringConverter());
        maValueLabel.textProperty().bindBidirectional(SerialPortManager.getInstance().ma, new javafx.util.converter.NumberStringConverter());
        msValueLabel.textProperty().bindBidirectional(SerialPortManager.getInstance().ms, new javafx.util.converter.NumberStringConverter());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        NumberStringConverter converter = new NumberStringConverter(decimalFormat);

        masValueLabel.textProperty().bindBidirectional(
                SerialPortManager.getInstance().mas,
                converter
        );

        consoleOnUi();

        try {
            studyOverlayList = studyOverlayService.findAll();
            if(!SystemInfoService.getInstance().getSystemInfo().isEmpty()){
                systemInfo = SystemInfoService.getInstance().getSystemInfo().getFirst();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isHolding = false;
    public void loadEvents() {
        kvIncButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> kvIncreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        kvIncButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });

        kvDecButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> kvDecreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        kvDecButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });


        maIncButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> maIncreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        maIncButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });

        maDecButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> maDecreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        maDecButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });


        masIncButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> masIncreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        masIncButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });

        masDecButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> masDecreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        masDecButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });

        msIncButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> msIncreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        msIncButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });

        msDecButton.setOnMousePressed(event -> {
            // Delay timer: starts the repeating increment after 500ms
            holdDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                // This timer repeats the increment every 50ms
                holdTimer = new Timeline(new KeyFrame(Duration.millis(50), ev -> msDecreaseClick()));
                holdTimer.setCycleCount(Timeline.INDEFINITE);
                holdTimer.play();
            }));
            holdDelay.setCycleCount(1);
            holdDelay.play();
        });

        msDecButton.setOnMouseReleased(event -> {
            if (holdDelay != null) holdDelay.stop();
            if (holdTimer != null) holdTimer.stop();
        });


    }


    private void setupImageToolsBtn() {
        imageTools.selectButton = selectButton;
        imageTools.zoomButton = zoomButton;
        imageTools.panButton = panButton;
        imageTools.brightnessButton = brightnessButton;
        imageTools.contrastButton = contrastButton;
        imageTools.cropButton = cropButton;
        imageTools.hFlipButton = flipButton;
        imageTools.inversionButton = inversionButton;
        imageTools.clockwiseRotationButton = cwButton;
        imageTools.counterClockWiseRotationButton = ccwButton;
        imageTools.fitButton = fitButton;
        imageTools.distanceButton = distanceButton;
        imageTools.angleButton = angleButton;
        imageTools.rightLabelButton = rightLabelButton;
        imageTools.leftLabelButton = leftLabelButton;
        imageTools.hideButton = hideButton;
        imageTools.deleteButton = deleteButton;
        imageTools.resetButton=resetButton;
        imageTools.textButton = textButton;
        imageTools.canvasStackPane = canvasStackPane;
        imageTools.canvasPane = canvasPane;
        imageTools.drawingCanvas = drawingCanvas;
        imageTools.textPane = textPane;
        imageTools.toolsBox=toolsBox;
        imageTools.canvasViewBox=canvasViewBox;
        imageTools.setUiTools();
    }

    public void newStudyProjectionUi(){
        drawingCanvas.setVisible(false);
        topLeftOverlayBox.getChildren().clear();
        topRightOverlayBox.getChildren().clear();
        bottomLeftOverlayBox.getChildren().clear();
        bottomRightOverlayBox.getChildren().clear();
        toolsBox.setDisable(true);
        toolsButton.setDisable(true);
        ImageTools.getInstance().resetUi();
        CanvasMainChange.mainDrawTool.resetAllDrawingTools();
        //consoleOffClick();
    }

    public void rejectedStudyProjection(){

    }

    public void exposedStudyProjection(PatientBodyPart currentBodyPart){
        Platform.runLater(()->{
            toolsBox.setDisable(currentBodyPart.isRejected());
            drawingCanvas.setVisible(true);
            toolsButton.setDisable(false);
            editProjection.setDisable(false);
            //consoleOffClick();
            ImageDrawWrapperHandling imageDrawWrapperHandling = new ImageDrawWrapperHandling(currentBodyPart);
            imageDrawWrapperHandling.applyAllDrawingTools();
            CanvasMainChange.mainDrawTool.applyAllImageParams(currentBodyPart,currentStepEntity);


            XrayParams xrayParams = currentBodyPart.getXrayParams();

            if(xrayParams!=null){
                kvValue  = xrayParams.getKV();
                maValue  = xrayParams.getmA();
                masValue = xrayParams.getmAs();
                msValue  = xrayParams.getMs();

                kvValueLabel.setText(String.valueOf(kvValue));
                maValueLabel.setText(String.valueOf(maValue));
                masValueLabel.setText(String.valueOf(masValue));
                msValueLabel.setText(String.valueOf(msValue));
            }
        });
    }


    private StepEntity currentStepEntity = null;
    public void loadConsoleValues(PatientBodyPart currentBodyPart) {
        ArrayList<StepEntity> stepEntityArrayList = stepService.findByStepName(currentBodyPart.getBodyPartName() + " " + currentBodyPart.getBodyPartPosition());
        PatientStudy patientStudy = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy();
        if(!stepEntityArrayList.isEmpty()){
            for (StepEntity stepEntity:stepEntityArrayList){
                if(stepEntity.getPatientSize().equals(patientStudy.getPatientInfo().getPatientSize())){

                    //logger.info("stepEntity {}",stepEntity);

                    currentStepEntity = stepEntity;

                     kvValue  = stepEntity.getConsoleKv();
                     maValue  = stepEntity.getConsoleMa();
                     masValue = stepEntity.getConsoleMas();
                     msValue  = stepEntity.getConsoleMs();

                    kvValueLabel.setText(String.valueOf(kvValue));
                    maValueLabel.setText(String.valueOf(maValue));
                    masValueLabel.setText(String.valueOf(masValue));
                    msValueLabel.setText(String.valueOf(msValue));
                    break;
                }
            }
        }
    }

    public void resetProcessingTools() {
        toolsButton.getStyleClass().removeAll("selectedToolsButton", "unSelectedToolsButton");
        toolsButton.getStyleClass().add("unSelectedToolsButton");

        bodyButton.getStyleClass().removeAll("selectedToolsButton", "unSelectedToolsButton");
        bodyButton.getStyleClass().add("unSelectedToolsButton");

    }

    public void bodyClick() {
        resetProcessingTools();
        BodyToolsContainer.getChildren().setAll(bodyParent);
        bodyButton.getStyleClass().remove("unSelectedToolsButton");
        bodyButton.getStyleClass().add("selectedToolsButton");
    }

    public void toolsClick() {
        resetProcessingTools();
        BodyToolsContainer.getChildren().setAll(toolsParent);
        toolsButton.getStyleClass().remove("unSelectedToolsButton");
        toolsButton.getStyleClass().add("selectedToolsButton");
    }

    public void selectClick() {
        imageTools.selectClick();
    }

    public void zoomClick() {
        imageTools.zoomClick();
    }

    public void panClick() {
       imageTools.panClick();
    }

    public void brightnessClick() {
        imageTools.imageBrightness();
    }

    public void contrastClick() {
        imageTools.contrastClick();
    }

    public void cropClick() {
       imageTools.cropClick();
    }

    public void flipClick() {
        imageTools.hFlipClick();
    }

    public void windowLevelClick() {
        imageTools.inversionClick();
    }

    public void cwClick() {
        imageTools.clockwiseRotation();
    }

    public void ccwClick() {
        imageTools.counterClockwiseRotation();
    }

    public void fitClick() {
       imageTools.fitClick();
    }

    public void distanceClick() {
       imageTools.distanceClick();
    }

    public void angleClick() {
       imageTools.angleClick();
    }

    public void rightLabelClick() {
        imageTools.rightLabelClick();
    }

    public void leftLabelClick() {
        imageTools.leftLabelClick();
    }

    public void hideClick() {
      imageTools.hideClick();
    }

    public void deleteClick() {
        imageTools.deleteClick();
    }

    public void textClick() {
        textButton.getParent().requestFocus();
       imageTools.textClick();
    }

    public void resetClick() {
        imageTools.resetClick();
    }

    public void resetAecButtons() {
        aec1Button.getStyleClass().removeAll("selectedAecButtons", "unSelectedAecButtons");
        aec1Button.getStyleClass().add("unSelectedAecButtons");

        aec2Button.getStyleClass().removeAll("selectedAecButtons", "unSelectedAecButtons");
        aec2Button.getStyleClass().add("unSelectedAecButtons");

        aec3Button.getStyleClass().removeAll("selectedAecButtons", "unSelectedAecButtons");
        aec3Button.getStyleClass().add("unSelectedAecButtons");
    }

    public void aec1Click() {
        if (aec1Button.getStyleClass().contains("selectedAecButtons")) {
            aec1Button.getStyleClass().remove("selectedAecButtons");
            aec1Button.getStyleClass().add("unSelectedAecButtons");
        } else {
            aec1Button.getStyleClass().remove("unSelectedAecButtons");
            aec1Button.getStyleClass().add("selectedAecButtons");
        }
    }

    public void aec2Click() {
        if (aec2Button.getStyleClass().contains("selectedAecButtons")) {
            aec2Button.getStyleClass().remove("selectedAecButtons");
            aec2Button.getStyleClass().add("unSelectedAecButtons");
        } else {
            aec2Button.getStyleClass().remove("unSelectedAecButtons");
            aec2Button.getStyleClass().add("selectedAecButtons");
        }
    }

    public void aec3Click() {
        if (aec3Button.getStyleClass().contains("selectedAecButtons")) {
            aec3Button.getStyleClass().remove("selectedAecButtons");
            aec3Button.getStyleClass().add("unSelectedAecButtons");
        } else {
            aec3Button.getStyleClass().remove("unSelectedAecButtons");
            aec3Button.getStyleClass().add("selectedAecButtons");
        }
    }


    public void exposeClick() {

        exposeButton.setDisable(true);
        bodyPartTabPane.setDisable(true);
        tabPane.setDisable(true);
        NavConnect.getInstance().disableNav(true);
        if(!StudyService.imageStatus){
            StudyService.imageStatus=true;
        }

       /* triggerExposed();
        serialPortCommunicationService.expose();*/
    }

    private volatile boolean isExposeTimerReset = false;
    private long startTimeMillis;
    private void triggerExposed(){
        isExposeTimerReset = false;
        startTimeMillis = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            try {

                while(!isExposeTimerReset){
                    long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
                    if(elapsedTimeMillis>15000){
                        isExposeTimerReset = true;
                        logger.info("Exposure timer reset after 15 seconds.");
                        Platform.runLater(() -> {
                            exposeButton.getStyleClass().add("exposeButton");
                        });
                        break;
                    }

                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private final SerialPortCommunicationService serialPortCommunicationService = SerialPortCommunicationService.getInstance();
    public void consoleOnClick() {
        // Run the heavy work in a background thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                List<Integer> availableList = serialPortCommunicationService.getAvailablePorts();
                if (!availableList.isEmpty()) {
                    serialPortCommunicationService.initializeSerialPort(availableList.getFirst());
                }
                return null;
            }
        };

        // Optional: Handle errors
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace(); // Or show error in UI
        });

        task.setOnSucceeded(e->{
           logger.info("done");
        });

        // Start task in background thread
        new Thread(task).start();
    }


    public void consoleOnUi(){
        logger.info("Console is ON");
        Platform.runLater(()->{
            consoleOnButton.setDisable(true);
            consoleOffButton.setDisable(false);
            consoleToolBox.setDisable(false);
        });
    }

    public void consoleOffClick() {

        //serialPortCommunicationService.disconnectSerialPort();

        consoleOnButton.setDisable(false);
        consoleOffButton.setDisable(true);
        consoleToolBox.setDisable(true);
    }


    private final SerialPortManager serialPortManager = SerialPortManager.getInstance();

    private double currentKv;
    private double currentMa;
    private double currentMas;
    private double currentMs;

    public void kvDecreaseClick() {
        currentKv = kvValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(kvValueLabel.getText());
        currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
        logger.info("DecKv KV: {}, MA: {}", currentKv, currentMa);
        serialPortManager.decKv(currentKv,currentMa);
    }


    private Timeline holdTimer;
    private Timeline holdDelay;

    public void kvIncreaseClick() {
         currentKv = kvValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(kvValueLabel.getText());
         currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
        logger.info("IncKv KV: {},  MA: {}", currentKv, currentMa);
        serialPortManager.incKv(currentKv,currentMa);
    }

    public void maDecreaseClick() {
        if(!masButton.isSelected()) {
            currentKv = kvValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(kvValueLabel.getText());
            currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
            logger.info("DecMa kv: {},  MA: {}", currentKv, currentMa);
            serialPortManager.decMA(currentKv, currentMa);
        }
    }

    public void maIncreaseClick() {
        if(!masButton.isSelected()) {
            currentKv = kvValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(kvValueLabel.getText());
            currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
            logger.info("IncMa kv: {},  MA: {}", currentKv, currentMa);
            serialPortManager.incMa(currentKv, currentMa);
        }
    }

    public void masDecreaseClick() {
        currentMas = masValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(masValueLabel.getText());
        currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
        logger.info("decMas mas: {},  MA: {}", currentMas, currentMa);
        serialPortManager.decmAs(currentMa, currentMas);
    }

    public void masIncreaseClick() {
        currentMas = masValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(masValueLabel.getText());
        currentMa = maValueLabel.getText().isEmpty() ? 0 : Double.parseDouble(maValueLabel.getText());
        logger.info("IncMas mas: {},  MA: {}", currentMas, currentMa);
        serialPortManager.incmAs(currentMa, currentMas);
    }

    public void msDecreaseClick() {

        if(!masButton.isSelected()){
            serialPortManager.decMs();
        }

    }

    public void msIncreaseClick() {
        if(!masButton.isSelected()){
            serialPortManager.incMs();
        }
    }

    public void deleteSelectedBodyPart() {
        StudyService.getInstance().deleteSelectedBodyPartButtonClick();
    }

    public void saveAllStudies() throws IOException {
        saveStudyList.clear();
        saveStudyList.addAll(StudyService.patientStudyHandler.getAllTabs());
        StudyService.getInstance().saveAllStudyTabs(saveStudyList);
    }

    public void saveCurrentStudy() throws IOException {
        saveStudyList.clear();
        saveStudyList.add(StudyService.patientStudyHandler.getCurrentStudy());
        StudyService.getInstance().saveAllStudyTabs(saveStudyList);
    }

    public void resetAedReadyButton(){
        aedReadyButton.setId("aedUnselectedReadyButton");
        aedReadyLabel.setId("aedUnselectedReadyIcon");
    }

    public void aedReadyClick() {
        if(aedReadyButton.getId().equals("aedSelectedReadyButton")){
            aedReadyButton.setId("aedUnselectedReadyButton");
            aedReadyLabel.setId("aedUnselectedReadyIcon");
        }
        else{
            aedReadyButton.setId("aedSelectedReadyButton");
            aedReadyLabel.setId("aedSelectedReadyIcon");
        }
    }

    public void exportClick() {
        LocalDateTime exposedDate = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getExposedDateTime();
        if(exposedDate==null){
            return;
        }

        //save the all params and image annotations
        logger.info("saving for export...");
        StudyService.getInstance().handleBodyPartTabButtonAction(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart());


        try {
            Screen secondaryScreen = Screen.getScreens().getFirst();
            Rectangle2D screenBounds = secondaryScreen.getVisualBounds();

            double scaleX;
            double scaleY;

            double parentHeight = screenBounds.getHeight();
            double parentWidth = screenBounds.getWidth();

            double ratio = parentWidth / parentHeight;
            double targetRatio = (double) 800 / 700;

            if (targetRatio < ratio) {
                scaleX = parentHeight / 700;
                scaleY = parentHeight / 700;
            } else if (targetRatio > ratio) {
                scaleX = parentWidth / 800;
                scaleY = parentWidth / 800;
            } else {
                scaleX = parentWidth / 800;
                scaleY = parentWidth / 800;
            }

            double newWidth = 800 * scaleX * 0.7;
            double newHeight = 700 * scaleY * 0.7;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ExportWindow.fxml"));
            Parent root1 = fxmlLoader.load();

            ExportWindowController exportWindowController = ExportWindowController.getInstance();
            exportWindowController.loadData(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy(),"study");

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
            stage.setTitle("Export Window");
            stage.setScene(scene);
            stage.show();

            // Center the stage on the secondary screen
            stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2);

        } catch (IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    public void pacsServerClick() {
        LocalDateTime exposedDate = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getExposedDateTime();
        if(exposedDate==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "0 Exposed Studies Found", ButtonType.OK);
            alert.setTitle("PACS Server");
            alert.initOwner(getCurrentStage());
            alert.showAndWait();
            return;
        }


        //save the all params and image annotations
        logger.info("Saving for Pacs...");
        StudyService.getInstance().handleBodyPartTabButtonAction(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart());


        try {
            dicomService.loadData(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy());
            dicomService.uploadToStorageServer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Stage getCurrentStage() {
        return (Stage) exposeButton.getScene().getWindow();
    }

    public void printClick() {
        if(StudyService.patientStudyHandler.getCurrentStudy()==null || StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getExposedDateTime()==null){
            return;
        }

        PatientStudyNode currentStudyNode = StudyService.patientStudyHandler.getCurrentStudy();

        try {
            StudyService.getInstance().saveForPrint(currentStudyNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintController.getInstance().loadData();
        PrintService.getInstance().loadStudyData(PrintInputCreator.getInstance().createPrintInput(currentStudyNode.getPatientStudy()));


        NavConnect.getInstance().navigateToPrint();
    }

    public void editClick() {
        toggleEditButton(true);
        NavConnect.getInstance().navigateToRegisterMain();
        NavConnect.getInstance().navigateToManual();
        ManualRegisterController.getInstance().loadRegisterForEdit(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy());
    }

    public static void toggleEditButton(boolean b){
        editButton.setDisable(b);
        isEdit=b;
    }

    public void emailClick() {

    }

    public void rejectStudyClick() {
        PatientStudy patientStudy = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy();
        patientStudy.setRejected(true);

        try {
            saveCurrentStudy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editSelectedBodyPart() {
        StudyService.getInstance().editSelectedBodyPart();
    }


    public void setConsoleXrayValues(PatientBodyPart currentBodyPart) {

        //uncomment when console in integrated
       /* if (!SerialPortCommunicationService.getInstance().initialized) {
            return;
        }*/

        exposeButton.setDisable(false);

        String bodyPartName = currentBodyPart.getBodyPartName();
        String projectionType = currentBodyPart.getBodyPartPosition();
        logger.info("Setting console values for body part: {}", bodyPartName);

        switch (bodyPartName.toLowerCase()) {
            case "chest":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(80, 80, 8.0);
                }
                else if (projectionType.contains("AP") || projectionType.contains("PA") || projectionType.contains("Obl")) {
                    serialPortManager.setXrayConsole(70, 80, 4.0);
                }
                else {
                    serialPortManager.setXrayConsole(70, 80, 4.0);
                }
                break;

            case "hand":
                serialPortManager.setXrayConsole(60, 80, 3.0);
                break;

            case "ankle", "foot":
                serialPortManager.setXrayConsole(60, 100, 5.0);
                break;

            case "elbow", "knee", "toe":
                serialPortManager.setXrayConsole(60, 100, 3.0);
                break;

            case "shoulder":
                serialPortManager.setXrayConsole(65, 100, 10.0);
                break;

            case "tibia":
                serialPortManager.setXrayConsole(60, 80, 2.0);
                break;

            case "wrist":
                serialPortManager.setXrayConsole(50, 80, 2.0);
                break;

            case "femur","calvicle","hip":
                serialPortManager.setXrayConsole(70, 100, 16.0);
                break;

            case "scapula":
                serialPortManager.setXrayConsole(70, 100, 10.0);
                break;

            case "sinuses":
                serialPortManager.setXrayConsole(80, 80, 32.0);
                break;

            case "patella":
                serialPortManager.setXrayConsole(60, 100, 4.0);
                break;

            case "zygomatic", "c-spine":
                serialPortManager.setXrayConsole(65, 100, 5.0);
                break;

            case "abdomen":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(80, 80, 10.0);
                } else if (projectionType.contains("AP")) {
                    serialPortManager.setXrayConsole(75, 80, 16.0);
                } else {
                    serialPortManager.setXrayConsole(70, 100, 16.0);
                }
                break;

            case "pelvis":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(85, 80, 64.0);
                } else {
                    serialPortManager.setXrayConsole(70, 100, 16.0);
                }
                break;

            case "coccyx":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(85, 80, 25.0);
                } else if (projectionType.contains("AP")) {
                    serialPortManager.setXrayConsole(75, 80, 20.0);
                } else {
                    serialPortManager.setXrayConsole(75, 80, 16.0);
                }
                break;

            case "skull":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(70, 100, 8.0);
                } else if (projectionType.contains("PA")) {
                    serialPortManager.setXrayConsole(80, 80, 32.0);
                } else if (projectionType.contains("AP")) {
                    serialPortManager.setXrayConsole(75, 100, 16.0);
                } else {
                    serialPortManager.setXrayConsole(80, 80, 10.0);
                }
                break;

            case "l-spine":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(85, 80, 25.0);
                } else if (projectionType.contains("Obl")) {
                    serialPortManager.setXrayConsole(75, 80, 32.0);
                } else {
                    serialPortManager.setXrayConsole(80, 80, 20.0);
                }
                break;

            case "t-spine":
                if (projectionType.contains("LAT")) {
                    serialPortManager.setXrayConsole(80, 80, 40.0);
                } else {
                    serialPortManager.setXrayConsole(75, 80, 16.0);
                }
                break;

            default:
                logger.warn("Unrecognized body part: {}", bodyPartName);
                break;
        }
    }

    public void toggleLbdClick() {
        //serialPortCommunicationService.setLbd(lbdButton.isSelected());
    }





}
