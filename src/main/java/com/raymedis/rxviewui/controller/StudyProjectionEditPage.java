package com.raymedis.rxviewui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table.CategoryBodyPartsService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StudyProjectionEditPage {


    public JFXButton closeButton;
    public JFXButton updateButton;
    public JFXComboBox bodyPartComboBox;
    public JFXComboBox projectionComboBox;
    public Label updatedProjectionLabel;
    public Label currentProjectionLabel;

    private final CategoryBodyPartsService categoryBodyPartsService = CategoryBodyPartsService.getInstance();
    private PatientStudy patientStudy ;
    private PatientBodyPart patientBodyPart;

    public void initialize(){
        loadData();
    }

    public void loadData(){
        patientStudy = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy();

        String patientGender = patientStudy.getPatientInfo().getSex();
        String patientSize = patientStudy.getPatientInfo().getPatientSize();

        ArrayList<CategoryBodyPartsEntity> selectedBodyParts;

        if(patientSize.equals("Infant")){
            selectedBodyParts = categoryBodyPartsService.getAllSelectedBodyParts("infant");
        }else if(patientGender.equals("male")){
            selectedBodyParts = categoryBodyPartsService.getAllSelectedBodyParts("male");
        }else if(patientGender.equals("female")){
            selectedBodyParts = categoryBodyPartsService.getAllSelectedBodyParts("female");
        }else{
            selectedBodyParts=null;
        }

        if (selectedBodyParts != null) {
            for (CategoryBodyPartsEntity bodyPartsEntity:selectedBodyParts){
                bodyPartComboBox.getItems().add(bodyPartsEntity.getBodyPartName());
            }
        }

        patientBodyPart= patientStudy.getBodyPartHandler().getCurrentBodyPart().getBodyPart();
        currentProjectionLabel.setText(patientBodyPart.getBodyPartName() + " " + patientBodyPart.getBodyPartPosition());
    }


    public void closeClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void updateClick(ActionEvent actionEvent) {
        patientBodyPart.setBodyPartName(bodyPartComboBox.getSelectionModel().getSelectedItem().toString());
        patientBodyPart.setBodyPartPosition(projectionComboBox.getSelectionModel().getSelectedItem().toString());

        StudyService.getInstance().createBodyPartTabs();
        closeClick(actionEvent);
    }

    public void bodyPartSelection() {
        projectionComboBox.getItems().clear();

        String bodyPart = bodyPartComboBox.getSelectionModel().getSelectedItem().toString();

        ArrayList<StepEntity> selectedBodyPartStepsList = StepService.getInstance().findByStepName(bodyPart);

        for(StepEntity step : selectedBodyPartStepsList){
            if(step.getPatientSize().equals(patientStudy.getPatientInfo().getPatientSize())){
                projectionComboBox.getItems().add(step.getStepName().replace(bodyPart,""));
            }
        }
    }

    public void projectionSelection() {
        if (bodyPartComboBox.getSelectionModel().getSelectedItem() != null
                && projectionComboBox.getSelectionModel().getSelectedItem() != null) {
            String bodyPart = bodyPartComboBox.getSelectionModel().getSelectedItem().toString();
            String projection = projectionComboBox.getSelectionModel().getSelectedItem().toString();
            updatedProjectionLabel.setText(bodyPart + projection);

            updateButton.setDisable(false);
            if(currentProjectionLabel.getText().equals(updatedProjectionLabel.getText())){
                updateButton.setDisable(true);
            }

        } else {
            updatedProjectionLabel.setText("Selection Error");
        }
    }

}
