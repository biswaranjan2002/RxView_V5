package com.raymedis.rxviewui.controller.adminSettings.procedureManager;

import com.raymedis.rxviewui.service.adminSettings.procedureManager.ProcedureManagerProcedureController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public class ProcedureManagerProcedure {


    public TableView procedureTable;
    public TableColumn procedureIdColumn;
    public TableColumn procedureNameColumn;
    public TableColumn procedureDescriptionColumn;

    public TableView ProcedureStepsTable;
    public TableColumn procedureStepNameColumn;

    public TableView stepTable;
    public TableColumn stepNameColumn;

    public TableView bodyPartsTable;
    public TableColumn bodyPartsNameColumn;

    public TableView patientSizeTable;
    public TableColumn patientSizeColumn;
    public TableColumn procedureCodeColumn;

    private ProcedureManagerProcedureController procedureManagerProcedureController = ProcedureManagerProcedureController.getInstance();

    public void initialize(){
        procedureManagerProcedureController.procedureTable = procedureTable;
        procedureManagerProcedureController.procedureIdColumn = procedureIdColumn;
        procedureManagerProcedureController.procedureNameColumn = procedureNameColumn;
        procedureManagerProcedureController.procedureDescriptionColumn = procedureDescriptionColumn;
        procedureManagerProcedureController.procedureCodeColumn = procedureCodeColumn;

        procedureManagerProcedureController.procedureStepsTable = ProcedureStepsTable;
        procedureManagerProcedureController.procedureStepNameColumn = procedureStepNameColumn;

        procedureManagerProcedureController.bodyPartStepTable = stepTable;
        procedureManagerProcedureController.bodyPartStepNameColumn = stepNameColumn;

        procedureManagerProcedureController.bodyPartsTable=bodyPartsTable;
        procedureManagerProcedureController.bodyPartsNameColumn=bodyPartsNameColumn;

        procedureManagerProcedureController.patientSizeTable=patientSizeTable;
        procedureManagerProcedureController.patientSizeColumn=patientSizeColumn;



        procedureManagerProcedureController.loadEvents();
        try {
            procedureManagerProcedureController.loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void procedureAddClick() {
        procedureManagerProcedureController.procedureAddClick();
    }

    public void procedureDeleteClick() {
    procedureManagerProcedureController.procedureDeleteClick();
    }

    public void stepAddClick() {
        procedureManagerProcedureController.stepAddClick();
    }

    public void stepDeleteClick() {
        procedureManagerProcedureController.stepDeleteClick();
    }
}
