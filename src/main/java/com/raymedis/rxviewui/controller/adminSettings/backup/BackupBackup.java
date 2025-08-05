package com.raymedis.rxviewui.controller.adminSettings.backup;

import com.raymedis.rxviewui.service.adminSettings.backup.BackupController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class BackupBackup {

    public TableView backupDataGrid;

    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateColumn;
    public TableColumn rejectedColumn;

    public TextField selectedPathLabel;

    private BackupController backupController;

    public void initialize() throws SQLException {
        backupController = BackupController.getInstance();

        backupController.backupDataGrid =backupDataGrid;
        backupController.registerDateTimeColumn = registerDateTimeColumn;
        backupController.patientIdColumn = patientIdColumn;
        backupController.patientNameColumn = patientNameColumn;
        backupController.sexColumn = sexColumn;
        backupController.accessionNumberColumn = accessionNumberColumn;
        backupController.studyDescriptionColumn = studyDescriptionColumn;
        backupController.exposureDateTimeColumn = exposureDateColumn;
        backupController.rejectedColumn = rejectedColumn;
        backupController.selectedPathLabel = selectedPathLabel;

        backupController.loadEvents();
        backupController.loadData();

    }


    public void pathSelectionClick() {
        backupController.pathSelectionClick();
    }

    public void deleteClick() {
        backupController.deleteClick();
    }

    public void backupClick() {
        backupController.backupClick();
    }


}
