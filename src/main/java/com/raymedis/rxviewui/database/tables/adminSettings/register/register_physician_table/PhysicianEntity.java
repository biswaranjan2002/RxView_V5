package com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table;

public class PhysicianEntity {


    private String physicianId;
    private String physicianName;
    private String physicianGroup;

    //constructor
    public PhysicianEntity() {
    }

    public PhysicianEntity(String physicianId, String physicianName,
                           String physicianGroup) {
        this.physicianId = physicianId;
        this.physicianName = physicianName;
        this.physicianGroup = physicianGroup;
    }


    //getter and setter
    public String getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(String physicianId) {
        this.physicianId = physicianId;
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public void setPhysicianName(String physicianName) {
        this.physicianName = physicianName;
    }

    public String getPhysicianGroup() {
        return physicianGroup;
    }

    public void setPhysicianGroup(String physicianGroup) {
        this.physicianGroup = physicianGroup;
    }
}
