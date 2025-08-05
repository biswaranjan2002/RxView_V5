package com.raymedis.rxviewui.controller;


import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.PatientStudyNode;
import com.raymedis.rxviewui.service.adminSettings.backup.BackupCleanController;
import com.raymedis.rxviewui.service.adminSettings.backup.BackupController;
import com.raymedis.rxviewui.service.adminSettings.backup.BackupRestoreController;
import com.raymedis.rxviewui.service.adminSettings.dicom.*;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerCategoryController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProcedureController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProjectionController;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerStepController;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationGeneralController;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationManualController;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationPhysicianController;
import com.raymedis.rxviewui.service.adminSettings.studySettings.RejectedListController;
import com.raymedis.rxviewui.service.adminSettings.studySettings.RejectedReasonController;
import com.raymedis.rxviewui.service.adminSettings.studySettings.StudyDeleteController;
import com.raymedis.rxviewui.service.adminSettings.studySettings.StudyOverlayController;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemAccountController;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemInfoController;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemThemesController;
import com.raymedis.rxviewui.service.database.DatabaseController;
import com.raymedis.rxviewui.service.fpdSdks.controller.FpdController;
import com.raymedis.rxviewui.service.fpdSdks.service.InnoCareService;
import com.raymedis.rxviewui.service.json.DetectorDetails;
import com.raymedis.rxviewui.service.login.LoginController;
import com.raymedis.rxviewui.service.study.StudyBodyPartsController;
import com.raymedis.rxviewui.service.study.StudyService;
import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class NavConnect {

    public GridPane contentNode;
    public GridPane footerNode;
    public GridPane workListManualContainer;

    public Parent registerFxmlContent;
    public Parent studyFxmlContent;
    public Parent dataBaseFxmlContent;
    public Parent printFxmlContent;
    public Parent footerFxmlContent;
    public Parent statsPageParent;
    public Parent comparePageParent;

    public Parent manualRegisterParent;
    public Parent worklistParent;
    public Parent localListParent;
    public Parent adminPageParent;

    public FontIcon studyPageIcon;
    public FontIcon dataBasePageIcon;
    public FontIcon printPageIcon;
    public FontIcon adminIcon;
    public FontIcon registerPageIcon;

    public Label adminLabel;
    public Label registerPage;
    public Label studyPage;
    public Label dataBasePage;
    public Label printPage;

    public JFXButton adminButton;

    public JFXButton manualButton;
    public JFXButton workListButton;
    public JFXButton emergencyButton;
    public JFXButton localListButton;

    public FontIcon workListPageIcon;
    public FontIcon emergencyPageIcon;
    public FontIcon localListPageIcon;
    public FontIcon manualPageIcon;

    public FontIcon fpd1FontIcon;
    public FontIcon fpd2FontIcon;
    public FontIcon fpd3FontIcon;
    public JFXButton registerPageButton;
    public JFXButton studyPageButton;
    public JFXButton databasePageButton;
    public JFXButton printPageButton;


    public Label pacsUploadStatusLabel;

    private final Logger logger = LoggerFactory.getLogger(NavConnect.class);


    public static NavConnect instance = new NavConnect();
    public FontIcon batteryFontIcon;
    public FontIcon wifiSignalFontIcon;
    public Label wifiLabel;
    public Label batteryLabel;


    public static NavConnect getInstance(){
        return instance;
    }


    public void  resetFooter() {
        setIconAndLabelStyle(adminIcon, adminLabel, "unSelectedFontIcon", "unSelectedLabel");
        setIconAndLabelStyle(registerPageIcon, registerPage, "unSelectedFontIcon", "unSelectedLabel");
        setIconAndLabelStyle(studyPageIcon, studyPage, "unSelectedFontIcon", "unSelectedLabel");
        setIconAndLabelStyle(dataBasePageIcon, dataBasePage, "unSelectedFontIcon", "unSelectedLabel");
        setIconAndLabelStyle(printPageIcon, printPage, "unSelectedFontIcon", "unSelectedLabel");
    }

    private void setIconAndLabelStyle(FontIcon icon, Label label, String iconStyle, String labelStyle) {
        // Remove both selected and unselected styles
        icon.getStyleClass().removeAll("SelectedFontIcon", "unSelectedFontIcon");
        label.getStyleClass().removeAll("SelectedLabel", "unSelectedLabel");

        // Add unselected styles back
        icon.getStyleClass().add(iconStyle);
        label.getStyleClass().add(labelStyle);
    }


    public void resetButtons() {
        manualButton.getParent().requestFocus();
        setButtonAndIconStyle(manualPageIcon, manualButton, "unSelectedFontIcon", "unSelectedButton");
        setButtonAndIconStyle(workListPageIcon, workListButton, "unSelectedFontIcon", "unSelectedButton");
        setButtonAndIconStyle(localListPageIcon, localListButton, "unSelectedFontIcon", "unSelectedButton");
    }

    private void setButtonAndIconStyle(FontIcon icon, Button button, String iconStyle, String buttonStyle) {
        // Remove both selected and unselected styles
        icon.getStyleClass().removeAll("selectedFontIcon", "unSelectedFontIcon");
        button.getStyleClass().removeAll("selectedButton", "unSelectedButton");

        // Add unselected styles
        icon.getStyleClass().add(iconStyle);
        button.getStyleClass().add(buttonStyle);
    }

    public void navigateToManual(){
        resetButtons();
        setButtonAndIconStyle(manualPageIcon, manualButton, "selectedFontIcon", "selectedButton");
        workListManualContainer.getChildren().setAll(manualRegisterParent);
    }

    public void navigateToWorkList(){
        resetButtons();
        setButtonAndIconStyle(workListPageIcon, workListButton, "selectedFontIcon", "selectedButton");
        workListManualContainer.getChildren().setAll(worklistParent);
    }

    public void navigateToLocalList(){
        resetButtons();
        setButtonAndIconStyle(localListPageIcon, localListButton, "selectedFontIcon", "selectedButton");
        workListManualContainer.getChildren().setAll(localListParent);
    }


    public void setFooter(){
        footerNode.getChildren().setAll(footerFxmlContent);
    }

    public void loadFpdStatus(DetectorDetails defaultDetector){
     /*   logger.info("index {}",LoginController.defaultDetectorIndex);*/


        Tooltip tooltip = new Tooltip(LoginController.defaultDetector.getSerial());
        /*logger.info(tooltip.getText());*/

        switch (LoginController.defaultDetectorIndex){
            case 1:
                fpd1FontIcon.getStyleClass().remove("unSelectedFontIcon");
                fpd1FontIcon.getStyleClass().add("fpdSelectedFontIcon");
                Tooltip.install(fpd1FontIcon, tooltip);
                break;
            case 2:
                fpd2FontIcon.getStyleClass().remove("unSelectedFontIcon");
                fpd2FontIcon.getStyleClass().add("fpdSelectedFontIcon");
                Tooltip.install(fpd2FontIcon, tooltip);
                break;
            case 3:
                fpd3FontIcon.getStyleClass().remove("unSelectedFontIcon");
                fpd3FontIcon.getStyleClass().add("fpdSelectedFontIcon");
                Tooltip.install(fpd3FontIcon, tooltip);
                break;
        }

        String batteryLevel = "10"; // FpdController.getInstance().getBatteryStatus();
        int signalStrength = Integer.parseInt(FpdController.getInstance().signalStrength());

        String signal;

        if(signalStrength>=0 && signalStrength<=25){
            signal = "1";
        }
        else if(signalStrength>25 && signalStrength<=50){
            signal="2";
        }
        else if(signalStrength>50 && signalStrength<=75){
            signal="3";
        }
        else if(signalStrength>75 && signalStrength<=100){
            signal="4";
        } else {
            signal = "outline";
        }

        Platform.runLater(()->{
            wifiSignalFontIcon.setIconLiteral("mdi2w-wifi-strength-" + signal);
            batteryFontIcon.setIconLiteral("mdi2b-battery-" + batteryLevel);
            wifiLabel.setText(signalStrength + "%");
            batteryLabel.setText(batteryLevel + "%");


            wifiSignalFontIcon.getStyleClass().remove("unSelectedFontIcon");
            wifiSignalFontIcon.getStyleClass().add("fpdSelectedFontIcon");
            batteryFontIcon.getStyleClass().remove("unSelectedFontIcon");
            batteryFontIcon.getStyleClass().add("fpdSelectedFontIcon");
        });


    }


    private void saveCurrentStudy() {
        PatientStudyNode patientStudyNode = StudyService.patientStudyHandler.getCurrentStudy();
        if(patientStudyNode!=null){
            StudyService.getInstance().handleBodyPartTabButtonAction(patientStudyNode.getPatientStudy().getBodyPartHandler().getCurrentBodyPart());
        }
    }

    public void navigateToRegisterMain() {

        saveCurrentStudy();

        resetFooter();
        setIconAndLabelStyle(registerPageIcon, registerPage, "SelectedFontIcon", "SelectedLabel");
        contentNode.getChildren().setAll(registerFxmlContent);

        /*Platform.runLater(()->{
            InnoCareService.innoCareNative.cancelOperationMode();
            InnoCareService.innoCareNative.exitOperationMode();
        });*/

        ManualRegisterController.getInstance().resetManualRegister();

    }


    public void navigateToStudy() {

        //PrintPageHandler.getInstance().getAllTabs().forEach(printPage -> System.out.println(printPage.getLayoutCode()));

        resetFooter();
        setIconAndLabelStyle(studyPageIcon, studyPage, "SelectedFontIcon", "SelectedLabel");
        contentNode.getChildren().setAll(studyFxmlContent);

        Platform.runLater(()->{
            InnoCareService.innoCareNative.enterOperationMode();
        });

        StudyService.getInstance().createStudyTabs();
        StudyBodyPartsController.getInstance().loadStudyBodyParts();
       /* ImageTools.getInstance().disableTools(true);*/

    }


    public void navigateToDataBase() {

        saveCurrentStudy();
        resetFooter();
        setIconAndLabelStyle(dataBasePageIcon, dataBasePage, "SelectedFontIcon", "SelectedLabel");
        contentNode.getChildren().setAll(dataBaseFxmlContent);

        /*Platform.runLater(()->{
            InnoCareService.innoCareNative.cancelOperationMode();
            InnoCareService.innoCareNative.exitOperationMode();
        });*/

        try {
            DatabaseController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void navigateToPrint() {
        saveCurrentStudy();
        resetFooter();
        setIconAndLabelStyle(printPageIcon, printPage, "SelectedFontIcon", "SelectedLabel");
        contentNode.getChildren().setAll(printFxmlContent);

        /*Platform.runLater(()->{
            InnoCareService.innoCareNative.cancelOperationMode();
            InnoCareService.innoCareNative.exitOperationMode();
        });*/


    }


    public VBox adminContentNode;
    public void navigateToAdminPage() {
        saveCurrentStudy();
        resetFooter();
        setIconAndLabelStyle(adminIcon, adminLabel, "SelectedFontIcon", "SelectedLabel");
       /* InnoCareService.innoCareNative.cancelOperationMode();
        InnoCareService.innoCareNative.exitOperationMode();*/
        contentNode.getChildren().setAll(adminPageParent);
       // adminContentNode.getChildren().clear();


        try {
            SystemAccountController.getInstance().loadData();
            SystemInfoController.getInstance().loadData();
            SystemThemesController.getInstance().loadData();

            RegistrationManualController.getInstance().loadData();
            RegistrationGeneralController.getInstance().loadData();
            RegistrationPhysicianController.getInstance().loadData();

            StudyOverlayController.getInstance().loadData();
            StudyDeleteController.getInstance().loadData();
            RejectedListController.getInstance().loadData();
            RejectedReasonController.getInstance().loadData();

            ProcedureManagerCategoryController.getInstance().loadData();
            ProcedureManagerProjectionController.getInstance().loadData();
            ProcedureManagerStepController.getInstance().loadData();
            ProcedureManagerProcedureController.getInstance().loadData();

            DicomGeneralController.getInstance().loadData();
            DicomQueueController.getInstance().loadData();
            DicomPrintLayoutController.getInstance().loadData();
            DicomPrintOverlayController.getInstance().loadData();
            DicomMWLController.getInstance().loadData();
            DicomMppsController.getInstance().loadData();
            DicomStorageController.getInstance().loadData();
            DicomStorageCommitmentController.getInstance().loadData();
            DicomPrintController.getInstance().loadData();
            DicomTagMappingController.getInstance().loadData();

            BackupController.getInstance().loadData();
            BackupCleanController.getInstance().loadData();
            BackupRestoreController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void navigateToStatsPage(){
        resetFooter();
        contentNode.getChildren().setAll(statsPageParent);
    }


    public void navigateToComparePage() {
        resetFooter();
        contentNode.getChildren().setAll(comparePageParent);
    }

    public static int totalUploads = 0;
    public static int successfulUploads = 0;
    public static int failedUploads = 0;

    public void uploadFailed() {
        failedUploads++;
        updateUploadStatus();
    }

    public void uploadSuccess() {
        successfulUploads++;
        updateUploadStatus();
    }

    private void updateUploadStatus() {
        String msg = successfulUploads + "/" + totalUploads;
        pacsUploadStatusLabel.setText(msg);
    }


    public void disableNav(boolean disable) {
        if(disable){
            registerPageButton.setDisable(true);
            studyPageButton.setDisable(true);
            databasePageButton.setDisable(true);
            printPageButton.setDisable(true);
        } else {
            registerPageButton.setDisable(false);
            studyPageButton.setDisable(false);
            databasePageButton.setDisable(false);
            printPageButton.setDisable(false);
        }

    }
}
