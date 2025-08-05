package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

public class LabelWrapper implements Cloneable{

    private double value;
    private double x;
    private double y;


    public LabelWrapper() {
    }

    public LabelWrapper(double value, double x, double y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

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
        return "LabelWrapper{" +
                "value=" + value +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public LabelWrapper clone() {
        try {
            // Shallow copy is sufficient (all fields are primitives)
            return (LabelWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("LabelWrapper cloning failed", e);
        }
    }

}
