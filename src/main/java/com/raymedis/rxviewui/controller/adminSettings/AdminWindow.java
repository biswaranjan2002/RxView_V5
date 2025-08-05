package com.raymedis.rxviewui.controller.adminSettings;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.controller.NavConnect;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class AdminWindow {

    public VBox systemSubSection;
    public VBox registrationSubSection;
    public VBox studySubSection;
    public VBox procedureSubSection;
    public VBox dicomSubSection;
    public VBox backupSubSection;
    
    
    public JFXButton systemSystemInfoButton;
    public JFXButton systemAccountButton;
    public JFXButton systemLicenseButton;
    public JFXButton systemThemesButton;


    public JFXButton registrationGeneralButton;
    public JFXButton registrationManualButton;
    public JFXButton registrationPhysicianButton;


    public JFXButton studyStudyDeleteButton;
    public JFXButton studyRejectedListButton;
    public JFXButton studyRejectedReasonButton;

    public JFXButton procedureProcedureManagerButton;

    public JFXButton dicomGeneralButton;
    public JFXButton dicomQueueButton;
    public JFXButton dicomMWLButton;
    public JFXButton dicomMPPSButton;
    public JFXButton dicomStorageButton;
    public JFXButton dicomStorageCommitmentButton;
    public JFXButton dicomPrintButton;
    public JFXButton dicomPrintOverlayButton;
    public JFXButton dicomPrintLayoutButton;
    public JFXButton dicomTagMappingButton;

    public JFXButton backupBackupButton;
    public JFXButton backupRestoreButton;
    public JFXButton backupCleanButton;

    public JFXButton dicomButton;
    public JFXButton procedureButton;
    public JFXButton systemButton;
    public JFXButton registrationButton;
    public JFXButton studyButton;
    public JFXButton backupButton;

    public VBox adminContentNode;
    public JFXButton studyStudyOverlayButton;


    //Parents
    Parent SystemThemesParent;
    Parent SystemSystemInfoParent;
    Parent SystemAccountParent;
    Parent SystemLicenseParent;

    Parent RegistrationGeneralParent;
    Parent RegistrationManualParent;
    Parent RegistrationPhysicianParent;


    Parent StudyStudyOverlayParent;
    Parent StudyStudyDeleteParent;
    Parent StudyRejectedListParent;
    Parent StudyRejectedReasonParent;

    Parent ProcedureProcedureManagerParent;

    Parent DicomGeneralParent;
    Parent DicomQueueParent;
    Parent DicomMWLParent;
    Parent DicomMPPSParent;
    Parent DicomStorageParent;
    Parent DicomStorageCommitmentParent;
    Parent DicomPrintParent;
    Parent DicomPrintOverlayParent;
    Parent DicomPrintLayoutParent;
    Parent DicomTagMappingParent;

    Parent BackupBackupParent;
    Parent BackupRestoreParent;
    Parent BackupCleanParent;



    public void initialize() throws IOException {
        adminContentNode.requestFocus();
        dicomButton.getStyleClass().add("fontStyle2");
        procedureButton.getStyleClass().add("fontStyle2");
        systemButton.getStyleClass().add("fontStyle2");
        registrationButton.getStyleClass().add("fontStyle2");
        studyButton.getStyleClass().add("fontStyle2");
        backupButton.getStyleClass().add("fontStyle2");


        systemSystemInfoButton.getStyleClass().add("fontStyle");
        systemAccountButton.getStyleClass().add("fontStyle");
        systemLicenseButton.getStyleClass().add("fontStyle");
        systemThemesButton.getStyleClass().add("fontStyle");


        registrationGeneralButton.getStyleClass().add("fontStyle");
        registrationManualButton.getStyleClass().add("fontStyle");
        registrationPhysicianButton.getStyleClass().add("fontStyle");


        studyStudyDeleteButton.getStyleClass().add("fontStyle");
        studyRejectedListButton.getStyleClass().add("fontStyle");
        studyRejectedReasonButton.getStyleClass().add("fontStyle");

        procedureProcedureManagerButton.getStyleClass().add("fontStyle");

        dicomGeneralButton.getStyleClass().add("fontStyle");
        dicomQueueButton.getStyleClass().add("fontStyle");
        dicomMWLButton.getStyleClass().add("fontStyle");
        dicomMPPSButton.getStyleClass().add("fontStyle");
        dicomStorageButton.getStyleClass().add("fontStyle");
        dicomStorageCommitmentButton.getStyleClass().add("fontStyle");
        dicomPrintButton.getStyleClass().add("fontStyle");
        dicomPrintOverlayButton.getStyleClass().add("fontStyle");
        dicomPrintLayoutButton.getStyleClass().add("fontStyle");
        dicomTagMappingButton.getStyleClass().add("fontStyle");

        backupBackupButton.getStyleClass().add("fontStyle");
        backupRestoreButton.getStyleClass().add("fontStyle");
        backupCleanButton.getStyleClass().add("fontStyle");

        //system
        FXMLLoader SystemThemes = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/SystemThemes.fxml"));
        SystemThemesParent = SystemThemes.load();

        FXMLLoader SystemSystemInfo = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/SystemSystemInfo.fxml"));
        SystemSystemInfoParent = SystemSystemInfo.load();

        FXMLLoader SystemAccount = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/SystemAccount.fxml"));
        SystemAccountParent = SystemAccount.load();

        FXMLLoader SystemLicense = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/SystemLicense.fxml"));
        SystemLicenseParent = SystemLicense.load();


        // Registration
        FXMLLoader RegistrationGeneral = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/RegistrationGeneral.fxml"));
        RegistrationGeneralParent = RegistrationGeneral.load();

        FXMLLoader RegistrationManual = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/RegistrationManual.fxml"));
        RegistrationManualParent = RegistrationManual.load();

        FXMLLoader RegistrationPhysician = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/RegistrationPhysician.fxml"));
        RegistrationPhysicianParent = RegistrationPhysician.load();


        // Study
        FXMLLoader studyStudyOverlay = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/StudyStudyOverlay.fxml"));
        StudyStudyOverlayParent = studyStudyOverlay.load();

        FXMLLoader StudyStudyDelete = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/StudyStudyDelete.fxml"));
        StudyStudyDeleteParent = StudyStudyDelete.load();

        FXMLLoader StudyRejectedList = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/StudyRejectedList.fxml"));
        StudyRejectedListParent = StudyRejectedList.load();

        FXMLLoader StudyRejectedReason = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/StudyRejectedReason.fxml"));
        StudyRejectedReasonParent = StudyRejectedReason.load();



        // DICOM
        FXMLLoader DicomGeneral = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomGeneral.fxml"));
        DicomGeneralParent = DicomGeneral.load();

        FXMLLoader DicomQueue = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomQueue.fxml"));
        DicomQueueParent = DicomQueue.load();

        FXMLLoader DicomMWL = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomMWL.fxml"));
        DicomMWLParent = DicomMWL.load();

        FXMLLoader DicomMPPS = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomMPPS.fxml"));
        DicomMPPSParent = DicomMPPS.load();

        FXMLLoader DicomStorage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomStorage.fxml"));
        DicomStorageParent = DicomStorage.load();

        FXMLLoader DicomStorageCommitment = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomStorageCommitment.fxml"));
        DicomStorageCommitmentParent = DicomStorageCommitment.load();

        FXMLLoader DicomPrint = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomPrint.fxml"));
        DicomPrintParent = DicomPrint.load();

        FXMLLoader DicomPrintOverlay = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomPrintOverlay.fxml"));
        DicomPrintOverlayParent = DicomPrintOverlay.load();

        FXMLLoader DicomPrintLayout = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomPrintLayout.fxml"));
        DicomPrintLayoutParent = DicomPrintLayout.load();

        FXMLLoader DicomTagMapping = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/DicomTagMapping.fxml"));
        DicomTagMappingParent = DicomTagMapping.load();



        // Backup
        FXMLLoader BackupBackup = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/BackupBackup.fxml"));
        BackupBackupParent = BackupBackup.load();

        FXMLLoader BackupRestore = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/BackupRestore.fxml"));
        BackupRestoreParent = BackupRestore.load();

        FXMLLoader BackupClean = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/BackupClean.fxml"));
        BackupCleanParent = BackupClean.load();


        // Procedure
        FXMLLoader ProcedureProcedureManager = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/adminSettings/ProcedureProcedureManager.fxml"));
        ProcedureProcedureManagerParent = ProcedureProcedureManager.load();


        NavConnect.getInstance().adminContentNode = adminContentNode;



    }


    public void toggleSystemSubSection() {
        boolean isVisible = systemSubSection.isVisible();
        systemSubSection.setVisible(!isVisible);
        systemSubSection.setManaged(!isVisible);
    }


    public void toggleRegistrationSubSection() {
        boolean isVisible = registrationSubSection.isVisible();
        registrationSubSection.setVisible(!isVisible);
        registrationSubSection.setManaged(!isVisible);
    }


    public void toggleStudySubSection() {
        boolean isVisible = studySubSection.isVisible();
        studySubSection.setVisible(!isVisible);
        studySubSection.setManaged(!isVisible);

    }

    public void toggleProcedureSubSection() {
        boolean isVisible = procedureSubSection.isVisible();
        procedureSubSection.setVisible(!isVisible);
        procedureSubSection.setManaged(!isVisible);
    }

    public void toggleDicomSubSection() {
        boolean isVisible = dicomSubSection.isVisible();
        dicomSubSection.setVisible(!isVisible);
        dicomSubSection.setManaged(!isVisible);
    }

    public void toggleBackupSubSection() {
        boolean isVisible = backupSubSection.isVisible();
        backupSubSection.setVisible(!isVisible);
        backupSubSection.setManaged(!isVisible);
    }


    public void resetAllSubsection() {
        adminContentNode.requestFocus();
        // Reset all buttons to unselected class in all sections

        // System buttons
        systemSystemInfoButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        systemSystemInfoButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        systemAccountButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        systemAccountButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        systemLicenseButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        systemLicenseButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        systemThemesButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        systemThemesButton.getStyleClass().add("unSelectedAdminSubOptionButtons");


        // Registration buttons
        registrationGeneralButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        registrationGeneralButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        registrationManualButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        registrationManualButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        registrationPhysicianButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        registrationPhysicianButton.getStyleClass().add("unSelectedAdminSubOptionButtons");


        // Study buttons
        studyStudyOverlayButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        studyStudyOverlayButton.getStyleClass().add("unSelectedAdminSubOptionButtons");


        studyStudyDeleteButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        studyStudyDeleteButton.getStyleClass().add("unSelectedAdminSubOptionButtons");


        studyRejectedListButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        studyRejectedListButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        studyRejectedReasonButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        studyRejectedReasonButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        // Procedure buttons
        procedureProcedureManagerButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        procedureProcedureManagerButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        // DICOM buttons
        dicomGeneralButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomGeneralButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomQueueButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomQueueButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomMWLButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomMWLButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomMPPSButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomMPPSButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomStorageButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomStorageButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomStorageCommitmentButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomStorageCommitmentButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomPrintButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomPrintButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomPrintOverlayButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomPrintOverlayButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomPrintLayoutButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomPrintLayoutButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        dicomTagMappingButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        dicomTagMappingButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        // Backup buttons
        backupBackupButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        backupBackupButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        backupRestoreButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        backupRestoreButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

        backupCleanButton.getStyleClass().remove("selectedAdminSubOptionButtons");
        backupCleanButton.getStyleClass().add("unSelectedAdminSubOptionButtons");

    }

    public void systemSystemInfoClick() {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(SystemSystemInfoParent);
        systemSystemInfoButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        systemSystemInfoButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            SystemInfoController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void systemAccountClick() {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(SystemAccountParent);
        systemAccountButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        systemAccountButton.getStyleClass().add("selectedAdminSubOptionButtons");

        SystemAccountController.getInstance().loadData();
    }

    public void systemLicenseClick() {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(SystemLicenseParent);
        systemLicenseButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        systemLicenseButton.getStyleClass().add("selectedAdminSubOptionButtons");
    }

    public void systemThemesClick() throws SQLException {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(SystemThemesParent);
        systemThemesButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        systemThemesButton.getStyleClass().add("selectedAdminSubOptionButtons");

        SystemThemesController.getInstance().loadData();
    }



    public void registrationGeneralClick() {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(RegistrationGeneralParent);
        registrationGeneralButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        registrationGeneralButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            RegistrationGeneralController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registrationManualClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(RegistrationManualParent);
        registrationManualButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        registrationManualButton.getStyleClass().add("selectedAdminSubOptionButtons");

        RegistrationManualController.getInstance().loadData();
    }

    public void registrationPhysicianClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(RegistrationPhysicianParent);
        registrationPhysicianButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        registrationPhysicianButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            RegistrationPhysicianController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void studyStudyOverlayClick() {
        resetAllSubsection();

        adminContentNode.getChildren().setAll(StudyStudyOverlayParent);
        studyStudyOverlayButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        studyStudyOverlayButton.getStyleClass().add("selectedAdminSubOptionButtons");

        StudyOverlayController.getInstance().loadData();
    }


    public void studyStudyDeleteClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(StudyStudyDeleteParent);
        studyStudyDeleteButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        studyStudyDeleteButton.getStyleClass().add("selectedAdminSubOptionButtons");

        StudyDeleteController.getInstance().loadData();
    }


    public void studyRejectedListClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(StudyRejectedListParent);
        studyRejectedListButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        studyRejectedListButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            RejectedListController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void studyRejectedReasonClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(StudyRejectedReasonParent);
        studyRejectedReasonButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        studyRejectedReasonButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            RejectedReasonController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public void procedureProcedureManagerClick() throws SQLException {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(ProcedureProcedureManagerParent);
        procedureProcedureManagerButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        procedureProcedureManagerButton.getStyleClass().add("selectedAdminSubOptionButtons");

        ProcedureManagerCategoryController.getInstance().loadData();
        ProcedureManagerProjectionController.getInstance().loadData();
        ProcedureManagerStepController.getInstance().loadData();
        ProcedureManagerProcedureController.getInstance().loadData();
    }



    public void dicomGeneralClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomGeneralParent);
        dicomGeneralButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomGeneralButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            DicomGeneralController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dicomQueueClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomQueueParent);
        dicomQueueButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomQueueButton.getStyleClass().add("selectedAdminSubOptionButtons");
        DicomQueueController.getInstance().loadData();
    }

    public void dicomMWLClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomMWLParent);
        dicomMWLButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomMWLButton.getStyleClass().add("selectedAdminSubOptionButtons");

        DicomMWLController.getInstance().loadData();
    }

    public void dicomMPPSClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomMPPSParent);
        dicomMPPSButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomMPPSButton.getStyleClass().add("selectedAdminSubOptionButtons");

        DicomMppsController.getInstance().loadData();
    }

    public void dicomStorageClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomStorageParent);
        dicomStorageButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomStorageButton.getStyleClass().add("selectedAdminSubOptionButtons");
        DicomStorageController.getInstance().loadData();
    }

    public void dicomStorageCommitmentClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomStorageCommitmentParent);
        dicomStorageCommitmentButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomStorageCommitmentButton.getStyleClass().add("selectedAdminSubOptionButtons");
        DicomStorageCommitmentController.getInstance().loadData();
    }

    public void dicomPrintClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomPrintParent);
        dicomPrintButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomPrintButton.getStyleClass().add("selectedAdminSubOptionButtons");

        DicomPrintController.getInstance().loadData();
    }

    public void dicomPrintOverlayClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomPrintOverlayParent);
        dicomPrintOverlayButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomPrintOverlayButton.getStyleClass().add("selectedAdminSubOptionButtons");
        DicomPrintOverlayController.getInstance().loadData();
    }

    public void dicomPrintLayoutClick() {
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomPrintLayoutParent);
        dicomPrintLayoutButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomPrintLayoutButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            DicomPrintLayoutController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dicomTagMappingClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(DicomTagMappingParent);
        dicomTagMappingButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        dicomTagMappingButton.getStyleClass().add("selectedAdminSubOptionButtons");

        DicomTagMappingController.getInstance().loadData();
    }




    public void backupBackupClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(BackupBackupParent);
        backupBackupButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        backupBackupButton.getStyleClass().add("selectedAdminSubOptionButtons");

        try {
            BackupController.getInstance().loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void backupCleanClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(BackupCleanParent);
        backupCleanButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        backupCleanButton.getStyleClass().add("selectedAdminSubOptionButtons");

        BackupCleanController.getInstance().loadData();
    }

    public void backupRestoreClick(){
        resetAllSubsection();
        adminContentNode.getChildren().setAll(BackupRestoreParent);
        backupRestoreButton.getStyleClass().remove("unSelectedAdminSubOptionButtons");
        backupRestoreButton.getStyleClass().add("selectedAdminSubOptionButtons");

        BackupRestoreController.getInstance().loadData();
    }



}
