package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleDraw implements IDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;

    private Rectangle rectangle = null;
    private boolean isDrawing = false;
    private boolean isDragging = false;
    private double startX, startY, currentX, currentY;
    private double offsetX, offsetY;

    public static boolean isComplete = true;

    public RectangleDraw(Canvas theCanvas, Point2D point){
        this.canvas = theCanvas;
        gc = canvas.getGraphicsContext2D();
        //handleClick(point);

        double x = point.getX();
        double y = point.getY();

        if (!isDrawing) {
            // First click - set starting point
            startX = x;
            startY = y;
            isDrawing = true;
        }

        isComplete = false;
    }

    @Override
    public void removeAll() {
        CanvasMainChange.removeRectangle(rectangle);
        rectangle = null;
        isComplete = true;
    }

    @Override
    public Boolean containsElement(Node element) {
        if(rectangle == element)return true;

        return false;
    }

    public void handleMousePressed(MouseEvent event) {

        double x = event.getX();
        double y = event.getY();

        if (rectangle != null && rectangle.contains(x, y)) {
            isDragging = true;
            offsetX = x - rectangle.getX();
            offsetY = y - rectangle.getY();
        } else {
            if (!isDrawing) {
                startX = x;
                startY = y;
                isDrawing = true;
            }
        }
    }

    public void handleMouseDragged(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (isDragging && rectangle != null) {
            rectangle.setX(x - offsetX);
            rectangle.setY(y - offsetY);
            drawRectangle();
        } else if (isDrawing) {
            currentX = x;
            currentY = y;
            drawRectangle();
        }
    }

    public void handleCanvasReleased(MouseEvent event) {

        if (isDrawing) {
            double x = event.getX();
            double y = event.getY();
            double rectX = Math.min(startX, x);
            double rectY = Math.min(startY, y);
            double rectWidth = Math.abs(startX - x);
            double rectHeight = Math.abs(startY - y);

            if(rectangle == null && startX != x && startY != y){
                rectangle = new Rectangle(rectX, rectY, rectWidth, rectHeight);
                CanvasMainChange.addRectangle(rectangle);
                drawRectangle();

            }


            isDrawing = false;
            isComplete = true;
        }
        isDragging = false;
    }


    public void drawRectangle() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        CanvasMainChange.redraw();

        if (rectangle != null) {
            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(4.0);
            gc.setFill(Color.TRANSPARENT);
            gc.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }
        else if (isDrawing) {
            double rectX = Math.min(startX, currentX);
            double rectY = Math.min(startY, currentY);
            double rectWidth = Math.abs(startX - currentX);
            double rectHeight = Math.abs(startY - currentY);

            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(4.0);






            gc.setFill(Color.TRANSPARENT);
            gc.fillRect(rectX, rectY, rectWidth, rectHeight);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
        }
    }

    public void selected(){
        if (rectangle != null) {
            if(rectangle.getX() !=0 || rectangle.getY() != 0 ) {
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(4.0);
                gc.setFill(Color.TRANSPARENT);
                gc.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            }
        }
    }


    public void hideAll(){
        CanvasMainChange.removeRectangle(rectangle);
    }


    public void unHideAll(){
        CanvasMainChange.addRectangle(rectangle);
    }



}
