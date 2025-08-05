package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CanvasMainChange {

    private static final Logger logger = LoggerFactory.getLogger(CanvasMainChange.class);

    public static Canvas drawingCanvas;

    public static Pane overlayPane;

    public static ObservableList<Ellipse> ellipses = FXCollections.observableArrayList();

    public static ObservableList<Line> lines = FXCollections.observableArrayList();

    public static ObservableList<Rectangle> rectangles = FXCollections.observableArrayList();

    public static ObservableList<Ellipse> ellipseDrawEllipses = FXCollections.observableArrayList();

    public static ObservableList<DegreeLabel> degreeLabels = FXCollections.observableArrayList();

    public static ObservableList<DistanceLabel> distanceLabels = FXCollections.observableArrayList();

    public static ObservableList<TextLabel> textLabels = FXCollections.observableArrayList();

    public static CustomFontIcon customFontIcon;

    public static Image mainImage = null;

    public static Image displayImage = null;

    public static MainDrawTool mainDrawTool;

    public static ImageTools imageTools;



    public static void clearCanvas(){
        ellipses.clear();
        lines.clear();
        rectangles.clear();
        ellipseDrawEllipses.clear();
        degreeLabels.clear();
        distanceLabels.clear();
        textLabels.clear();
        customFontIcon = null;

        mainImage = null;
        displayImage = null;
        redraw();
    }

    public static void disableImageToolsForCrop(boolean b){
        imageTools.resetUiForCrop(b);
    }

    public static void disableImageTools(){
        MainDrawTool.isImageToolEnable = false;
        CanvasMainChange.clearCanvas();

        mainDrawTool.reset();
        mainDrawTool.isHFlip = false;
        mainDrawTool.isVFlip = false;
        mainDrawTool.isInvert = false;
        mainDrawTool.isCrop = false;

        mainDrawTool.brightnessBefore =0;
        mainDrawTool.contrastBefore=1;
        mainDrawTool.imageLeftBefore = 0;
        mainDrawTool.imageTopBefore=0;
        mainDrawTool.zoomFactorBefore = 1;

        mainDrawTool.brightness = 0;
        mainDrawTool.contrast =1;
        mainDrawTool.zoomFactor = 1;
        mainDrawTool.angleFactor = 0;

        mainDrawTool.imageLeft = 0;
        mainDrawTool.imageTop = 0;

        mainDrawTool.canvasViewBox.setRotate(0);

        imageTools.resetUi();
    }


    public static void init(Pane textPane){
        overlayPane = textPane;
        setTextMouse();
    }

    public static void setTextMouse(){
        overlayPane.setMouseTransparent(true);
        //System.out.println("Mouse set true");
    }

    public static void removeTextMouse(){
        overlayPane.setMouseTransparent(false);
        //System.out.println("Mouse set false");
    }

    public static Mat croppedMat = new Mat();
    public static void redraw() {

        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());


        if(displayImage != null ) {

            Rect cropRect = new Rect((int) ((CropLayout.rectX)), (int) (CropLayout.rectY), (int) (CropLayout.rectWidth), (int) (CropLayout.rectHeight));

           /* logger.info("cropRect : {}", cropRect);*/

            if(cropRect.width> displayImage.getWidth()){
                cropRect.width= (int) displayImage.getWidth();

            }
            if(cropRect.height > displayImage.getHeight()){
                cropRect.height = (int) displayImage.getHeight();
            }

            /*logger.info("after cropRect : {}", cropRect);

             logger.info("displayImage : {}", displayImage.getWidth() + "x" + displayImage.getHeight());*/


            croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(displayImage), cropRect);

            //Imgcodecs.imwrite("D:\\Work\\Temp\\croppedImage.png", croppedMat);

            Imgproc.resize(croppedMat, croppedMat, new Size(drawingCanvas.getWidth(), drawingCanvas.getHeight()));

            gc.drawImage(ImageConversionService.getInstance().matToImage(croppedMat), mainDrawTool.imageLeft, mainDrawTool.imageTop);

        }

        for (Line line : lines) {
            gc.setLineDashes();
            gc.setStroke(Color.ORANGERED);
            gc.setLineWidth(DynamicCanvasElementsResize.getLineWidth());
            gc.strokeLine(DynamicCanvasElementsResize.getMultiple()*line.getStartX(),DynamicCanvasElementsResize.getMultiple()*line.getStartY(), DynamicCanvasElementsResize.getMultiple()*line.getEndX(), DynamicCanvasElementsResize.getMultiple()*line.getEndY());

        }

        for (Ellipse ellipse : ellipses) {
            gc.setFill(Color.ORANGERED);
            gc.fillOval(DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterX() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX(),
                    DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterY() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY(),
                    DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX() * 2,
                    DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY() * 2);

        }

        for(Rectangle rectangle : rectangles){
            if (rectangle != null) {
                if(rectangle.getX() !=0 || rectangle.getY() != 0 ){
                    gc.setStroke(Color.ORANGERED);
                    gc.setLineWidth(4.0);
                    gc.setFill(Color.TRANSPARENT);
                    gc.fillRect(DynamicCanvasElementsResize.getMultiple()*rectangle.getX(), DynamicCanvasElementsResize.getMultiple()*rectangle.getY(), DynamicCanvasElementsResize.getMultiple()*rectangle.getWidth(), DynamicCanvasElementsResize.getMultiple()*rectangle.getHeight());
                    gc.strokeRect(DynamicCanvasElementsResize.getMultiple()*rectangle.getX(), DynamicCanvasElementsResize.getMultiple()*rectangle.getY(), DynamicCanvasElementsResize.getMultiple()*rectangle.getWidth(), DynamicCanvasElementsResize.getMultiple()*rectangle.getHeight());
                }else{
                    rectangles.remove(rectangle);
                }
            }
        }

        for (Ellipse ellipse : ellipseDrawEllipses) {
            if (ellipse != null) {
                if(ellipse.getRadiusX() !=0 || ellipse.getRadiusY() != 0 ){
                    gc.setStroke(Color.ORANGERED);
                    gc.setLineWidth(4.0);
                    gc.setFill(Color.TRANSPARENT);
                    gc.fillOval(DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterX() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX(), DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterY() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY(), DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX() * 2, DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY() * 2);
                    gc.strokeOval(DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterX() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX(), DynamicCanvasElementsResize.getMultiple()*ellipse.getCenterY() - DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY(), DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusX() * 2, DynamicCanvasElementsResize.getMultiple()*ellipse.getRadiusY() * 2);
                }
                else{
                    ellipseDrawEllipses.remove(ellipse);
                }
            }
        }

        for (DegreeLabel label : degreeLabels) {
            double angleDeg = label.getAngle();
            double px = DynamicCanvasElementsResize.getMultiple()*label.getX();
            double py = DynamicCanvasElementsResize.getMultiple()*label.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());

        }

        for (DistanceLabel label : distanceLabels) {
            double distanceInmm = label.getDistance();
            double px = DynamicCanvasElementsResize.getMultiple()*label.getX();
            double py = DynamicCanvasElementsResize.getMultiple()*label.getY();

            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(String.format("%.2f mm", distanceInmm), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(String.format("%.2f mm", distanceInmm), px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


        }

        for(TextLabel textLabel : textLabels){
            String text = textLabel.getText();
            double px = DynamicCanvasElementsResize.getMultiple()*textLabel.getX();
            double py = DynamicCanvasElementsResize.getMultiple()*textLabel.getY();


            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.strokeText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());


            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * DynamicCanvasElementsResize.getLabelFontSize()));
            gc.fillText(text, px, py+ 2*DynamicCanvasElementsResize.getLabelFontSize());

        }

        if(customFontIcon != null){
            gc.save();

            // Calculate center of the icon for rotation pivot
            double x = DynamicCanvasElementsResize.getMultiple()*customFontIcon.getX();
            double y = DynamicCanvasElementsResize.getMultiple()*customFontIcon.getY();
            double width = customFontIcon.getCurrentImage().getWidth()/DynamicCanvasElementsResize.getMultiple();
            double height = customFontIcon.getCurrentImage().getHeight()/DynamicCanvasElementsResize.getMultiple();
            double rotationAngle = mainDrawTool.canvasViewBox.getRotate(); // degrees

            double rt = rotationAngle/90;

            if(rt%2!=0){
                rotationAngle +=180;
            }

            //System.out.println(rotationAngle);

            // Translate to pivot, rotate, translate back
            gc.translate(x + width / 2, y + height / 2);
            gc.rotate(rotationAngle);
            gc.translate(-width / 2, -height / 2);

            // Draw image at (0, 0) since we've already translated
            gc.drawImage(customFontIcon.getCurrentImage(), 0, 0);

            // Restore previous transform
            gc.restore();
        }


        //logger.info("multiple : {} + \n\n\n",  DynamicCanvasElementsResize.getMultiple());

    }

    public static void addEllipse(Ellipse ellipse){
        if(!ellipses.contains(ellipse)){
            ellipses.add(ellipse);
        }

    }

    public static void addLine(Line line){
        lines.add(line);
    }

    public static void addRectangle(Rectangle rectangle){
        if(!rectangles.contains(rectangle)){
            rectangles.add(rectangle);
        }

    }

    public static void addEllipseDraw(Ellipse ellipse) {
        if(!ellipseDrawEllipses.contains(ellipse)){
            ellipseDrawEllipses.add(ellipse);
        }
    }

    public static void addDegreeLabel(DegreeLabel degreeLabel){
        degreeLabels.add(degreeLabel);
    }

    public static void addDistanceLabel(DistanceLabel distanceLabel){
        distanceLabels.add(distanceLabel);
    }

    public static void addTextLabel(TextLabel textLabel){
        textLabels.add(textLabel);
    }

    public static Ellipse isEllipse(Point2D point){
        for (Ellipse ellipse : ellipses) {
            if (isPointInEllipse(point, ellipse)) {
                return ellipse;
            }
        }
        return null;
    }

    public static Ellipse isEllipseDraw(Point2D point) {
        for (Ellipse ellipse : ellipseDrawEllipses) {
            if (isPointInEllipse(point, ellipse)) {
                return ellipse;
            }
        }

        return null;
    }

    private static boolean isPointInEllipse(Point2D point, Ellipse ellipse) {
        if(ellipse==null){
            return false;
        }
        double dx = point.getX() - ellipse.getCenterX();
        double dy = point.getY() - ellipse.getCenterY();
        return (dx * dx) / (ellipse.getRadiusX() * ellipse.getRadiusX()) +
                (dy * dy) / (ellipse.getRadiusY() * ellipse.getRadiusY()) <= 1;
    }

    public static void removeAllEllipse(ObservableList<Ellipse> theEllipses){
        ellipses.removeAll(theEllipses);
    }

    public static void removeAllLines(ObservableList<Line> theLines){
        lines.removeAll(theLines);
    }

    public static void removeLine(Line theLines){
        lines.remove(theLines);
    }

    public static void removeEllipse(Ellipse ellipse){
        ellipses.remove(ellipse);
    }

    public static void removeRectangle(Rectangle rectangle){
        rectangles.remove(rectangle);
    }

    public static void removeEllipseDraw(Ellipse ellipse) {
        ellipseDrawEllipses.remove(ellipse);
    }

    public static void removeDegreeLabel(DegreeLabel degreeLabel){
        degreeLabels.remove(degreeLabel);
    }

    public static void removeDistanceLabel(DistanceLabel distanceLabel){
        distanceLabels.remove(distanceLabel);
    }

    public static void removeTextLabel(TextLabel textLabel){
        textLabels.remove(textLabel);
    }

    public static Rectangle isRectangle(Point2D clickPoint) {
        for (Rectangle rectangle : rectangles) {
            if(rectangle!=null && clickPoint!=null){
                if (rectangle.contains(clickPoint)) {
                    return rectangle;
                }
            }else {
                rectangles.remove(rectangle);
            }
        }
        return null;
    }

    public static Line isLine(Point2D clickPoint) {
        for (Line line : lines) {
            if (line.contains(clickPoint)) {
                return line;
            }
        }

        return null;
    }

    public static TextLabel isTextLabel(Point2D clickPoint) {
        for (TextLabel textLabels : textLabels) {
            if (textLabels.contains(clickPoint.getX(), clickPoint.getY())) {
                return textLabels;
            }
        }

        return null;
    }

    public static CustomFontIcon isCustomFontIcon(Point2D clickPoint) {
        if (customFontIcon != null && customFontIcon.contains(clickPoint.getX(), clickPoint.getY())) {
            return customFontIcon;
        }

        return null;
    }

    public static void removeCustomFontIcon() {
        customFontIcon = null;
    }


}
