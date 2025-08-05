package com.raymedis.rxviewui.modules.print.printInput;

import javafx.collections.FXCollections;
import java.util.List;

public class PatientPrintDistanceDrawWrapper {

    private List<PatientPrintEllipseWrapper> ellipses = FXCollections.observableArrayList();
    private PatientPrintLineWrapper lines;
    private PatientPrintLabelWrapper labelWrapper;

    //constructors
    public PatientPrintDistanceDrawWrapper() {
    }

    public PatientPrintDistanceDrawWrapper(List<PatientPrintEllipseWrapper> ellipses, PatientPrintLineWrapper lines, PatientPrintLabelWrapper labelWrapper) {
        this.ellipses = ellipses;
        this.lines = lines;
        this.labelWrapper = labelWrapper;
    }


    //setters and getters
    public List<PatientPrintEllipseWrapper> getEllipses() {
        return ellipses;
    }

    public void setEllipses(List<PatientPrintEllipseWrapper> ellipses) {
        this.ellipses = ellipses;
    }

    public PatientPrintLineWrapper getLines() {
        return lines;
    }

    public void setLines(PatientPrintLineWrapper lines) {
        this.lines = lines;
    }

    public PatientPrintLabelWrapper getLabelWrapper() {
        return labelWrapper;
    }

    public void setLabelWrapper(PatientPrintLabelWrapper labelWrapper) {
        this.labelWrapper = labelWrapper;
    }


    @Override
    public String toString() {
        return "PatientPrintDistanceDrawWrapper{" +
                "ellipses=" + ellipses +
                ", lines=" + lines +
                ", labelWrapper=" + labelWrapper +
                '}';
    }
}
