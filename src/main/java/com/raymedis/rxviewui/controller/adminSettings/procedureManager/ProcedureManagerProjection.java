package com.raymedis.rxviewui.controller.adminSettings.procedureManager;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProjectionController;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public class ProcedureManagerProjection {
    
    public TableView bodyPartDataGrid;


    public TableColumn bodyPartMeaningColumn;
    public TableColumn bodyPartAliasColumn;
    public TableColumn bodyPartHideColumn;
    public TableColumn bodyPartCodeValueColumn;
    public TableColumn bodyPartDesignatorColumn;
    public TableColumn bodyPartVersionColumn;
    public TableColumn bodyPartExaminationColumn;

    public TableView projectionDataGrid;
    public TableColumn projectionMeaning;
    public TableColumn projectionAlias;
    public TableColumn projectionHide;
    public TableColumn projectionCodeValue;
    public TableColumn projectionDesignator;
    public TableColumn projectionVersion;
    public TableColumn projectionViewPosition;

    public JFXButton projectionHideButton;
    public ScrollPane projectionScrollPane;

    private ProcedureManagerProjectionController procedureManagerProjectionController;

    public void initialize(){
        procedureManagerProjectionController = ProcedureManagerProjectionController.getInstance();

        procedureManagerProjectionController.bodyPartDataGrid = bodyPartDataGrid;


        procedureManagerProjectionController.bodyPartMeaningColumn = bodyPartMeaningColumn;
        procedureManagerProjectionController.bodyPartAliasColumn = bodyPartAliasColumn;
        procedureManagerProjectionController.bodyPartHideColumn = bodyPartHideColumn;
        procedureManagerProjectionController.bodyPartCodeValueColumn = bodyPartCodeValueColumn;
        procedureManagerProjectionController.bodyPartDesignatorColumn = bodyPartDesignatorColumn;
        procedureManagerProjectionController.bodyPartVersionColumn = bodyPartVersionColumn;
        procedureManagerProjectionController.bodyPartExaminationColumn = bodyPartExaminationColumn;


        procedureManagerProjectionController.projectionDataGrid = projectionDataGrid;
        procedureManagerProjectionController.projectionMeaningColumn = projectionMeaning;
        procedureManagerProjectionController.projectionAliasColumn = projectionAlias;
        procedureManagerProjectionController.projectionHideColumn = projectionHide;
        procedureManagerProjectionController.projectionCodeValueColumn = projectionCodeValue;
        procedureManagerProjectionController.projectionDesignatorColumn = projectionDesignator;
        procedureManagerProjectionController.projectionVersionColumn = projectionVersion;
        procedureManagerProjectionController.projectionViewPositionColumn = projectionViewPosition;

        procedureManagerProjectionController.projectionScrollPane = projectionScrollPane;
        procedureManagerProjectionController.loadEvents();
        try {
            procedureManagerProjectionController.loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void bodyPartAddClick() {
        procedureManagerProjectionController.bodyPartAddClick();
    }



    public void projectionAddClick() {
        procedureManagerProjectionController.projectionAddClick();
    }

    public void projectionDeleteClick() {
        procedureManagerProjectionController.projectionDeleteClick();
    }

    public void bodyPartDeleteClick() {
        procedureManagerProjectionController.bodyPartDeleteClick();
    }
}
