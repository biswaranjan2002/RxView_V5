package com.raymedis.rxviewui.controller.registrationModule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.registration.WorkListController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class WorkList {
    
    private final WorkListController workListController = WorkListController.getInstance();

    public JFXComboBox searchByComboBox;
    public TextField detailsText;
    public JFXButton todayButton;
    public JFXButton weekButton;
    public JFXButton monthButton;
    public DatePicker fromDate;
    public DatePicker toDate;
    public JFXButton searchButton;
    public JFXButton clearButton;
    public JFXButton refreshButton;
    public JFXButton startButton;

    public TableView dataGrid;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn dobColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn referringPhysician;
    public TableColumn studyDescriptionColumn;
    public TableColumn scheduleDateColumn;
    public TableColumn scheduledTimeColumn;
    public TableColumn statusColumn;
    public TableColumn procedureCodeColumn;
    public TableColumn bodyPartColumn;
    public TableColumn viewPositionColumn;

    private static final Logger logger = LoggerFactory.getLogger(WorkList.class);


    public void initialize(){

        searchByComboBox.getStyleClass().add("fontStyle");

        workListController.searchByComboBox=searchByComboBox;
        workListController.detailsText=detailsText;
        workListController.todayButton=todayButton;
        workListController.weekButton=weekButton;
        workListController.monthButton=monthButton;
        workListController.fromDate=fromDate;
        workListController.toDate=toDate;
        workListController.searchButton=searchButton;
        workListController.clearButton=clearButton;
        workListController.refreshButton=refreshButton;
        workListController.startButton=startButton;

        workListController.dataGrid=dataGrid;
        workListController.patientIdColumn=patientIdColumn;
        workListController.patientNameColumn=patientNameColumn;
        workListController.sexColumn=sexColumn;
        workListController.dobColumn=dobColumn;
        workListController.accessionNumberColumn=accessionNumberColumn;
        workListController.referringPhysician=referringPhysician;
        workListController.studyDescriptionColumn=studyDescriptionColumn;
        workListController.scheduleDateColumn=scheduleDateColumn;
        workListController.scheduledTimeColumn=scheduledTimeColumn;
        workListController.procedureCodeColumn =procedureCodeColumn;
        workListController.bodyPartColumn = bodyPartColumn;
        workListController.viewPositionColumn =viewPositionColumn;
        workListController.statusColumn=statusColumn;

        workListController.loadEvents();
        try {
            workListController.loadData();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void selectionChanged() {
        workListController.selectionChanged();
    }

    public void todayBtnClick() {
        workListController.todayBtnClick();
    }

    public void weekBtnClick() {
        workListController.weekBtnClick();
    }

    public void monthBtnClick() {
        workListController.monthBtnClick();
    }

    public void searchClick() {
        workListController.searchClick();
    }

    public void startButtonClick() {
        workListController.startButtonClick();
    }

    public void clearClick() {
        workListController.clearClick();
    }

    public void refreshButtonClick() {
        try {
            workListController.refreshButtonClick();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
