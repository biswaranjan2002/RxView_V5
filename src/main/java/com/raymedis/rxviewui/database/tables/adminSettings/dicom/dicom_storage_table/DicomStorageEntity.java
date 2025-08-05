package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table;

public class DicomStorageEntity {

    private int id;
    private String name;
    private String aeTitle;
    private String ipAddress;
    private int port;
    private int maxPdu;
    private int timeout;
    private int verificationTimeout;
    private boolean isSelected;
    private boolean burnedInAnnotation;
    private boolean burnedInInformation;
    private boolean burnedWithCrop;
    private int lut;
    private int dapUnitType;
    private int softwareCollimation;
    private int transferSyntax;
    private int compression;

    //constructors
    public DicomStorageEntity() {

    }

    public DicomStorageEntity(int id, String name, String aeTitle, String ipAddress, int port, int maxPdu, int timeout,
                              int verificationTimeout, boolean isSelected, boolean burnedInAnnotation, boolean burnedInInformation,
                              boolean burnedWithCrop, int lut, int dapUnitType, int softwareCollimation, int transferSyntax, int compression) {
        this.id = id;
        this.name = name;
        this.aeTitle = aeTitle;
        this.ipAddress = ipAddress;
        this.port = port;
        this.maxPdu = maxPdu;
        this.timeout = timeout;
        this.verificationTimeout = verificationTimeout;
        this.isSelected = isSelected;
        this.burnedInAnnotation = burnedInAnnotation;
        this.burnedInInformation = burnedInInformation;
        this.burnedWithCrop = burnedWithCrop;
        this.lut = lut;
        this.dapUnitType = dapUnitType;
        this.softwareCollimation = softwareCollimation;
        this.transferSyntax = transferSyntax;
        this.compression = compression;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAeTitle() {
        return aeTitle;
    }

    public void setAeTitle(String aeTitle) {
        this.aeTitle = aeTitle;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxPdu() {
        return maxPdu;
    }

    public void setMaxPdu(int maxPdu) {
        this.maxPdu = maxPdu;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getVerificationTimeout() {
        return verificationTimeout;
    }

    public void setVerificationTimeout(int verificationTimeout) {
        this.verificationTimeout = verificationTimeout;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isBurnedInAnnotation() {
        return burnedInAnnotation;
    }

    public void setBurnedInAnnotation(boolean burnedInAnnotation) {
        this.burnedInAnnotation = burnedInAnnotation;
    }

    public boolean isBurnedInInformation() {
        return burnedInInformation;
    }

    public void setBurnedInInformation(boolean burnedInInformation) {
        this.burnedInInformation = burnedInInformation;
    }


    public boolean isBurnedWithCrop() {
        return burnedWithCrop;
    }

    public void setBurnedWithCrop(boolean burnedWithCrop) {
        this.burnedWithCrop = burnedWithCrop;
    }

    public int getLut() {
        return lut;
    }

    public void setLut(int lut) {
        this.lut = lut;
    }

    public int getDapUnitType() {
        return dapUnitType;
    }

    public void setDapUnitType(int dapUnitType) {
        this.dapUnitType = dapUnitType;
    }

    public int getSoftwareCollimation() {
        return softwareCollimation;
    }

    public void setSoftwareCollimation(int softwareCollimation) {
        this.softwareCollimation = softwareCollimation;
    }

    public int getTransferSyntax() {
        return transferSyntax;
    }

    public void setTransferSyntax(int transferSyntax) {
        this.transferSyntax = transferSyntax;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }
}

