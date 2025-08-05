package com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table;


import java.time.LocalDateTime;

public class AutoDeleteEntity {

    private int id;
    private boolean isAutoDelete;
    private boolean isTimeEnabled;
    private boolean isStorageEnabled;
    private boolean allStudies;
    private boolean sentOrPrintedStudies;
    private boolean rejectedStudies;

    private int weekDuration;
    private int storageSize;
    private LocalDateTime savedDate;

    //constructors
    public AutoDeleteEntity() {

    }


    public AutoDeleteEntity(int id, boolean isAutoDelete, boolean isTimeEnabled, boolean isStorageEnabled, boolean allStudies,
                            boolean sentOrPrintedStudies, boolean rejectedStudies, int weekDuration,
                            int storageSize, LocalDateTime savedDate) {
        this.id = id;
        this.isAutoDelete = isAutoDelete;
        this.isTimeEnabled = isTimeEnabled;
        this.isStorageEnabled = isStorageEnabled;
        this.allStudies = allStudies;
        this.sentOrPrintedStudies = sentOrPrintedStudies;
        this.rejectedStudies = rejectedStudies;
        this.weekDuration = weekDuration;
        this.storageSize = storageSize;
        this.savedDate = savedDate;
    }


    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAutoDelete() {
        return isAutoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        isAutoDelete = autoDelete;
    }

    public boolean isTimeEnabled() {
        return isTimeEnabled;
    }

    public void setTimeEnabled(boolean timeEnabled) {
        isTimeEnabled = timeEnabled;
    }

    public boolean isStorageEnabled() {
        return isStorageEnabled;
    }

    public void setStorageEnabled(boolean storageEnabled) {
        isStorageEnabled = storageEnabled;
    }

    public boolean isAllStudies() {
        return allStudies;
    }

    public void setAllStudies(boolean allStudies) {
        this.allStudies = allStudies;
    }

    public boolean isSentOrPrintedStudies() {
        return sentOrPrintedStudies;
    }

    public void setSentOrPrintedStudies(boolean sentOrPrintedStudies) {
        this.sentOrPrintedStudies = sentOrPrintedStudies;
    }

    public boolean isRejectedStudies() {
        return rejectedStudies;
    }

    public void setRejectedStudies(boolean rejectedStudies) {
        this.rejectedStudies = rejectedStudies;
    }

    public int getWeekDuration() {
        return weekDuration;
    }

    public void setWeekDuration(int weekDuration) {
        this.weekDuration = weekDuration;
    }

    public int getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(int storageSize) {
        this.storageSize = storageSize;
    }

    public LocalDateTime getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(LocalDateTime savedDate) {
        this.savedDate = savedDate;
    }
}






