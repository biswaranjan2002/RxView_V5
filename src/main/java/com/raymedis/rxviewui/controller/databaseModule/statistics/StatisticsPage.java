package com.raymedis.rxviewui.controller.databaseModule.statistics;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.controller.NavConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class StatisticsPage {

    public GridPane contentNode;

    public JFXButton generalButton;
    public JFXButton rejectButton;
    public JFXButton doseButton;
    public JFXButton personalButton;


    Parent generalParent;
    Parent rejectParent;
    Parent doseParent;
    Parent personalParent;


    public void initialize() throws IOException {

        FXMLLoader general = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StatisticsGeneral.fxml"));
        generalParent = general.load();

        FXMLLoader reject = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StatisticsReject.fxml"));
        rejectParent = reject.load();

        FXMLLoader dose = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StatisticsDose.fxml"));
        doseParent = dose.load();

        FXMLLoader personal = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/StatisticsPersonal.fxml"));
        personalParent = personal.load();

        generalClick();
    }


    public void resetButtons() {
        generalButton.getStyleClass().removeAll("selectedButton", "unSelectedButton");
        generalButton.getStyleClass().add("unSelectedButton");

        rejectButton.getStyleClass().removeAll("selectedButton", "unSelectedButton");
        rejectButton.getStyleClass().add("unSelectedButton");

        doseButton.getStyleClass().removeAll("selectedButton", "unSelectedButton");
        doseButton.getStyleClass().add("unSelectedButton");

        personalButton.getStyleClass().removeAll("selectedButton", "unSelectedButton");
        personalButton.getStyleClass().add("unSelectedButton");
    }


    public void generalClick() {
        resetButtons();
        generalButton.getStyleClass().remove("unSelectedButton");
        generalButton.getStyleClass().add("selectedButton");

        contentNode.getChildren().setAll(generalParent);
    }

    public void rejectClick(){
        resetButtons();
        rejectButton.getStyleClass().remove("unSelectedButton");
        rejectButton.getStyleClass().add("selectedButton");

        contentNode.getChildren().setAll(rejectParent);
    }

    public void doseClick(){
        resetButtons();
        doseButton.getStyleClass().remove("unSelectedButton");
        doseButton.getStyleClass().add("selectedButton");

        contentNode.getChildren().setAll(doseParent);
    }

    public void personalClick(){
        resetButtons();
        personalButton.getStyleClass().remove("unSelectedButton");
        personalButton.getStyleClass().add("selectedButton");

        contentNode.getChildren().setAll(personalParent);
    }

   
    public void statsExitClick(ActionEvent actionEvent) {
        NavConnect.getInstance().navigateToDataBase();
    }
}
