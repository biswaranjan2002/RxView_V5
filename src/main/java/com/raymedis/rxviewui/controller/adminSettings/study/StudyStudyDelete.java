package com.raymedis.rxviewui.controller.adminSettings.study;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.raymedis.rxviewui.service.adminSettings.studySettings.StudyDeleteController;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class StudyStudyDelete {

    public JFXSlider warningSlider;
    public JFXSlider criticalSlider;

    public TextField warningLabel;
    public TextField criticalLabel;

    public JFXCheckBox enableAutoDelete;
    public JFXCheckBox enableTimeLimit;
    public JFXComboBox weekComboBox;
    public JFXCheckBox enableStorageLimit;
    public JFXComboBox storageSizeComboBox;
    
    public JFXCheckBox enableAllStudies;
    public JFXCheckBox enableSentAndPrintedStudies;
    public JFXCheckBox enableRejectedStudies;

    private StudyDeleteController studyDeleteController;

    public void initialize(){
        studyDeleteController = StudyDeleteController.getInstance();

        studyDeleteController.warningSlider = warningSlider;
        studyDeleteController.criticalSlider =criticalSlider;

        studyDeleteController.warningLabel = warningLabel;
        studyDeleteController.criticalLabel = criticalLabel;


        studyDeleteController.enableAutoDelete = enableAutoDelete;
        studyDeleteController.enableTimeLimit = enableTimeLimit;
        studyDeleteController.weekComboBox = weekComboBox;
        studyDeleteController.enableStorageLimit = enableStorageLimit;
        studyDeleteController.storageSizeComboBox = storageSizeComboBox;

        studyDeleteController.enableAllStudies = enableAllStudies;
        studyDeleteController.enableSentAndPrintedStudies = enableSentAndPrintedStudies;
        studyDeleteController.enableRejectedStudies = enableRejectedStudies;

        studyDeleteController.loadEvents();
        studyDeleteController.loadData();
    }

    public void saveStorageAlerts() {
        studyDeleteController.saveStorageAlerts();
    }

    public void autoDeleteOptionsSave() throws SQLException {
        studyDeleteController.saveAutoDeleteOptions();
    }
}
