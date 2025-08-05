package com.raymedis.rxviewui.modules.dicom;

import javafx.beans.property.SimpleStringProperty;

public class DicomWorkListData {
    private final SimpleStringProperty dob;
    private final SimpleStringProperty patientId;
    private final SimpleStringProperty patientName;
    private final SimpleStringProperty sex;
    private final SimpleStringProperty accessionNumber;
    private final SimpleStringProperty referringPhysician;
    private final SimpleStringProperty studyDescription;
    private final SimpleStringProperty scheduledTime;
    private final SimpleStringProperty scheduleDateTime;

    public DicomWorkListData(String dob, String patientId, String patientName, String sex, String accessionNumber,
                             String referringPhysician, String studyDescription, String scheduledTime, String scheduleDateTime) {
        this.dob = new SimpleStringProperty(dob);
        this.patientId = new SimpleStringProperty(patientId);
        this.patientName = new SimpleStringProperty(patientName);
        this.sex = new SimpleStringProperty(sex);
        this.accessionNumber = new SimpleStringProperty(accessionNumber);
        this.referringPhysician = new SimpleStringProperty(referringPhysician);
        this.studyDescription = new SimpleStringProperty(studyDescription);
        this.scheduledTime = new SimpleStringProperty(scheduledTime);
        this.scheduleDateTime = new SimpleStringProperty(scheduleDateTime);
    }

    public DicomWorkListData(SimpleStringProperty dob, SimpleStringProperty patientId, SimpleStringProperty patientName, SimpleStringProperty sex, SimpleStringProperty accessionNumber, SimpleStringProperty referringPhysician, SimpleStringProperty studyDescription, SimpleStringProperty scheduledTime, SimpleStringProperty scheduleDateTime) {
        this.dob = dob;
        this.patientId = patientId;
        this.patientName = patientName;
        this.sex = sex;
        this.accessionNumber = accessionNumber;
        this.referringPhysician = referringPhysician;
        this.studyDescription = studyDescription;
        this.scheduledTime = scheduledTime;
        this.scheduleDateTime = scheduleDateTime;
    }

    public DicomWorkListData() {
        studyDescription = null;
        patientId = null;
        dob = null;
        patientName = null;
        sex = null;
        accessionNumber = null;
        referringPhysician = null;
        scheduledTime = null;
        scheduleDateTime = null;
    }

    public String getDob() { return dob.get(); }
    public String getPatientId() { return patientId.get(); }
    public String getPatientName() { return patientName.get(); }
    public String getSex() { return sex.get(); }
    public String getAccessionNumber() { return accessionNumber.get(); }
    public String getReferringPhysician() { return referringPhysician.get(); }
    public String getStudyDescription() { return studyDescription.get(); }
    public String getScheduledTime() { return scheduledTime.get(); }
    public String getScheduleDate() { return scheduleDateTime.get(); }


}
