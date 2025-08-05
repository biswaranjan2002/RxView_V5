package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import javafx.scene.paint.Color;

public class PrintDistanceLabel {

    private double distance;
    private double x;
    private double y;
    private Color color;

    public PrintDistanceLabel(double distance, double x, double y, Color color) {
        this.distance = distance;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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
