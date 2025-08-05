package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomGeneralController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class DicomGeneral {

    public JFXRadioButton group1Yes;
    public JFXRadioButton group1No;

    public JFXRadioButton group2Yes;
    public JFXRadioButton group2No;

    public JFXRadioButton group3Yes;
    public JFXRadioButton group3No;

    public JFXRadioButton group4Yes;
    public JFXRadioButton group4No;

    public TextField stationAeTitleInput;
    public TextField stationNameInput;
    public TextField stationPortInput;
    public VBox settingsBox;
    public JFXButton editDetailsButton;
    public JFXButton saveDetailsButton;

    @FXML ToggleGroup radioGroup1 = new ToggleGroup();
    @FXML ToggleGroup radioGroup2 = new ToggleGroup();
    @FXML ToggleGroup radioGroup3 = new ToggleGroup();
    @FXML ToggleGroup radioGroup4 = new ToggleGroup();

    private DicomGeneralController dicomGeneralController = DicomGeneralController.getInstance();



    public void initialize() {
        dicomGeneralController.group1Yes=group1Yes;
        dicomGeneralController.group1No=group1No;

        dicomGeneralController.group2Yes=group2Yes;
        dicomGeneralController.group2No=group2No;

        dicomGeneralController.group3Yes=group3Yes;
        dicomGeneralController.group3No=group3No;

        dicomGeneralController.group4Yes=group4Yes;
        dicomGeneralController.group4No=group4No;

        dicomGeneralController.stationAeTitleInput=stationAeTitleInput;
        dicomGeneralController.stationNameInput=stationNameInput;
        dicomGeneralController.stationPortInput=stationPortInput;

        dicomGeneralController.settingsBox=settingsBox;

        dicomGeneralController.editDetailsButton=editDetailsButton;
        dicomGeneralController.saveDetailsButton=saveDetailsButton;

        dicomGeneralController.loadEvents();
        try {
            dicomGeneralController.loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void editDetailsClick() {
        dicomGeneralController.editDetailsClick();
    }

    public void saveDetailsClick() {
        dicomGeneralController.saveDetailsClick();
    }
}
