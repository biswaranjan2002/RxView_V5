package com.raymedis.rxviewui.controller.adminSettings.study;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.studySettings.RejectedListController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class StudyRejectedList {

    public JFXComboBox searchByComboBox;
    public TextField detailsText;

    public DatePicker fromDate;
    public DatePicker toDate;

    public TableView rejectedListDataGrid;

    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn dobColumn;
    public TableColumn sexColumn;
    public TableColumn studyDateTimeColumn;
    public JFXButton deleteClick;

    private final RejectedListController rejectedListController = RejectedListController.getInstance();


    public void initialize(){
        rejectedListController.searchByComboBox=searchByComboBox;
        rejectedListController.detailsText=detailsText;

        rejectedListController.fromDate=fromDate;
        rejectedListController.toDate=toDate;

        rejectedListController.rejectedListDataGrid=rejectedListDataGrid;

        rejectedListController.patientIdColumn=patientIdColumn;
        rejectedListController.patientNameColumn=patientNameColumn;
        rejectedListController.dobColumn=dobColumn;
        rejectedListController.sexColumn=sexColumn;
        rejectedListController.studyDateTimeColumn=studyDateTimeColumn;

        rejectedListController.loadEvents();
        try {
            rejectedListController.loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void selectionChanged() {
        rejectedListController.selectionChanged();
    }

    public void todayBtnClick() {
        rejectedListController.todayBtnClick();
    }

    public void weekBtnClick() {
        rejectedListController.weekBtnClick();
    }

    public void monthBtnClick() {
        rejectedListController.monthBtnClick();
    }

    public void searchClick() {
        rejectedListController.searchClick();
    }

    public void clearClick() {
        rejectedListController.clearClick();
    }


    public void deleteClick() throws SQLException {
        rejectedListController.deleteClick();
    }
}

