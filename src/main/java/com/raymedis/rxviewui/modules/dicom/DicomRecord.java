package com.raymedis.rxviewui.modules.dicom;

public class DicomRecord extends DicomWorkListData {
    private final String dob;
    private final String patientId;
    private final String patientName;
    private final String sex;
    private final String accessionNumber;
    private final String referringPhysician;
    private final String studyDescription;
    private final String scheduledTime;
    private final String scheduleDate;
    private final String studyId;
    private String status;
    private String procedureCode;
    private String bodyPartExamined;
    private String viewPosition;

    public DicomRecord(String dob, String patientId, String patientName, String sex, String accessionNumber, String referringPhysician, String studyDescription, String scheduledTime, String scheduleDate, String studyId, String status,String procedureCode,String bodyPartExamined,String viewPosition) {
        super();
        this.dob = dob;
        this.patientId = patientId;
        this.patientName = patientName;
        this.sex = sex;
        this.accessionNumber = accessionNumber;
        this.referringPhysician = referringPhysician;
        this.studyDescription = studyDescription;
        this.scheduledTime = scheduledTime;
        this.scheduleDate = scheduleDate;
        this.studyId = studyId;
        this.procedureCode = procedureCode;
        this.bodyPartExamined = bodyPartExamined;
        this.viewPosition = viewPosition;
        this.status = status;
    }

    public String getDob() {
        return dob;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSex() {
        return sex;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public String getReferringPhysician() {
        return referringPhysician;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getBodyPartExamined() {
        return bodyPartExamined;
    }

    public void setBodyPartExamined(String bodyPartExamined) {
        this.bodyPartExamined = bodyPartExamined;
    }

    public String getViewPosition() {
        return viewPosition;
    }

    public void setViewPosition(String viewPosition) {
        this.viewPosition = viewPosition;
    }

    @Override
    public String toString() {
        return "DicomRecord{" +
                "dob='" + dob + '\'' +
                ", patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", sex='" + sex + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                ", referringPhysician='" + referringPhysician + '\'' +
                ", studyDescription='" + studyDescription + '\'' +
                ", scheduledTime='" + scheduledTime + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", studyId='" + studyId + '\'' +
                ", status='" + status + '\'' +
                ", procedureCode='" + procedureCode + '\'' +
                ", bodyPartExamined='" + bodyPartExamined + '\'' +
                ", viewPosition='" + viewPosition + '\'' +
                '}';
    }
}