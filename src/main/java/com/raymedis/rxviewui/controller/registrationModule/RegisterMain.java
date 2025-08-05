package com.raymedis.rxviewui.controller.registrationModule;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.service.registration.LocalListController;
import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterMain {

    @FXML GridPane registerContainer;
    @FXML GridPane rootNode;

    @FXML JFXButton manualButton;
    @FXML JFXButton workListButton;
    @FXML JFXButton emergencyButton;
    @FXML JFXButton localListButton;

    @FXML FontIcon manualIcon;
    @FXML FontIcon workListPageIcon;
    @FXML FontIcon emergencyPageIcon;
    @FXML FontIcon localListPageIcon;

    Parent manualParent;
    Parent workListParent;
    Parent localListParent;

    private final NavConnect navConnect = NavConnect.getInstance();
    private final ManualRegisterController manualRegisterController = ManualRegisterController.getInstance();






    public void initialize() throws IOException, SQLException {
        FXMLLoader manualRegister = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ManualRegister.fxml"));
        manualParent = manualRegister.load();

        FXMLLoader workList = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/WorkList.fxml"));
        workListParent = workList.load();

        FXMLLoader localList = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/LocalList.fxml"));
        localListParent = localList.load();

        navConnect.manualRegisterParent=manualParent;
        navConnect.worklistParent=workListParent;
        navConnect.localListParent=localListParent;
        navConnect.workListManualContainer=registerContainer;

        navConnect.manualButton=manualButton;
        navConnect.workListButton=workListButton;
        navConnect.emergencyButton=emergencyButton;
        navConnect.localListButton=localListButton;

        navConnect.manualPageIcon = manualIcon;
        navConnect.workListPageIcon=workListPageIcon;
        navConnect.emergencyPageIcon=emergencyPageIcon;
        navConnect.localListPageIcon=localListPageIcon;

    }






    public void manualButtonClick() {
        navConnect.navigateToManual();
    }



    public void workListButtonClick() {
        navConnect.navigateToWorkList();
    }


    public void localListButtonClick() {
        navConnect.navigateToLocalList();
        LocalListController.getInstance().loadData();
    }



    public void emergencyButtonClick() {
        manualRegisterController.emergencyClick();
    }



}
