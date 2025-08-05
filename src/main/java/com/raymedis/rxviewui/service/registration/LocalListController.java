package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListEntity;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListService;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LocalListController {

    private static LocalListController instance = new LocalListController();
    public static LocalListController getInstance(){
        return instance;
    }

    public JFXComboBox searchByComboBox;
    public TextField detailsText;
    public DatePicker fromDate;
    public DatePicker toDate;

    public TableView localListTableView;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn dobColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn referringPhysician;
    public TableColumn studyDescriptionColumn;
    public TableColumn scheduleDateTimeColumn;
    public TableColumn registerDateColumn;
    public TableColumn registerTimeColumn;
    public TableColumn weightColumn;
    public TableColumn physicianNameColumn;
    public TableColumn modalityColumn;

    private final PatientLocalListService patientLocalListService = PatientLocalListService.getInstance();
    private ArrayList<PatientLocalListEntity> patientLocalListEntityArrayList = new ArrayList<>();


    public void loadEvents(){

        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        accessionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accessionNumber"));
        referringPhysician.setCellValueFactory(new PropertyValueFactory<>("referringPhysician"));
        studyDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("studyDescription"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        physicianNameColumn.setCellValueFactory(new PropertyValueFactory<>("performingPhysician"));
        modalityColumn.setCellValueFactory(new PropertyValueFactory<>("modality"));

        scheduleDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("scheduledDateTime"));
        scheduleDateTimeColumn.setCellFactory(col -> {
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

        registerDateColumn.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        registerDateColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String formattedDate = item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            };
            return cell;
        });

        registerTimeColumn.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        registerTimeColumn.setCellFactory(col -> {
            TableCell<PatientStudyEntity, LocalDateTime> cell = new TableCell<PatientStudyEntity, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        LocalTime time = item.toLocalTime();
                        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                        setText(formattedTime);
                    }
                }
            };
            return cell;
        });

    }

    public void loadData(){
        localListTableView.getItems().clear();
        try {
            patientLocalListEntityArrayList = patientLocalListService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        localListTableView.getItems().addAll(patientLocalListEntityArrayList);
    }

    public void selectionChanged() {
        int selectedIndex = searchByComboBox.getSelectionModel().getSelectedIndex();
        detailsText.setDisable(selectedIndex == 0);
    }

    public void todayBtnClick() {
        patientLocalListEntityArrayList = patientLocalListService.findLastDay();
        localListTableView.getItems().setAll(patientLocalListEntityArrayList);
    }

    public void weekBtnClick() {
        patientLocalListEntityArrayList = patientLocalListService.findLastWeek();
        localListTableView.getItems().setAll(patientLocalListEntityArrayList);
    }

    public void monthBtnClick() {
        patientLocalListEntityArrayList = patientLocalListService.findLastMonth();
        localListTableView.getItems().setAll(patientLocalListEntityArrayList);
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

    private void filterByDates(LocalDateTime from, LocalDateTime to) {
        int index = searchByComboBox.getSelectionModel().getSelectedIndex();
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
                    patientLocalListEntityArrayList = patientLocalListService.findAllPatientsFilterByPatientName(from, to, detail);
                    break;
                case 2:
                    patientLocalListEntityArrayList = patientLocalListService.findAllPatientsFilterByPatientId(from, to, detail);
                    break;
                case 3:
                    patientLocalListEntityArrayList = patientLocalListService.findAllPatientsFilterByAccessionNumber(from, to, detail);
                    break;
                default:
                    patientLocalListEntityArrayList = patientLocalListService.findCustomDateRange(from, to);
                    break;
            }
        } else {
            patientLocalListEntityArrayList = patientLocalListService.findCustomDateRange(from, to);
        }

        localListTableView.getItems().setAll(patientLocalListEntityArrayList);
    }

    private final Logger logger = LoggerFactory.getLogger(LocalListController.class);

    private final LocalListService localListService = LocalListService.getInstance();
    public void startButtonClick() {
        PatientLocalListEntity patientLocalListEntity = (PatientLocalListEntity) localListTableView.getSelectionModel().getSelectedItem();

        if(patientLocalListEntity !=null){
            int result = localListService.localListTrigger(patientLocalListEntity);

            switch (result){
                case 0:
                    NavConnect.getInstance().navigateToStudy();
                    break;
                case -2:
                    showMaxStudiesAlert();
                    break;
                case 1:
                    break;
                default:
                    logger.info("Something Wrong Study Trigger");
            }
        }

    }

    public void showMaxStudiesAlert() {
        showAlert(Alert.AlertType.WARNING,
                "Warning",
                "Maximum Studies Opened",
                "The maximum number of studies are opened. Please save and close the existing studies before proceeding.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }


    public void clearClick() {
        localListTableView.getItems().clear();
        localListTableView.refresh();
        resetSearchFields();
        searchClick();
    }

    private void resetSearchFields() {
        searchByComboBox.getSelectionModel().select(0);
        toDate.setValue(null);
        fromDate.setValue(null);
        detailsText.clear();
    }


}
