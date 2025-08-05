package com.raymedis.rxviewui.service.adminSettings.backup;

import com.raymedis.rxviewui.database.tables.adminSettings.backup_table.BackupEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.backup_table.BackupService;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BackupTask extends Task<Void> {



    private final Map<String, PatientStudyEntity> totalPatients;
    private final String selectedPath;
    private final ProgressBar progressBar;
    private final Alert alert;
    private final PatientStudyService patientStudyService = PatientStudyService.getInstance();
    private final BackupService backupService = BackupService.getInstance();

    private Map<String ,String> backupPaths = new HashMap<>();

    public BackupTask(Map<String, PatientStudyEntity> totalPatients, String selectedPath, ProgressBar progressBar, Alert alert) {
        this.totalPatients = totalPatients;
        this.selectedPath = selectedPath;
        this.progressBar = progressBar;
        this.alert = alert;
    }

    private final Logger logger = LoggerFactory.getLogger(BackupTask.class);

    @Override
    protected Void call() throws Exception {
        int totalFiles = totalPatients.size();
        int filesProcessed = 0;

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        File backupBaseFolder = new File(selectedPath + File.separator + "Rx_Backup_" + todayDate);

        if (!backupBaseFolder.exists()) {
            backupBaseFolder.mkdirs();
        }

        logger.info("starting the loop...");

        // Loop through all patients and back up their files
        for (PatientStudyEntity p : totalPatients.values()) {
            File sourceFile = new File(System.getProperty("user.dir") + "\\output\\" + p.getPatientId() + "\\" + p.getStudyId() + ".rxv");

            if (sourceFile.exists()) {
                File patientFolder = new File(backupBaseFolder, p.getPatientId());
                if (!patientFolder.exists()) {
                    patientFolder.mkdirs();
                }

                Path targetFile = patientFolder.toPath().resolve(sourceFile.getName());

                try {
                    Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PatientStudyEntity patientStudyEntity = patientStudyService.findPatientByStudyId(p.getStudyId());
                patientStudyEntity.setIsBackedUp(true);
                patientStudyEntity.setIsCleaned(false);
                patientStudyService.updatePatient(patientStudyEntity.getStudyId(),patientStudyEntity);

                backupPaths.put(p.getStudyId(),targetFile.toAbsolutePath().toString());
            }

            // Update progress
            filesProcessed++;
            updateProgress(filesProcessed, totalFiles);


        }

        updateProgress(totalFiles, totalFiles);

        Label completedLabel = new Label("Backup Completed");
        completedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
        VBox vbox = new VBox(completedLabel);
        vbox.setSpacing(10);

        Platform.runLater(()->{
            alert.setHeaderText("Backup Completed");
            alert.setContentText("The backup has been successfully completed.");
            alert.getDialogPane().setContent(vbox);
            BackupController.backUpPath = backupBaseFolder;
        });

        for (Map.Entry<String,String> entry : backupPaths.entrySet()){
            BackupEntity backupEntity = new BackupEntity(entry.getKey(), entry.getValue());
            backupService.save(backupEntity);
        }

        return null;
    }
}
