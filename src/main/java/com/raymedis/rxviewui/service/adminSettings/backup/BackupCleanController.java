package com.raymedis.rxviewui.service.adminSettings.backup;

import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BackupCleanController {

    private final static BackupCleanController instance = new BackupCleanController();
    public TableView backupClearDataGrid;

    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn rejectedColumn;

    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private ArrayList<PatientStudyEntity> backedUpStudies=new ArrayList<>();
    public static BackupCleanController getInstance(){
        return instance;
    }

    public void loadEvents(){
        registerDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        registerDateTimeColumn.setCellFactory(col -> {
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

        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));


        accessionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accessionNumber"));
        studyDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("studyDescription"));


        exposureDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exposureDateTime"));
        exposureDateTimeColumn.setCellFactory(col -> {
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

        rejectedColumn.setCellValueFactory(new PropertyValueFactory<>("isRejected"));

        rejectedColumn.setCellFactory(tc -> new TableCell<PatientStudyEntity, Boolean>() {
            @Override
            protected void updateItem(Boolean isRejected, boolean empty) {
                super.updateItem(isRejected, empty);
                if (empty || isRejected == null) {
                    setText(null);
                } else {
                    setText(isRejected ? "True" : "False");
                }
            }
        });

    }

    public void loadData() {
        backupClearDataGrid.getItems().clear();
        backedUpStudies = (ArrayList<PatientStudyEntity>) patientStudyService.findAllBackedUpStudies();
        backupClearDataGrid.getItems().addAll(backedUpStudies);
    }

    public void cleanClick() throws SQLException {
        PatientStudyEntity patientStudyEntity = (PatientStudyEntity) backupClearDataGrid.getSelectionModel().getSelectedItem();

        if(patientStudyEntity==null){
            return;
        }

        File selectedFile = new File(System.getProperty("user.dir") + "\\output\\" + patientStudyEntity.getPatientId() + "\\" + patientStudyEntity.getStudyId() + ".rxv");
        if(selectedFile.exists()){
            selectedFile.delete();

            PatientStudyEntity p = patientStudyService.findPatientByStudyId(patientStudyEntity.getStudyId());
            p.setIsCleaned(true);
            p.setIsBackedUp(true);
            patientStudyService.updatePatient(p.getStudyId(),p);


            File selectedDirectory = new File(System.getProperty("user.dir") + "\\output\\" + patientStudyEntity.getPatientId());

            if (selectedDirectory.isDirectory() && selectedDirectory.list().length == 0) {
                selectedDirectory.delete();
            }
        }

        backupClearDataGrid.getItems().clear();
        backedUpStudies = (ArrayList<PatientStudyEntity>) patientStudyService.findAllBackedUpStudies();
        backupClearDataGrid.getItems().addAll(backedUpStudies);
    }

    public void cleanAllClick() throws SQLException {
        for (PatientStudyEntity patientStudyEntity : backedUpStudies){
            File selectedFile = new File(System.getProperty("user.dir") + "\\output\\" + patientStudyEntity.getPatientId() + "\\" + patientStudyEntity.getStudyId() + ".rxv");

            if(selectedFile.exists()){
                selectedFile.delete();

                patientStudyEntity.setIsCleaned(true);
                patientStudyEntity.setIsBackedUp(true);
                patientStudyService.updatePatient(patientStudyEntity.getStudyId(),patientStudyEntity);

                File selectedDirectory = new File(System.getProperty("user.dir") + "\\output\\" + patientStudyEntity.getPatientId());

                if (selectedDirectory.isDirectory() && selectedDirectory.list().length == 0) {
                    selectedDirectory.delete();
                }
            }
        }

        backupClearDataGrid.getItems().clear();
        backedUpStudies = (ArrayList<PatientStudyEntity>) patientStudyService.findAllBackedUpStudies();
        backupClearDataGrid.getItems().addAll(backedUpStudies);
    }


}
