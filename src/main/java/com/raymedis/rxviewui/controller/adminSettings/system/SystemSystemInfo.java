package com.raymedis.rxviewui.controller.adminSettings.system;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemInfoController;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class SystemSystemInfo {

    public TextField institutionNameLabel;
    public TextField institutionAddressLabel;
    public TextField departmentLabel;

    public JFXComboBox languageComboBox;

    public TextField manufacturerLabel;
    public TextField modelNameLabel;
    public TextField telephoneLabel;
    public TextField emailLabel;
    public TextField homePageLabel;
    public TextField softwareVersionLabel;
    public TextField serialNumberLabel;

    public JFXButton configureDetailsButton;

    private SystemInfoController systemInfoController;

    public void initialize() throws SQLException {
        systemInfoController = SystemInfoController.getInstance();


        systemInfoController.institutionNameLabel = institutionNameLabel;
        systemInfoController.institutionAddressLabel = institutionAddressLabel;
        systemInfoController.departmentLabel = departmentLabel;
        systemInfoController.manufacturerLabel = manufacturerLabel;
        systemInfoController.modelNameLabel = modelNameLabel;
        systemInfoController.telephoneLabel = telephoneLabel;
        systemInfoController.emailLabel = emailLabel;
        systemInfoController.homePageLabel = homePageLabel;
        systemInfoController.softwareVersionLabel = softwareVersionLabel;
        systemInfoController.serialNumberLabel = serialNumberLabel;
        systemInfoController.languageComboBox = languageComboBox;
        systemInfoController.configureDetailsButton = configureDetailsButton;

        systemInfoController.loadData();
    }

    public void onSelectionChange() {
        systemInfoController.onSelectionChange();
    }

    public void configureDetailsClick() {
        systemInfoController.configureDetailsClick();
    }
}
