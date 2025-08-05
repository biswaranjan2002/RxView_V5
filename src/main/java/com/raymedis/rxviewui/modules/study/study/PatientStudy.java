package com.raymedis.rxviewui.modules.study.study;

import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;

import java.time.LocalDateTime;


public class PatientStudy {

    private PatientInfo patientInfo;
    private String studyId;
    private String studyUid;
    private BodyPartHandler bodyPartHandler;
    private boolean isRejected;

    // study  related information :
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


    //constructors
    public PatientStudy() {
    }

    public PatientStudy(PatientInfo patientInfo, BodyPartHandler bodyPartHandler, String studyId, String accessionNum, String modality,
                        String requestProcedurePriority, String additionalPatientHistory, String admittingDiagnosisDescription,
                        String studyDescription, String procedureCode, LocalDateTime studyDateTime, LocalDateTime registerDateTime,
                        LocalDateTime scheduledDateTime, String patientComments, String readingPhysician, String performingPhysician,
                        String referringPhysician, String studyUid, String institution, LocalDateTime exposedDateTime,boolean isRejected) {
        this.patientInfo = patientInfo;
        this.bodyPartHandler = bodyPartHandler;
        this.studyId = studyId;
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
        this.patientComments = patientComments;
        this.readingPhysician =readingPhysician;
        this.performingPhysician = performingPhysician;
        this.referringPhysician = referringPhysician;
        this.studyUid = studyUid;
        this.institution =institution;
        this.exposedDateTime =exposedDateTime;
        this.isRejected=isRejected;
    }


    //getters and setters
    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public BodyPartHandler getBodyPartHandler() {
        return bodyPartHandler;
    }

    public void setBodyPartHandler(BodyPartHandler bodyPartHandler) {
        this.bodyPartHandler = bodyPartHandler;
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

    public LocalDateTime getExposedDateTime() {
        return exposedDateTime;
    }

    public void setExposedDateTime(LocalDateTime exposedDateTime) {
        this.exposedDateTime = exposedDateTime;
    }


    @Override
    public String toString() {
        return "PatientStudy{" +
                "patientInfo=" + patientInfo +
                ", studyId='" + studyId + '\'' +
                ", studyUid='" + studyUid + '\'' +
                ", bodyPartHandler=" + bodyPartHandler +
                ", isRejected=" + isRejected +
                ", accessionNum='" + accessionNum + '\'' +
                ", modality='" + modality + '\'' +
                ", requestProcedurePriority='" + requestProcedurePriority + '\'' +
                ", additionalPatientHistory='" + additionalPatientHistory + '\'' +
                ", admittingDiagnosisDescription='" + admittingDiagnosisDescription + '\'' +
                ", studyDescription='" + studyDescription + '\'' +
                ", procedure='" + procedureCode + '\'' +
                ", studyDateTime=" + studyDateTime +
                ", registerDateTime=" + registerDateTime +
                ", scheduledDateTime=" + scheduledDateTime +
                ", patientComments='" + patientComments + '\'' +
                ", readingPhysician='" + readingPhysician + '\'' +
                ", performingPhysician='" + performingPhysician + '\'' +
                ", referringPhysician='" + referringPhysician + '\'' +
                ", institution='" + institution + '\'' +
                '}';
    }


}
