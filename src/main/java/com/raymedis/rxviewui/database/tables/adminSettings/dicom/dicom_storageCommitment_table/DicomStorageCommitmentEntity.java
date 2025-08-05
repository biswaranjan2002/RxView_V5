package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table;

public class DicomStorageCommitmentEntity {

    private int id;
    private String name;
    private String aeTitle;
    private String ipAddress;
    private int port;
    private int maxPdu;
    private int verificationTimeout;
    private boolean isSelected;


    //constructors
    public DicomStorageCommitmentEntity() {

    }

    public DicomStorageCommitmentEntity(int id, String name, String aeTitle, String ipAddress, int port, int maxPdu,  int verificationTimeout, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.aeTitle = aeTitle;
        this.ipAddress = ipAddress;
        this.port = port;
        this.maxPdu = maxPdu;
        this.verificationTimeout = verificationTimeout;
        this.isSelected = isSelected;
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
}
