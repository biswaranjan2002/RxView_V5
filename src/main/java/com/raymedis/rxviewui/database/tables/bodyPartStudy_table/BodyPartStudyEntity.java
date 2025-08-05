package com.raymedis.rxviewui.database.tables.bodyPartStudy_table;

import java.time.LocalDateTime;

public class BodyPartStudyEntity {

    private int id;
    private String studyId;
    private String bodyPartName;
    private String position;
    private String seriesId;
    private String seriesUid;
    private String instanceId;
    private String instanceUid;
    private boolean isRejected;
    private boolean isExposed;
    private double windowWidth;
    private double windowLevel;
    private double kv;
    private double ma;
    private double mas;
    private double ms;
    private LocalDateTime exposedDate;

    //constructors
    public BodyPartStudyEntity() {
    }

    public BodyPartStudyEntity(int id, String studyId, String bodyPartName,
                               String position, String seriesId, String seriesUid,
                               String instanceId, String instanceUid, boolean isRejected,
                               boolean isExposed, double windowWidth, double windowLevel,
                               double kv, double ma, double mas, double ms, LocalDateTime exposedDate) {
        this.id = id;
        this.studyId = studyId;
        this.bodyPartName = bodyPartName;
        this.position = position;
        this.seriesId = seriesId;
        this.seriesUid = seriesUid;
        this.instanceId = instanceId;
        this.instanceUid = instanceUid;
        this.isRejected = isRejected;
        this.isExposed = isExposed;
        this.windowWidth = windowWidth;
        this.windowLevel = windowLevel;
        this.kv = kv;
        this.ma = ma;
        this.mas = mas;
        this.ms = ms;
        this.exposedDate = exposedDate;
    }

    //setters and getters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
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

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesUid() {
        return seriesUid;
    }

    public void setSeriesUid(String seriesUid) {
        this.seriesUid = seriesUid;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceUid() {
        return instanceUid;
    }

    public void setInstanceUid(String instanceUid) {
        this.instanceUid = instanceUid;
    }

    public boolean getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean getIsExposed() {
        return isExposed;
    }

    public void setIsExposed(boolean exposed) {
        isExposed = exposed;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public double getWindowLevel() {
        return windowLevel;
    }

    public void setWindowLevel(double windowLevel) {
        this.windowLevel = windowLevel;
    }

    public double getKv() {
        return kv;
    }

    public void setKv(double kv) {
        this.kv = kv;
    }

    public double getMa() {
        return ma;
    }

    public void setMa(double ma) {
        this.ma = ma;
    }

    public double getMas() {
        return mas;
    }

    public void setMas(double mas) {
        this.mas = mas;
    }

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public LocalDateTime getExposedDate() {
        return exposedDate;
    }

    public void setExposedDate(LocalDateTime exposedDate) {
        this.exposedDate = exposedDate;
    }
}
