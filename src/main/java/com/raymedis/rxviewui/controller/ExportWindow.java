package com.raymedis.rxviewui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.service.export.ExportWindowController;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ExportWindow {

    public HBox imageFormat;
    public Label imageFormatMandatory;
    public JFXButton dicomButton;
    public JFXButton bmpButton;
    public JFXButton pngButton;
    public JFXButton jpegButton;
    public HBox exportPathBox;
    public Label exportMandatory;
    public TextField selectedPath;
    public JFXButton pathButton;
    public Label statusLabel;
    public JFXButton startButton;
    public JFXButton closeButton;
    public ProgressBar progressBar;
    public JFXCheckBox burnAnnotationsCheckBox;
    public JFXCheckBox burnInformationCheckBox;

    public JFXCheckBox burnCropCheckBox;
    public ViewBox viewBox;

    private ExportWindowController exportWindowController = ExportWindowController.getInstance();

    public void initialize(){

        exportWindowController.imageFormat=imageFormat;
        exportWindowController.imageFormatMandatory=imageFormatMandatory;
        exportWindowController.dicomButton=dicomButton;
        exportWindowController.bmpButton=bmpButton;
        exportWindowController.pngButton=pngButton;
        exportWindowController.jpegButton=jpegButton;
        exportWindowController.exportPathBox=exportPathBox;
        exportWindowController.exportMandatory=exportMandatory;
        exportWindowController.selectedPath=selectedPath;
        exportWindowController.pathButton=pathButton;
        exportWindowController.statusLabel=statusLabel;
        exportWindowController.startButton=startButton;
        exportWindowController.closeButton=closeButton;
        exportWindowController.progressBar=progressBar;
        exportWindowController.burnAnnotationsCheckBox=burnAnnotationsCheckBox;
        exportWindowController.burnInformationCheckBox=burnInformationCheckBox;

        exportWindowController.burnCropCheckBox=burnCropCheckBox;
        exportWindowController.viewBox=viewBox;

        exportWindowController.loadEvents();
    }



    public void dicomClick() {
       exportWindowController.dicomClick();
    }

    public void bmpClick() {
        exportWindowController.bmpClick();
    }

    public void pngClick() {
        exportWindowController.pngClick();
    }

    public void jpegClick() {
      exportWindowController.jpegClick();
    }

    public void dialogBoxClick() {
      exportWindowController.dialogBoxClick();
    }

    public void startExport() {
        exportWindowController.startExport();
    }


    public void closeClick(ActionEvent actionEvent) {
      exportWindowController.closeClick(actionEvent);

    }


}
