package com.raymedis.rxviewui.service.export;

import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import org.opencv.core.Mat;

public class ExportData {

    private String path; // or whatever the String represents
    private Mat image;
    private PatientBodyPart patientBodyPart;


    public ExportData(String path, Mat image, PatientBodyPart patientBodyPart) {
        this.path = path;
        this.image = image;
        this.patientBodyPart = patientBodyPart;
    }


    public ExportData() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public PatientBodyPart getPatientBodyPart() {
        return patientBodyPart;
    }

    public void setPatientBodyPart(PatientBodyPart patientBodyPart) {
        this.patientBodyPart = patientBodyPart;
    }
}
