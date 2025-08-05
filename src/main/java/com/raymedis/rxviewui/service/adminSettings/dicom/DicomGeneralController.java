package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomGeneralController {

    private static DicomGeneralController instance = new DicomGeneralController();
    public static DicomGeneralController getInstance(){
        return instance;
    }

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


    public void loadEvents(){
        group1Yes.setToggleGroup(radioGroup1);
        group1No.setToggleGroup(radioGroup1);

        group2Yes.setToggleGroup(radioGroup2);
        group2No.setToggleGroup(radioGroup2);

        group3Yes.setToggleGroup(radioGroup3);
        group3No.setToggleGroup(radioGroup3);

        group4Yes.setToggleGroup(radioGroup4);
        group4No.setToggleGroup(radioGroup4);

        settingsBox.setVisible(false);
        settingsBox.setManaged(false);
        numericValidation(stationPortInput);

    }

    private void numericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 5) {
                textField.setText(oldValue);
            }
        });
    }


    private DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();
    private ArrayList<DicomGeneralEntity> dicomGeneralList = new ArrayList<>();

    public void loadData() throws SQLException {
        dicomGeneralList = dicomGeneralService.findAll();

        if (dicomGeneralList != null && !dicomGeneralList.isEmpty()) {
            DicomGeneralEntity firstEntity = dicomGeneralList.getFirst();

            stationNameInput.setText(firstEntity.getStationName());
            stationAeTitleInput.setText(firstEntity.getStationAETitle());
            stationPortInput.setText(firstEntity.getStationPort());

            stationPortInput.setDisable(true);
            stationNameInput.setDisable(true);
            stationAeTitleInput.setDisable(true);
            saveDetailsButton.setDisable(true);
            editDetailsButton.setDisable(false);
        } else {
            stationNameInput.setText("");
            stationAeTitleInput.setText("");
            stationPortInput.setText("");

            stationPortInput.setDisable(false);
            stationNameInput.setDisable(false);
            stationAeTitleInput.setDisable(false);
            saveDetailsButton.setDisable(false);
            editDetailsButton.setDisable(true);
        }
    }



    public void editDetailsClick() {
        stationPortInput.setDisable(false);
        stationNameInput.setDisable(false);
        stationAeTitleInput.setDisable(false);
        saveDetailsButton.setDisable(false);
        editDetailsButton.setDisable(true);
    }

    public void saveDetailsClick() {

        DicomGeneralEntity dicomGeneralEntity = null;
        if(!dicomGeneralList.isEmpty()){
            dicomGeneralEntity = dicomGeneralList.getFirst();
            dicomGeneralEntity.setStationName(stationNameInput.getText());
            dicomGeneralEntity.setStationAETitle(stationAeTitleInput.getText());
            dicomGeneralEntity.setStationPort(stationPortInput.getText());

            try {
                dicomGeneralService.update(dicomGeneralEntity.getId(),dicomGeneralEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            dicomGeneralEntity = new DicomGeneralEntity();
            dicomGeneralEntity.setStationName(stationNameInput.getText());
            dicomGeneralEntity.setStationAETitle(stationAeTitleInput.getText());
            dicomGeneralEntity.setStationPort(stationPortInput.getText());

            try {
                dicomGeneralService.save(dicomGeneralEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        stationPortInput.setDisable(true);
        stationNameInput.setDisable(true);
        stationAeTitleInput.setDisable(true);
        saveDetailsButton.setDisable(true);
        editDetailsButton.setDisable(false);
    }


}
