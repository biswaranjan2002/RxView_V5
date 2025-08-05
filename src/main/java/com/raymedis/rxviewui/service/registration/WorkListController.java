package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.pixelmed.dicom.AttributeList;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table.DicomMwlEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table.DicomMwlService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.modules.dicom.DicomMain;
import com.raymedis.rxviewui.modules.dicom.DicomWorkListData;
import com.raymedis.rxviewui.modules.dicom.DicomRecord;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WorkListController {

    public JFXComboBox searchByComboBox;
    public TextField detailsText;
    public JFXButton todayButton;
    public JFXButton weekButton;
    public JFXButton monthButton;
    public DatePicker fromDate;
    public DatePicker toDate;
    public JFXButton searchButton;
    public JFXButton clearButton;
    public JFXButton refreshButton;
    public JFXButton startButton;

    public TableView dataGrid;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn dobColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn referringPhysician;
    public TableColumn studyDescriptionColumn;
    public TableColumn scheduleDateColumn;
    public TableColumn scheduledTimeColumn;
    public TableColumn statusColumn;
    public TableColumn procedureCodeColumn;
    public TableColumn bodyPartColumn;
    public TableColumn viewPositionColumn;

    private final static WorkListController instance = new WorkListController();
    public static WorkListController getInstance(){
        return instance;
    }

    private final ObservableList<DicomWorkListData> data = FXCollections.observableArrayList();
    private FilteredList<DicomWorkListData> filteredData;
    private DicomRecord selectedRecord;
    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private ChangeListener<String> genderChangeListener;
    private final DicomMwlService dicomMwlService = DicomMwlService.getInstance();
    private final DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();

    private final Logger logger = LoggerFactory.getLogger(WorkListController.class);

    public void loadEvents(){

        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        dobColumn.setCellFactory(column -> new TableCell<DicomWorkListData, String>() {
            @Override
            protected void updateItem(String dob, boolean empty) {
                super.updateItem(dob, empty);
                if (empty || dob == null) {
                    setText(null);
                } else {
                    setText(formatDateString(dob));
                }
            }
        });
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        accessionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accessionNumber"));
        referringPhysician.setCellValueFactory(new PropertyValueFactory<>("referringPhysician"));
        studyDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("studyDescription"));

        scheduleDateColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));
        scheduleDateColumn.setCellFactory(column -> new TableCell<DicomWorkListData, String>() {
            @Override
            protected void updateItem(String scheduledTime, boolean empty) {
                super.updateItem(scheduledTime, empty);
                if (empty || scheduledTime == null) {
                    setText(null);
                } else {
                    setText(formatDateString(scheduledTime));
                }
            }
        });

        scheduledTimeColumn.setCellValueFactory(new PropertyValueFactory<>("scheduledTime"));
        scheduledTimeColumn.setCellFactory(column -> new TableCell<DicomWorkListData, String>() {
            @Override
            protected void updateItem(String scheduledTime, boolean empty) {
                super.updateItem(scheduledTime, empty);
                if (empty || scheduledTime == null) {
                    setText(null);
                } else {
                    setText(formatTimeString(scheduledTime));
                }
            }
        });

        procedureCodeColumn.setCellValueFactory(new PropertyValueFactory<>("procedureCode"));
        bodyPartColumn.setCellValueFactory(new PropertyValueFactory<>("bodyPartExamined"));
        viewPositionColumn.setCellValueFactory(new PropertyValueFactory<>("viewPosition"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    public void loadData() throws SQLException {
        dataGrid.getItems().clear();
        data.clear();
        selectedRecord = null;
        DicomMwlEntity dicomMwlEntity = dicomMwlService.findSelectedMwl();
        ArrayList<DicomGeneralEntity> dicomGeneralEntityArrayList = dicomGeneralService.findAll();

        if(dicomMwlEntity!=null && !dicomGeneralEntityArrayList.isEmpty()){
            DicomGeneralEntity dicomGeneralEntity = dicomGeneralEntityArrayList.getFirst();

            //logger.info("{} ",dicomGeneralEntity);
            DicomMain dicomMain = new DicomMain(dicomMwlEntity.getIpAddress(), dicomMwlEntity.getPort(), dicomMwlEntity.getAeTitle(), dicomGeneralEntity.getStationAETitle());
            ArrayList<AttributeList> output = dicomMain.find("series", "DX");

            List<PatientStudyEntity> totalPatient = patientStudyService.findAllPatients();

            if(output==null){
                return;
            }

            for (AttributeList attributes : output) {
                String attributesString = attributes.toString();

                //logger.info("attribute string {} : " ,attributesString);

                String dob = extractValue(attributesString, "PatientBirthDate");
                String patientId = extractValue(attributesString, "PatientID");
                String patientName = extractValue(attributesString, "PatientName");
                String sex = extractValue(attributesString, "PatientSex");
                String accessionNumber = extractValue(attributesString, "AccessionNumber");
                String referringPhysician = extractValue(attributesString, "ReferringPhysicianName");
                String studyDescription = extractValue(attributesString, "StudyDescription");
                String scheduledTime = extractValue(attributesString, "ScheduledProcedureStepStartTime");
                String scheduleDate = extractValue(attributesString, "ScheduledProcedureStepStartDate");
                String studyId = extractValue(attributesString, "StudyID");
                String procedureCode= extractValue(attributesString, "ProcedureCodeSequence");
                String bodyPartExamined = extractValue(attributesString,"BodyPartExamined");
                String viewPosition = extractValue(attributesString,"ViewPosition");


                if(scheduleDate.isEmpty()){
                    scheduleDate = "";
                }

                if(scheduledTime.isEmpty()){
                    scheduledTime = "" ;
                }

                String status = "No";

                if(!studyId.isEmpty()){
                    PatientStudyEntity patient = patientStudyService.findPatientByStudyId(studyId);
                    if(patient!=null){
                        status = "Yes";
                    }
                }else{
                    for(PatientStudyEntity p  : totalPatient){
                        if(p.getPatientName().equals(patientName) && p.getPatientId().equals(patientId) && p.getDob().equals(dob) && p.getAccessionNumber().equals(accessionNumber) && p.getSex().equals(sex) && p.getStudyDescription().equals(studyDescription) && p.getReferringPhysician().equals(referringPhysician)){
                            status = "Yes";
                            break;
                        }
                    }
                }


                DicomRecord record = new DicomRecord(dob, patientId, patientName, sex, accessionNumber, referringPhysician, studyDescription, scheduledTime, scheduleDate, studyId,status, procedureCode,bodyPartExamined,viewPosition );
                data.add(record);
                //logger.info("record : {}",record);
            }

            filteredData = new FilteredList<>(data, p -> true);
            dataGrid.getItems().addAll(data);
        }
        else {
            logger.info("no data found");
        }
    }

    private String extractValue(String attributesString, String tag) {
        // Regular expression to extract the value of a specific tag
        String regex = String.format("\\(0x[0-9a-fA-F]+,0x[0-9a-fA-F]+\\) %s VR=<[^>]+> VL=<[^>]+> <([^>]*)>", tag);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(attributesString);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }

    public static String formatDateString(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.length() != 8) {
            return yyyymmdd;
        }

        String year = yyyymmdd.substring(0, 4);
        String month = yyyymmdd.substring(4, 6);
        String day = yyyymmdd.substring(6, 8);

        return day + "/" + month + "/" + year;
    }


    private String formatTimeString(String time) {
        if (time == null || time.length() != 6) {
            return time;
        }

        return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
    }

    public void selectionChanged() {
        int selectedIndex = searchByComboBox.getSelectionModel().getSelectedIndex();
        detailsText.setDisable(selectedIndex == 0);

        if (selectedIndex == 3) {
            addGenderValidation(detailsText);
        } else {
            removeGenderValidation(detailsText);
        }
    }

    private void addGenderValidation(TextField textField) {
        if (genderChangeListener == null) {
            genderChangeListener = (observable, oldValue, newValue) -> {
                // Validate input
                String lowerCaseValue = newValue.toLowerCase();
                if (!(lowerCaseValue.equals("m") || lowerCaseValue.equals("f") || lowerCaseValue.equals("o") || lowerCaseValue.isEmpty())) {
                    // Revert to the previous valid value
                    textField.setText(oldValue);
                }
            };
            textField.textProperty().addListener(genderChangeListener);
        }
    }

    private void removeGenderValidation(TextField textField) {
        if (genderChangeListener != null) {
            textField.textProperty().removeListener(genderChangeListener);
            genderChangeListener = null;
        }
    }

    public void todayBtnClick() {
        todayButton.getParent().requestFocus();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        filterByDates(startOfDay, now);
    }

    public void weekBtnClick() {
        weekButton.getParent().requestFocus();
        filterByDates(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    public void monthBtnClick() {
        monthButton.getParent().requestFocus();
        filterByDates(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
    }

    public void searchClick() {
        searchButton.getParent().requestFocus();
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

        // Validate the date range
        if ((from == null && to != null) || (from != null && to == null) || (from != null && from.isAfter(to))) {
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


        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;
        filteredData.setPredicate(record -> {

            LocalDateTime scheduledDate = handleDob(record.getScheduleDate()).atStartOfDay();

            boolean  dateMatches;
            if(!record.getScheduleDate().isEmpty()){
                dateMatches = (scheduledDate.isAfter(finalFrom) || scheduledDate.isEqual(finalFrom)) &&
                        (scheduledDate.isBefore(finalTo) || scheduledDate.isEqual(finalTo));
            }else{
                dateMatches = true;
            }

            return switch (index) {
                case 1 -> dateMatches && record.getPatientName().toLowerCase().contains(detail.toLowerCase());
                case 2 -> dateMatches && record.getPatientId().toLowerCase().contains(detail.toLowerCase());
                case 3 -> dateMatches && record.getSex().equalsIgnoreCase(detail);
                case 4 -> dateMatches && record.getAccessionNumber().toLowerCase().contains(detail.toLowerCase());
                case 5 -> dateMatches && record.getReferringPhysician().toLowerCase().contains(detail.toLowerCase());
                default -> dateMatches;
            };
        });



        // Refresh the TableView with the filtered data
        dataGrid.getItems().setAll(filteredData);
    }

    public void startButtonClick() {
        selectedRecord = (DicomRecord) dataGrid.getSelectionModel().getSelectedItem();
        //logger.info("record : {}",selectedRecord);
        if (selectedRecord != null) {
            displayRecordDetails(selectedRecord);
        }
    }

    public void clearClick() {
        clearButton.getParent().requestFocus();
        dataGrid.getItems().clear();
        dataGrid.refresh();
        resetSearchFields();
        searchClick();
    }

    private void resetSearchFields() {
        searchByComboBox.getSelectionModel().select(0);
        toDate.setValue(null);
        fromDate.setValue(null);
        detailsText.clear();
    }

    public void refreshButtonClick() throws SQLException {
        refreshButton.getParent().requestFocus();
        loadData();
    }


    private void displayRecordDetails(DicomRecord record) {
        int result = ManualRegisterService.getInstance().workListTrigger(record);
        logger.info("WorkList Trigger Result : {} ",result);
        switch (result){
            case 0:
                NavConnect.getInstance().navigateToStudy();
                break;
            case -2:
                ManualRegisterController.showMaxStudiesAlert();
                break;
            case -1:
                NavConnect.getInstance().navigateToStudy();
                System.out.println();
                break;
            default:
                System.out.println("Something Wrong Study Trigger");
        }
    }

    public LocalDate handleDob(String selectedDob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            if (!selectedDob.isEmpty()) {
                return LocalDate.parse(selectedDob, formatter);
            } else {
                return LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + selectedDob);
            return LocalDate.now();
        }
    }

}
