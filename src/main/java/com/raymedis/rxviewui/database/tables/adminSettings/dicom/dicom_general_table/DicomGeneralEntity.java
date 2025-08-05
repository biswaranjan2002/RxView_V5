package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table;

public class DicomGeneralEntity {

    private int id;
    private String stationName;
    private String stationAETitle;
    private String stationPort;


    //Constructors
    public DicomGeneralEntity() {
    }

    public DicomGeneralEntity(int id, String stationName, String stationAETitle, String stationPort) {
        this.id = id;
        this.stationName = stationName;
        this.stationAETitle = stationAETitle;
        this.stationPort = stationPort;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAETitle() {
        return stationAETitle;
    }

    public void setStationAETitle(String stationAETitle) {
        this.stationAETitle = stationAETitle;
    }

    public String getStationPort() {
        return stationPort;
    }

    public void setStationPort(String stationPort) {
        this.stationPort = stationPort;
    }
}
