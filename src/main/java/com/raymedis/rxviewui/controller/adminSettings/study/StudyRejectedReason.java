package com.raymedis.rxviewui.controller.adminSettings.study;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.adminSettings.studySettings.RejectedReasonController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class StudyRejectedReason {

    public VBox rejectedReasonOuterBox;

    public TableView rejectedReasonsDataGrid;
    public TableColumn reasonColumn;

    public TextField reasonLabel;
    public JFXButton addButton;

    private RejectedReasonController rejectedReasonController;

    public void initialize() throws SQLException {
        rejectedReasonController = RejectedReasonController.getInstance();

        rejectedReasonController.rejectedReasonOuterBox = rejectedReasonOuterBox;
        rejectedReasonController.rejectedReasonsDataGrid = rejectedReasonsDataGrid;
        rejectedReasonController.reasonColumn = reasonColumn;
        rejectedReasonController.reasonLabel = reasonLabel;
        rejectedReasonController.addButton = addButton;

        rejectedReasonController.loadEvents();
        rejectedReasonController.loadData();

    }


    public void deleteClick() {
        rejectedReasonController.deleteClick();
    }

    public void addDataClick() {
        rejectedReasonController.addDataClick();
    }
}
