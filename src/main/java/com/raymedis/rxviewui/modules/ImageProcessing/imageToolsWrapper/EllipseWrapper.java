package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

public class EllipseWrapper implements Cloneable {


    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;

    public EllipseWrapper() {
    }


    public EllipseWrapper(double centerX, double centerY, double radiusX, double radiusY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(double radiusX) {
        this.radiusX = radiusX;
    }

    public double getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(double radiusY) {
        this.radiusY = radiusY;
    }


    @Override
    public String toString() {
        return "EllipseWrapper{" +
                "centerX=" + centerX +
                ", centerY=" + centerY +
                ", radiusX=" + radiusX +
                ", radiusY=" + radiusY +
                '}';
    }

    @Override
    public EllipseWrapper clone() {
        try {
            // Shallow copy is sufficient (all fields are primitives)
            return (EllipseWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("EllipseWrapper cloning failed", e);
        }
    }


}
