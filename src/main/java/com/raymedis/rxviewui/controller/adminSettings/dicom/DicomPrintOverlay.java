package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomPrintOverlayController;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class DicomPrintOverlay {

    public TableView overlayTable;
    public TableColumn overlayNameColumn;


    public TextField overlayNameInput;

    public JFXComboBox logoComboBox;
    public TextField logoPathInput;

    public TableView headerTable;
    public TableColumn headerColumn;

    public TableView topLeftTable;
    public TableColumn topLeftColumn;

    public TableView bottomLeftTable;
    public TableColumn bottomLeftColumn;

    public TableView overlayItemsTable;
    public TableColumn itemsColumn;

    public TableView topRightTable;
    public TableColumn topRightColumn;

    public TableView bottomRightTable;
    public TableColumn bottomRightColumn;
    public Label appliedLabel;
    public VBox overlayStylesBox;


    private DicomPrintOverlayController dicomPrintOverlayController = DicomPrintOverlayController.getInstance();
    public void initialize(){

        dicomPrintOverlayController.overlayTable=overlayTable;
        dicomPrintOverlayController.overlayNameColumn = overlayNameColumn;

        dicomPrintOverlayController.overlayNameInput=overlayNameInput;

        dicomPrintOverlayController.logoComboBox=logoComboBox;
        dicomPrintOverlayController.logoPathInput=logoPathInput;

        dicomPrintOverlayController.headerTable=headerTable;
        dicomPrintOverlayController.headerColumn=headerColumn;

        dicomPrintOverlayController.topLeftTable=topLeftTable;
        dicomPrintOverlayController.topLeftColumn=topLeftColumn;

        dicomPrintOverlayController.bottomLeftTable=bottomLeftTable;
        dicomPrintOverlayController.bottomLeftColumn=bottomLeftColumn;

        dicomPrintOverlayController.overlayItemsTable=overlayItemsTable;
        dicomPrintOverlayController.itemsColumn=itemsColumn;

        dicomPrintOverlayController.topRightTable=topRightTable;
        dicomPrintOverlayController.topRightColumn=topRightColumn;

        dicomPrintOverlayController.bottomRightTable=bottomRightTable;
        dicomPrintOverlayController.bottomRightColumn=bottomRightColumn;

        dicomPrintOverlayController.appliedLabel=appliedLabel;
        dicomPrintOverlayController.overlayStylesBox=overlayStylesBox;

        dicomPrintOverlayController.loadEvents();
        dicomPrintOverlayController.loadData();
    }

    public void logoSelectionClick() {
        dicomPrintOverlayController.logoSelectionClick();
    }

    public void addOverlayClick() {
        dicomPrintOverlayController.addOverlayClick();
    }

    public void deleteOverlayClick() throws SQLException {
        dicomPrintOverlayController.deleteOverlayClick();
    }

    public void deleteHeaderItemClick() {
        dicomPrintOverlayController.deleteHeaderItemClick();
    }

    public void addHeaderItemClick() {
        dicomPrintOverlayController.addHeaderItemClick();
    }

    public void deleteTopLeftClick() {
        dicomPrintOverlayController.deleteTopLeftClick();
    }

    public void addTopLeftClick() {
        dicomPrintOverlayController.addTopLeftClick();
    }

    public void addBottomLeftClick() {
        dicomPrintOverlayController.addBottomLeftClick();
    }

    public void deleteBottomLeftClick() {
        dicomPrintOverlayController.deleteBottomLeftClick();
    }

    public void addTopRightClick() {
        dicomPrintOverlayController.addTopRightClick();
    }

    public void deleteTopRightClick() {
        dicomPrintOverlayController.deleteTopRightClick();
    }

    public void addBottomRightClick() {
        dicomPrintOverlayController.addBottomRightClick();
    }

    public void deleteBottomRightClick() {
        dicomPrintOverlayController.deleteBottomRightClick();
    }

    public void saveCurrentOverlayClick() throws SQLException {
        dicomPrintOverlayController.saveCurrentOverlayClick();
    }

    public void applySelectedOverlayClick() throws SQLException {
     dicomPrintOverlayController.applySelectedOverlayClick();
    }
}
