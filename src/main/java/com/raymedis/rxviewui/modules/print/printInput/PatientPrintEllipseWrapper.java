package com.raymedis.rxviewui.modules.print.printInput;

public class PatientPrintEllipseWrapper {

    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;


    //constructors
    public PatientPrintEllipseWrapper() {
    }

    public PatientPrintEllipseWrapper(double centerX, double centerY, double radiusX, double radiusY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }


    //setters and getters
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
        return "PatientPrintEllipseWrapper{" +
                "centerX=" + centerX +
                ", centerY=" + centerY +
                ", radiusX=" + radiusX +
                ", radiusY=" + radiusY +
                '}';
    }
}
