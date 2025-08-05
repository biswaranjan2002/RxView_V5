package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CropLayout implements IDrawTools{

    private Canvas canvas;
    private GraphicsContext gc;
    private final Logger logger = LoggerFactory.getLogger(CropLayout.class);

    // Crop rectangle properties
    public static double rectX=0, rectY=0, rectWidth=0, rectHeight=0;
    private boolean dragging = false;
    private boolean resizing = false;
    private double lastX, lastY;
    private String activeCorner = null;
    public Image croppedImage;

    public static boolean isComplete=false;

    // Resize Handle Size
    private final double HANDLE_SIZE = 15;



    public CropLayout() {

    }

    public void reset(double width, double height) {
        isComplete=true;

        // Initial Crop Area
        rectX = 0;
        rectY = 0;

        rectWidth = width;
        rectHeight = height;
    }

    public void saveCroppedImage() {
        StudyService.imageFrameResize(croppedImage);
        canvas.setCursor(Cursor.DEFAULT);
        isComplete=true;

        logger.info("rectX: {}, rectY: {}, rectWidth: {}, rectHeight: {}", rectX, rectY, rectWidth, rectHeight);

       Mat thumNailMat = ImageConversionService.getInstance().imageToMat(croppedImage);

        if(thumNailMat.channels()==4){
            Imgproc.cvtColor(thumNailMat, thumNailMat, Imgproc.COLOR_RGBA2BGR);
        }

        thumNailMat = ImageProcessService.getInstance().resizeImageToSquareShape(thumNailMat);
        Imgproc.resize(thumNailMat,thumNailMat,new Size(150,150), 0, 0, Imgproc.INTER_AREA);
        //Imgcodecs.imwrite("D:\\temp\\thumNailMat.png", thumNailMat);

        StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart().getBodyPart().setThumbNail(thumNailMat);

        Platform.runLater(()->StudyService.getInstance().createBodyPartTabs2());
    }


    public void drawLayout() {
        //logger.info("drawLayout called");

        Rect cropRect = new Rect((int) rectX, (int)rectY,(int) rectWidth,(int) rectHeight);

        //logger.info("Crop Rectangle: {} " , cropRect);

        // Crop the image
        Mat croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(CanvasMainChange.displayImage), cropRect);

        croppedImage = ImageConversionService.getInstance().matToImage(croppedMat);

        Mat background = ImageConversionService.getInstance().imageToMat(CanvasMainChange.displayImage);
        Mat mask = new Mat(background.size(), background.type(), new Scalar(28, 100, 236));

        // Set transparency level (0.0 = fully transparent, 1.0 = fully opaque)
        double alpha = 0.8;

        // Blend the mask with the image
        Core.addWeighted(mask, alpha, background, 1.0 - alpha, 0, background);

        // Draw the image first
        gc.drawImage(ImageConversionService.getInstance().matToImage(background), 0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(croppedImage, rectX, rectY, rectWidth, rectHeight);

        // Draw the dashed crop rectangle
        gc.setLineDashes(10);
        gc.setStroke(Color.ORANGERED);
        gc.setLineWidth(2);
        gc.strokeRect(rectX, rectY, rectWidth, rectHeight);

        // Draw corner handles
        gc.setFill(Color.GREEN);

        //4 corners
        drawHandle(rectX, rectY);
        drawHandle(rectX + rectWidth, rectY);
        drawHandle(rectX, rectY + rectHeight);
        drawHandle(rectX + rectWidth, rectY + rectHeight);

        //4 sides
        drawHandle(rectX + rectWidth / 2, rectY); // Top-center
        drawHandle(rectX + rectWidth / 2, rectY + rectHeight); // Bottom-center
        drawHandle(rectX, rectY + rectHeight / 2); // Left-center
        drawHandle(rectX + rectWidth, rectY + rectHeight / 2); // Right-center

    }


    private void drawHandle(double x, double y) {
        gc.fillRect(x - HANDLE_SIZE / 2, y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
    }


    private void dragRectangle(double dx, double dy) {
        // Ensure the crop area stays within bounds
        if (rectX + dx >= 0 && rectX + dx + rectWidth <= canvas.getWidth()) {
            rectX += dx;
        }
        if (rectY + dy >= 0 && rectY + dy + rectHeight <= canvas.getHeight()) {
            rectY += dy;
        }
    }



    private boolean isInsideRectangle(double x, double y) {
        return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight;
    }

    private String getCorner(double x, double y) {
        if (isNear(x, rectX) && isNear(y, rectY)) return "TOP_LEFT";
        if (isNear(x, rectX + rectWidth) && isNear(y, rectY)) return "TOP_RIGHT";
        if (isNear(x, rectX) && isNear(y, rectY + rectHeight)) return "BOTTOM_LEFT";
        if (isNear(x, rectX + rectWidth) && isNear(y, rectY + rectHeight)) return "BOTTOM_RIGHT";

        // New midpoint checks
        if (isNear(x, rectX + rectWidth / 2) && isNear(y, rectY)) return "TOP_MID";
        if (isNear(x, rectX + rectWidth / 2) && isNear(y, rectY + rectHeight)) return "BOTTOM_MID";
        if (isNear(x, rectX) && isNear(y, rectY + rectHeight / 2)) return "LEFT_MID";
        if (isNear(x, rectX + rectWidth) && isNear(y, rectY + rectHeight / 2)) return "RIGHT_MID";

        return null;
    }


    private boolean isNear(double a, double b) {
        return Math.abs(a - b) <= HANDLE_SIZE;
    }

    private void resizeRectangle(String corner, double dx, double dy) {
        switch (corner) {
            case "TOP_LEFT":
                if (rectX + dx >= 0 && rectY + dy >= 0 && rectWidth - dx > 20 && rectHeight - dy > 20) {
                    rectX += dx;
                    rectY += dy;
                    rectWidth -= dx;
                    rectHeight -= dy;
                }
                break;
            case "TOP_RIGHT":
                if (rectY + dy >= 0 && rectWidth + dx < canvas.getWidth() - rectX && rectHeight - dy > 20) {
                    rectY += dy;
                    rectWidth += dx;
                    rectHeight -= dy;
                }
                break;
            case "BOTTOM_LEFT":
                if (rectX + dx >= 0 && rectWidth - dx > 20 && rectHeight + dy < canvas.getHeight() - rectY) {
                    rectX += dx;
                    rectWidth -= dx;
                    rectHeight += dy;
                }
                break;
            case "BOTTOM_RIGHT":
                if (rectWidth + dx < canvas.getWidth() - rectX && rectHeight + dy < canvas.getHeight() - rectY) {
                    rectWidth += dx;
                    rectHeight += dy;
                }
                break;

            // Midpoint Resizing
            case "TOP_MID":
                if (rectY + dy >= 0 && rectHeight - dy > 20) {
                    rectY += dy;
                    rectHeight -= dy;
                }
                break;
            case "BOTTOM_MID":
                if (rectHeight + dy < canvas.getHeight() - rectY) {
                    rectHeight += dy;
                }
                break;
            case "LEFT_MID":
                if (rectX + dx >= 0 && rectWidth - dx > 20) {
                    rectX += dx;
                    rectWidth -= dx;
                }
                break;
            case "RIGHT_MID":
                if (rectWidth + dx < canvas.getWidth() - rectX) {
                    rectWidth += dx;
                }
                break;
        }
    }


    @Override
    public void removeAll() {
        rectX=-1; rectY=-1; rectWidth=-1; rectHeight=-1;
    }

    @Override
    public Boolean containsElement(Node element) {
        return null;
    }

    @Override
    public void handleMousePressed(MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();

        activeCorner = getCorner(event.getX(), event.getY());

        if (activeCorner != null) {
            resizing = true;

            // Change cursor for resizing
            switch (activeCorner) {
                case "TOP_LEFT":
                case "BOTTOM_RIGHT":
                    canvas.setCursor(Cursor.NW_RESIZE);
                    break;
                case "TOP_RIGHT":
                case "BOTTOM_LEFT":
                    canvas.setCursor(Cursor.NE_RESIZE);
                    break;
                case "TOP_MID":
                case "BOTTOM_MID":
                    canvas.setCursor(Cursor.V_RESIZE);
                    break;
                case "LEFT_MID":
                case "RIGHT_MID":
                    canvas.setCursor(Cursor.H_RESIZE);
                    break;
                default:
                    canvas.setCursor(Cursor.CROSSHAIR);
            }

        } else if (isInsideRectangle(event.getX(), event.getY())) {
            dragging = true;
            canvas.setCursor(Cursor.DEFAULT);
        }
    }


    @Override
    public void handleMouseDragged(MouseEvent event) {
        double dx = event.getX() - lastX;
        double dy = event.getY() - lastY;

        if (resizing) {
            resizeRectangle(activeCorner, dx, dy);
        } else if (dragging) {
            dragRectangle(dx, dy);
        }

        lastX = event.getX();
        lastY = event.getY();

        drawLayout();
    }

    @Override
    public void handleCanvasReleased(MouseEvent mouseEvent) {
        dragging = false;
        resizing = false;
        activeCorner = null;
    }

    @Override
    public void hideAll() {

    }

    @Override
    public void unHideAll() {

    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public static double getRectX() {
        return rectX;
    }

    public static void setRectX(double rectX) {
        CropLayout.rectX = rectX;
    }

    public static double getRectY() {
        return rectY;
    }

    public static void setRectY(double rectY) {
        CropLayout.rectY = rectY;
    }

    public static double getRectWidth() {
        return rectWidth;
    }

    public static void setRectWidth(double rectWidth) {
        CropLayout.rectWidth = rectWidth;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public static double getRectHeight() {
        return rectHeight;
    }

    public static void setRectHeight(double rectHeight) {
        CropLayout.rectHeight = rectHeight;
    }

    public boolean isResizing() {
        return resizing;
    }

    public void setResizing(boolean resizing) {
        this.resizing = resizing;
    }

    public String getActiveCorner() {
        return activeCorner;
    }

    public void setActiveCorner(String activeCorner) {
        this.activeCorner = activeCorner;
    }

    public static boolean isIsComplete() {
        return isComplete;
    }

    public static void setIsComplete(boolean isComplete) {
        CropLayout.isComplete = isComplete;
    }
}
