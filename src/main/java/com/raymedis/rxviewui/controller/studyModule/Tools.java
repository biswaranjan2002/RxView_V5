package com.raymedis.rxviewui.controller.studyModule;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class Tools {

    public GridPane toolsAndProcessingContainer;

    public JFXButton additionalToolsButton;
    public JFXButton processingToolsButton;

    Parent processingToolsParent;

    public void initialize() throws IOException {

       /* FXMLLoader AdditionalTools = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/AdditionalTools.fxml"));
        AdditionalToolsParent = AdditionalTools.load();*/

        FXMLLoader processingTools = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/ProcessingTools.fxml"));
        processingToolsParent = processingTools.load();

        processingTools();
    }

    public void resetTools() {
        /*additionalToolsButton.getStyleClass().removeAll("selectedToolsButton");
        additionalToolsButton.getStyleClass().add("unSelectedToolsButton");*/

        processingToolsButton.getStyleClass().removeAll("selectedToolsButton");
        processingToolsButton.getStyleClass().add("unSelectedToolsButton");
    }

   /* public void additionalTools() {
        resetTools();
        toolsAndProcessingContainer.getChildren().setAll(AdditionalToolsParent);

        // Set the selected style for the additionalToolsButton
        additionalToolsButton.getStyleClass().removeAll("unSelectedToolsButton");
        additionalToolsButton.getStyleClass().add("selectedToolsButton");
    }*/

    public void processingTools() {
        resetTools();
        toolsAndProcessingContainer.getChildren().setAll(processingToolsParent);

        // Set the selected style for the processingToolsButton
        processingToolsButton.getStyleClass().removeAll("unSelectedToolsButton");
        processingToolsButton.getStyleClass().add("selectedToolsButton");
    }

}
