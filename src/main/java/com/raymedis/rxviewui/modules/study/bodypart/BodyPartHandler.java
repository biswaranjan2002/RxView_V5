package com.raymedis.rxviewui.modules.study.bodypart;

import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.CropLayoutWrapper;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabManager;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BodyPartHandler implements TabManager,Cloneable {

    private static final transient Logger logger = LoggerFactory.getLogger(BodyPartHandler.class);


    private HashMap<String, BodyPartNode> patientBodyPartMap = new HashMap<>();
    private BodyPartNode currentBodyPart;

    private BodyPartNode bodyPartHead;
    private BodyPartNode bodyPartTail;

    public BodyPartHandler() {
    }

    public HashMap<String, BodyPartNode> getPatientBodyPartMap() {
        return patientBodyPartMap;
    }

    public void setPatientBodyPartMap(HashMap<String, BodyPartNode> patientBodyPartMap) {
        this.patientBodyPartMap = patientBodyPartMap;
    }

    public BodyPartNode getCurrentBodyPart() {
        return currentBodyPart;
    }

    public void setCurrentBodyPart(BodyPartNode currentBodyPart) {
        this.currentBodyPart = currentBodyPart;
    }

    public void setCurrentBodyPart(String sessionId) {
        this.currentBodyPart = patientBodyPartMap.get(sessionId);
    }

    public BodyPartNode getBodyPartHead() {
        return bodyPartHead;
    }

    public void setBodyPartHead(BodyPartNode bodyPartHead) {
        this.bodyPartHead = bodyPartHead;
    }

    public BodyPartNode getBodyPartTail() {
        return bodyPartTail;
    }

    public void setBodyPartTail(BodyPartNode bodyPartTail) {
        this.bodyPartTail = bodyPartTail;
    }

    public void addTab(String bodyPartName, String bodyPartPosition,
                       String imageSessionId, Mat thumbNail) {

        PatientBodyPart patientBodyPart=new PatientBodyPart(false,false,bodyPartName, bodyPartPosition,thumbNail,null,new XrayParams(),null,null,null,"","","","",null,null,null,new CropLayoutWrapper(),null,"");
        addTab(imageSessionId,new BodyPartNode(imageSessionId,patientBodyPart));
    }


    @Override
    public void addTab(String sessionId, TabNode tab) {

        if(bodyPartHead==null){
            bodyPartHead= bodyPartTail= (BodyPartNode) tab;
        }
        else {
            bodyPartTail.setNextTab(tab);
            bodyPartTail.getNextTab().setPreviousTab(bodyPartTail);
            bodyPartTail = (BodyPartNode) bodyPartTail.getNextTab();
        }
        patientBodyPartMap.put(sessionId, bodyPartTail);

        if(currentBodyPart == null){
            currentBodyPart= (BodyPartNode)tab;
        }

    }

    @Override
    public void removeTab(String id) {
        BodyPartNode node=  patientBodyPartMap.get(id);

        if(node==null){
            return;
        }

        if(node == currentBodyPart){
            if(node.getPreviousTab() != null){
                currentBodyPart = (BodyPartNode) node.getPreviousTab();
            }
            else if(node.getNextTab() != null){
                currentBodyPart = (BodyPartNode) node.getNextTab();
            }
            else{
                currentBodyPart = null;
            }
        }

        if(node.getPreviousTab() != null) {
            node.getPreviousTab().setNextTab(node.getNextTab());
        }
        else{
            bodyPartHead= (BodyPartNode) node.getNextTab();
            if(bodyPartHead != null){
                bodyPartHead.setPreviousTab(null);
            }
            else{
                currentBodyPart = null;
            }
        }

        if(node.getNextTab() != null){
            node.getNextTab().setPreviousTab(node.getPreviousTab());
        }
        else{
            bodyPartTail= (BodyPartNode) node.getPreviousTab();
            if(bodyPartTail != null){
                bodyPartTail.setNextTab(null);
            }
            else{
                currentBodyPart = null;
            }
        }

        patientBodyPartMap.remove(id);
    }

    @Override
    public void markTabAsEdited(String id) {
//        TabNode node=patientBodyPartList.get(id);
//        if(node!=null && !node.isEdited){
//            node.isEdited=true;
//
//        }
    }

    @Override
    public void moveTab(String id, int index) {
        BodyPartNode node =  patientBodyPartMap.get(id);
        if(node==null){
            return;
        }

        removeTab(id);
        patientBodyPartMap.put(id,node);
        TabNode current=bodyPartHead;
        for(int i=0;i< index && current != null; i++){
            current=current.getNextTab();
        }
        if(current!=null){
            node.setNextTab(current);
            node.setPreviousTab(current.getPreviousTab());
            if(current.getPreviousTab()!=null) {
                current.getPreviousTab().setNextTab(node);
            }
            else{
                bodyPartHead= (BodyPartNode) node;
            }
            current.setPreviousTab(node);
        }
        else{
            if(bodyPartTail!=null){
                bodyPartTail.setNextTab(node);
                node.setPreviousTab(bodyPartTail);
                node.setNextTab(null);
                bodyPartTail= (BodyPartNode) node;
            }
        }


    }

    @Override
    public TabNode getTab(String id) {
        return patientBodyPartMap.get(id);

    }


    @Override
    public List<TabNode> getAllTabs() {
        List<TabNode> tabs = new ArrayList<>();
        TabNode current = bodyPartHead;

        //System.out.println("Traversing tabs...");
        while (current != null) {
            //System.out.println("Found TabNode: " + current.toString());
            tabs.add(current);
            current = current.getNextTab();
        }

        //System.out.println("Total tabs found: " + tabs.size());
        return tabs;
    }

    public BodyPartNode selectPatientBodypart(String imageSessionId){

        BodyPartNode patientBodyPart= patientBodyPartMap.get(imageSessionId);
        currentBodyPart=patientBodyPart;
        return currentBodyPart;
    }


    @Override
    public String toString() {
        String output = "";
        for(TabNode node : getAllTabs()){
            BodyPartNode n = (BodyPartNode)node;
            logger.info(String.valueOf(n.getBodyPart()));
        }
        return "";
    }

    public boolean containsBodyPart(String name){
        for(BodyPartNode node : patientBodyPartMap.values()){
            if((node.getBodyPart().getBodyPartName() + " " +node.getBodyPart().getBodyPartPosition()).equals(name)){
                return true;
            }
        }

        return false;
    }


    @Override
    public BodyPartHandler clone() {
        try {
            BodyPartHandler cloned = (BodyPartHandler) super.clone();
            cloned.patientBodyPartMap = new HashMap<>();
            cloned.bodyPartHead = null;
            cloned.bodyPartTail = null;
            cloned.currentBodyPart = null;

            // Rebuild the linked list and map
            BodyPartNode originalCurrent = this.bodyPartHead;
            BodyPartNode clonedPrevious = null;

            while (originalCurrent != null) {
                BodyPartNode clonedNode = originalCurrent.clone(); // Deep clone node
                clonedNode.setPreviousTab(null);
                clonedNode.setNextTab(null);

                if (cloned.bodyPartHead == null) {
                    cloned.bodyPartHead = clonedNode;
                    cloned.bodyPartTail = clonedNode;
                } else {
                    clonedPrevious.setNextTab(clonedNode);
                    clonedNode.setPreviousTab(clonedPrevious);
                    cloned.bodyPartTail = clonedNode;
                }

                cloned.patientBodyPartMap.put(clonedNode.getSessionId(), clonedNode);

                // Update currentBodyPart if matches original
                if (originalCurrent == this.currentBodyPart) {
                    cloned.currentBodyPart = clonedNode;
                }

                clonedPrevious = clonedNode;
                originalCurrent = (BodyPartNode) originalCurrent.getNextTab();
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("BodyPartHandler cloning failed", e);
        }
    }

}
