package com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table;

public class StorageAlertsEntity {

    private int id;
    private double warningValue;
    private double criticalValue;


    //constructors
    public StorageAlertsEntity() {
    }

    public StorageAlertsEntity(int id, double warningValue, double criticalValue) {
        this.id = id;
        this.warningValue = warningValue;
        this.criticalValue = criticalValue;
    }

    //setters and getters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(double warningValue) {
        this.warningValue = warningValue;
    }

    public double getCriticalValue() {
        return criticalValue;
    }

    public void setCriticalValue(double criticalValue) {
        this.criticalValue = criticalValue;
    }
}
