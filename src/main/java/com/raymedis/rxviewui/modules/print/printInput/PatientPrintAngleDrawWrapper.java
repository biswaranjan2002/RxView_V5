package com.raymedis.rxviewui.modules.print.printInput;

import javafx.collections.FXCollections;

import java.util.List;

public class PatientPrintAngleDrawWrapper {

    private List<PatientPrintEllipseWrapper> ellipsesWrapperList = FXCollections.observableArrayList();
    private List<PatientPrintLineWrapper> lineWrapperList = FXCollections.observableArrayList();
    private PatientPrintLabelWrapper labelWrapper;

    //constructors
    public PatientPrintAngleDrawWrapper() {
    }

    public PatientPrintAngleDrawWrapper(List<PatientPrintEllipseWrapper> ellipsesWrapperList, List<PatientPrintLineWrapper> lineWrapperList, PatientPrintLabelWrapper labelWrapper) {
        this.ellipsesWrapperList = ellipsesWrapperList;
        this.lineWrapperList = lineWrapperList;
        this.labelWrapper = labelWrapper;
    }

    //setters and getters
    public List<PatientPrintEllipseWrapper> getEllipsesWrapperList() {
        return ellipsesWrapperList;
    }

    public void setEllipsesWrapperList(List<PatientPrintEllipseWrapper> ellipsesWrapperList) {
        this.ellipsesWrapperList = ellipsesWrapperList;
    }

    public List<PatientPrintLineWrapper> getLineWrapperList() {
        return lineWrapperList;
    }

    public void setLineWrapperList(List<PatientPrintLineWrapper> lineWrapperList) {
        this.lineWrapperList = lineWrapperList;
    }

    public PatientPrintLabelWrapper getLabelWrapper() {
        return labelWrapper;
    }

    public void setLabelWrapper(PatientPrintLabelWrapper labelWrapper) {
        this.labelWrapper = labelWrapper;
    }


    @Override
    public String toString() {
        return "PatientPrintAngleDrawWrapper{" +
                "ellipsesWrapperList=" + ellipsesWrapperList +
                ", lineWrapperList=" + lineWrapperList +
                ", labelWrapper=" + labelWrapper +
                '}';
    }
}
