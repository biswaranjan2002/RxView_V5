package com.raymedis.rxviewui.controller.adminSettings.backup;

import com.raymedis.rxviewui.service.adminSettings.backup.BackupRestoreController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BackupRestore {

    public TableView backUpRestoreDataGrid;

    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn rejectedColumn;

    private BackupRestoreController backupRestoreController;

    public void initialize(){
        backupRestoreController = BackupRestoreController.getInstance();

        backupRestoreController.backUpRestoreDataGrid = backUpRestoreDataGrid;

        backupRestoreController.registerDateTimeColumn = registerDateTimeColumn;
        backupRestoreController.patientIdColumn = patientIdColumn;
        backupRestoreController.patientNameColumn = patientNameColumn;
        backupRestoreController.sexColumn = sexColumn;
        backupRestoreController.accessionNumberColumn = accessionNumberColumn;
        backupRestoreController.studyDescriptionColumn = studyDescriptionColumn;
        backupRestoreController.exposureDateTimeColumn = exposureDateTimeColumn;
        backupRestoreController.rejectedColumn = rejectedColumn;

        backupRestoreController.loadEvents();
        backupRestoreController.loadData();
    }



    public void restoreClick() {
        backupRestoreController.restoreClick();
    }

    public void restoreAllClick() {
        backupRestoreController.restoreAllClick();
    }
}
