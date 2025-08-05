package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomPrintLayoutController;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class DicomPrintLayout {


    private static DicomPrintLayoutController dicomPrintLayoutController;
    public TextField inputText;
    public TextField outputText;
    public JFXComboBox layoutTypeComboBox;
    public GridPane layoutBox;
    public VBox selectedLayoutContainer;
    public JFXButton addButton;

    public void initialize() throws SQLException {
        dicomPrintLayoutController = DicomPrintLayoutController.getInstance();

        dicomPrintLayoutController.inputText = inputText;
        dicomPrintLayoutController.outputText = outputText;
        dicomPrintLayoutController.layoutTypeComboBox = layoutTypeComboBox;
        dicomPrintLayoutController.layoutBox =layoutBox;
        dicomPrintLayoutController.selectedLayoutContainer = selectedLayoutContainer;
        dicomPrintLayoutController.addButton=addButton;

        dicomPrintLayoutController.loadEvents();
        dicomPrintLayoutController.loadData();
    }

    public void checkClick() {
        dicomPrintLayoutController.checkClick();
    }

    public void addClick() {
        dicomPrintLayoutController.addClick();
    }


    public void saveClick() {
        try {
            dicomPrintLayoutController.saveClick();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteLayoutClick() {
        dicomPrintLayoutController.deleteLayoutClick();
    }



}
