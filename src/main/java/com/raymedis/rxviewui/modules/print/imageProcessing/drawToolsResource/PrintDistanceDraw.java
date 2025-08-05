package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;


import com.raymedis.rxviewui.controller.printModule.Print;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PrintDistanceDraw implements PrintInterfaceDrawTools{


    private Canvas canvas;
    private GraphicsContext gc;
    private ObservableList<Ellipse> ellipses = FXCollections.observableArrayList();
    private Line line;

    private PrintDistanceLabel distanceLabel;

    private int clickCount;


    private Ellipse selectedEllipse = null;
    private Point2D dragStart = null;


    private PrintCanvasMainChange printCanvasMainChange;


    public PrintDistanceDraw() {
    }

    public PrintDistanceDraw(Canvas theCanvas, Point2D point,PrintCanvasMainChange printCanvasMainChange){
        this.canvas = theCanvas;
        this.clickCount = 0;
        this.printCanvasMainChange=printCanvasMainChange;
        printCanvasMainChange.isDistanceDrawComplete =false;
        gc = canvas.getGraphicsContext2D();
        handleClick(point);
        printCanvasMainChange.isDistanceDrawComplete = false;
    }

    //region canvas function

    public double showLength(Ellipse ellipse1, Ellipse ellipse2) {
        // Get the center coordinates of the ellipses
        double lx1 = ellipse1.getCenterX();
        double ly1 = ellipse1.getCenterY();
        double lx2 = ellipse2.getCenterX();
        double ly2 = ellipse2.getCenterY();


        double imageWidthInPixels = Math.max(printCanvasMainChange.getMainImage().getWidth(), printCanvasMainChange.getMainImage().getHeight()); // Replace with your image's actual width in pixels
        double imageWidthInInches = 17; // Replace with your image's actual width in inches

        double dpi = imageWidthInPixels / imageWidthInInches;

        // Calculate the length and convert to millimeters
        double length = ((25.4) * (Math.sqrt(Math.pow(lx2 - lx1, 2) + Math.pow(ly2 - ly1, 2)) / dpi))/ printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple();

        printCanvasMainChange.removeDistanceLabel(distanceLabel);

        distanceLabel = new PrintDistanceLabel(length, (lx1 + lx2) / 2 - 20, (ly1 + ly2) / 2 + 15, Color.rgb(255, 235, 42));


        printCanvasMainChange.addDistanceLabel(distanceLabel);
        return length;
    }

    public void handleMousePressed(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());


        for (Ellipse ellipse : ellipses) {
            if (printCanvasMainChange.isDistanceDrawComplete && isPointInEllipse(clickPoint, ellipse)) {
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


    private void handleClick(Point2D clickPoint) {

        if(clickCount < 2){
            addEllipse(clickPoint);
            if(clickCount == 0){

            }
            else if(clickCount == 1){
                drawLine(ellipses.getFirst(), ellipses.get(clickCount));
                printCanvasMainChange.isDistanceDrawComplete = true;
            }
            clickCount++;
        }
    }

    private void addEllipse(Point2D clickPoint) {
        Ellipse ellipse = drawEllipse(clickPoint, printCanvasMainChange.printDynamicCanvasElementsResize.getEllipseRadiusX(), printCanvasMainChange.printDynamicCanvasElementsResize.getEllipseRadiusY(), Color.BLUE);
    }

    private Ellipse drawEllipse(Point2D center, double radiusX, double radiusY, Color color) {
        Ellipse ellipse = new Ellipse(center.getX(), center.getY(), radiusX, radiusY);
        ellipse.setFill(color);
        ellipses.add(ellipse);
        printCanvasMainChange.addEllipse(ellipse);
        redraw(printCanvasMainChange);
        return ellipse;
    }

    private void updateLines() {
        Ellipse startEllipse = ellipses.get(0);
        Ellipse endEllipse = ellipses.get(1);
        line.setStartX(startEllipse.getCenterX());
        line.setStartY(startEllipse.getCenterY());
        line.setEndX(endEllipse.getCenterX());
        line.setEndY(endEllipse.getCenterY());
    }

    private void drawLine(Ellipse start, Ellipse end) {
        line = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(2.0);
        printCanvasMainChange.addLine(line);
        redraw(printCanvasMainChange);
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
            redraw(printCanvasMainChange);
        }
    }

    public void redraw(PrintCanvasMainChange printCanvasMainChange) {

        this.printCanvasMainChange = printCanvasMainChange;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        printCanvasMainChange.redraw();


        if(ellipses.size() == 2){
            //distance text display
            showLength(ellipses.get(0), ellipses.get(1));
            printCanvasMainChange.redraw();

            if(line != null){
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(printCanvasMainChange.printDynamicCanvasElementsResize.getLineWidth());  // Set line thickness here
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
            }

            for (Ellipse ellipse : ellipses) {
                gc.setFill(ellipse.getFill());
                gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            }

            double distanceInMm = distanceLabel.getDistance();
            double px = distanceLabel.getX();
            double py = distanceLabel.getY();
            Color color = distanceLabel.getColor();

           /* gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f mm", distanceInMm), px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());*/


            gc.setFill(color);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f mm", distanceInMm), px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());
        }
        else{
            if(line != null){
                gc.setStroke(Color.valueOf("#66FF00"));
                gc.setLineWidth(printCanvasMainChange.printDynamicCanvasElementsResize.getLineWidth());  // Set line thickness here
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
            }

            for (Ellipse ellipse : ellipses) {
                gc.setFill(ellipse.getFill());
                gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
            }
        }

    }

    public void selected(PrintCanvasMainChange printCanvasMainChange){
        this.printCanvasMainChange = printCanvasMainChange;
        if(line != null){
            gc.setStroke(Color.valueOf("#66FF00"));
            gc.setLineWidth(printCanvasMainChange.printDynamicCanvasElementsResize.getLineWidth());  // Set line thickness here
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        }

        for (Ellipse ellipse : ellipses) {
            gc.setFill(ellipse.getFill());
            gc.fillOval(ellipse.getCenterX() - ellipse.getRadiusX(), ellipse.getCenterY() - ellipse.getRadiusY(), ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2);
        }

        if(ellipses.size() == 2){
            //distance text display
            showLength(ellipses.get(0), ellipses.get(1));

            double distanceInMm = distanceLabel.getDistance();
            double px = distanceLabel.getX();
            double py = distanceLabel.getY();
            Color color = distanceLabel.getColor();

          /*  gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f mm", distanceInMm), px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());*/


            gc.setFill(color);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f mm", distanceInMm), px, py+ 2*printCanvasMainChange.printDynamicCanvasElementsResize.getLabelFontSize());
        }
    }

    @Override
    public void removeAll() {
        printCanvasMainChange.removeAllEllipse(ellipses);
        ellipses.clear();
        printCanvasMainChange.removeLine(line);
        printCanvasMainChange.removeDistanceLabel(distanceLabel);
        redraw(printCanvasMainChange);
        printCanvasMainChange.isDistanceDrawComplete = true;
    }

    @Override
    public Boolean containsElement(Node element) {
        for(Ellipse ellipse : ellipses){
            if(ellipse == element)return true;
        }
        if(line == element)return true;
        return false;
    }

    public void hideAll(PrintCanvasMainChange printCanvasMainChange){
        this.printCanvasMainChange= printCanvasMainChange;
        printCanvasMainChange.removeAllEllipse(ellipses);
        printCanvasMainChange.removeLine(line);
        printCanvasMainChange.removeDistanceLabel(distanceLabel);
    }


    public void unHideAll(){
        for(Ellipse ellipse : ellipses){
            printCanvasMainChange.addEllipse(ellipse);
        }
        printCanvasMainChange.addLine(line);
        printCanvasMainChange.addDistanceLabel(distanceLabel);
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

    public ObservableList<Ellipse> getEllipses() {
        return ellipses;
    }

    public void setEllipses(ObservableList<Ellipse> ellipses) {
        this.ellipses = ellipses;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public PrintDistanceLabel getPrintDistanceLabel() {
        return distanceLabel;
    }

    public void setPrintDistanceLabel(PrintDistanceLabel distanceLabel) {
        this.distanceLabel = distanceLabel;
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

}
