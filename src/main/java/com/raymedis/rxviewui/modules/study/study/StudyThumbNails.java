package com.raymedis.rxviewui.modules.study.study;

import org.opencv.core.Mat;

import java.util.Map;

public class StudyThumbNails {

    private String id;
    private String bodyPartName;
    private Mat thumbnail;


    public StudyThumbNails(String id, String bodyPartName, Mat thumbnail) {
        this.id = id;
        this.bodyPartName = bodyPartName;
        this.thumbnail = thumbnail;
    }

    public StudyThumbNails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public Mat getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Mat thumbnail) {
        this.thumbnail = thumbnail;
    }
}
