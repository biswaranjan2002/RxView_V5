package com.raymedis.rxviewui.controller.adminSettings.system;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemAccountController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SystemAccount {
    
    public TableView dataGrid;
    
    public TextField userIdLabel;
    public TextField userNameLabel;
    public PasswordField passwordLabel;
    public TextField confirmPasswordLabel;
    public JFXComboBox groupComboBox;
    public JFXButton addButton;

    public TableColumn userIdColumn;
    public TableColumn userNameColumn;
    public TableColumn groupColumn;
    public VBox tableRootBox;

    private SystemAccountController systemAccountController;

    public void initialize(){
        systemAccountController = SystemAccountController.getInstance();

        systemAccountController.dataGrid = dataGrid;
        systemAccountController.userIdLabel = userIdLabel;
        systemAccountController.userNameLabel = userNameLabel;
        systemAccountController.passwordLabel = passwordLabel;
        systemAccountController.confirmPasswordLabel = confirmPasswordLabel;
        systemAccountController.groupComboBox = groupComboBox;
        systemAccountController.addButton = addButton;

        systemAccountController.userIdColumn= userIdColumn;
        systemAccountController.userNameColumn = userNameColumn;
        systemAccountController.groupColumn = groupColumn;
        systemAccountController.tableRootBox =tableRootBox;

        systemAccountController.loadEvents();
        systemAccountController.loadData();
    }


    public void deleteClick() {
        systemAccountController.deleteClick();
    }


    public void addDataClick() {
        systemAccountController.addDataClick();
    }
}
