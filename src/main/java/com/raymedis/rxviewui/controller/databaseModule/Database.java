package com.raymedis.rxviewui.controller.databaseModule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.service.database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class Database {

    @FXML public TableColumn registerDateColumn;
    @FXML public TableColumn  registerTimeColumn;
    @FXML public TableColumn patientIdColumn;
    @FXML public TableColumn patientNameColumn;
    @FXML public TableColumn sexColumn;
    @FXML public TableColumn weightColumn;
    @FXML public TableColumn dobColumn;
    @FXML public TableColumn accessionNumberColumn;
    @FXML public TableColumn physicianNameColumn;
    @FXML public TableColumn studyDescriptionColumn;
    @FXML public TableColumn exposureDateColumn;
    @FXML public TableColumn modalityColumn;
    @FXML public TableColumn studyDateTimeColumn;
    @FXML public TableColumn rejectedColumn;
    
    @FXML public TableView dataGrid;
    
    @FXML public VBox databaseMediaContainer;
    
    @FXML public TextField detailsText;
    @FXML public JFXComboBox searchByComboBox;
    @FXML public JFXButton todayButton;
    @FXML public JFXButton weekButton;
    @FXML public JFXButton monthButton;
    @FXML public DatePicker fromDate;
    @FXML public DatePicker toDate;
    @FXML public JFXButton searchButton;
    @FXML public JFXButton clearButton;

    private final NavConnect navConnect = NavConnect.getInstance();
    public JFXButton editButton;

    private DatabaseController databaseController;

    public void initialize() throws SQLException {

        databaseController = DatabaseController.getInstance();

        databaseController.registerDateColumn =registerDateColumn;
        databaseController.registerTimeColumn =registerTimeColumn;
        databaseController.patientIdColumn =patientIdColumn;
        databaseController.patientNameColumn =patientNameColumn;
        databaseController.sexColumn =sexColumn;
        databaseController.weightColumn =weightColumn;
        databaseController.dobColumn =dobColumn;
        databaseController.accessionNumberColumn =accessionNumberColumn;
        databaseController.physicianNameColumn =physicianNameColumn;
        databaseController.studyDescriptionColumn =studyDescriptionColumn;
        databaseController.exposureDateTimeColumn =exposureDateColumn;
        databaseController.modalityColumn =modalityColumn;
        databaseController.studyDateTimeColumn = studyDateTimeColumn;
        databaseController.rejectedColumn = rejectedColumn;

        databaseController.dataGrid =dataGrid;

        databaseController.databaseMediaContainer =databaseMediaContainer;

        databaseController.detailsText =detailsText;
        databaseController.searchByComboBox =searchByComboBox;
        databaseController.todayButton =todayButton;
        databaseController.weekButton =weekButton;
        databaseController.monthButton =monthButton;
        databaseController.fromDate =fromDate;
        databaseController.toDate =toDate;
        databaseController.searchButton =searchButton;
        databaseController.clearButton =clearButton;
        databaseController.editButton=editButton;

        databaseController.loadData();
        databaseController.loadEvents();
    }

    public void selectionChanged() {
        databaseController.selectionChanged();
    }

    public void todayBtnClick() {
        databaseController.todayBtnClick();
    }

    public void weekBtnClick() {
        databaseController.weekBtnClick();
    }

    public void monthBtnClick() {
        databaseController.monthBtnClick();
    }

    public void searchClick() {
        databaseController.searchClick();
    }

    public void clearClick() {
        databaseController.clearClick();
    }

    public void statsPageClick() {
        databaseController.statsPageClick();
    }

    public void comparePageClick() {
        databaseController.comparePageClick();
    }

    public void rejectStudy() {
        databaseController.rejectStudy();
    }

    public void printClick() {
        databaseController.printClick();
    }


    public void exportClick() {
        databaseController.exportClick();
    }

    public void pacsClick() {
        databaseController.pacsClick();
    }

    public void editClick () {
        databaseController.editClick();
    }

    public void importClick() {
        databaseController.importClick();
    }

    public void moveClick() {
        databaseController.moveClick();
    }

    public void stitchClick() {
    databaseController.stitchClick();
    }
}
