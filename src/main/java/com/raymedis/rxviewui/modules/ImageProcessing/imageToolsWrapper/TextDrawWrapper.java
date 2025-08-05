package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;


public class TextDrawWrapper {

    private String text;
    private double x;
    private double y;

    public TextDrawWrapper() {
    }

    public TextDrawWrapper(String text, double x, double y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

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
        return "TextDrawWrapper{" +
                "text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
