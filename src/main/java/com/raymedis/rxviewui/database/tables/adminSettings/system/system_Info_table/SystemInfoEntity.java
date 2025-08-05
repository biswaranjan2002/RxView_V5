package com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table;



public class SystemInfoEntity {

    private int id;
    private String institutionName;
    private String institutionAddress;
    private String department;

    private String manufacturer;
    private String modelName;
    private String telephone;
    private String email;
    private String homePage;
    private String softwareVersion;
    private String serialNumber;

    private String language;


    //constructors
    public SystemInfoEntity() {
    }

    public SystemInfoEntity(int id, String institutionName, String institutionAddress, String department,
                            String manufacturer, String modelName, String telephone, String email,
                            String homePage, String softwareVersion, String serialNumber, String language) {
        this.id = id;
        this.institutionName = institutionName;
        this.institutionAddress = institutionAddress;
        this.department = department;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.telephone = telephone;
        this.email = email;
        this.homePage = homePage;
        this.softwareVersion = softwareVersion;
        this.serialNumber = serialNumber;
        this.language = language;
    }

    //setters and getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionAddress() {
        return institutionAddress;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}