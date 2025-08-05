package com.raymedis.rxviewui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainWindow {
    @FXML public GridPane rootPane;
    @FXML public BorderPane contentBox;
    @FXML public GridPane contentNode;
    @FXML public GridPane footerNode;


    NavConnect navConnect=NavConnect.getInstance();

    Parent registerMainParent;
    Parent footerParent;
    Parent studyPageParent;
    Parent databasePageParent;
    Parent printPageParent;
    Parent adminPageParent;
    Parent statsPageParent;
    Parent comparePageParent;


    public void initialize() throws IOException {
        navConnect.contentNode=contentNode;
        navConnect.footerNode=footerNode;

        FXMLLoader registerMain = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/RegisterMain.fxml"));
        registerMainParent = registerMain.load();

        FXMLLoader studyPage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StudyMain.fxml"));
        studyPageParent = studyPage.load();

        FXMLLoader databasePage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/Database.fxml"));
        databasePageParent = databasePage.load();

        FXMLLoader printPage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/Print.fxml"));
        printPageParent = printPage.load();


        FXMLLoader Footer = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/Footer.fxml"));
        footerParent = Footer.load();

        FXMLLoader adminPage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/AdminWindow.fxml"));
        adminPageParent = adminPage.load();

        FXMLLoader statsPage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StatisticsPage.fxml"));
        statsPageParent = statsPage.load();

        FXMLLoader comparePage = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ComparePage.fxml"));
        comparePageParent = comparePage.load();



        navConnect.registerFxmlContent = registerMainParent;
        navConnect.studyFxmlContent = studyPageParent;
        navConnect.dataBaseFxmlContent = databasePageParent;
        navConnect.printFxmlContent = printPageParent;
        navConnect.adminPageParent = adminPageParent;
        navConnect.footerFxmlContent = footerParent;
        navConnect.statsPageParent = statsPageParent;
        navConnect.comparePageParent = comparePageParent;



        //navConnect.navigateToDataBase();
        navConnect.navigateToRegisterMain();
        navConnect.setFooter();
    }
}
