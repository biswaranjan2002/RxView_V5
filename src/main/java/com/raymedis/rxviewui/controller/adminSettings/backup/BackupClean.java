package com.raymedis.rxviewui.controller.adminSettings.backup;

import com.raymedis.rxviewui.service.adminSettings.backup.BackupCleanController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.SQLException;

public class BackupClean {

    public TableView backupClearDataGrid;

    public TableColumn registerDateTimeColumn;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn studyDescriptionColumn;
    public TableColumn exposureDateTimeColumn;
    public TableColumn rejectedColumn;


    private BackupCleanController backupCleanController;

    public void initialize(){
        backupCleanController = BackupCleanController.getInstance();

        backupCleanController.backupClearDataGrid = backupClearDataGrid;
        backupCleanController.registerDateTimeColumn = registerDateTimeColumn;
        backupCleanController.patientIdColumn = patientIdColumn;
        backupCleanController.patientNameColumn = patientNameColumn;
        backupCleanController.sexColumn = sexColumn;
        backupCleanController.accessionNumberColumn = accessionNumberColumn;
        backupCleanController.studyDescriptionColumn = studyDescriptionColumn;
        backupCleanController.exposureDateTimeColumn = exposureDateTimeColumn;
        backupCleanController.rejectedColumn = rejectedColumn;

        backupCleanController.loadEvents();
        backupCleanController.loadData();
    }

    public void cleanClick() throws SQLException {
        backupCleanController.cleanClick();
    }

    public void cleanAllClick() throws SQLException {
        backupCleanController.cleanAllClick();
    }


}
