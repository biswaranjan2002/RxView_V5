package com.raymedis.rxviewui.modules.study.params;



public  class ImagingParams implements Cloneable {

    private double windowLevel;
    private double windowWidth;

    private double zoomFactor;
    private double angleFactor;

    private double imageLeft;
    private double imageTop;

    private boolean isHFlip;
    private boolean isVFlip;
    private boolean isInvert;


    private double denoise;
    private double sharpness;



    public ImagingParams() {
    }


    public ImagingParams(double windowLevel, double windowWidth, double zoomFactor, double angleFactor, double imageLeft, double imageTop, boolean isHFlip, boolean isVFlip, boolean isInvert, double denoise, double sharpness) {
        this.windowLevel = windowLevel;
        this.windowWidth = windowWidth;
        this.zoomFactor = zoomFactor;
        this.angleFactor = angleFactor;
        this.imageLeft = imageLeft;
        this.imageTop = imageTop;
        this.isHFlip = isHFlip;
        this.isVFlip = isVFlip;
        this.isInvert = isInvert;
        this.denoise = denoise;
        this.sharpness = sharpness;
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
        return "ImagingParams{" +
                "windowLevel=" + windowLevel +
                ", windowWidth=" + windowWidth +
                ", zoomFactor=" + zoomFactor +
                ", angleFactor=" + angleFactor +
                ", imageLeft=" + imageLeft +
                ", imageTop=" + imageTop +
                ", isHFlip=" + isHFlip +
                ", isVFlip=" + isVFlip +
                ", isInvert=" + isInvert +
                ", denoise=" + denoise +
                ", sharpness=" + sharpness +
                '}';
    }

    @Override
    public ImagingParams clone() {
        try {
            return (ImagingParams) super.clone(); // Shallow copy is sufficient (all fields are primitives)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("ImagingParams cloning failed", e);
        }
    }

}