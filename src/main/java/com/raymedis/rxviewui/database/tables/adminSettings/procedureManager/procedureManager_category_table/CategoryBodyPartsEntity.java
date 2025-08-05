package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table;

public class CategoryBodyPartsEntity {

    private String bodyPartName;
    private Boolean maleIsSelected;
    private Boolean femaleIsSelected;
    private Boolean infantIsSelected;


    private double maleXPos;
    private double maleYPos;

    private double femaleXPos;
    private double femaleYPos;

    private double infantXPos;
    private double infantYPos;
    private Boolean isStepExist;

    //constructors
    public CategoryBodyPartsEntity(String bodyPartName, Boolean maleIsSelected, Boolean femaleIsSelected, Boolean infantIsSelected,
                                   double maleXPos, double maleYPos, double femaleXPos, double femaleYPos, double infantXPos, double infantYPos, Boolean isStepExist) {
        this.infantXPos = infantXPos;
        this.bodyPartName = bodyPartName;
        this.maleIsSelected = maleIsSelected;
        this.femaleIsSelected = femaleIsSelected;
        this.infantIsSelected = infantIsSelected;
        this.maleXPos = maleXPos;
        this.maleYPos = maleYPos;
        this.femaleXPos = femaleXPos;
        this.femaleYPos = femaleYPos;
        this.infantYPos = infantYPos;
        this.isStepExist = isStepExist;
    }

    // Constructor with AnatomyType
    public CategoryBodyPartsEntity(String bodyPartName, AnatomyType anatomyType, double xPos, double yPos, Boolean isStepExist) {
        this.bodyPartName = bodyPartName;
        switch (anatomyType) {
            case MALE:
                this.maleIsSelected = true;
                this.femaleIsSelected = false;
                this.infantIsSelected = false;
                this.maleXPos = xPos;
                this.maleYPos = yPos;
                this.isStepExist=false;
                break;
            case FEMALE:
                this.maleIsSelected = false;
                this.femaleIsSelected = true;
                this.infantIsSelected = false;
                this.femaleXPos = xPos;
                this.femaleYPos = yPos;
                this.isStepExist=false;
                break;
            case INFANT:
                this.maleIsSelected = false;
                this.femaleIsSelected = false;
                this.infantIsSelected = true;
                this.infantXPos = xPos;
                this.infantYPos = yPos;
                this.isStepExist=false;
                break;
            default:
                throw new IllegalArgumentException("Unknown anatomy type: " + anatomyType);
        }
    }

    public CategoryBodyPartsEntity() {
    }

    //setters and getters

    public double getMaleXPos() {
        return maleXPos;
    }

    public void setMaleXPos(double maleXPos) {
        this.maleXPos = maleXPos;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public Boolean getMaleIsSelected() {
        return maleIsSelected;
    }

    public void setMaleIsSelected(Boolean maleIsSelected) {
        this.maleIsSelected = maleIsSelected;
    }

    public Boolean getFemaleIsSelected() {
        return femaleIsSelected;
    }

    public void setFemaleIsSelected(Boolean femaleIsSelected) {
        this.femaleIsSelected = femaleIsSelected;
    }

    public Boolean getInfantIsSelected() {
        return infantIsSelected;
    }

    public void setInfantIsSelected(Boolean infantIsSelected) {
        this.infantIsSelected = infantIsSelected;
    }

    public double getMaleYPos() {
        return maleYPos;
    }

    public void setMaleYPos(double maleYPos) {
        this.maleYPos = maleYPos;
    }

    public double getFemaleXPos() {
        return femaleXPos;
    }

    public void setFemaleXPos(double femaleXPos) {
        this.femaleXPos = femaleXPos;
    }

    public double getFemaleYPos() {
        return femaleYPos;
    }

    public void setFemaleYPos(double femaleYPos) {
        this.femaleYPos = femaleYPos;
    }

    public double getInfantXPos() {
        return infantXPos;
    }

    public void setInfantXPos(double infantXPos) {
        this.infantXPos = infantXPos;
    }

    public double getInfantYPos() {
        return infantYPos;
    }

    public void setInfantYPos(double infantYPos) {
        this.infantYPos = infantYPos;
    }

    public Boolean getIsStepExist() {
        return isStepExist;
    }

    public void setIsStepExist(Boolean stepExist) {
        isStepExist = stepExist;
    }
}
