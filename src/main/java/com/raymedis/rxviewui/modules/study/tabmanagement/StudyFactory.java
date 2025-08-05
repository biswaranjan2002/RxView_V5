package com.raymedis.rxviewui.modules.study.tabmanagement;//package com.mpr3d.demo.study;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class StudyFactory implements  TabManager{
//
//    private  HashMap<String,PatientStudy> patientStudyHashMap;
//    private PatientStudy patientStudyHead;
//    private PatientStudyNode patientStudyTail;
//    private PatientStudy currentStudy;
////    public StudyFactory() {
//        this.patientStudyHashMap = new HashMap<>();
//    }
//
//    @Override
//    public void addTab(String id,TabNode tab) {
//
//        if(patientStudyHead==null){
//            patientStudyHead= patientStudyTail= (PatientStudy) tab;
//        }
//        else {
//            patientStudyTail.setNextTab(tab);
//            tab.setPreviousTab(patientStudyTail);
//            patientStudyTail = (PatientStudy) tab;
//        }
//        patientStudyHashMap.put(id, (PatientStudy) tab);
//
//        currentStudy= (PatientStudy) tab;
//
//
//    }
//
//    @Override
//    public void removeTab(String id) {
//        TabNode node= patientStudyHashMap.get(id);
//        if(node==null)
//            return;
//
//        if(node.getPreviousTab() != null) {
//            node.getPreviousTab().setNextTab(node.getNextTab());
//        }
//        else{
//            patientStudyHead= (PatientStudy) node.getNextTab();
//            patientStudyHead.setPreviousTab(null);
//        }
//
//        if(node.getNextTab() != null){
//            node.getNextTab().setPreviousTab(node.getPreviousTab());
//        }
//        else{
//            patientStudyTail= (PatientStudy) node.getPreviousTab();
//            patientStudyTail.setNextTab(null);
//        }
//
//        patientStudyHashMap.remove(id);
//
//    }
//    @Override
//    public void markTabAsEdited(String id) {
//
//    }
//    @Override
//    public void moveTab(String id, int index) {
//        TabNode node = patientStudyHashMap.get(id);
//        if(node==null){
//            return;
//        }
////        if(node.getPreviousTab()!=null){
////            node.getPreviousTab().setNextTab(node.getNextTab());
////        }
////
////        if(node.getNextTab()!=null){
////            node.getNextTab().setPreviousTab(node.getPreviousTab());
////        }
//        removeTab(id);
//        patientStudyHashMap.put(id, (PatientStudy) node);
//        TabNode current=patientStudyHead;
//        for(int i=0;i< index && current != null; i++){
//            current=current.getNextTab();
//        }
//        if(current!=null){
//            node.setNextTab(current);
//            node.setPreviousTab(current.getPreviousTab());
//            if(current.getPreviousTab()!=null) {
//                current.getPreviousTab().setNextTab(node);
//            }
//            else{
//                patientStudyHead= (PatientStudy) node;
//            }
//            current.setPreviousTab(node);
//        }
//        else{
//            if(patientStudyTail!=null){
//                patientStudyTail.setNextTab(node);
//                node.setPreviousTab(patientStudyTail);
//                node.setNextTab(null);
//                patientStudyTail= (PatientStudy) node;
//            }
//        }
//
//
//    }
//
//    @Override
//    public TabNode getTab(String id) {
//        return patientStudyHashMap.get(id);
//    }
//
//    @Override
//    public List<TabNode> getAllTabs() {
//        List<TabNode> tabs=new ArrayList<>();
//        TabNode current=patientStudyHead;
//        while(current != null){
//            tabs.add(current);
//            current=current.getNextTab();
//        }
//        return tabs;
//    }
//    public PatientStudy selectPatientstudy(int index){
//        PatientStudy tempPatientStudy= patientStudyHead;
//        for(int i=0; i < index ; i++){
//            tempPatientStudy= (PatientStudy) tempPatientStudy.getNextTab();
//        }
//        currentStudy=tempPatientStudy;
//        return currentStudy;
//    }
//    public PatientStudy selectPatientStudy(String studySessionId){
//
//        PatientStudy patientStudy= patientStudyHashMap.get(studySessionId);
//        currentStudy=patientStudy;
//        return currentStudy;
//    }
//}
