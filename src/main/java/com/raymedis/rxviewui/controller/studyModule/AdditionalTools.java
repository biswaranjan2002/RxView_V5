package com.raymedis.rxviewui.controller.studyModule;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;

import java.util.List;

public class AdditionalTools {

    public JFXButton pointButton;
    public JFXButton arrowButton;

    private List<JFXButton> buttonList;

    public void initialize(){
        buttonList =  List.of(pointButton, arrowButton);
    }

    public void resetTools() {
        for (JFXButton button : buttonList) {
            button.getStyleClass().removeAll("selectedAdditionalToolsButton", "unSelectedAdditionalToolsButton");
            button.getStyleClass().add("unSelectedAdditionalToolsButton");
        }
    }


    public void pointClick(ActionEvent actionEvent) {
        resetTools();
        pointButton.getStyleClass().remove("unSelectedAdditionalToolsButton");
        pointButton.getStyleClass().add("selectedAdditionalToolsButton");
    }


    public void arrowClick(ActionEvent actionEvent) {
        resetTools();
        arrowButton.getStyleClass().remove("unSelectedAdditionalToolsButton");
        arrowButton.getStyleClass().add("selectedAdditionalToolsButton");
    }
}
