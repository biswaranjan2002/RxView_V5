package com.raymedis.rxviewui.database.tables.patientStudy_table;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientStudyEntity {

    private String patientId;
    private String patientName;
    private String sex;
    private int age;
    private LocalDate dob;
    private double height;
    private double weight;
    private String patientSize;
    private String patientInstituteResidence;

    private String accessionNumber;
    private String modality;
    private String requestProcedurePriority;
    private String additionalPatientHistory;
    private String admittingDiagnosisDescription;
    private String studyDescription;
    private String studyProcedure;
    private LocalDateTime studyDateTime;
    private LocalDateTime registerDateTime;
    private LocalDateTime scheduledDateTime;
    private LocalDateTime exposureDateTime;
    private String patientComments;
    private String studyId;
    private String readingPhysician;
    private String referringPhysician;
    private String performingPhysician;
    private String institution;
    private String studyUid;

    private String seriesUid;
    private String instanceUid;
    private String seriesId;
    private String instanceId;

    private boolean isRejected;

    private boolean isPrinted;
    private boolean isDicomUploaded;
    private boolean isBackedUp;
    private boolean isCleaned;



    //constructor
    public PatientStudyEntity() {

    }

    public PatientStudyEntity(String patientId, String patientName, String sex, int age,
                              LocalDate dob, double height, double weight, String patientSize,
                              String patientInstituteResidence, String accessionNumber,
                              String modality, String requestProcedurePriority,
                              String additionalPatientHistory, String admittingDiagnosisDescription,
                              String studyDescription, String studyProcedure, LocalDateTime studyDateTime,
                              LocalDateTime registerDateTime, LocalDateTime scheduledDateTime,
                              LocalDateTime exposureDateTime, String patientComments, String studyId,
                              String readingPhysician, String referringPhysician, String performingPhysician,
                              String institution, String studyUid, String seriesUid, String instanceUid,
                              String seriesId, String instanceId, boolean isRejected, boolean isPrinted,
                              boolean isDicomUploaded, boolean isBackedUp, boolean isCleaned) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.sex = sex;
        this.age = age;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.patientSize = patientSize;
        this.patientInstituteResidence = patientInstituteResidence;
        this.accessionNumber = accessionNumber;
        this.modality = modality;
        this.requestProcedurePriority = requestProcedurePriority;
        this.additionalPatientHistory = additionalPatientHistory;
        this.admittingDiagnosisDescription = admittingDiagnosisDescription;
        this.studyDescription = studyDescription;
        this.studyProcedure = studyProcedure;
        this.studyDateTime = studyDateTime;
        this.registerDateTime = registerDateTime;
        this.scheduledDateTime = scheduledDateTime;
        this.exposureDateTime = exposureDateTime;
        this.patientComments = patientComments;
        this.studyId = studyId;
        this.readingPhysician = readingPhysician;
        this.referringPhysician = referringPhysician;
        this.performingPhysician = performingPhysician;
        this.institution = institution;
        this.studyUid = studyUid;
        this.seriesUid = seriesUid;
        this.instanceUid = instanceUid;
        this.seriesId = seriesId;
        this.instanceId = instanceId;
        this.isRejected = isRejected;
        this.isPrinted = isPrinted;
        this.isDicomUploaded = isDicomUploaded;
        this.isBackedUp = isBackedUp;
        this.isCleaned = isCleaned;
    }

    //setters and getters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getPatientSize() {
        return patientSize;
    }

    public void setPatientSize(String patientSize) {
        this.patientSize = patientSize;
    }

    public String getReadingPhysician() {
        return readingPhysician;
    }

    public void setReadingPhysician(String readingPhysician) {
        this.readingPhysician = readingPhysician;
    }

    public String getPerformingPhysician() {
        return performingPhysician;
    }

    public void setPerformingPhysician(String performingPhysician) {
        this.performingPhysician = performingPhysician;
    }

    public String getReferringPhysician() {
        return referringPhysician;
    }

    public void setReferringPhysician(String referringPhysician) {
        this.referringPhysician = referringPhysician;
    }

    public String getPatientInstituteResidence() {
        return patientInstituteResidence;
    }

    public void setPatientInstituteResidence(String patientInstituteResidence) {
        this.patientInstituteResidence = patientInstituteResidence;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getRequestProcedurePriority() {
        return requestProcedurePriority;
    }

    public void setRequestProcedurePriority(String requestProcedurePriority) {
        this.requestProcedurePriority = requestProcedurePriority;
    }

    public String getAdditionalPatientHistory() {
        return additionalPatientHistory;
    }

    public void setAdditionalPatientHistory(String additionalPatientHistory) {
        this.additionalPatientHistory = additionalPatientHistory;
    }

    public String getAdmittingDiagnosisDescription() {
        return admittingDiagnosisDescription;
    }

    public void setAdmittingDiagnosisDescription(String admittingDiagnosisDescription) {
        this.admittingDiagnosisDescription = admittingDiagnosisDescription;
    }

    public String getStudyProcedure() {
        return studyProcedure;
    }

    public void setStudyProcedure(String studyProcedure) {
        this.studyProcedure = studyProcedure;
    }

    public String getPatientComments() {
        return patientComments;
    }

    public void setPatientComments(String patientComments) {
        this.patientComments = patientComments;
    }

    public String getStudyUid() {
        return studyUid;
    }

    public void setStudyUid(String studyUid) {
        this.studyUid = studyUid;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public LocalDateTime getExposureDateTime() {
        return exposureDateTime;
    }

    public void setExposureDateTime(LocalDateTime exposureDateTime) {
        this.exposureDateTime = exposureDateTime;
    }

    public LocalDateTime getStudyDateTime() {
        return studyDateTime;
    }

    public void setStudyDateTime(LocalDateTime studyDateTime) {
        this.studyDateTime = studyDateTime;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSeriesUid() {
        return seriesUid;
    }

    public void setSeriesUid(String seriesUid) {
        this.seriesUid = seriesUid;
    }

    public String getInstanceUid() {
        return instanceUid;
    }

    public void setInstanceUid(String instanceUid) {
        this.instanceUid = instanceUid;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public boolean getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(boolean printed) {
        isPrinted = printed;
    }

    public boolean getIsDicomUploaded() {
        return isDicomUploaded;
    }

    public void setIsDicomUploaded(boolean dicomUploaded) {
        isDicomUploaded = dicomUploaded;
    }

    public boolean getIsBackedUp() {
        return isBackedUp;
    }

    public void setIsBackedUp(boolean backedUp) {
        isBackedUp = backedUp;
    }

    public boolean getIsCleaned() {
        return isCleaned;
    }

    public void setIsCleaned(boolean cleaned) {
        isCleaned = cleaned;
    }


}
