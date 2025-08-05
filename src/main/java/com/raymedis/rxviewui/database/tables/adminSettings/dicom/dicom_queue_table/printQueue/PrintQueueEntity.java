package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.printQueue;

import java.time.LocalDateTime;

public class PrintQueueEntity {

    private int id;
    private String state;
    private LocalDateTime examDateTime;
    private String patientName;
    private String patientId;

    //constructors
    public PrintQueueEntity() {
    }

    public PrintQueueEntity(int id, String state, LocalDateTime examDateTime, String patientName, String patientId) {
        this.id = id;
        this.state = state;
        this.examDateTime = examDateTime;
        this.patientName = patientName;
        this.patientId = patientId;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getExamDateTime() {
        return examDateTime;
    }

    public void setExamDateTime(LocalDateTime examDateTime) {
        this.examDateTime = examDateTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

}
