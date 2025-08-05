package com.raymedis.rxviewui.service.adminSettings.backup;

import com.raymedis.rxviewui.database.tables.adminSettings.backup_table.BackupEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.backup_table.BackupService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.service.serialization.KryoSerializer;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BackupRestoreController {

    private final static BackupRestoreController instance = new BackupRestoreController();

    public TableView backUpRestoreDataGrid;
    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn rejectedColumn;

    public static BackupRestoreController getInstance(){
        return instance;
    }
    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private ArrayList<PatientStudyEntity> restorableStudies = new ArrayList<>();
    private final BackupService backupService = BackupService.getInstance();

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

    public void loadData(){
        backUpRestoreDataGrid.getItems().clear();
        restorableStudies = (ArrayList<PatientStudyEntity>) patientStudyService.findAllRestorableStudies();
        backUpRestoreDataGrid.getItems().addAll(restorableStudies);
    }

    public void restoreClick() {
        PatientStudyEntity patientStudyEntity = (PatientStudyEntity) backUpRestoreDataGrid.getSelectionModel().getSelectedItem();

        if (patientStudyEntity == null) {
            return;
        }

        String studyId = patientStudyEntity.getStudyId();
        BackupEntity backupEntity = backupService.findByStudyId(studyId).getFirst();

        File sourceFile = new File(backupEntity.getBackupPath());
        if (sourceFile.exists()) {
            try {
                PatientStudy patientStudy = KryoSerializer.getInstance().deserializeFromFile(sourceFile);

                String patientId = patientStudy.getPatientInfo().getPatientId();

                PatientStudyEntity patientStudyEntityFromDb = patientStudyService.findPatientByStudyId(studyId);
                patientStudyEntityFromDb.setIsBackedUp(false);
                patientStudyEntityFromDb.setIsCleaned(false);
                patientStudyService.updatePatient(studyId, patientStudyEntityFromDb);

                File projectBaseFolder = new File(System.getProperty("user.dir") + "//output//");

                if (!projectBaseFolder.exists()) {
                    projectBaseFolder.mkdirs();
                }

                File patientFolder = new File(projectBaseFolder, patientId);
                if (!patientFolder.exists()) {
                    patientFolder.mkdirs();
                }

                File targetFile = new File(patientFolder, studyId + ".rxv");
                Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        loadData();
    }


    public void restoreAllClick() {
        try {
            for (BackupEntity backupEntity : backupService.findAll()){
                String studyId = backupEntity.getStudyId();
                File sourceFile = new File(backupEntity.getBackupPath());
                if (sourceFile.exists()) {
                    try {
                        PatientStudy patientStudy = KryoSerializer.getInstance().deserializeFromFile(sourceFile);
                        String patientId = patientStudy.getPatientInfo().getPatientId();

                        PatientStudyEntity patientStudyEntityFromDb = patientStudyService.findPatientByStudyId(studyId);
                        patientStudyEntityFromDb.setIsBackedUp(false);
                        patientStudyEntityFromDb.setIsCleaned(false);
                        patientStudyService.updatePatient(studyId, patientStudyEntityFromDb);

                        File projectBaseFolder = new File(System.getProperty("user.dir") + "//output//");

                        if (!projectBaseFolder.exists()) {
                            projectBaseFolder.mkdirs();
                        }

                        File patientFolder = new File(projectBaseFolder, patientId);
                        if (!patientFolder.exists()) {
                            patientFolder.mkdirs();
                        }

                        File targetFile = new File(patientFolder, studyId + ".rxv");
                        Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loadData();
    }


}
