package com.raymedis.rxviewui.modules.study.study;

import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabManager;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientStudyHandler implements TabManager {
    private HashMap<String, PatientStudyNode> patientStudyHashMap = new HashMap<>();
    private PatientStudyNode currentStudy;
    private PatientStudyNode patientStudyHead;
    private PatientStudyNode patientStudyTail;

    public PatientStudyHandler() {
    }

    public HashMap<String, PatientStudyNode> getPatientStudyHashMap() {
        return patientStudyHashMap;
    }

    public void setPatientStudyHashMap(HashMap<String, PatientStudyNode> patientStudyHashMap) {
        this.patientStudyHashMap = patientStudyHashMap;
    }

    public PatientStudyNode getCurrentStudy() {
        return currentStudy;
    }

//    public void setCurrentStudy(PatientStudyNode currentStudy) {
//        this.currentStudy = currentStudy;
//    }

    public PatientStudyNode getPatientStudyHead() {
        return patientStudyHead;
    }

    public void setPatientStudyHead(PatientStudyNode patientStudyHead) {
        this.patientStudyHead = patientStudyHead;
    }

    public PatientStudyNode getPatientStudyTail() {
        return patientStudyTail;
    }

    public void setPatientStudyTail(PatientStudyNode patientStudyTail) {
        this.patientStudyTail = patientStudyTail;
    }

    @Override
    public void addTab(String id, TabNode tab) {
        if(patientStudyHead==null){
            patientStudyHead= patientStudyTail= (PatientStudyNode) tab;
        }
        else {
            patientStudyTail.setNextTab(tab);
            patientStudyTail.getNextTab().setPreviousTab(patientStudyTail);
            patientStudyTail = (PatientStudyNode) patientStudyTail.getNextTab();
        }
        patientStudyHashMap.put(id, patientStudyTail);

        currentStudy= (PatientStudyNode) tab;
    }

    @Override
    public void removeTab(String id) {

        PatientStudyNode node = patientStudyHashMap.get(id);

        if(node==null)
            return;

        if(node == currentStudy){
            if(node.getPreviousTab() != null){
                currentStudy = (PatientStudyNode) node.getPreviousTab();
            }
            else if(node.getNextTab() != null){
                currentStudy = (PatientStudyNode) node.getNextTab();
            }
            else{
                currentStudy = null;
            }
        }

        if(node.getPreviousTab() != null) {
            node.getPreviousTab().setNextTab(node.getNextTab());
        }
        else{
            patientStudyHead= (PatientStudyNode) node.getNextTab();
            if(patientStudyHead != null){
                patientStudyHead.setPreviousTab(null);
            }
            else{
                currentStudy = null;
            }
        }

        if(node.getNextTab() != null){
            node.getNextTab().setPreviousTab(node.getPreviousTab());
        }
        else{
            patientStudyTail= (PatientStudyNode) node.getPreviousTab();
            if(patientStudyTail != null){
                patientStudyTail.setNextTab(null);
            }
            else{
                currentStudy = null;
            }
        }

        patientStudyHashMap.remove(id);

    }

    @Override
    public void markTabAsEdited(String id) {

    }

    @Override
    public void moveTab(String id, int index) {
        PatientStudyNode node =  patientStudyHashMap.get(id);
        if(node==null){
            return;
        }


        removeTab(id);
        patientStudyHashMap.put(id, (PatientStudyNode) node);
        PatientStudyNode current=patientStudyHead;
        for(int i=0;i< index && current != null; i++){
            current = (PatientStudyNode) current.getNextTab();
        }
        if(current!=null){
            node.setNextTab(current);
            node.setPreviousTab(current.getPreviousTab());
            if(current.getPreviousTab()!=null) {
                current.getPreviousTab().setNextTab(node);
            }
            else{
                patientStudyHead= (PatientStudyNode) node;
            }
            current.setPreviousTab(node);
        }
        else{
            if(patientStudyTail!=null){
                patientStudyTail.setNextTab(node);
                node.setPreviousTab(patientStudyTail);
                node.setNextTab(null);
                patientStudyTail= (PatientStudyNode) node;
            }
        }


    }

    @Override
    public TabNode getTab(String id) {
        return (TabNode) patientStudyHashMap.get(id);
    }

    @Override
    public List<TabNode> getAllTabs() {
        List<TabNode> tabs=new ArrayList<>();
        TabNode current=patientStudyHead;
        while(current != null){
            tabs.add(current);
            current=current.getNextTab();
        }
        return tabs;
    }
    public PatientStudyNode selectPatientStudy(int index){
        PatientStudyNode tempPatientStudy= patientStudyHead;
        for(int i=0; i < index ; i++){
            tempPatientStudy= (PatientStudyNode) tempPatientStudy.getNextTab();
        }
        currentStudy=tempPatientStudy;
        return currentStudy;
    }
    public PatientStudyNode selectPatientStudy(String studySessionId){
        currentStudy= patientStudyHashMap.get(studySessionId);
        return currentStudy;
    }


    public String containsStudyWithStudyId(String studyId){
        for(PatientStudyNode node : patientStudyHashMap.values()){
            if(node.getPatientStudy().getStudyId().equals(studyId)){
                return node.getStudySessionId();
            }
        }

        return null;
    }

    public PatientStudy getCurrentStudyClone() {
        return clone(currentStudy.getPatientStudy());
    }

    private PatientStudy clone(PatientStudy patientStudy) {
        if (patientStudy == null) {
            return null;
        }

        // Deep copy mutable objects
        PatientInfo clonedPatientInfo = clonePatientInfo(patientStudy.getPatientInfo());
        BodyPartHandler clonedBodyPartHandler = cloneBodyPartHandler(patientStudy.getBodyPartHandler());

        PatientStudy cloned = new PatientStudy();
        cloned.setPatientInfo(clonedPatientInfo);
        cloned.setBodyPartHandler(clonedBodyPartHandler);
        cloned.setStudyId(patientStudy.getStudyId());
        cloned.setStudyUid(patientStudy.getStudyUid());
        cloned.setRejected(patientStudy.isRejected());
        cloned.setAccessionNum(patientStudy.getAccessionNum());
        cloned.setModality(patientStudy.getModality());
        cloned.setRequestProcedurePriority(patientStudy.getRequestProcedurePriority());
        cloned.setAdditionalPatientHistory(patientStudy.getAdditionalPatientHistory());
        cloned.setAdmittingDiagnosisDescription(patientStudy.getAdmittingDiagnosisDescription());
        cloned.setStudyDescription(patientStudy.getStudyDescription());
        cloned.setProcedureCode(patientStudy.getProcedureCode());
        cloned.setStudyDateTime(patientStudy.getStudyDateTime());
        cloned.setRegisterDateTime(patientStudy.getRegisterDateTime());
        cloned.setScheduledDateTime(patientStudy.getScheduledDateTime());
        cloned.setExposedDateTime(patientStudy.getExposedDateTime());
        cloned.setPatientComments(patientStudy.getPatientComments());
        cloned.setReadingPhysician(patientStudy.getReadingPhysician());
        cloned.setPerformingPhysician(patientStudy.getPerformingPhysician());
        cloned.setReferringPhysician(patientStudy.getReferringPhysician());
        cloned.setInstitution(patientStudy.getInstitution());

        return cloned;
    }

    private PatientInfo clonePatientInfo(PatientInfo patientInfo) {
        if (patientInfo == null) {
            return null;
        }

        // Create a new PatientInfo with all copied fields
        PatientInfo cloned = new PatientInfo();
        cloned.setPatientId(patientInfo.getPatientId());
        cloned.setPatientName(patientInfo.getPatientName());
        cloned.setAge(patientInfo.getAge());
        cloned.setHeight(patientInfo.getHeight());
        cloned.setWeight(patientInfo.getWeight());

        // Create a new LocalDateTime object to avoid reference sharing
        if (patientInfo.getBirthDate() != null) {
            cloned.setBirthDate(LocalDateTime.of(
                    patientInfo.getBirthDate().toLocalDate(),
                    patientInfo.getBirthDate().toLocalTime()
            ));
        }

        cloned.setPatientSize(patientInfo.getPatientSize());
        cloned.setSex(patientInfo.getSex());
        cloned.setPatientInstituteResidence(patientInfo.getPatientInstituteResidence());

        return cloned;
    }

    private BodyPartHandler cloneBodyPartHandler(BodyPartHandler bodyPartHandler) {
        return bodyPartHandler.clone();
    }


}
