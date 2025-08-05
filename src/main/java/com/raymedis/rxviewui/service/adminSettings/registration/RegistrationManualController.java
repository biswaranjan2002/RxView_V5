package com.raymedis.rxviewui.service.adminSettings.registration;

import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.CandidateTagsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.RegistrationManualService;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationManualController {

    private static RegistrationManualController instance = new RegistrationManualController();

    private List<CandidateTagsEntity> candidateTags = new ArrayList<>();
    private List<CandidateTagsEntity> selectedTags = new ArrayList<>();

    private final RegistrationManualService registrationManualService = RegistrationManualService.getInstance();

    public TableView candidateTagsTable;
    public TableColumn<CandidateTagsEntity, String> candidateTagsName;


    public TableView selectedCandidateTagsTable;
    public TableColumn selectedCandidateTagsName;

    public ArrayList<String> mandetoryTags;

    public static RegistrationManualController getInstance(){
        return instance;
    }

    public RegistrationManualController(){
        mandetoryTags = new ArrayList<>();

        mandetoryTags.add("patient name");
        mandetoryTags.add("patient id");
    }


    public void loadData() {
        candidateTags.clear();
        selectedTags.clear();

        candidateTags = registrationManualService.getAllCandidateTags();
        selectedTags = registrationManualService.getAllSelectedCandidateTags();

        candidateTagsName.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        candidateTagsTable.getItems().setAll(candidateTags);


        selectedCandidateTagsName.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        selectedCandidateTagsTable.getItems().setAll(selectedTags);

    }

    ManualRegisterController manualRegisterController = ManualRegisterController.getInstance();

    public void addSelectedCandidateTag() {
        CandidateTagsEntity selectedRow = (CandidateTagsEntity) candidateTagsTable.getSelectionModel().getSelectedItem();

        if (selectedRow == null) {
            return;
        }

        try {
            if (selectedRow.getSelected().equals(false)) {
                selectedRow.setSelected(true);
                registrationManualService.updateCandidateTag(selectedRow.getTagName(), selectedRow);
                loadData();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        manualRegisterController.loadFields();
    }


    public void removeSelectedCandidateTag() {
        CandidateTagsEntity selectedRow = (CandidateTagsEntity) selectedCandidateTagsTable.getSelectionModel().getSelectedItem();


        if (selectedRow == null) {
            return;
        }

        try {
            if (selectedRow.getSelected().equals(true) && !mandetoryTags.contains(selectedRow.getTagName().toLowerCase())) {
                selectedRow.setSelected(false);
                registrationManualService.updateCandidateTag(selectedRow.getTagName(), selectedRow);
                loadData();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        manualRegisterController.loadFields();
    }
}

