package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PrintLeftRightDraw implements PrintInterfaceDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;

    private PrintCustomFontIcon rlIcon;

    private boolean isDragging = false;
    private double offsetX, offsetY;

    private String text;

    private PrintCanvasMainChange printCanvasMainChange;

    public PrintLeftRightDraw() {
    }



    public PrintLeftRightDraw(String iconName, Canvas theCanvas,PrintCanvasMainChange printCanvasMainChange){
        this.text = iconName;
        this.canvas = theCanvas;
        this.gc = canvas.getGraphicsContext2D();
        this.printCanvasMainChange=printCanvasMainChange;

        rlIcon = new PrintCustomFontIcon(50, 50, Color.GREEN, canvas,printCanvasMainChange.printDynamicCanvasElementsResize);
        rlIcon.setIconGetImage(iconName);

        printCanvasMainChange.setCustomFontIcon(rlIcon);
        redraw();
    }

    public void setIcon(String iconName){
        text=iconName;
        rlIcon.setIconGetImage(iconName);
    }

    public void reset(){
        rlIcon = null;
    }

    @Override
    public void removeAll() {
        rlIcon = null;
        printCanvasMainChange.removeCustomFontIcon();
    }

    @Override
    public Boolean containsElement(Node element) {
        return null;
    }

    public void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (rlIcon != null && rlIcon.contains(x, y)) {
            isDragging = true;
            offsetX = x - rlIcon.getX();
            offsetY = y - rlIcon.getY();
        }
    }

    public void handleMouseDragged(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (isDragging && rlIcon != null) {
            rlIcon.setX(x - offsetX);
            rlIcon.setY(y - offsetY);
            redraw();
        }
    }

    public void handleCanvasReleased(MouseEvent event) {
        isDragging = false;
    }

    public void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        printCanvasMainChange.redraw();
        gc.drawImage(rlIcon.getCurrentImage(), rlIcon.getX(), rlIcon.getY());
    }


    public void hideAll(PrintCanvasMainChange printCanvasMainChange){
        this.printCanvasMainChange=printCanvasMainChange;
        printCanvasMainChange.setCustomFontIcon(null) ;
    }


    public void unHideAll(){
        printCanvasMainChange.setCustomFontIcon(rlIcon);
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public PrintCustomFontIcon getRlIcon() {
        return rlIcon;
    }

    public void setRlIcon(PrintCustomFontIcon rlIcon) {
        this.rlIcon = rlIcon;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
