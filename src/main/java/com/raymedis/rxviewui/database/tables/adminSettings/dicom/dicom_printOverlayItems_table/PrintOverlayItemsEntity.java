package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table;

public class PrintOverlayItemsEntity {
    private int id;
    private String Item;

    //constructors
    public PrintOverlayItemsEntity() {
    }

    public PrintOverlayItemsEntity(int id, String item) {
        this.id = id;
        Item = item;
    }


    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
