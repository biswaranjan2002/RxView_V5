package com.raymedis.rxviewui.service.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"index", "model", "serial", "sdkType", "connectionType", "ipAddress"})
public class DetectorDetails {

    private int index;

    private String model;

    private String serial;

    private FpdSDK sdkType;

    private String connectionType;

    private String ipAddress;

    public DetectorDetails() {
    }

    public DetectorDetails(int index, String model, String serial, FpdSDK sdkType,
                           String connectionType, String ipAddress) {
        this.index = index;
        this.model = model;
        this.serial = serial;
        this.sdkType = sdkType;
        this.connectionType = connectionType;
        this.ipAddress = ipAddress;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public FpdSDK getSdkType() {
        return sdkType;
    }

    public void setSdkType(FpdSDK sdkType) {
        this.sdkType = sdkType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "\n\t\tDetectorDetails{" +
                "index=" + index +
                ", model='" + model + '\'' +
                ", serial='" + serial + '\'' +
                ", sdkType='" + sdkType + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                "}";
    }
}
