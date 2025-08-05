package com.raymedis.rxviewui.controller.adminSettings.procedureManager;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerStepController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ProcedureManagerStep {


    public TextField inputTextFiled;
    public JFXButton searchButton;
    
    public TableView bodyPartsTable;
    public TableColumn bodyPartsNameColumn;

    public TableView projectionTable;
    public TableColumn projectionNameColumn;

    public JFXButton insertStepButton;
    public JFXButton deleteStepButton;

    public TableView stepTable;
    public TableColumn stepIdColumn;
    public TableColumn stepNameColumn;
    public TableColumn patientSizeColumn;
    public TableColumn autoCropColumn;
    public TableColumn imageProcessParamKeyColumn;
    public TableColumn detectorNoColumn;
    public TableColumn lrLabelColumn;
    public TableColumn lrPosColumn;
    public TableColumn textColumn;
    public TableColumn textPosColumn;
    public TableColumn flipColumn;
    public TableColumn mirrorColumn;
    public TableColumn rotationColumn;
    public TableColumn imageRotationColumn;
    public TableColumn consoleKvColumn;
    public TableColumn consoleMasColumn;
    public TableColumn consoleMaColumn;
    public TableColumn consoleMsColumn;
    public TableColumn consoleAecColumn;
    public TableColumn consoleAecLeftColumn;
    public TableColumn consoleAecRightColumn;
    public TableColumn consoleAecCenterColumn;
    public TableColumn consoleDenColumn;
    public TableColumn generatorP1Column;
    public TableColumn generatorP2Column;
    public TableColumn imageProcTypeColumn;
    public TableColumn sidColumn;
    public TableColumn imageParamTypeColumn;
    public TableColumn armAngleColumn;
    public TableColumn detectorAngleColumn;
    public TableColumn tubeTiltAngleColumn;
    public TableColumn tubeRotateAngleColumn;
    public TableColumn ceilingPositionColumn;
    public TableColumn detectorArmPositionColumn;
    public TableColumn tubeArmPositionColumn;



    private ProcedureManagerStepController procedureManagerStepController = ProcedureManagerStepController.getInstance();

    public void initialize(){

        procedureManagerStepController.inputTextFiled=inputTextFiled;
        procedureManagerStepController.searchButton=searchButton;

        procedureManagerStepController.bodyPartsTable=bodyPartsTable;
        procedureManagerStepController.bodyPartsNameColumn=bodyPartsNameColumn;

        procedureManagerStepController.projectionTable=projectionTable;
        procedureManagerStepController.projectionNameColumn=projectionNameColumn;

        procedureManagerStepController.insertStepButton=insertStepButton;
        procedureManagerStepController.deleteStepButton=deleteStepButton;

        procedureManagerStepController.stepTable=stepTable;
        procedureManagerStepController.stepIdColumn=stepIdColumn;
        procedureManagerStepController.stepNameColumn=stepNameColumn;
        procedureManagerStepController.patientSizeColumn=patientSizeColumn;
        procedureManagerStepController.autoCropColumn=autoCropColumn;
        procedureManagerStepController.imageProcessParamKeyColumn=imageProcessParamKeyColumn;
        procedureManagerStepController.detectorNoColumn=detectorNoColumn;
        procedureManagerStepController.lrLabelColumn=lrLabelColumn;
        procedureManagerStepController.lrPosColumn=lrPosColumn;
        procedureManagerStepController.textColumn=textColumn;
        procedureManagerStepController.textPosColumn=textPosColumn;
        procedureManagerStepController.flipColumn=flipColumn;
        procedureManagerStepController.mirrorColumn=mirrorColumn;
        procedureManagerStepController.rotationColumn=rotationColumn;
        procedureManagerStepController.imageRotationColumn=imageRotationColumn;
        procedureManagerStepController.consoleKvColumn=consoleKvColumn;
        procedureManagerStepController.consoleMasColumn=consoleMasColumn;
        procedureManagerStepController.consoleMaColumn=consoleMaColumn;
        procedureManagerStepController.consoleMsColumn=consoleMsColumn;
        procedureManagerStepController.consoleAecColumn=consoleAecColumn;
        procedureManagerStepController.consoleAecLeftColumn=consoleAecLeftColumn;
        procedureManagerStepController.consoleAecRightColumn=consoleAecRightColumn;
        procedureManagerStepController.consoleAecCenterColumn=consoleAecCenterColumn;
        procedureManagerStepController.consoleDenColumn=consoleDenColumn;
        procedureManagerStepController.generatorP1Column=generatorP1Column;
        procedureManagerStepController.generatorP2Column=generatorP2Column;
        procedureManagerStepController.imageProcTypeColumn=imageProcTypeColumn;
        procedureManagerStepController.sidColumn=sidColumn;
        procedureManagerStepController.imageParamTypeColumn=imageParamTypeColumn;
        procedureManagerStepController.armAngleColumn=armAngleColumn;
        procedureManagerStepController.detectorAngleColumn=detectorAngleColumn;
        procedureManagerStepController.tubeTiltAngleColumn=tubeTiltAngleColumn;
        procedureManagerStepController.tubeRotateAngleColumn=tubeRotateAngleColumn;
        procedureManagerStepController.ceilingPositionColumn=ceilingPositionColumn;
        procedureManagerStepController.detectorArmPositionColumn=detectorArmPositionColumn;
        procedureManagerStepController.tubeArmPositionColumn=tubeArmPositionColumn;

        procedureManagerStepController.loadEvents();
        try {
            procedureManagerStepController.loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addStepClick() {
        procedureManagerStepController.addStepClick();
    }

    public void deleteStepClick() {
        procedureManagerStepController.deleteStepClick();
    }


    public void searchClick() {
        procedureManagerStepController.searchClick();
    }

    public void clearClick() {
        procedureManagerStepController.clearClick();
    }
}
