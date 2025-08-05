package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table;

public class DicomTagMappingEntity {

    private int id;
    private String originalTagNo;
    private String inputTagNo;
    private String outputTagNo;
    private String originalTagName;
    private String inputTagName;
    private String outputTagName;

    //constructors
    public DicomTagMappingEntity() {

    }

    public DicomTagMappingEntity(int id, String originalTagNo, String inputTagNo, String outputTagNo, String originalTagName, String inputTagName, String outputTagName) {
        this.id = id;
        this.originalTagNo = originalTagNo;
        this.inputTagNo = inputTagNo;
        this.outputTagNo = outputTagNo;
        this.originalTagName = originalTagName;
        this.inputTagName = inputTagName;
        this.outputTagName = outputTagName;
    }

    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTagNo() {
        return originalTagNo;
    }

    public void setOriginalTagNo(String originalTagNo) {
        this.originalTagNo = originalTagNo;
    }

    public String getInputTagNo() {
        return inputTagNo;
    }

    public void setInputTagNo(String inputTagNo) {
        this.inputTagNo = inputTagNo;
    }

    public String getOutputTagNo() {
        return outputTagNo;
    }

    public void setOutputTagNo(String outputTagNo) {
        this.outputTagNo = outputTagNo;
    }

    public String getOriginalTagName() {
        return originalTagName;
    }

    public void setOriginalTagName(String originalTagName) {
        this.originalTagName = originalTagName;
    }

    public String getInputTagName() {
        return inputTagName;
    }

    public void setInputTagName(String inputTagName) {
        this.inputTagName = inputTagName;
    }

    public String getOutputTagName() {
        return outputTagName;
    }

    public void setOutputTagName(String outputTagName) {
        this.outputTagName = outputTagName;
    }
}


