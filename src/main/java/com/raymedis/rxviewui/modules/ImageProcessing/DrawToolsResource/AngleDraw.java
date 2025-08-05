package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AngleDraw implements IDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;
    public ObservableList<Ellipse> ellipses = FXCollections.observableArrayList();
    public ObservableList<Line> lines = FXCollections.observableArrayList();
    public DegreeLabel degreeLabel;

    private int clickCount;


    private Ellipse selectedEllipse = null;
    private Point2D dragStart = null;

    public static boolean isComplete = true;

    public AngleDraw() {
    }

    public AngleDraw(Canvas theCanvas, Point2D point){
        this.canvas = theCanvas;
        this.clickCount = 0;
        gc = canvas.getGraphicsContext2D();
        handleClick(point);
        isComplete = false;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public static boolean isIsComplete() {
        return isComplete;
    }

    public static void setIsComplete(boolean isComplete) {
        AngleDraw.isComplete = isComplete;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public Ellipse getSelectedEllipse() {
        return selectedEllipse;
    }

    public void setSelectedEllipse(Ellipse selectedEllipse) {
        this.selectedEllipse = selectedEllipse;
    }

    public Point2D getDragStart() {
        return dragStart;
    }

    public void setDragStart(Point2D dragStart) {
        this.dragStart = dragStart;
    }

    public ObservableList<Ellipse> getEllipses() {
        return ellipses;
    }

    public void setEllipses(ObservableList<Ellipse> ellipses) {
        this.ellipses = ellipses;
    }

    public ObservableList<Line> getLines() {
        return lines;
    }

    public void setLines(ObservableList<Line> lines) {
        this.lines = lines;
    }

    public DegreeLabel getDegreeLabel() {
        return degreeLabel;
    }

    public void setDegreeLabel(DegreeLabel degreeLabel) {
        this.degreeLabel = degreeLabel;
    }


    //region canvas function



    public void handleMousePressed(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());


        for (Ellipse ellipse : ellipses) {
            if (isComplete && isPointInEllipse(clickPoint, ellipse)) {
                selectedEllipse = ellipse;
                dragStart = clickPoint;
                break;
            }
        }

        handleClick(clickPoint);
    }

    public void handleMouseDragged(MouseEvent event) {
        if (selectedEllipse != null) {
            Point2D dragPoint = new Point2D(event.getX(), event.getY());
            double dx = dragPoint.getX() - dragStart.getX();
            double dy = dragPoint.getY() - dragStart.getY();
            moveEllipse(selectedEllipse, dx, dy);
            dragStart = dragPoint;
        }
    }

    public void handleCanvasReleased(MouseEvent mouseEvent) {
        selectedEllipse = null;
    }

    //endregion


    //region arc and degree text

    private void drawArc(GraphicsContext gc, Ellipse e1, Ellipse e2, Ellipse e3) {
        double dx1 = e1.getCenterX();
        double dy1 = e1.getCenterY();
        double dx2 = e2.getCenterX();
        double dy2 = e2.getCenterY();
        double dx3 = e3.getCenterX();
        double dy3 = e3.getCenterY();

        // Calculate vector magnitudes
        double v1mod = Math.hypot(dx2 - dx1, dy2 - dy1);
        double v2mod = Math.hypot(dx3 - dx2, dy3 - dy2);

        // Determine the scaling factor
        double t = (v1mod > v2mod ? (v2mod / 8) : (v1mod / 8));
        double t1 = t / v1mod;
        double t2 = t / v2mod;

        // Calculate the start and end points of the arc
        double p1x1 = (1 - t1) * dx2 + t1 * dx1;
        double p1y1 = (1 - t1) * dy2 + t1 * dy1;

        double p2x1 = (1 - t2) * dx2 + t2 * dx3;
        double p2y1 = (1 - t2) * dy2 + t2 * dy3;

        // Calculate the radius from the center
        double radius = Math.hypot(p1x1 - dx2, p1y1 - dy2);

        // Calculate the angles
        double angleStart = Math.toDegrees(Math.atan2(p1y1 - dy2, p1x1 - dx2));
        double angleEnd = Math.toDegrees(Math.atan2(p2y1 - dy2, p2x1 - dx2));

        // Calculate the angle span
        double angleSpan = angleEnd - angleStart;

        // Ensure the angle span is the smallest angle
        if (Math.abs(angleSpan) > 180) {
            if (angleSpan > 0) {
                angleSpan -= 360;
            } else {
                angleSpan += 360;
            }
        }

        // Draw the arc
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);

        // Calculate the bounding box for the arc
        double arcX = dx2 - radius;
        double arcY = dy2 - radius;
        double arcWidth = 2 * radius;
        double arcHeight = 2 * radius;

        gc.strokeArc(
                arcX, arcY,           // Top-left corner of the bounding box
                arcWidth, arcHeight,  // Width and height of the bounding box
                angleStart,           // Starting angle
                angleSpan,            // Extent of the arc
                ArcType.OPEN  // Type of the arc
        );
    }

    public void showDegree(Ellipse e1, Ellipse e2, Ellipse e3) {
        double dx1 = e1.getCenterX();
        double dy1 = e1.getCenterY();
        double dx2 = e2.getCenterX();
        double dy2 = e2.getCenterY();
        double dx3 = e3.getCenterX();
        double dy3 = e3.getCenterY();

        // Vectors from e2
        double v1x = dx1 - dx2;
        double v1y = dy1 - dy2;
        double v2x = dx3 - dx2;
        double v2y = dy3 - dy2;

        // Calculate dot product and magnitudes
        double dotProduct = v1x * v2x + v1y * v2y;
        double v1mag = Math.hypot(v1x, v1y);
        double v2mag = Math.hypot(v2x, v2y);

        // Calculate angle in radians
        double angleRad = Math.acos(dotProduct / (v1mag * v2mag));

        // Convert angle to degrees
        double angleDeg = Math.toDegrees(angleRad);

        // Ensure the angle is the smallest angle
        if (angleDeg > 180) {
            angleDeg = 360 - angleDeg;
        }

        // Calculate the midpoint for displaying the text
        double t = Math.min(v1mag, v2mag) / 2.5;
        double t1 = t / v1mag;
        double t2 = t / v2mag;

        double p1x1 = (1 - t1) * dx2 + t1 * dx1;
        double p1y1 = (1 - t1) * dy2 + t1 * dy1;

        double p2x1 = (1 - t2) * dx2 + t2 * dx3;
        double p2y1 = (1 - t2) * dy2 + t2 * dy3;

        double px = (p1x1 + p2x1) / 2;
        double py = (p1y1 + p2y1) / 2;

        CanvasMainChange.removeDegreeLabel(degreeLabel);

        degreeLabel = new DegreeLabel(angleDeg, px, py, Color.rgb(255, 235, 42));

        CanvasMainChange.addDegreeLabel(degreeLabel);
    }


    //endregion




    private void handleClick(Point2D clickPoint) {
        if(clickCount < 3){
            addEllipse(clickPoint);
            switch (clickCount) {
                case 1 -> drawLine(ellipses.getFirst(), ellipses.get(clickCount));
                case 2 -> {
                    drawLine(ellipses.get(clickCount - 1), ellipses.get(clickCount));
                    isComplete = true;
                }
            }
            clickCount++;
        }
    }

    private void addEllipse(Point2D clickPoint) {
        Ellipse ellipse = drawEllipse(clickPoint, DynamicCanvasElementsResize.getEllipseRadiusX(), DynamicCanvasElementsResize.getEllipseRadiusY());
    }

    private Ellipse drawEllipse(Point2D center, double radiusX, double radiusY) {
        Ellipse ellipse = new Ellipse(center.getX(), center.getY(), radiusX, radiusY);
        ellipse.setFill(Color.BLUE);
        ellipses.add(ellipse);
        CanvasMainChange.addEllipse(ellipse);
        redraw();
        return ellipse;
    }


    private void updateLines() {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            Ellipse startEllipse = ellipses.get(i);
            Ellipse endEllipse = ellipses.get(i + 1);
            line.setStartX(startEllipse.getCenterX());
            line.setStartY(startEllipse.getCenterY());
            line.setEndX(endEllipse.getCenterX());
            line.setEndY(endEllipse.getCenterY());
        }
    }

    private void drawLine(Ellipse start, Ellipse end) {
        Line line = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
        line.setStroke(Color.rgb(255, 235, 42));
        line.setStrokeWidth(DynamicCanvasElementsResize.getLineWidth());
        lines.add(line);
        CanvasMainChange.addLine(line);
        redraw();
    }

    private boolean isPointInEllipse(Point2D point, Ellipse ellipse) {
        double dx = point.getX() - ellipse.getCenterX();
        double dy = point.getY() - ellipse.getCenterY();
        return (dx * dx) / (ellipse.getRadiusX() * ellipse.getRadiusX()) +
                (dy * dy) / (ellipse.getRadiusY() * ellipse.getRadiusY()) <= 1;
    }

    private void moveEllipse(Ellipse ellipse, double dx, double dy) {
        double newCenterX = ellipse.getCenterX() + dx;
        double newCenterY = ellipse.getCenterY() + dy;

        double newRadiusX = ellipse.getRadiusX();
        double newRadiusY = ellipse.getRadiusY();

        // Calculate the new bounding box of the ellipse
        double left = newCenterX - newRadiusX;
        double right = newCenterX + newRadiusX;
        double top = newCenterY - newRadiusY;
        double bottom = newCenterY + newRadiusY;

        // Check if the ellipse fits within the canvas bounds
        boolean isWithinBounds = left >= 0 &&
                right <= canvas.getWidth() &&
                top >= 0 &&
                bottom <= canvas.getHeight();

        if (isWithinBounds) {
            ellipse.setCenterX(newCenterX);
            ellipse.setCenterY(newCenterY);
            updateLines();
            redraw();
        }
    }

    public void redraw() {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        CanvasMainChange.redraw();


        if(ellipses.size() == 3){
            showDegree(ellipses.get(0), ellipses.get(1), ellipses.get(2));
            CanvasMainChange.redraw();

            for (Line line : lines) {
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(DynamicCanvasElementsResize.getLineWidth());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
            }

            for (Ellipse ellipse : ellipses) {
                gc.setFill(ellipse.getFill());
                gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            }

            double angleDeg = degreeLabel.getAngle();
            double px = degreeLabel.getX();
            double py = degreeLabel.getY();
            Color color = degreeLabel.getColor();

            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.valueOf("#66FF00"));
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());

        }
        else{
            for (Line line : lines) {
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(DynamicCanvasElementsResize.getLineWidth());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
            }

            for (Ellipse ellipse : ellipses) {
                gc.setFill(ellipse.getFill());
                gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            }
        }
    }

    public void selected(){
        for (Line line : lines) {
            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(4.0);  // Set line thickness here
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        }

        for (Ellipse ellipse : ellipses) {
            gc.setFill(ellipse.getFill());
            gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
        }

        if(ellipses.size() == 3){
            //drawArc(gc, ellipses.get(0), ellipses.get(1), ellipses.get(2));
            showDegree(ellipses.get(0), ellipses.get(1), ellipses.get(2));

            //CanvasMainChange.redraw();

            double angleDeg = degreeLabel.getAngle();
            double px = degreeLabel.getX();
            double py = degreeLabel.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.valueOf("#66FF00"));
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());
        }
    }



    @Override
    public void removeAll() {
        CanvasMainChange.removeAllEllipse(ellipses);
        ellipses.clear();
        CanvasMainChange.removeAllLines(lines);
        lines.clear();
        CanvasMainChange.removeDegreeLabel(degreeLabel);
        redraw();
        isComplete = true;
    }



    @Override
    public Boolean containsElement(Node element) {
        for(Ellipse ellipse : ellipses){
            if(ellipse == element)return true;
        }
        for(Line line : lines){
            if(line == element)return true;
        }

        return false;
    }

    public void hideAll(){
        CanvasMainChange.removeAllEllipse(ellipses);
        CanvasMainChange.removeAllLines(lines);
        CanvasMainChange.removeDegreeLabel(degreeLabel);
    }


    public void unHideAll(){
        for(Ellipse ellipse : ellipses){
            CanvasMainChange.addEllipse(ellipse);
        }

        for(Line line : lines){
            CanvasMainChange.addLine(line);
        }

        CanvasMainChange.addDegreeLabel(degreeLabel);
    }



}

