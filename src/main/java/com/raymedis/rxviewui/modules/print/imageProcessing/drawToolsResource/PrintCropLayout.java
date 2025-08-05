package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintCropLayout implements PrintInterfaceDrawTools{
    private Canvas canvas;
    private GraphicsContext gc;

    private final Logger logger = LoggerFactory.getLogger(PrintCropLayout.class);

    // Crop rectangle properties
    private boolean dragging = false;
    private boolean resizing = false;
    private double lastX, lastY;
    private String activeCorner = null;
    public Image croppedImage;

    // Cached resources for better performance
    private Image cachedBackground;
    private Mat backgroundMat;
    private Mat maskMat;

    // Frame rate control
    private long lastRenderTime = 0;
    private static final long RENDER_DELAY = 16; // ~60fps

    private PrintCanvasMainChange printCanvasMainChange;

    // Resize Handle Size
    private final double HANDLE_SIZE = 30;
    private final double alpha = 0.8;

    // Threshold for corner detection
    private final double CORNER_DETECTION_THRESHOLD = 20.0;


    public void saveCroppedImage() {
        canvas.setCursor(Cursor.DEFAULT);
        printCanvasMainChange.getLayoutTab().applyImageFrameResize(croppedImage.getWidth(), croppedImage.getHeight());
        printCanvasMainChange.isCropLayoutComplete=true;
    }



    public void drawLayout(PrintCanvasMainChange printCanvasMainChange) {

        Rect finalcropRect = printCanvasMainChange.printMainDrawTool.finalcropRect;

        this.printCanvasMainChange = printCanvasMainChange;

        // Update the cropped image if needed
        if (finalcropRect.width > 0 && finalcropRect.height > 0) {
            try {
                Mat croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(printCanvasMainChange.displayImage), finalcropRect);
                croppedImage = ImageConversionService.getInstance().matToImage0(croppedMat);
                croppedMat.release();
            } catch (Exception e) {
                logger.error("Error creating cropped image", e);
            }
        }

        Rect cropRect = new Rect(finalcropRect.x, finalcropRect.y, finalcropRect.width, finalcropRect.height);

        // Crop the image
        Mat croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(printCanvasMainChange.getDisplayImage()), cropRect);

        croppedImage = ImageConversionService.getInstance().matToImage(croppedMat);

        Mat background = ImageConversionService.getInstance().imageToMat(printCanvasMainChange.getDisplayImage());

        Mat mask = new Mat(background.size(), background.type(), new Scalar(28, 100, 236));

        // Set transparency level (0.0 = fully transparent, 1.0 = fully opaque)
        double alpha = 0.8;

        // Blend the mask with the image
        Core.addWeighted(mask, alpha, background, 1.0 - alpha, 0, background);

        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        // Draw the image first
        gc.drawImage(ImageConversionService.getInstance().matToImage(background), 0, 0, printCanvasMainChange.displayImage.getWidth(), printCanvasMainChange.displayImage.getHeight());
        gc.drawImage(croppedImage, finalcropRect.x, finalcropRect.y, finalcropRect.width, finalcropRect.height);

        // Draw the dashed crop rectangle
        gc.setLineDashes(10);
        gc.setStroke(Color.ORANGERED);
        gc.setLineWidth(2);
        gc.strokeRect(finalcropRect.x, finalcropRect.y, finalcropRect.width, finalcropRect.height);

        // Draw corner handles
        gc.setFill(Color.GREEN);

        //4 corners
        drawHandle(finalcropRect.x, finalcropRect.y);
        drawHandle(finalcropRect.x + finalcropRect.width, finalcropRect.y);
        drawHandle(finalcropRect.x, finalcropRect.y + finalcropRect.height);
        drawHandle(finalcropRect.x + finalcropRect.width, finalcropRect.y + finalcropRect.height);

        //4 sides
        drawHandle(finalcropRect.x + (double) finalcropRect.width / 2, finalcropRect.y); // Top-center
        drawHandle(finalcropRect.x + (double) finalcropRect.width / 2, finalcropRect.y + finalcropRect.height); // Bottom-center
        drawHandle(finalcropRect.x, finalcropRect.y + (double) finalcropRect.height / 2); // Left-center
        drawHandle(finalcropRect.x + finalcropRect.width, finalcropRect.y + (double) finalcropRect.height / 2); // Right-center

    }


    private void drawHandle(double x, double y) {
        gc.fillRect(x - HANDLE_SIZE / 2, y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
    }


    private void dragRectangle(double dx, double dy) {
        Rect rect = printCanvasMainChange.printMainDrawTool.finalcropRect;

        // Ensure the crop area stays within bounds
        boolean canMoveX = rect.x + dx >= 0 && rect.x + dx + rect.width <= printCanvasMainChange.getDisplayImage().getWidth();
        boolean canMoveY = rect.y + dy >= 0 && rect.y + dy + rect.height <= printCanvasMainChange.getDisplayImage().getHeight();

        if (canMoveX) {
            rect.x += (int) dx;
        }
        if (canMoveY) {
            rect.y += (int) dy;
        }
    }

    private boolean isInsideRectangle(double x, double y) {
        Rect rect = printCanvasMainChange.printMainDrawTool.finalcropRect;
        return x >= rect.x && x <= rect.x + rect.width &&
                y >= rect.y && y <= rect.y + rect.height;
    }

    private String getCorner(double x, double y) {

        // Check corners with better distance calculation
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x, printCanvasMainChange.printMainDrawTool.finalcropRect.y)) return "TOP_LEFT";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x + printCanvasMainChange.printMainDrawTool.finalcropRect.width, printCanvasMainChange.printMainDrawTool.finalcropRect.y)) return "TOP_RIGHT";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x, printCanvasMainChange.printMainDrawTool.finalcropRect.y + printCanvasMainChange.printMainDrawTool.finalcropRect.height)) return "BOTTOM_LEFT";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x + printCanvasMainChange.printMainDrawTool.finalcropRect.width, printCanvasMainChange.printMainDrawTool.finalcropRect.y + printCanvasMainChange.printMainDrawTool.finalcropRect.height)) return "BOTTOM_RIGHT";

        // Check midpoints
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x + (double) printCanvasMainChange.printMainDrawTool.finalcropRect.width / 2, printCanvasMainChange.printMainDrawTool.finalcropRect.y)) return "TOP_MID";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x + (double) printCanvasMainChange.printMainDrawTool.finalcropRect.width / 2, printCanvasMainChange.printMainDrawTool.finalcropRect.y + printCanvasMainChange.printMainDrawTool.finalcropRect.height)) return "BOTTOM_MID";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x, printCanvasMainChange.printMainDrawTool.finalcropRect.y + (double) printCanvasMainChange.printMainDrawTool.finalcropRect.height / 2)) return "LEFT_MID";
        if (isNearPoint(x, y, printCanvasMainChange.printMainDrawTool.finalcropRect.x + printCanvasMainChange.printMainDrawTool.finalcropRect.width, printCanvasMainChange.printMainDrawTool.finalcropRect.y + (double) printCanvasMainChange.printMainDrawTool.finalcropRect.height / 2)) return "RIGHT_MID";

        return null;
    }

    private boolean isNearPoint(double mouseX, double mouseY, double pointX, double pointY) {
        double distance = Math.sqrt(Math.pow(mouseX - pointX, 2) + Math.pow(mouseY - pointY, 2));
        return distance <= CORNER_DETECTION_THRESHOLD;
    }

    private void resizeRectangle(String corner, int dx, int dy) {

        switch (corner) {
            case "TOP_LEFT":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.x + dx >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.y + dy >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.width - dx > 20 && printCanvasMainChange.printMainDrawTool.finalcropRect.height - dy > 20) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.x += dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.y += dy;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width -= dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height -= dy;
                }
                break;
            case "TOP_RIGHT":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.y + dy >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.width + dx < printCanvasMainChange.getDisplayImage().getWidth() - printCanvasMainChange.printMainDrawTool.finalcropRect.x && printCanvasMainChange.printMainDrawTool.finalcropRect.height - dy > 20) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.y += dy;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width += dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height -= dy;
                }
                break;
            case "BOTTOM_LEFT":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.x + dx >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.width - dx > 20 && printCanvasMainChange.printMainDrawTool.finalcropRect.height + dy < printCanvasMainChange.getDisplayImage().getHeight() - printCanvasMainChange.printMainDrawTool.finalcropRect.y) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.x += dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width -= dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height += dy;
                }
                break;
            case "BOTTOM_RIGHT":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.width + dx < printCanvasMainChange.getDisplayImage().getWidth() - printCanvasMainChange.printMainDrawTool.finalcropRect.x && printCanvasMainChange.printMainDrawTool.finalcropRect.height + dy < printCanvasMainChange.getDisplayImage().getHeight() - printCanvasMainChange.printMainDrawTool.finalcropRect.y) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width += dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height += dy;
                }
                break;

            // Midpoint Resizing
            case "TOP_MID":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.y + dy >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.height - dy > 20) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.y += dy;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height -= dy;
                }
                break;
            case "BOTTOM_MID":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.height + dy < printCanvasMainChange.getDisplayImage().getHeight() - printCanvasMainChange.printMainDrawTool.finalcropRect.y) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.height += dy;
                }
                break;
            case "LEFT_MID":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.x + dx >= 0 && printCanvasMainChange.printMainDrawTool.finalcropRect.width - dx > 20) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.x += dx;
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width -= dx;
                }
                break;
            case "RIGHT_MID":
                if (printCanvasMainChange.printMainDrawTool.finalcropRect.width + dx < printCanvasMainChange.getDisplayImage().getWidth() - printCanvasMainChange.printMainDrawTool.finalcropRect.x) {
                    printCanvasMainChange.printMainDrawTool.finalcropRect.width += dx;
                }
                break;
        }
    }


    @Override
    public void removeAll() {

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
            canvas.setCursor(Cursor.MOVE); // Change to MOVE cursor for better feedback
        }
    }


    @Override
    public void handleMouseDragged(MouseEvent event) {
        int dx = (int) (event.getX() - lastX);
        int dy = (int) (event.getY() - lastY);

        if (resizing) {
            resizeRectangle(activeCorner, dx, dy);
        } else if (dragging) {
            dragRectangle(dx, dy);
        }

        lastX = event.getX();
        lastY = event.getY();

        drawLayout(printCanvasMainChange);
    }

    @Override
    public void handleCanvasReleased(MouseEvent mouseEvent) {
        dragging = false;
        resizing = false;
        activeCorner = null;
        canvas.setCursor(Cursor.DEFAULT);
    }

    @Override
    public void hideAll(PrintCanvasMainChange printCanvasMainChange) {
        this.printCanvasMainChange = printCanvasMainChange;
    }

    @Override
    public void unHideAll() {
        canvas.setCursor(Cursor.DEFAULT);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();

    }
}