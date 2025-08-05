package com.raymedis.rxviewui.controller.adminSettings.registration.manual;

import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.CandidateTagsEntity;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationManualController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RegistrationManual {

    @FXML public  TableView candidateTagsTable;
    @FXML public  TableColumn<CandidateTagsEntity, String> candidateTagsName;



    @FXML public TableView selectedCandidateTagsTable;
    @FXML public TableColumn<CandidateTagsEntity, String> selectedCandidateTagsName;



  RegistrationManualController registrationManualController;



    public void initialize(){

        // Listener for Table 1's selection
        candidateTagsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    selectedCandidateTagsTable.getSelectionModel().clearSelection();
                }
            }
        });

        // Listener for Table 2's selection
        selectedCandidateTagsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    candidateTagsTable.getSelectionModel().clearSelection();
                }
            }
        });


        registrationManualController = RegistrationManualController.getInstance();

        registrationManualController.candidateTagsTable = candidateTagsTable;
        registrationManualController.candidateTagsName =candidateTagsName;

        registrationManualController.selectedCandidateTagsTable = selectedCandidateTagsTable;
        registrationManualController.selectedCandidateTagsName =selectedCandidateTagsName;


        registrationManualController.loadData();
    }


    public void addClick(ActionEvent actionEvent) {
        registrationManualController.addSelectedCandidateTag();
    }

    public void removeClick(ActionEvent actionEvent) {
        registrationManualController.removeSelectedCandidateTag();
    }
}
