package com.raymedis.rxviewui.service.adminSettings.studySettings;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RejectedListController {

    private final static RejectedListController instance = new RejectedListController();

    public static RejectedListController getInstance(){
        return instance;
    }

    public JFXComboBox searchByComboBox;
    public TextField detailsText;
    public DatePicker fromDate;
    public DatePicker toDate;
    public TableView rejectedListDataGrid;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn dobColumn;
    public TableColumn sexColumn;
    public TableColumn studyDateTimeColumn;


    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private List<PatientStudyEntity> totalRejectedStudies = new ArrayList<>();

    private static PatientStudyEntity selectedPatient = null;

    public void loadEvents() {
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        studyDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("studyDateTime"));
        studyDateTimeColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDateTime to "dd/MM/yyyy - HH:mm:ss"
                        String formattedDateTime = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
                        setText(formattedDateTime);
                    }
                }
            };
            return cell;
        });

        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        dobColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDate> cell = new TableCell<PatientStudyEntity, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the LocalDate to "dd/MM/yyyy"
                        String formattedDate = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            };
            return cell;
        });
    }

    public void loadData() throws SQLException {
        searchByComboBox.getSelectionModel().select(0);
        detailsText.setDisable(true);

        rejectedListDataGrid.getItems().clear();
        totalRejectedStudies = patientStudyService.findAllRejectedStudies();
        rejectedListDataGrid.getItems().addAll(totalRejectedStudies);
    }


    public void todayBtnClick() {
        totalRejectedStudies = patientStudyService.findLastDayRejectedStudies();
        rejectedListDataGrid.getItems().setAll(totalRejectedStudies);
    }

    public void weekBtnClick() {
        totalRejectedStudies = patientStudyService.findLastWeekRejectedStudies();
        rejectedListDataGrid.getItems().setAll(totalRejectedStudies);
    }

    public void monthBtnClick() {
        totalRejectedStudies = patientStudyService.findLastMonthRejectedStudies();
        rejectedListDataGrid.getItems().setAll(totalRejectedStudies);
    }

    private void filterByDates(LocalDateTime from, LocalDateTime to) {
        int index = searchByComboBox.getSelectionModel().getSelectedIndex();
        System.out.println("selected : " + index);
        String detail = detailsText.getText();

        if (from == null && to != null || from != null && to == null || from != null && from.isAfter(to)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates");
            alert.showAndWait();
            return;
        }

        if (from == null) {
            from = LocalDateTime.MIN;
            to = LocalDateTime.now();
        }

        if (detail != null && !detail.isEmpty()) {
            switch (index) {
                case 1:
                    totalRejectedStudies = patientStudyService.findAllRejectedStudiesFilterByPatientName(from, to, detail);
                    break;
                case 2:
                    totalRejectedStudies = patientStudyService.findAllRejectedStudiesFilterByPatientId(from, to, detail);
                    break;
                case 3:
                    totalRejectedStudies = patientStudyService.findAllRejectedStudiesFilterByAccessionNumber(from, to, detail);
                    break;
                default:
                    totalRejectedStudies = patientStudyService.findAllRejectedStudiesCustomDateRange(from, to);
                    break;
            }
        } else {
            totalRejectedStudies = patientStudyService.findAllRejectedStudiesCustomDateRange(from, to);
        }

        rejectedListDataGrid.getItems().setAll(totalRejectedStudies);
    }

    private void resetSearchFields() {
        searchByComboBox.getSelectionModel().select(0);
        toDate.setValue(null);
        fromDate.setValue(null);
        detailsText.clear();
    }

    public void clearClick() {
        rejectedListDataGrid.getItems().clear();
        rejectedListDataGrid.refresh();
        resetSearchFields();
        searchClick();
    }

    public void searchClick() {
        try {
            LocalDateTime from = fromDate.getValue() != null ? fromDate.getValue().atStartOfDay() : null;
            LocalDateTime to = toDate.getValue() != null ? toDate.getValue().atStartOfDay().plusDays(1).minusNanos(1) : null;
            filterByDates(from, to);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates");
            alert.showAndWait();
        }
    }

    public void selectionChanged() {
        int selectedIndex = searchByComboBox.getSelectionModel().getSelectedIndex();
        detailsText.setDisable(selectedIndex == 0);
    }

    public void deleteClick() throws SQLException {
        PatientStudyEntity patientStudy = (PatientStudyEntity) rejectedListDataGrid.getSelectionModel().getSelectedItem();

        if(patientStudy==null){
            return;
        }

        patientStudyService.deletePatient(patientStudy.getStudyId());
        rejectedListDataGrid.getItems().clear();
        totalRejectedStudies = patientStudyService.findAllRejectedStudies();
        rejectedListDataGrid.getItems().addAll(totalRejectedStudies);
    }

}
