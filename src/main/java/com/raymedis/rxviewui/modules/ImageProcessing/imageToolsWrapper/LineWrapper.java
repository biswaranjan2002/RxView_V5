package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

public class LineWrapper implements Cloneable{

    private double startX;
    private double startY;
    private double endX;
    private double endY;


    public LineWrapper() {
    }


    public LineWrapper(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    @Override
    public String toString() {
        return "LineWrapper{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }


    @Override
    public LineWrapper clone() {
        try {
            // Shallow copy is sufficient (all fields are primitives)
            return (LineWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("LineWrapper cloning failed", e);
        }
    }

}
