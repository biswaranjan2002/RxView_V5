package com.raymedis.rxviewui.modules.study.bodypart;

import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.*;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import org.opencv.core.Mat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientBodyPart implements Cloneable {

    private boolean isExposed;
    private boolean isRejected;

    private String bodyPartName;
    private String bodyPartPosition;

    private Mat thumbNail;

    private ImagingParams imagingParams;
    private XrayParams xrayParams;

    private Mat imageMat;
    private Mat editedMat;

    private LocalDateTime exposureDateTime;

    private String instanceUid;
    private String seriesUid;

    private String instanceId;
    private String seriesId;

    private  String studyInstanceUID;

    private boolean isCrop;


    //additional
    public List<AngleDrawWrapper> angleDrawWrapper;

    public List<DistanceDrawWrapper> distanceDrawList;

    public List<TextDrawWrapper> textDrawList;

    public CropLayoutWrapper cropLayoutWrapper;

    public LeftRightDrawWrapper leftRightDrawWrapper;


    public PatientBodyPart() {

    }


    public PatientBodyPart(boolean isExposed, boolean isRejected, String bodyPartName,
                           String bodyPartPosition, Mat thumbNail, ImagingParams imagingParams,
                           XrayParams xrayParams, Mat imageMat, Mat editedMat, LocalDateTime exposureDateTime,
                           String instanceUid, String seriesUid, String instanceId, String seriesId,
                           List<AngleDrawWrapper> angleDrawWrapper,
                           List<DistanceDrawWrapper> distanceDrawList,List<TextDrawWrapper> textDrawList,
                           CropLayoutWrapper cropLayoutWrapper,LeftRightDrawWrapper leftRightDrawWrapper,String studyInstanceUID) {
        this.isExposed = isExposed;
        this.isRejected = isRejected;
        this.bodyPartName = bodyPartName;
        this.bodyPartPosition = bodyPartPosition;
        this.thumbNail = thumbNail;
        this.imagingParams = imagingParams;
        this.xrayParams = xrayParams;
        this.imageMat = imageMat;
        this.editedMat = editedMat;
        this.exposureDateTime = exposureDateTime;
        this.instanceUid = instanceUid;
        this.seriesUid = seriesUid;
        this.instanceId = instanceId;
        this.seriesId = seriesId;
        this.angleDrawWrapper=  angleDrawWrapper;
        this.distanceDrawList = distanceDrawList;
        this.textDrawList=textDrawList;
        this.cropLayoutWrapper=cropLayoutWrapper;
        this.leftRightDrawWrapper=leftRightDrawWrapper;
        this.studyInstanceUID=studyInstanceUID;


    }

    public boolean isCrop() {
        return isCrop;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    //getter and setter
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

        System.out.println("mat size : " + editedMat.size());
    }

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

    public Mat getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Mat thumbNail) {
        this.thumbNail = thumbNail;
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

    public ImagingParams getImagingParams() {
        return imagingParams;
    }

    public void setImagingParams(ImagingParams imagingParams) {
        this.imagingParams = imagingParams;
    }

    public XrayParams getXrayParams() {
        return xrayParams;
    }

    public void setXrayParams(XrayParams xrayParams) {
        this.xrayParams = xrayParams;
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


    public  List<AngleDrawWrapper> getAngleDrawList() {
        return angleDrawWrapper;
    }

    public void setAngleDrawList(List<AngleDrawWrapper> angleDrawWrapper) {
        this.angleDrawWrapper = angleDrawWrapper;
    }

    public List<DistanceDrawWrapper> getDistanceDrawList() {
        return distanceDrawList;
    }

    public void setDistanceDrawList(List<DistanceDrawWrapper> distanceDrawList) {
        this.distanceDrawList = distanceDrawList;
    }

    public List<TextDrawWrapper> getTextDrawList() {
        return textDrawList;
    }

    public void setTextDrawList(List<TextDrawWrapper> textDrawList) {
        this.textDrawList = textDrawList;
    }

    public CropLayoutWrapper getCropLayoutWrapper() {
        return cropLayoutWrapper;
    }

    public void setCropLayoutWrapper(CropLayoutWrapper cropLayoutWrapper) {
        this.cropLayoutWrapper = cropLayoutWrapper;
    }

    public LeftRightDrawWrapper getLeftRightDrawWrapper() {
        return leftRightDrawWrapper;
    }

    public void setLeftRightDrawWrapper(LeftRightDrawWrapper leftRightDrawWrapper) {
        this.leftRightDrawWrapper = leftRightDrawWrapper;
    }

    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    @Override
    public String toString() {
        return "PatientBodyPart{" +
                "isExposed=" + isExposed +
                ", isRejected=" + isRejected +
                ", bodyPartName='" + bodyPartName + '\'' +
                ", bodyPartPosition='" + bodyPartPosition + '\'' +
                ", thumbNail=" + thumbNail +
                ", imagingParams=" + imagingParams +
                ", xrayParams=" + xrayParams +
                ", imageMat=" + imageMat +
                ", editedMat=" + editedMat +
                ", exposureDateTime=" + exposureDateTime +
                ", instanceUid='" + instanceUid + '\'' +
                ", seriesUid='" + seriesUid + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", seriesId='" + seriesId + '\'' +
                ", studyInstanceUID='" + studyInstanceUID + '\'' +
                ", angleDrawWrapper=" + angleDrawWrapper +
                ", distanceDrawList=" + distanceDrawList +
                ", textDrawList=" + textDrawList +
                ", cropLayoutWrapper=" + cropLayoutWrapper +
                ", leftRightDrawWrapper=" + leftRightDrawWrapper +
                '}';
    }

    @Override
    public PatientBodyPart clone() {
        try {
            PatientBodyPart cloned = (PatientBodyPart) super.clone();

            // Deep clone OpenCV Mat objects
            cloned.thumbNail = this.thumbNail != null ? this.thumbNail.clone() : null;
            cloned.imageMat = this.imageMat != null ? this.imageMat.clone() : null;
            cloned.editedMat = this.editedMat != null ? this.editedMat.clone() : null;

            // Deep clone complex objects
            cloned.imagingParams = this.imagingParams != null ? this.imagingParams.clone() : null;
            cloned.xrayParams = this.xrayParams != null ? this.xrayParams.clone() : null;



            // Deep clone lists
            cloned.angleDrawWrapper = this.angleDrawWrapper != null ?
                    new ArrayList<>(this.angleDrawWrapper) : null;
            cloned.distanceDrawList = this.distanceDrawList != null ?
                    new ArrayList<>(this.distanceDrawList) : null;
            cloned.textDrawList = this.textDrawList != null ?
                    new ArrayList<>(this.textDrawList) : null;

            // Deep clone wrappers
            cloned.cropLayoutWrapper = this.cropLayoutWrapper != null ?
                    this.cropLayoutWrapper.clone() : null;
            cloned.leftRightDrawWrapper = this.leftRightDrawWrapper != null ?
                    this.leftRightDrawWrapper.clone() : null;




            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("PatientBodyPart cloning failed", e);
        }
    }


}
