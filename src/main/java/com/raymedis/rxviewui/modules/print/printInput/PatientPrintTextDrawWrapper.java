package com.raymedis.rxviewui.modules.print.printInput;

public class PatientPrintTextDrawWrapper {

    private String text;
    private double x;
    private double y;


    //constructors
    public PatientPrintTextDrawWrapper() {
    }

    public PatientPrintTextDrawWrapper(String text, double x, double y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }


    //setters and getters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "PatientPrintTextDrawWrapper{" +
                "text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
