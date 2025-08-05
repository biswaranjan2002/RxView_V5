package com.raymedis.rxviewui.service.adminSettings.dicom;

public class VerificationResult {
    private final String time;
    private final String information;

    public VerificationResult(String time, String information) {
        this.time = time;
        this.information = information;
    }

    public String getTime() {
        return time;
    }

    public String getInformation() {
        return information;
    }
}