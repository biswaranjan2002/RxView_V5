package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table;

public class ProcedureEntity {

    private int procedureId;
    private String procedureCode;
    private String procedureName;
    private String procedureDescription;

    //constructors
    public ProcedureEntity() {

    }

    public ProcedureEntity(int procedureId, String procedureCode, String procedureName, String procedureDescription) {
        this.procedureId = procedureId;
        this.procedureCode = procedureCode;
        this.procedureName = procedureName;
        this.procedureDescription = procedureDescription;
    }

    //getters and setters


    public int getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(int procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureDescription() {
        return procedureDescription;
    }

    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }
}
