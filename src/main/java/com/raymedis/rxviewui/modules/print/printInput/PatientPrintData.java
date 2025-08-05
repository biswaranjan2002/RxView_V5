package com.raymedis.rxviewui.modules.print.printInput;

import java.time.LocalDateTime;
import java.util.List;

public class PatientPrintData {

    private String patientId;
    private String patientName;
    private int age;
    private double height;
    private double weight;
    private LocalDateTime birthDate;
    private String patientSize;
    private String sex;
    private String patientInstituteResidence;
    private String studyId;
    private String studyUid;
    private boolean isRejected;
    private String accessionNum;
    private String modality;
    private String requestProcedurePriority;
    private String additionalPatientHistory;
    private String admittingDiagnosisDescription;
    private String studyDescription;
    private String procedureCode;
    private LocalDateTime studyDateTime;
    private LocalDateTime registerDateTime;
    private LocalDateTime scheduledDateTime;
    private LocalDateTime exposedDateTime;
    private String patientComments;
    private String readingPhysician;
    private String performingPhysician;
    private String referringPhysician;
    private String institution;
    //create list of body part
    private List<PatientPrintImageData> patientPrintImageDataList;


    //constructors
    public PatientPrintData() {
    }

    public PatientPrintData(String patientId, String patientName, int age, double height, double weight, LocalDateTime birthDate, String patientSize, String sex, String patientInstituteResidence, String studyId, String studyUid, boolean isRejected, String accessionNum, String modality, String requestProcedurePriority, String additionalPatientHistory, String admittingDiagnosisDescription, String studyDescription, String procedureCode, LocalDateTime studyDateTime, LocalDateTime registerDateTime, LocalDateTime scheduledDateTime, LocalDateTime exposedDateTime, String patientComments, String readingPhysician, String performingPhysician, String referringPhysician, String institution, List<PatientPrintImageData> patientPrintImageDataList) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.patientSize = patientSize;
        this.sex = sex;
        this.patientInstituteResidence = patientInstituteResidence;
        this.studyId = studyId;
        this.studyUid = studyUid;
        this.isRejected = isRejected;
        this.accessionNum = accessionNum;
        this.modality = modality;
        this.requestProcedurePriority = requestProcedurePriority;
        this.additionalPatientHistory = additionalPatientHistory;
        this.admittingDiagnosisDescription = admittingDiagnosisDescription;
        this.studyDescription = studyDescription;
        this.procedureCode = procedureCode;
        this.studyDateTime = studyDateTime;
        this.registerDateTime = registerDateTime;
        this.scheduledDateTime = scheduledDateTime;
        this.exposedDateTime = exposedDateTime;
        this.patientComments = patientComments;
        this.readingPhysician = readingPhysician;
        this.performingPhysician = performingPhysician;
        this.referringPhysician = referringPhysician;
        this.institution = institution;
        this.patientPrintImageDataList = patientPrintImageDataList;
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getPatientSize() {
        return patientSize;
    }

    public void setPatientSize(String patientSize) {
        this.patientSize = patientSize;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getStudyUid() {
        return studyUid;
    }

    public void setStudyUid(String studyUid) {
        this.studyUid = studyUid;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public String getAccessionNum() {
        return accessionNum;
    }

    public void setAccessionNum(String accessionNum) {
        this.accessionNum = accessionNum;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
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

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public LocalDateTime getStudyDateTime() {
        return studyDateTime;
    }

    public void setStudyDateTime(LocalDateTime studyDateTime) {
        this.studyDateTime = studyDateTime;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public LocalDateTime getExposedDateTime() {
        return exposedDateTime;
    }

    public void setExposedDateTime(LocalDateTime exposedDateTime) {
        this.exposedDateTime = exposedDateTime;
    }

    public String getPatientComments() {
        return patientComments;
    }

    public void setPatientComments(String patientComments) {
        this.patientComments = patientComments;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public List<PatientPrintImageData> getPatientPrintImageDataList() {
        return patientPrintImageDataList;
    }

    public void setPatientPrintImageDataList(List<PatientPrintImageData> patientPrintImageDataList) {
        this.patientPrintImageDataList = patientPrintImageDataList;
    }


    @Override
    public String toString() {
        return "PatientPrintData{" +
                "patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", birthDate=" + birthDate +
                ", patientSize='" + patientSize + '\'' +
                ", sex='" + sex + '\'' +
                ", patientInstituteResidence='" + patientInstituteResidence + '\'' +
                ", studyId='" + studyId + '\'' +
                ", studyUid='" + studyUid + '\'' +
                ", isRejected=" + isRejected +
                ", accessionNum='" + accessionNum + '\'' +
                ", modality='" + modality + '\'' +
                ", requestProcedurePriority='" + requestProcedurePriority + '\'' +
                ", additionalPatientHistory='" + additionalPatientHistory + '\'' +
                ", admittingDiagnosisDescription='" + admittingDiagnosisDescription + '\'' +
                ", studyDescription='" + studyDescription + '\'' +
                ", procedureCode='" + procedureCode + '\'' +
                ", studyDateTime=" + studyDateTime +
                ", registerDateTime=" + registerDateTime +
                ", scheduledDateTime=" + scheduledDateTime +
                ", exposedDateTime=" + exposedDateTime +
                ", patientComments='" + patientComments + '\'' +
                ", readingPhysician='" + readingPhysician + '\'' +
                ", performingPhysician='" + performingPhysician + '\'' +
                ", referringPhysician='" + referringPhysician + '\'' +
                ", institution='" + institution + '\'' +
                ", patientPrintImageDataList=" + patientPrintImageDataList +
                '}';
    }
}
