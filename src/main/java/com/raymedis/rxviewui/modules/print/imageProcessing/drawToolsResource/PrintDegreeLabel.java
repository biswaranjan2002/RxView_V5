package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import javafx.scene.paint.Color;

public class PrintDegreeLabel {

    private double angle;
    private double x;
    private double y;
    private Color color;


    public PrintDegreeLabel(double angle, double x, double y, Color color) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.color = color;

    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}

