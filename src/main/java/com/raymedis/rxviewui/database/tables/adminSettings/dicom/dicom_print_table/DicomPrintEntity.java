package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table;

public class DicomPrintEntity {

    private int id;
    private String name;
    private String aeTitle;
    private String ipAddress;
    private int port;
    private int maxPdu;
    private int timeout;
    private int verificationTimeout;
    private String printLayout;
    private boolean isSelected;


    //constructors
    public DicomPrintEntity() {

    }

    public DicomPrintEntity(int id, String name, String aeTitle, String ipAddress, int port, int maxPdu, int timeout, int verificationTimeout, String printLayout, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.aeTitle = aeTitle;
        this.ipAddress = ipAddress;
        this.port = port;
        this.maxPdu = maxPdu;
        this.timeout = timeout;
        this.verificationTimeout = verificationTimeout;
        this.printLayout = printLayout;
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

    public String getPrintLayout() {
        return printLayout;
    }

    public void setPrintLayout(String printLayout) {
        this.printLayout = printLayout;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}
