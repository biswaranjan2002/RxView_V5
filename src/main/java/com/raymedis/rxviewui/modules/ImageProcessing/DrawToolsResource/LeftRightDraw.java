package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LeftRightDraw implements IDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;

    private CustomFontIcon rlIcon;

    private boolean isDragging = false;
    private double offsetX, offsetY;

    private String text;

    public LeftRightDraw() {
    }


    public LeftRightDraw(String iconName, Canvas theCanvas){
        this.text = iconName;
        this.canvas = theCanvas;
        gc = canvas.getGraphicsContext2D();

        rlIcon = new CustomFontIcon(50, 50, Color.GREEN, canvas);
        rlIcon.setIconGetImage(iconName);

        CanvasMainChange.customFontIcon = rlIcon;
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
        CanvasMainChange.removeCustomFontIcon();
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

        CanvasMainChange.redraw();

        gc.save();

        // Calculate center of the icon for rotation pivot
        double x = rlIcon.getX();
        double y = rlIcon.getY();
        double width = rlIcon.getCurrentImage().getWidth();
        double height = rlIcon.getCurrentImage().getHeight();
        double rotationAngle = CanvasMainChange.mainDrawTool.canvasViewBox.getRotate(); // degrees

        double rt = rotationAngle/90;

        if(rt%2!=0){
            rotationAngle +=180;
        }

        // Translate to pivot, rotate, translate back
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(rotationAngle);
        gc.translate(-width / 2, -height / 2);

        // Draw image at (0, 0) since we've already translated
        gc.drawImage(rlIcon.getCurrentImage(), 0, 0);

    // Restore previous transform
        gc.restore();
    }


    public void hideAll(){
        CanvasMainChange.customFontIcon = null;
    }


    public void unHideAll(){
        CanvasMainChange.customFontIcon = rlIcon;
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

    public CustomFontIcon getRlIcon() {
        return rlIcon;
    }

    public void setRlIcon(CustomFontIcon rlIcon) {
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
