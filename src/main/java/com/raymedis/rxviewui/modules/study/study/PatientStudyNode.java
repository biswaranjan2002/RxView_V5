package com.raymedis.rxviewui.modules.study.study;

import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;

public class PatientStudyNode implements TabNode {
    private PatientStudyNode previousPatientStudy;
    private PatientStudyNode nextPatientStudy;
    private PatientStudy patientStudy;
    private String studySessionId;

    public PatientStudyNode() {
    }

    public PatientStudyNode(PatientStudy patientStudy,String studySessionId) {
        this.patientStudy = patientStudy;
        this.studySessionId=studySessionId;
    }


    public PatientStudyNode getPreviousPatientStudy() {
        return previousPatientStudy;
    }

    public void setPreviousPatientStudy(PatientStudyNode previousPatientStudy) {
        this.previousPatientStudy = previousPatientStudy;
    }

    public PatientStudyNode getNextPatientStudy() {
        return nextPatientStudy;
    }

    public void setNextPatientStudy(PatientStudyNode nextPatientStudy) {
        this.nextPatientStudy = nextPatientStudy;
    }

    public PatientStudy getPatientStudy() {
        return patientStudy;
    }

    public void setPatientStudy(PatientStudy patientStudy) {
        this.patientStudy = patientStudy;
    }

    public String getStudySessionId() {
        return studySessionId;
    }

    public void setStudySessionId(String studySessionId) {
        this.studySessionId = studySessionId;
    }

    @Override
    public TabNode getNextTab() {
        return nextPatientStudy;
    }

    @Override
    public TabNode getPreviousTab() {
        return previousPatientStudy;
    }

    @Override
    public void setNextTab(TabNode tab) {
        this.nextPatientStudy= (PatientStudyNode) tab;
    }

    @Override
    public void setPreviousTab(TabNode tab) {
        this.previousPatientStudy= (PatientStudyNode) tab;
    }



}
