package com.raymedis.rxviewui.modules.study.patient;

import java.time.LocalDateTime;

public class PatientInfo {
    // Patient related information :
    private String patientId;
    private String patientName;
    private int age;
    private double height;
    private double weight;
    private LocalDateTime birthDate;
    private String patientSize;
    private String sex;

    private String patientInstituteResidence;


    public PatientInfo() {
    }

    public PatientInfo(String patientId, String patientName, int age, double height, double weight, LocalDateTime birthDate, String patientSize, String sex, String patientInstituteResidence) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.patientSize = patientSize;
        this.sex = sex;
        this.patientInstituteResidence = patientInstituteResidence;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getPatientSize() {
        return patientSize;
    }

    public void setPatientSize(String patientSize) {
        this.patientSize = patientSize;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPatientInstituteResidence() {
        return patientInstituteResidence;
    }

    public void setPatientInstituteResidence(String patientInstituteResidence) {
        this.patientInstituteResidence = patientInstituteResidence;
    }


    @Override
    public String toString() {
        return "PatientInfo{" +
                "patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", birthDate=" + birthDate +
                ", patientSize='" + patientSize + '\'' +
                ", sex='" + sex + '\'' +
                ", patientInstituteResidence='" + patientInstituteResidence + '\'' +
                '}';
    }
}
