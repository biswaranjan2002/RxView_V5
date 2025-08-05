package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table;

public class PrintOverlayEntity {

    private int id;
    private String overlayId;
    private String overlayName;
    private String logoPath;
    private PositionType positionType;
    private String itemContent;
    private Boolean isSelected;

    //Constructors
    public PrintOverlayEntity() {
    }

    public PrintOverlayEntity(int id, String overlayId, String overlayName, String logoPath, PositionType positionType, String itemContent, Boolean isSelected) {
        this.id = id;
        this.overlayId = overlayId;
        this.overlayName = overlayName;
        this.logoPath = logoPath;
        this.positionType = positionType;
        this.itemContent = itemContent;
        this.isSelected = isSelected;
    }

    private static PrintOverlayRowMapper instance = new PrintOverlayRowMapper();

    public static PrintOverlayRowMapper getInstance() {
        return instance;
    }


    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverlayId() {
        return overlayId;
    }

    public void setOverlayId(String overlayId) {
        this.overlayId = overlayId;
    }

    public String getOverlayName() {
        return overlayName;
    }

    public void setOverlayName(String overlayName) {
        this.overlayName = overlayName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean selected) {
        isSelected = selected;
    }
}
