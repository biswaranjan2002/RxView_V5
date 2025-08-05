package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

public class LeftRightDrawWrapper implements Cloneable{

    private double offsetX, offsetY;
    private  String text;

    public LeftRightDrawWrapper() {
    }

    public LeftRightDrawWrapper(double offsetX, double offsetY, String text) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.text = text;
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


    @Override
    public String toString() {
        return "LeftRightDrawWrapper{" +
                "offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public LeftRightDrawWrapper clone() {
        try {
            // Shallow copy is sufficient (all fields are primitives)
            return (LeftRightDrawWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("LeftRightDrawWrapper cloning failed", e);
        }
    }



}
