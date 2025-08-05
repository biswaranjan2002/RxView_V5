package com.raymedis.rxviewui.database.tables.adminSettings.backup_table;

public class BackupEntity {

    private String StudyId;
    private String backupPath;

    //constructors
    public BackupEntity() {

    }

    public BackupEntity(String studyId, String backupPath) {
        StudyId = studyId;
        this.backupPath = backupPath;
    }


    //setters and getters
    public String getStudyId() {
        return StudyId;
    }

    public void setStudyId(String studyId) {
        StudyId = studyId;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }


    @Override
    public String toString() {
        return "BackupEntity{" +
                "StudyId='" + StudyId + '\'' +
                ", backupPath='" + backupPath + '\'' +
                '}';
    }
}
