package com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table;

public class DefaultTabEntity {

    private int id;
    private Boolean isManual;
    private Boolean isWorkList;
    private Boolean isLocalList;


    //constructors
    public DefaultTabEntity() {

    }

    public DefaultTabEntity(Boolean isManual, Boolean isWorkList, Boolean isLocalList) {
        this.isManual = isManual;
        this.isWorkList = isWorkList;
        this.isLocalList = isLocalList;
    }

    public DefaultTabEntity(int id, Boolean isManual, Boolean isWorkList, Boolean isLocalList) {
        this.id = id;
        this.isManual = isManual;
        this.isWorkList = isWorkList;
        this.isLocalList = isLocalList;
    }

    //getters and setters
    public Boolean getManual() {
        return isManual;
    }

    public void setManual(Boolean manual) {
        isManual = manual;
    }

    public Boolean getWorkList() {
        return isWorkList;
    }

    public void setWorkList(Boolean workList) {
        isWorkList = workList;
    }

    public Boolean getLocalList() {
        return isLocalList;
    }

    public void setLocalList(Boolean localList) {
        isLocalList = localList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
