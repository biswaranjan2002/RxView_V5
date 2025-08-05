package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class PrintEllipseDraw implements PrintInterfaceDrawTools{
    private Canvas canvas;
    private GraphicsContext gc;

    private Ellipse ellipse = null;
    private boolean isDrawing = false;
    private boolean isDragging = false;
    private double startX, startY, currentX, currentY;
    private double offsetX, offsetY;

    private PrintCanvasMainChange printCanvasMainChange;


    public PrintEllipseDraw(Canvas theCanvas, Point2D point,PrintCanvasMainChange printCanvasMainChange){
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
        this.printCanvasMainChange = printCanvasMainChange;
        printCanvasMainChange.isEllipseDrawComplete = false;
    }

    @Override
    public void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (ellipse != null) {
            if(ellipse.contains(x,y)){
                isDragging = true;
                offsetX = x - ellipse.getCenterX();
                offsetY = y - ellipse.getCenterY();
            }

        } else {
            if (!isDrawing) {
                startX = x;
                startY = y;
                isDrawing = true;
            }
        }
    }

    private boolean isPointInEllipse(Point2D point, Ellipse ellipse) {
        double dx = point.getX() - ellipse.getCenterX();
        double dy = point.getY() - ellipse.getCenterY();
        return (dx * dx) / (ellipse.getRadiusX() * ellipse.getRadiusX()) +
                (dy * dy) / (ellipse.getRadiusY() * ellipse.getRadiusY()) <= 1;
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (isDragging && ellipse != null) {
            ellipse.setCenterX(x - offsetX);
            ellipse.setCenterY(y - offsetY);
            drawEllipse();
        } else if (isDrawing) {
            currentX = x;
            currentY = y;
            drawEllipse();
        }
    }

    @Override
    public void handleCanvasReleased(MouseEvent event) {
        if (isDrawing) {
            double x = event.getX();
            double y = event.getY();
            double centerX = (startX + x) / 2;
            double centerY = (startY + y) / 2;
            double radiusX = Math.abs(startX - x) / 2;
            double radiusY = Math.abs(startY - y) / 2;

            if(ellipse == null && startX != x && startY != y){
                ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
                printCanvasMainChange.addEllipseDraw(ellipse);
                drawEllipse();

            }
            isDrawing = false;
            printCanvasMainChange.isEllipseDrawComplete = true;
        }
        isDragging = false;
    }

    public void drawEllipse() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        printCanvasMainChange.redraw();

        if (ellipse != null) {
            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(4.0);
            gc.setFill(Color.TRANSPARENT);
            gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            gc.strokeOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
        }

        else if (isDrawing) {
            double centerX = (startX + currentX) / 2;
            double centerY = (startY + currentY) / 2;
            double radiusX = Math.abs(startX - currentX) / 2;
            double radiusY = Math.abs(startY - currentY) / 2;

            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(4.0);
            gc.setFill(Color.TRANSPARENT);
            gc.fillOval(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);
            gc.strokeOval(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);

        }
    }

    public void selected(){
        if (ellipse != null) {
            if(ellipse.getRadiusX() !=0 || ellipse.getRadiusY() != 0 ) {
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(4.0);
                gc.setFill(Color.TRANSPARENT);
                gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
                gc.strokeOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            }
        }
    }


    @Override
    public void removeAll() {
        printCanvasMainChange.removeEllipseDraw(ellipse);
        ellipse = null;
        printCanvasMainChange.isEllipseDrawComplete = true;
    }

    @Override
    public Boolean containsElement(Node element) {
        if(ellipse == element)return true;
        return false;
    }

    public void hideAll(PrintCanvasMainChange printCanvasMainChange){
        this.printCanvasMainChange= printCanvasMainChange;
        printCanvasMainChange.removeEllipseDraw(ellipse);
    }


    public void unHideAll(){
        printCanvasMainChange.addEllipseDraw(ellipse);
    }


}
