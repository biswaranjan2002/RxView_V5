package com.raymedis.rxviewui.controller.studyModule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.raymedis.rxviewui.service.study.ProcessingToolsController;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;

public class ProcessingTools {
    public JFXButton boneButton;
    public JFXButton softTissueButton;
    public JFXButton balanceButton;

    public JFXSlider denoiseSlider;
    public JFXSlider sharpnessSlider;
    public JFXSlider contrastSlider;
    public JFXSlider brightnessSlider;

    public TextField denoiseValueLabel;
    public TextField sharpnessValueLabel;
    public TextField contrastValueLabel;
    public TextField brightnessValueLabel;
    public Canvas histogramCanvas;
    public JFXComboBox lutComboBox;

    private ProcessingToolsController processingToolsController = ProcessingToolsController.getInstance();
    public void initialize(){

        processingToolsController.boneButton= boneButton;
        processingToolsController.softTissueButton= softTissueButton;
        processingToolsController.balanceButton=balanceButton;

        processingToolsController.denoiseSlider=denoiseSlider;
        processingToolsController.sharpnessSlider=sharpnessSlider;
        processingToolsController.contrastSlider=contrastSlider;
        processingToolsController.brightnessSlider=brightnessSlider;

        processingToolsController.denoiseValueLabel=denoiseValueLabel;
        processingToolsController.sharpnessValueLabel=sharpnessValueLabel;
        processingToolsController.contrastValueLabel=contrastValueLabel;
        processingToolsController.brightnessValueLabel=brightnessValueLabel;
        processingToolsController.histogramCanvas = histogramCanvas;

        processingToolsController.loadEvents();
        processingToolsController.loadData();

        lutComboBox.getSelectionModel().select(0);

        lutComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected LUT: " + newValue);
        });
    }


    public void boneClick() {
        processingToolsController.boneClick();
    }

    public void softTissueClick() {
        processingToolsController.softTissueClick();
    }

    public void balanceClick() {
        processingToolsController.balanceClick();
    }


}
