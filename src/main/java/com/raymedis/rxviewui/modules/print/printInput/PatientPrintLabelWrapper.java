package com.raymedis.rxviewui.modules.print.printInput;

public class PatientPrintLabelWrapper {

    private double value;
    private double x;
    private double y;


    //constructors
    public PatientPrintLabelWrapper() {
    }

    public PatientPrintLabelWrapper(double value, double x, double y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    //setters and getters
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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
        return "PatientPrintLabelWrapper{" +
                "value=" + value +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
