package com.raymedis.rxviewui.modules.print.imageProcessing;

public class PrintDynamicCanvasElementsResize {

    private double lineWidth;

    private double ellipseRadiusX;
    private double ellipseRadiusY;

    private double labelFontSize;
    private double rectWidth;
    private double rectHeight;

    private double customFontIconSize;

    private double multiple;

    public PrintDynamicCanvasElementsResize(){

    }


    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public double getEllipseRadiusX() {
        return ellipseRadiusX;
    }

    public void setEllipseRadiusX(double ellipseRadiusX) {
        this.ellipseRadiusX = ellipseRadiusX;
    }

    public double getEllipseRadiusY() {
        return ellipseRadiusY;
    }

    public void setEllipseRadiusY(double ellipseRadiusY) {
        this.ellipseRadiusY = ellipseRadiusY;
    }

    public double getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(double labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public double getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(double rectWidth) {
        this.rectWidth = rectWidth;
    }

    public double getRectHeight() {
        return rectHeight;
    }

    public void setRectHeight(double rectHeight) {
        this.rectHeight = rectHeight;
    }

    public double getCustomFontIconSize() {
        return customFontIconSize;
    }

    public void setCustomFontIconSize(double customFontIconSize) {
        this.customFontIconSize = customFontIconSize;
    }

    public double getMultiple() {
        return multiple;
    }

    public void setMultiple(double multiple) {
        this.multiple = multiple;
    }


    @Override
    public String toString() {
        return "PrintDynamicCanvasElementsResize{" +
                "lineWidth=" + lineWidth +
                ", ellipseRadiusX=" + ellipseRadiusX +
                ", ellipseRadiusY=" + ellipseRadiusY +
                ", labelFontSize=" + labelFontSize +
                ", labelRectWidth=" + rectWidth +
                ", labelRectHeight=" + rectHeight +
                ", customFontIconSize=" + customFontIconSize +
                ", multiple=" + multiple +
                '}';
    }
}
