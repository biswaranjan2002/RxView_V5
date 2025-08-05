package com.raymedis.rxviewui.service.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"type", "selectedDetector", "firstDetector", "secondDetector", "thirdDetector", "sdk"})
public class ApplicationDetails {

    private ApplicationType type;

    private int selectedDetector;

    private DetectorDetails firstDetector;

    private DetectorDetails secondDetector;

    private DetectorDetails thirdDetector;

    private ArrayList<FpdSDK> sdk;

    public ApplicationDetails() {
    }

    public ApplicationDetails(ApplicationType type, int selectedDetector, DetectorDetails firstDetector,
                              DetectorDetails secondDetector, DetectorDetails thirdDetector,
                              ArrayList<FpdSDK> sdk) {
        this.type = type;
        this.selectedDetector = selectedDetector;
        this.firstDetector = firstDetector;
        this.secondDetector = secondDetector;
        this.thirdDetector = thirdDetector;
        this.sdk = sdk;
    }

    public int getSelectedDetector() {
        return selectedDetector;
    }

    public void setSelectedDetector(int selectedDetector) {
        this.selectedDetector = selectedDetector;
    }

    public ApplicationType getType() {
        return type;
    }

    public void setType(ApplicationType type) {
        this.type = type;
    }

    public DetectorDetails getFirstDetector() {
        return firstDetector;
    }

    public void setFirstDetector(DetectorDetails firstDetector) {
        this.firstDetector = firstDetector;
    }

    public DetectorDetails getSecondDetector() {
        return secondDetector;
    }

    public void setSecondDetector(DetectorDetails secondDetector) {
        this.secondDetector = secondDetector;
    }

    public DetectorDetails getThirdDetector() {
        return thirdDetector;
    }

    public void setThirdDetector(DetectorDetails thirdDetector) {
        this.thirdDetector = thirdDetector;
    }

    public ArrayList<FpdSDK> getSdk() {
        return sdk;
    }

    public void setSdk(ArrayList<FpdSDK> sdk) {
        this.sdk = sdk;
    }

    @Override
    public String toString() {
        return "ApplicationDetails{" +
                "type=" + type +
                ", selectedDetector=" + selectedDetector +
                ", FirstDetector=" + firstDetector +
                ", SecondDetector=" + secondDetector +
                ", ThirdDetector=" + thirdDetector +
                ", sdk=" + sdk +
                '}';
    }
}
