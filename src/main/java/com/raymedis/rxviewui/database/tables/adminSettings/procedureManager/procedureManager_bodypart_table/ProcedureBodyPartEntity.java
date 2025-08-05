package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table;

public class ProcedureBodyPartEntity {

    private int id;
    private String meaning;
    private String alias;
    private Boolean hide;
    private String codeValue;
    private String designator;
    private String version;
    private String bodyPartExamination;

    //constructor
    public ProcedureBodyPartEntity() {

    }

    public ProcedureBodyPartEntity(int id,String meaning, String alias, Boolean hide,
                                   String codeValue, String designator, String version,
                                   String bodyPartExamination) {
        this.id = id;
        this.meaning = meaning;
        this.alias = alias;
        this.hide = hide;
        this.codeValue = codeValue;
        this.designator = designator;
        this.version = version;
        this.bodyPartExamination = bodyPartExamination;
    }


    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBodyPartExamination() {
        return bodyPartExamination;
    }

    public void setBodyPartExamination(String bodyPartExamination) {
        this.bodyPartExamination = bodyPartExamination;
    }

    @Override
    public String toString() {
        return "ProcedureBodyPartEntity{" +
                "id=" + id +
                ", meaning='" + meaning + '\'' +
                ", alias='" + alias + '\'' +
                ", hide=" + hide +
                ", codeValue='" + codeValue + '\'' +
                ", designator='" + designator + '\'' +
                ", version='" + version + '\'' +
                ", bodyPartExamination='" + bodyPartExamination + '\'' +
                '}';
    }
}
