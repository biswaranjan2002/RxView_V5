package com.raymedis.rxviewui.database.tables.register.patientLocalList_table;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientLocalListEntity {

    private int id;
    private String accessionNumber;
    private String additionalPatientHistory;
    private int age;
    private String admittingDiagnosisDescription;
    private LocalDate dob;
    private double height;
    private String institution;

    private String modality;
    private String patientComments;
    private String patientId;
    private String patientInstituteResidence;
    private String patientName;
    private String patientSize;
    private String performingPhysician;

    private String readingPhysician;
    private LocalDateTime registerDateTime;
    private String referringPhysician;
    private String requestProcedurePriority;

    private LocalDateTime scheduledDateTime;
    private String sex;
    private String studyDescription;
    private double weight;

    private String studyId;
    private String studySessionId;

    //constructors
    public PatientLocalListEntity() {

    }


    public PatientLocalListEntity(int id, String accessionNumber, String additionalPatientHistory, int age, String admittingDiagnosisDescription, LocalDate dob, double height, String institution, String modality, String patientComments, String patientId, String patientInstituteResidence, String patientName, String patientSize, String performingPhysician, String readingPhysician, LocalDateTime registerDateTime, String referringPhysician, String requestProcedurePriority, LocalDateTime scheduledDateTime, String sex, String studyDescription, double weight, String studyId, String studySessionId) {
        this.id = id;
        this.accessionNumber = accessionNumber;
        this.additionalPatientHistory = additionalPatientHistory;
        this.age = age;
        this.admittingDiagnosisDescription = admittingDiagnosisDescription;
        this.dob = dob;
        this.height = height;
        this.institution = institution;
        this.modality = modality;
        this.patientComments = patientComments;
        this.patientId = patientId;
        this.patientInstituteResidence = patientInstituteResidence;
        this.patientName = patientName;
        this.patientSize = patientSize;
        this.performingPhysician = performingPhysician;
        this.readingPhysician = readingPhysician;
        this.registerDateTime = registerDateTime;
        this.referringPhysician = referringPhysician;
        this.requestProcedurePriority = requestProcedurePriority;
        this.scheduledDateTime = scheduledDateTime;
        this.sex = sex;
        this.studyDescription = studyDescription;
        this.weight = weight;
        this.studyId = studyId;
        this.studySessionId = studySessionId;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getAdditionalPatientHistory() {
        return additionalPatientHistory;
    }

    public void setAdditionalPatientHistory(String additionalPatientHistory) {
        this.additionalPatientHistory = additionalPatientHistory;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAdmittingDiagnosisDescription() {
        return admittingDiagnosisDescription;
    }

    public void setAdmittingDiagnosisDescription(String admittingDiagnosisDescription) {
        this.admittingDiagnosisDescription = admittingDiagnosisDescription;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getPatientComments() {
        return patientComments;
    }

    public void setPatientComments(String patientComments) {
        this.patientComments = patientComments;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientInstituteResidence() {
        return patientInstituteResidence;
    }

    public void setPatientInstituteResidence(String patientInstituteResidence) {
        this.patientInstituteResidence = patientInstituteResidence;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSize() {
        return patientSize;
    }

    public void setPatientSize(String patientSize) {
        this.patientSize = patientSize;
    }

    public String getPerformingPhysician() {
        return performingPhysician;
    }

    public void setPerformingPhysician(String performingPhysician) {
        this.performingPhysician = performingPhysician;
    }

    public String getReadingPhysician() {
        return readingPhysician;
    }

    public void setReadingPhysician(String readingPhysician) {
        this.readingPhysician = readingPhysician;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getReferringPhysician() {
        return referringPhysician;
    }

    public void setReferringPhysician(String referringPhysician) {
        this.referringPhysician = referringPhysician;
    }

    public String getRequestProcedurePriority() {
        return requestProcedurePriority;
    }

    public void setRequestProcedurePriority(String requestProcedurePriority) {
        this.requestProcedurePriority = requestProcedurePriority;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getStudySessionId() {
        return studySessionId;
    }

    public void setStudySessionId(String studySessionId) {
        this.studySessionId = studySessionId;
    }
}
