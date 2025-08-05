package com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;

public class PrintOverlayEntity {

    private int id;
    private PositionType positionType;
    private String itemContent;private Boolean isSelected;

    //constructors
    public PrintOverlayEntity() {
    }

    public PrintOverlayEntity(int id, PositionType positionType, String itemContent ) {
        this.id = id;
        this.positionType = positionType;
        this.itemContent = itemContent;
    }

    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
