package com.raymedis.rxviewui.database.tables.register.localListBodyParts_table;

public class LocalListBodyPartsEntity {

    private int id;
    private String bodyPartName;
    private String position;
    private int localListId;


    //constructors
    public LocalListBodyPartsEntity() {

    }

    public LocalListBodyPartsEntity(int id, String bodyPartName, String position, int localListId) {
        this.id = id;
        this.bodyPartName = bodyPartName;
        this.position = position;
        this.localListId = localListId;
    }

    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getLocalListId() {
        return localListId;
    }

    public void setLocalListId(int localListId) {
        this.localListId = localListId;
    }

}
