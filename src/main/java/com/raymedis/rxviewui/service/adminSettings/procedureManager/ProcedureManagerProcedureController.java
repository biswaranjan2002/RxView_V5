package com.raymedis.rxviewui.service.adminSettings.procedureManager;

import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table.ProcedureBodyPartService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProcedureManagerProcedureController {


    private static final ProcedureManagerProcedureController instance = new ProcedureManagerProcedureController();



    public static ProcedureManagerProcedureController getInstance(){
        return instance;
    }

    public TableView procedureTable;
    public TableColumn<ProcedureEntity,String> procedureIdColumn;
    public TableColumn<ProcedureEntity,String> procedureCodeColumn;
    public TableColumn<ProcedureEntity,String> procedureNameColumn;
    public TableColumn<ProcedureEntity,String> procedureDescriptionColumn;

    public TableView procedureStepsTable;
    public TableColumn procedureStepNameColumn;

    public TableView bodyPartStepTable;
    public TableColumn bodyPartStepNameColumn;

    public TableView bodyPartsTable;
    public TableColumn bodyPartsNameColumn;

    public TableView patientSizeTable;
    public TableColumn patientSizeColumn;

    private ProcedureService procedureService = ProcedureService.getInstance();
    private StepService stepService = StepService.getInstance();
    public ArrayList<ProcedureEntity> procedureList  = new ArrayList<>();
    private ArrayList<ProcedureBodyPartEntity> bodyPartList = new ArrayList<>();
    private ArrayList<StepEntity> selectedProcedureStepList = new ArrayList<>();
    private ArrayList<StepEntity> selectedBodyPartStepList = new ArrayList<>();


    public void loadEvents(){
        procedureIdColumn.setCellValueFactory(new PropertyValueFactory<>("procedureId"));
        procedureCodeColumn.setCellValueFactory(new PropertyValueFactory<>("procedureCode"));
        procedureNameColumn.setCellValueFactory(new PropertyValueFactory<>("procedureName"));
        procedureDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("procedureDescription"));

        procedureStepNameColumn.setCellValueFactory(new PropertyValueFactory<>("stepName"));

        bodyPartStepNameColumn.setCellValueFactory(new PropertyValueFactory<>("stepName"));

        bodyPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("meaning"));

        patientSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        patientSizeTable.getItems().clear();
        ObservableList<PatientSize> patientSizes = FXCollections.observableArrayList(
                new PatientSize("Small"),
                new PatientSize("Medium"),
                new PatientSize("Big"),
                new PatientSize("Infant")
        );
        patientSizeTable.getItems().addAll(patientSizes);

        procedureCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        procedureCodeColumn.setOnEditCommit(event -> {
            ProcedureEntity entity = event.getRowValue();
            entity.setProcedureCode(event.getNewValue());

            try {
                procedureService.update(entity.getProcedureId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        procedureNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        procedureNameColumn.setOnEditCommit(event -> {
            ProcedureEntity entity = event.getRowValue();
            entity.setProcedureName(event.getNewValue());

            try {
                procedureService.update(entity.getProcedureId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        procedureDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        procedureDescriptionColumn.setOnEditCommit(event -> {
            ProcedureEntity entity = event.getRowValue();
            entity.setProcedureDescription(event.getNewValue());

            try {
                procedureService.update(entity.getProcedureId(), entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        procedureTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
               loadSelectedProcedureSteps();
            }
        });

        bodyPartsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                patientSizeTable.getSelectionModel().clearSelection();
                bodyPartStepTable.getItems().clear();
            }
        });


        patientSizeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isSelected = bodyPartStepTable.getSelectionModel().getSelectedItems().isEmpty();
                if(isSelected){
                    loadSelectedBodyPartSteps();
                }
            }
        });


    }

    private void loadSelectedBodyPartSteps() {
        ProcedureBodyPartEntity entity = (ProcedureBodyPartEntity) bodyPartsTable.getSelectionModel().getSelectedItem();
        String selectedBodyPart = entity.getMeaning();

        PatientSize patientSize = (PatientSize) patientSizeTable.getSelectionModel().getSelectedItem();
        String Size = patientSize.getSize();

        bodyPartStepTable.getItems().clear();
        selectedBodyPartStepList = stepService.findStepByPatientSize(Size,selectedBodyPart);
        bodyPartStepTable.getItems().addAll(selectedBodyPartStepList);
    }

    private void loadSelectedProcedureSteps() {
        ProcedureEntity entity = (ProcedureEntity) procedureTable.getSelectionModel().getSelectedItem();
        int selectedProcedureId = entity.getProcedureId();

        procedureStepsTable.getItems().clear();
        selectedProcedureStepList = stepService.findByProcedureId(selectedProcedureId);
        procedureStepsTable.getItems().addAll(selectedProcedureStepList);
    }

    public void loadData() throws SQLException {
        procedureTable.getSelectionModel().clearSelection();
        patientSizeTable.getSelectionModel().clearSelection();
        procedureTable.getItems().clear();
        procedureList=procedureService.findAll();
        procedureTable.getItems().addAll(procedureList);

        procedureStepsTable.getItems().clear();
        bodyPartStepTable.getItems().clear();

        bodyPartsTable.getItems().clear();
        bodyPartList = ProcedureBodyPartService.getInstance().findAll();
        bodyPartsTable.getItems().addAll(bodyPartList);
    }

    public void procedureAddClick() {
        if (!procedureList.isEmpty() && procedureList.get(procedureList.size() - 1).getProcedureName() == null) {
            return;
        }

        ProcedureEntity procedureEntity = new ProcedureEntity(procedureList.size()+1,"","","");
        try {
            procedureService.save(procedureEntity);
            procedureList = procedureService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        procedureTable.getItems().clear();
        procedureTable.getItems().addAll(procedureList);
        procedureTable.scrollTo(procedureList.size() - 1);
    }

    public void procedureDeleteClick() {
        ProcedureEntity selectedProcedure = (ProcedureEntity) procedureTable.getSelectionModel().getSelectedItem();

        if(selectedProcedure.getProcedureId()==1){
            return;
        }

        try {
            ProcedureEntity entity = (ProcedureEntity) procedureTable.getSelectionModel().getSelectedItem();
            int selectedProcedureId = entity.getProcedureId();

            selectedProcedureStepList = stepService.findByProcedureId(selectedProcedureId);

            for (StepEntity step : selectedProcedureStepList) {
                step.setProcedureId(0);
                stepService.update(step.getStepId(), step);
            }

            procedureService.delete(selectedProcedure.getProcedureId());

            procedureTable.getItems().clear();
            procedureList = procedureService.findAll();
            procedureTable.getItems().addAll(procedureList);

            procedureStepsTable.getItems().clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stepAddClick() {
        if(procedureTable.getSelectionModel().getSelectedItem()==null) {
            return;
        }

        StepEntity selectedStep = (StepEntity) bodyPartStepTable.getSelectionModel().getSelectedItem();
        int selectedProcedureId = procedureTable.getSelectionModel().getSelectedIndex()+1;
        selectedStep.setProcedureId(selectedProcedureId);
        try {
            stepService.update(selectedStep.getStepId(),selectedStep);
            loadSelectedProcedureSteps();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stepDeleteClick() {
        StepEntity selectedStepEntity = (StepEntity) procedureStepsTable.getSelectionModel().getSelectedItem();
        selectedStepEntity.setProcedureId(0);
        try {
            stepService.update(selectedStepEntity.getStepId(),selectedStepEntity);
            loadSelectedProcedureSteps();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static class PatientSize {
        private final String size;

        public PatientSize(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }
    }

}
