package com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table;

public class RejectedReasonEntity {

    private int id;
    private String RejectedReason;

    //constructors
    public RejectedReasonEntity() {
    }

    public RejectedReasonEntity(int id, String rejectedReason) {
        this.id = id;
        RejectedReason = rejectedReason;
    }


    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRejectedReason() {
        return RejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        RejectedReason = rejectedReason;
    }
}
