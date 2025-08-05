package com.raymedis.rxviewui.modules.study.bodypart;

import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;

public class BodyPartNode implements TabNode,Cloneable {

    private BodyPartNode previousBodyPart;
    private BodyPartNode nextBodyPart;
    private String imageSessionId;

    private PatientBodyPart bodyPart;

    public BodyPartNode() {
    }

    public BodyPartNode(String imageSessionId,PatientBodyPart bodyPart) {
        this.bodyPart = bodyPart;
        this.imageSessionId=imageSessionId;
    }

    public BodyPartNode getPreviousBodyPart() {
        return previousBodyPart;
    }

    public void setPreviousBodyPart(BodyPartNode previousBodyPart) {
        this.previousBodyPart = previousBodyPart;
    }

    public BodyPartNode getNextBodyPart() {
        return nextBodyPart;
    }

    public void setNextBodyPart(BodyPartNode nextBodyPart) {
        this.nextBodyPart = nextBodyPart;
    }

    public String getImageSessionId() {
        return imageSessionId;
    }

    public void setImageSessionId(String imageSessionId) {
        this.imageSessionId = imageSessionId;
    }

    public PatientBodyPart getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(PatientBodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    @Override
    public TabNode getNextTab() {
        return nextBodyPart;
    }

    @Override
    public TabNode getPreviousTab() {
        return previousBodyPart;
    }

    @Override
    public void setNextTab(TabNode tab) {
        this.nextBodyPart= (BodyPartNode) tab;
    }

    @Override
    public void setPreviousTab(TabNode tab) {
        this.previousBodyPart= (BodyPartNode) tab;

    }

    @Override
    public BodyPartNode clone() {
        try {
            BodyPartNode cloned = (BodyPartNode) super.clone();
            cloned.setBodyPart(this.bodyPart != null ? this.bodyPart.clone() : null); // Deep clone
            cloned.setNextTab(null); // Will be rebuilt by BodyPartHandler
            cloned.setPreviousTab(null);
            cloned.setImageSessionId(this.imageSessionId);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("BodyPartNode cloning failed", e);
        }
    }


    public String getSessionId() {
        return imageSessionId;
    }
}
