package com.raymedis.rxviewui.modules.print.printInput;


import org.opencv.core.Mat;

import java.time.LocalDateTime;
import java.util.List;

public class PatientPrintImageData {

    private String id;

    private String imageSessionId;
    private boolean isExposed;
    private boolean isRejected;
    private String bodyPartName;
    private String bodyPartPosition;
    private Mat thumbNail;
    private Mat imageMat;
    private Mat editedMat;
    private LocalDateTime exposureDateTime;
    private String instanceUid;
    private String seriesUid;
    private String instanceId;
    private String seriesId;
    private String studyInstanceUID;
    private boolean isCrop;
    private double windowLevel;
    private double windowWidth;
    private double zoomFactor;
    private double angleFactor;
    private double imageLeft;
    private double imageTop;
    private boolean isHFlip;
    private boolean isVFlip;
    private boolean isInvert;
    private double mAs;
    private double mA;
    private double KV;
    private double ms;
    private double denoise;
    private double sharpness;

    public List<PatientPrintAngleDrawWrapper> patientPrintAngleDrawWrappers;
    public List<PatientPrintDistanceDrawWrapper> patientPrintDistanceDrawWrappers;
    public List<PatientPrintTextDrawWrapper> patientPrintTextDrawWrappers;

    private double rectX, rectY, rectWidth, rectHeight;
    private double offsetX, offsetY;
    private  String text;



    //constructors
    public PatientPrintImageData() {
    }

    public PatientPrintImageData(String id, String imageSessionId, boolean isExposed, boolean isRejected,
                                 String bodyPartName, String bodyPartPosition, Mat thumbNail, Mat imageMat,
                                 Mat editedMat, LocalDateTime exposureDateTime, String instanceUid, String seriesUid,
                                 String instanceId, String seriesId, String studyInstanceUID, boolean isCrop, double windowLevel,
                                 double windowWidth, double zoomFactor, double angleFactor, double imageLeft, double imageTop,
                                 boolean isHFlip, boolean isVFlip, boolean isInvert, double mAs, double mA, double KV, double ms,
                                 double denoise, double sharpness, List<PatientPrintAngleDrawWrapper> patientPrintAngleDrawWrappers,
                                 List<PatientPrintDistanceDrawWrapper> patientPrintDistanceDrawWrappers, List<PatientPrintTextDrawWrapper> patientPrintTextDrawWrappers,
                                 double rectX, double rectY, double rectWidth, double rectHeight, double offsetX, double offsetY, String text) {
        this.id = id;
        this.imageSessionId = imageSessionId;
        this.isExposed = isExposed;
        this.isRejected = isRejected;
        this.bodyPartName = bodyPartName;
        this.bodyPartPosition = bodyPartPosition;
        this.thumbNail = thumbNail;
        this.imageMat = imageMat;
        this.editedMat = editedMat;
        this.exposureDateTime = exposureDateTime;
        this.instanceUid = instanceUid;
        this.seriesUid = seriesUid;
        this.instanceId = instanceId;
        this.seriesId = seriesId;
        this.studyInstanceUID = studyInstanceUID;
        this.isCrop = isCrop;
        this.windowLevel = windowLevel;
        this.windowWidth = windowWidth;
        this.zoomFactor = zoomFactor;
        this.angleFactor = angleFactor;
        this.imageLeft = imageLeft;
        this.imageTop = imageTop;
        this.isHFlip = isHFlip;
        this.isVFlip = isVFlip;
        this.isInvert = isInvert;
        this.mAs = mAs;
        this.mA = mA;
        this.KV = KV;
        this.ms = ms;
        this.denoise = denoise;
        this.sharpness = sharpness;
        this.patientPrintAngleDrawWrappers = patientPrintAngleDrawWrappers;
        this.patientPrintDistanceDrawWrappers = patientPrintDistanceDrawWrappers;
        this.patientPrintTextDrawWrappers = patientPrintTextDrawWrappers;
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //setters and getters
    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public String getBodyPartPosition() {
        return bodyPartPosition;
    }

    public void setBodyPartPosition(String bodyPartPosition) {
        this.bodyPartPosition = bodyPartPosition;
    }

    public Mat getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Mat thumbNail) {
        this.thumbNail = thumbNail;
    }

    public Mat getImageMat() {
        return imageMat;
    }

    public void setImageMat(Mat imageMat) {
        this.imageMat = imageMat;
    }

    public Mat getEditedMat() {
        return editedMat;
    }

    public void setEditedMat(Mat editedMat) {
        this.editedMat = editedMat;
    }

    public LocalDateTime getExposureDateTime() {
        return exposureDateTime;
    }

    public void setExposureDateTime(LocalDateTime exposureDateTime) {
        this.exposureDateTime = exposureDateTime;
    }

    public String getInstanceUid() {
        return instanceUid;
    }

    public void setInstanceUid(String instanceUid) {
        this.instanceUid = instanceUid;
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

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public double getWindowLevel() {
        return windowLevel;
    }

    public void setWindowLevel(double windowLevel) {
        this.windowLevel = windowLevel;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public double getAngleFactor() {
        return angleFactor;
    }

    public void setAngleFactor(double angleFactor) {
        this.angleFactor = angleFactor;
    }

    public double getImageLeft() {
        return imageLeft;
    }

    public void setImageLeft(double imageLeft) {
        this.imageLeft = imageLeft;
    }

    public double getImageTop() {
        return imageTop;
    }

    public void setImageTop(double imageTop) {
        this.imageTop = imageTop;
    }

    public boolean isHFlip() {
        return isHFlip;
    }

    public void setHFlip(boolean HFlip) {
        isHFlip = HFlip;
    }

    public boolean isVFlip() {
        return isVFlip;
    }

    public void setVFlip(boolean VFlip) {
        isVFlip = VFlip;
    }

    public boolean isInvert() {
        return isInvert;
    }

    public void setInvert(boolean invert) {
        isInvert = invert;
    }

    public double getmAs() {
        return mAs;
    }

    public void setmAs(double mAs) {
        this.mAs = mAs;
    }

    public double getmA() {
        return mA;
    }

    public void setmA(double mA) {
        this.mA = mA;
    }

    public double getKV() {
        return KV;
    }

    public void setKV(double KV) {
        this.KV = KV;
    }

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public List<PatientPrintAngleDrawWrapper> getPatientPrintAngleDrawWrappers() {
        return patientPrintAngleDrawWrappers;
    }

    public void setPatientPrintAngleDrawWrappers(List<PatientPrintAngleDrawWrapper> patientPrintAngleDrawWrappers) {
        this.patientPrintAngleDrawWrappers = patientPrintAngleDrawWrappers;
    }

    public List<PatientPrintDistanceDrawWrapper> getPatientPrintDistanceDrawWrappers() {
        return patientPrintDistanceDrawWrappers;
    }

    public void setPatientPrintDistanceDrawWrappers(List<PatientPrintDistanceDrawWrapper> patientPrintDistanceDrawWrappers) {
        this.patientPrintDistanceDrawWrappers = patientPrintDistanceDrawWrappers;
    }

    public List<PatientPrintTextDrawWrapper> getPatientPrintTextDrawWrappers() {
        return patientPrintTextDrawWrappers;
    }

    public void setPatientPrintTextDrawWrappers(List<PatientPrintTextDrawWrapper> patientPrintTextDrawWrappers) {
        this.patientPrintTextDrawWrappers = patientPrintTextDrawWrappers;
    }

    public double getRectX() {
        return rectX;
    }

    public void setRectX(double rectX) {
        this.rectX = rectX;
    }

    public double getRectY() {
        return rectY;
    }

    public void setRectY(double rectY) {
        this.rectY = rectY;
    }

    public double getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(double rectWidth) {
        this.rectWidth = rectWidth;
    }

    public double getRectHeight() {
        return rectHeight;
    }

    public void setRectHeight(double rectHeight) {
        this.rectHeight = rectHeight;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getImageSessionId() {
        return imageSessionId;
    }

    public void setImageSessionId(String imageSessionId) {
        this.imageSessionId = imageSessionId;
    }

    public double getDenoise() {
        return denoise;
    }

    public void setDenoise(double denoise) {
        this.denoise = denoise;
    }

    public double getSharpness() {
        return sharpness;
    }

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    @Override
    public String toString() {
        return "PatientPrintImageData{" +
                "id='" + id + '\'' +
                ", imageSessionId='" + imageSessionId + '\'' +
                ", isExposed=" + isExposed +
                ", isRejected=" + isRejected +
                ", bodyPartName='" + bodyPartName + '\'' +
                ", bodyPartPosition='" + bodyPartPosition + '\'' +
                ", thumbNail=" + thumbNail +
                ", imageMat=" + imageMat +
                ", editedMat=" + editedMat +
                ", exposureDateTime=" + exposureDateTime +
                ", instanceUid='" + instanceUid + '\'' +
                ", seriesUid='" + seriesUid + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", seriesId='" + seriesId + '\'' +
                ", studyInstanceUID='" + studyInstanceUID + '\'' +
                ", isCrop=" + isCrop +
                ", windowLevel=" + windowLevel +
                ", windowWidth=" + windowWidth +
                ", zoomFactor=" + zoomFactor +
                ", angleFactor=" + angleFactor +
                ", imageLeft=" + imageLeft +
                ", imageTop=" + imageTop +
                ", isHFlip=" + isHFlip +
                ", isVFlip=" + isVFlip +
                ", isInvert=" + isInvert +
                ", mAs=" + mAs +
                ", mA=" + mA +
                ", KV=" + KV +
                ", ms=" + ms +
                ", denoise=" + denoise +
                ", sharpness=" + sharpness +
                ", patientPrintAngleDrawWrappers=" + patientPrintAngleDrawWrappers +
                ", patientPrintDistanceDrawWrappers=" + patientPrintDistanceDrawWrappers +
                ", patientPrintTextDrawWrappers=" + patientPrintTextDrawWrappers +
                ", rectX=" + rectX +
                ", rectY=" + rectY +
                ", rectWidth=" + rectWidth +
                ", rectHeight=" + rectHeight +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", text='" + text + '\'' +
                '}';
    }
}
