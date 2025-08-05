package com.raymedis.rxviewui.controller.printModule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.service.print.PrintController;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class Print {

    public JFXButton selectButton;
    public JFXButton zoomButton;
    public JFXButton panButton;
    public JFXButton windowLevelButton;
    public JFXButton fitButton;
    public JFXButton cropButton;
    public JFXButton vFlipButton;
    public JFXButton hFlipButton;
    public JFXButton brightnessButton;
    public JFXButton cwButton;
    public JFXButton ccwButton;


    public JFXButton distanceButton;
    public JFXButton angleButton;
    public JFXButton labelLButton;
    public JFXButton labelRButton;
    public JFXButton textButton;
    public JFXButton deleteButton;
    public JFXButton contrastButton;
    public JFXButton inversionButton;
    public JFXButton rightLabelButton;
    public JFXButton leftLabelButton;
    public JFXButton hideButton;
    public JFXButton resetButton;
    public JFXButton portraitButton;
    public JFXButton landScapeButton;
    public VBox layoutBox;

    public JFXComboBox aeTitleComboBox;
    public JFXComboBox sizeComboBox;
    public ViewBox imageContainerViewBox;
    public VBox bodyPartsContainer;
    public VBox pageContainer;
    public VBox toolsBox;
    public GridPane mainViewBoxParent;


    private PrintController printController = PrintController.getInstance();

    public void initialize() throws IOException {
        selectButton.getParent().requestFocus();




        printController.imageContainerViewBox=imageContainerViewBox;
        printController.layoutBox= layoutBox;
        printController.portraitButton=portraitButton;
        printController.landScapeButton=landScapeButton;
        printController.bodyPartsContainer=bodyPartsContainer;
        printController.pageContainer=pageContainer;

        printController.distanceButton=distanceButton;
        printController.angleButton=angleButton;
        printController.labelLButton=labelLButton;
        printController.labelRButton=labelRButton;
        printController.textButton=textButton;
        printController.deleteButton=deleteButton;

        printController.selectButton=selectButton;
        printController.zoomButton=zoomButton;
        printController.panButton=panButton;
        printController.fitButton=fitButton;
        printController.cropButton=cropButton;
        printController.vFlipButton=vFlipButton;
        printController.hFlipButton=hFlipButton;
        printController.brightnessButton=brightnessButton;
        printController.cwButton=cwButton;
        printController.ccwButton=ccwButton;

        printController.contrastButton =contrastButton;
        printController.inversionButton =inversionButton;
        printController.rightLabelButton =rightLabelButton;
        printController.leftLabelButton =leftLabelButton;
        printController.hideButton =hideButton;
        printController.resetButton =resetButton;
        printController.mainViewBoxParent=mainViewBoxParent;

        printController.toolsBox=toolsBox;


        printController.aeTitleComboBox=aeTitleComboBox;
        printController.sizeComboBox=sizeComboBox;

        printController.loadEvents();
        printController.loadData();
        printController.loadPrintSizeAndOrientation();
    }




    public void selectClick() {
        printController.selectClick();
    }

    public void zoomClick() {
        printController.zoomClick();
    }

    public void panClick() {
        printController.panClick();
    }

    public void inversionClick() {
        printController.inversionClick();
    }

    public void fitClick() {
        printController.fitClick();
    }

    public void cropClick() {
        printController.cropClick();
    }

    public void brightnessClick() {
        printController.brightnessClick();
    }

    public void cwClick() {
        printController.cwClick();
    }

    public void ccwClick() {
        printController.ccwClick();
    }




    public void distanceClick() {
        printController.distanceClick();
    }

    public void angleClick() {
        printController.angleClick();
    }

    public void textClick() {
        printController.textClick();
    }

    public void deleteClick() {
        printController.deleteClick();
    }

    public void contrastClick() {
        printController.contrastClick();
    }

    public void flipClick() {
        printController.hFlipClick();
    }

    public void rightLabelClick() {
        printController.rightLabelClick();
    }

    public void leftLabelClick() {
        printController.leftLabelClick();
    }


    public void hideClick() {
        printController.hideClick();
    }


    public void resetClick() {
        printController.resetClick();
    }


    public void portraitClick() {
       printController.portraitClick();
    }

    public void landScapeClick() {
        printController.landScapeClick();
    }

    public void sizeComboBoxSelection() {
        printController.sizeComboBoxSelection();
    }


    public void newPageClick() {
        printController.newPageClick();
    }

    public void deletePageClick() {
        printController.deletePageClick();
    }


    public void printClick() throws SQLException {
        printController.printClick();
    }

    public void cancelClick() {
        printController.cancelClick();
    }
}
