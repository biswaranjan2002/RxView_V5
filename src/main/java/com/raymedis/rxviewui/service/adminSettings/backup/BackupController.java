package com.raymedis.rxviewui.service.adminSettings.backup;

import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackupController {

    private final static BackupController instance = new BackupController();
    public static BackupController getInstance(){
        return instance;
    }

    public TableView backupDataGrid;
    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn rejectedColumn;

    public TextField selectedPathLabel;

    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private Map<String,PatientStudyEntity> totalPatients = new LinkedHashMap<>();
    public static File backUpPath = new File("");


    public void loadData() throws SQLException {
        backupDataGrid.getItems().clear();
        totalPatients.clear();

        for (PatientStudyEntity p : patientStudyService.findAllNonBackedUpStudies()) {
            totalPatients.put(p.getStudyId(), p);
        }
        backupDataGrid.getItems().addAll(totalPatients.values());
    }

    public void loadEvents(){

        selectedPathLabel.setEditable(false);

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

    public void pathSelectionClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        // Show open directory dialog
        Stage stage = (Stage) selectedPathLabel.getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);

        if (directory != null) {
            selectedPathLabel.setText(directory.getAbsolutePath());
        }
    }

    public void deleteClick() {
        PatientStudyEntity p = (PatientStudyEntity) backupDataGrid.getSelectionModel().getSelectedItem();
        if(p==null){
            return;
        }

        if(totalPatients.containsKey(p.getStudyId())){
            totalPatients.remove(p.getStudyId());
            backupDataGrid.getItems().clear();
            backupDataGrid.getItems().addAll(totalPatients.values());
        }
    }

    public void backupClick() {
        if (totalPatients != null && !totalPatients.isEmpty()) {
            String selectedPath = selectedPathLabel.getText();
            if (!selectedPath.isEmpty()) {
                ProgressBar progressBar = new ProgressBar(0);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Backup Progress");
                alert.setHeaderText("Backing up files...");
                alert.setContentText("Please wait while the backup is in progress.");

                VBox vbox = new VBox(progressBar);
                vbox.setSpacing(10);
                alert.getDialogPane().setContent(vbox);
                BackupTask backupTask = new BackupTask(totalPatients, selectedPath, progressBar, alert);
                progressBar.progressProperty().bind(backupTask.progressProperty());
                alert.show();

                Thread backupThread = new Thread(backupTask);
                backupThread.setDaemon(true);
                backupThread.start();
            }
        }
    }


}
