package com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table;

public class CandidateTagsEntity {

    private String tagName;
    private Boolean isSelected;


    public CandidateTagsEntity() {

    }

    public CandidateTagsEntity(String tagName, Boolean isSelected) {
        this.tagName = tagName;
        this.isSelected = isSelected;
    }
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
