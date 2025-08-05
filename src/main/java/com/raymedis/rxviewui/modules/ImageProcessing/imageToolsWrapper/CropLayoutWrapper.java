package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

public class CropLayoutWrapper implements Cloneable {

    private double rectX, rectY, rectWidth, rectHeight;

    public CropLayoutWrapper() {
    }

    public CropLayoutWrapper(double rectX, double rectY, double rectWidth, double rectHeight) {
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
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

    @Override
    public String toString() {
        return "CropLayoutWrapper{" +
                "rectX=" + rectX +
                ", rectY=" + rectY +
                ", rectWidth=" + rectWidth +
                ", rectHeight=" + rectHeight +
                '}';
    }

    @Override
    public CropLayoutWrapper clone() {
        try {
            return (CropLayoutWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("LeftRightDrawWrapper cloning failed", e);
        }
    }

}
