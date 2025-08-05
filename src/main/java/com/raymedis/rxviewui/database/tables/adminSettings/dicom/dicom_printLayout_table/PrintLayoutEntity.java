package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table;

public class PrintLayoutEntity {

    private int id;
    private String layoutName;


    //constructors
    public PrintLayoutEntity() {

    }

    public PrintLayoutEntity(int id, String layoutName) {
        this.id = id;
        this.layoutName = layoutName;
    }


    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

}
