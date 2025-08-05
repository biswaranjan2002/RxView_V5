package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import com.ramedis.zioxa.bilateral.Bilateral;
import com.ramedis.zioxa.usm.USM;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageTools;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.service.study.ProcessingToolsController;
import com.raymedis.rxviewui.service.study.ProcessingToolsService;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainDrawTool {

    public static Canvas drawingCanvas;
    public ViewBox canvasViewBox;

    private final StackPane canvasStackPane;

    private final Pane textPane;

    private boolean isAngleDraw = false;
    private boolean isTextDraw = false;
    private boolean isDistanceDraw = false;
    private boolean isRectangle = false;
    private boolean isEllipse = false;
    public static boolean isR = false;
    public static boolean isL = false;

    private boolean isBrightness = false;
    private boolean isContrast = false;
    private boolean isPan = false;
    public boolean isCrop = false;
    private boolean isZoom = false;
    public boolean isHFlip = false;
    public boolean isVFlip = false;

    public boolean isInvert = false;

    private boolean isHide = false;

    public static boolean isImageToolEnable = false;

    private boolean isSelect = false;

    private double startSceneX, startSceneY;

    private double viewportWidth;
    private double viewportHeight;

    public static Group canvasGroup;

    public static List<AngleDraw> angleDrawList = new ArrayList<>();
    public static List<DistanceDraw> distanceDrawList = new ArrayList<>();
    public static CropLayout cropLayout = new CropLayout();

    public static List<TextDraw> textDrawList = new ArrayList<>();

    public List<RectangleDraw> rectangleDrawsList = new ArrayList<>();

    public List<EllipseDraw> ellipseDrawList = new ArrayList<>();

    public List<IDrawTools> selectedDrawToolsList = new ArrayList<>();

    public static List<IDrawTools> allDrawTools = new ArrayList<>();

    public static LeftRightDraw leftRightDraw = null;

    public static IDrawTools currentTool = null;

    private Point2D clickPosition;
    private Point2D currentPosition;

    public double brightnessBefore = 0;
    public double contrastBefore = 1;
    public double imageLeftBefore;
    public double imageTopBefore = 0;
    public double zoomFactorBefore = 0;

    public double sharpness = 1;
    public double denoise = 3;

    //public double brightness = 0;
    public double brightness = 0;
    public double contrast = 1;
    public double zoomFactor = 1;
    public double angleFactor = 0;

    public double imageLeft = 0;
    public double imageTop = 0;

    private double zoomStartY;
    private double scaleFactor = 1.0;

    private final double MIN_SCALE = 0.5;
    private final double MAX_SCALE = 3.0;


    private final boolean isProcessing = false;

    public Mat mainImageMat;

    private boolean isDragging = false;

    private final Scale scale;

    private final ImageProcessService imageProcessService = new ImageProcessService();

    private final ImageConversionService imageConversionService = new ImageConversionService();
    private final Logger logger = LoggerFactory.getLogger(MainDrawTool.class);

    public void resetAllDrawingTools(){
        currentTool=null;
        leftRightDraw=null;
        distanceDrawList.clear();
        angleDrawList.clear();
        textDrawList.clear();
        rectangleDrawsList.clear();
        ellipseDrawList.clear();
        allDrawTools.clear();
        CanvasMainChange.clearCanvas();
        reset();
    }

    public MainDrawTool(Canvas drawingCanvas, StackPane canvasStackPane, Pane textPane, Pane canvasPane) {
        this.drawingCanvas = drawingCanvas;
        this.canvasStackPane = canvasStackPane;
        this.textPane = textPane;

//        textPane.setBackground(Background.fill(Paint.valueOf("#ec541c")));

        viewportWidth = drawingCanvas.getWidth();
        viewportHeight = drawingCanvas.getHeight();

        //System.out.println(viewportWidth + " : " + viewportHeight);

        canvasGroup = new Group(drawingCanvas,textPane);
        Rectangle clip = new Rectangle(viewportWidth, viewportHeight);
        canvasGroup.setClip(clip);



        canvasPane.getChildren().add(canvasGroup);
        scale = new Scale();
        drawingCanvas.getTransforms().add(scale);
        textPane.getTransforms().add(scale);



        CanvasMainChange.drawingCanvas = drawingCanvas;
        CanvasMainChange.init(textPane);
        attachCanvasFunction();
        cropLayout.setCanvas(drawingCanvas);
    }

    public void attachCanvasFunction() {
        drawingCanvas.setOnMouseReleased(this::drawingCanvasMouseReleased);
        drawingCanvas.setOnMousePressed(this::drawingCanvasMousePressed);
        drawingCanvas.setOnMouseDragged(this::drawingCanvasMouseDragged);
    }

    public static void enableImageTools(){
        isImageToolEnable = true;
    }

    public void drawingCanvasMousePressed(MouseEvent mouseEvent) {
        if(!isImageToolEnable){
            return;
        }

        Point2D clickPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        if(isCrop){
            cropLayout.handleMousePressed(mouseEvent);
            return;
        }

        //region image processing tools
        clickPosition = clickPoint;
        isDragging = true;

        if(isBrightness){
            return;
        }
        else if(isContrast){
            return;
        }
        else if(isPan){
            //System.out.println("pan pressed");
            if (CanvasMainChange.mainImage != null) {
                startSceneX = mouseEvent.getSceneX();
                startSceneY = mouseEvent.getSceneY();
            }
            return;
        }
        else if(isZoom){
            zoomStartY = mouseEvent.getSceneY();
            return;
        }
        //endregion


        if(currentTool!=null){
            currentTool.handleMousePressed(mouseEvent);
            //logger.info("current tool pressed");
        }

        Ellipse isEllipseClick = CanvasMainChange.isEllipse(clickPoint);
        Ellipse isEllipseDrawClick = CanvasMainChange.isEllipseDraw(clickPoint);
        Rectangle isRectangleClick = CanvasMainChange.isRectangle(clickPoint);
        TextLabel isTextLabelClick = CanvasMainChange.isTextLabel(clickPoint);
        Line isLineClicked = CanvasMainChange.isLine(clickPoint);
        CustomFontIcon isCustomFontIconClicked = CanvasMainChange.isCustomFontIcon(clickPoint);

        if(isSelect)
        {
            if(isEllipseClick!=null || isRectangleClick != null ||
                    isEllipseDrawClick != null || isTextLabelClick != null || isLineClicked != null)
            {

                for(AngleDraw angleDraw : angleDrawList){
                    if(angleDraw.containsElement(isEllipseClick) || angleDraw.containsElement(isLineClicked)){
                        if(!selectedDrawToolsList.contains(angleDraw)){
                            selectedDrawToolsList.add(angleDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(angleDraw);
                        }
                    }
                }

                for(DistanceDraw distanceDraw : distanceDrawList){
                    if(distanceDraw.containsElement(isEllipseClick)|| distanceDraw.containsElement(isLineClicked)){
                        if(!selectedDrawToolsList.contains(distanceDraw)){
                            selectedDrawToolsList.add(distanceDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(distanceDraw);
                        }

                    }
                }

                for(TextDraw textDraw : textDrawList){
                    if(textDraw.containsElement(isTextLabelClick)){
                        if(!selectedDrawToolsList.contains(textDraw)){
                            selectedDrawToolsList.add(textDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(textDraw);
                        }
                    }
                }

                for(RectangleDraw rectangleDraw : rectangleDrawsList){
                    if(rectangleDraw.containsElement(isRectangleClick)){
                        if(!selectedDrawToolsList.contains(rectangleDraw)){
                            selectedDrawToolsList.add(rectangleDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(rectangleDraw);
                        }

                    }
                }

                for(EllipseDraw ellipseDraw : ellipseDrawList){
                    if(ellipseDraw.containsElement(isEllipseDrawClick)){
                        if(!selectedDrawToolsList.contains(ellipseDraw)){
                            selectedDrawToolsList.add(ellipseDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(ellipseDraw);
                        }
                    }
                }

            }
            else
            {
                selectedDrawToolsList.clear();
            }

            makeSelectUIChange();
            return;
        }

        selectedDrawToolsList.clear();

        if(isAngleDraw) {
            if(AngleDraw.isComplete && (isEllipseClick==null) && (isLineClicked == null)){
                CanvasMainChange.redraw();
                currentTool = new AngleDraw(drawingCanvas,clickPoint);
                angleDrawList.add((AngleDraw) currentTool);
                allDrawTools.add( currentTool);
                selectedDrawToolsList.add(currentTool);
            }
        }
        else if(isDistanceDraw){
            if(DistanceDraw.isComplete && (isEllipseClick==null) && (isLineClicked == null)){
                CanvasMainChange.redraw();
                currentTool = new DistanceDraw(drawingCanvas,clickPoint);
                distanceDrawList.add((DistanceDraw) currentTool);
                allDrawTools.add( currentTool);
                selectedDrawToolsList.add(currentTool);
            }
        }
        else if(isTextDraw){
            if(TextDraw.isComplete && (isTextLabelClick==null)){
                currentTool = new TextDraw(drawingCanvas,clickPoint, textPane);
                textDrawList.add((TextDraw) currentTool);
                allDrawTools.add(currentTool);
                selectedDrawToolsList.add(currentTool);
                CanvasMainChange.redraw();
            }
        }
        else if(isRectangle){
            if(RectangleDraw.isComplete && (isRectangleClick==null)){
                currentTool = new RectangleDraw(drawingCanvas,clickPoint);
                rectangleDrawsList.add((RectangleDraw) currentTool);
                allDrawTools.add( currentTool);
                selectedDrawToolsList.add(currentTool);
                CanvasMainChange.redraw();
            }
        }
        else if(isEllipse){
            if(EllipseDraw.isComplete && (isEllipseDrawClick==null)){
                currentTool = new EllipseDraw(drawingCanvas,clickPoint);
                ellipseDrawList.add((EllipseDraw) currentTool);
                allDrawTools.add( currentTool);
                selectedDrawToolsList.add(currentTool);
                CanvasMainChange.redraw();
            }
        }
        else if(isR){
            currentTool = leftRightDraw;
            allDrawTools.add( currentTool);
            CanvasMainChange.redraw();
        }
        else if(isL){
            currentTool = leftRightDraw;
            allDrawTools.add( currentTool);
            CanvasMainChange.redraw();
        }

        if(AngleDraw.isComplete && DistanceDraw.isComplete &&
                TextDraw.isComplete && RectangleDraw.isComplete && EllipseDraw.isComplete &&
                (isEllipseClick!=null || isRectangleClick != null || isEllipseDrawClick != null ||
                        isTextLabelClick != null || isLineClicked != null || isCustomFontIconClicked != null)){
            if(isAngleDraw){
                for(AngleDraw angleDraw : angleDrawList){
                    if(angleDraw.containsElement(isEllipseClick) || angleDraw.containsElement(isLineClicked)){
                        currentTool = angleDraw;
                        angleDraw.redraw();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isDistanceDraw){
                for(DistanceDraw distanceDraw : distanceDrawList){
                    if(distanceDraw.containsElement(isEllipseClick) || distanceDraw.containsElement(isLineClicked)){
                        currentTool = distanceDraw;
                        distanceDraw.redraw();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isTextDraw){
                for(TextDraw textDraw : textDrawList){
                    if(textDraw.containsElement(isTextLabelClick)){
                        currentTool = textDraw;
                        textDraw.drawTexts();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isRectangle) {
                for(RectangleDraw rectangleDraw : rectangleDrawsList){
                    if(rectangleDraw.containsElement(isRectangleClick)){
                        currentTool = rectangleDraw;
                        rectangleDraw.drawRectangle();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isEllipse){
                for(EllipseDraw ellipseDraw : ellipseDrawList){
                    if(ellipseDraw.containsElement(isEllipseDrawClick)){
                        currentTool = ellipseDraw;
                        ellipseDraw.drawEllipse();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isR){
                if(isCustomFontIconClicked != null){
                    leftRightDraw.setIcon("RIGHT");
                    currentTool = leftRightDraw;
                    leftRightDraw.redraw();
                    currentTool.handleMousePressed(mouseEvent);
                }

            }
            else if(isL){
                if(isCustomFontIconClicked != null){
                    leftRightDraw.setIcon("LEFT");
                    currentTool = leftRightDraw;
                    leftRightDraw.redraw();
                    currentTool.handleMousePressed(mouseEvent);
                }

            }

        }
        else if(AngleDraw.isComplete && DistanceDraw.isComplete && TextDraw.isComplete && RectangleDraw.isComplete && EllipseDraw.isComplete){
            currentTool = null;
            CanvasMainChange.redraw();
        }

    }

    public void drawingCanvasMouseDragged(MouseEvent mouseEvent) {
        if(!isImageToolEnable){
            return;
        }

        if(isCrop){
            cropLayout.handleMouseDragged(mouseEvent);
            return;
        }

        if(currentTool!=null){
            currentTool.handleMouseDragged(mouseEvent);
        }

        currentPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        isDragging = true;

        if(isBrightness){
            brightnessChange();
        }
        else if(isContrast){
            contrastChange();
        }
        else if(isPan){
            panChange(mouseEvent);
        }
        else if(isZoom){
            zoomChange(mouseEvent);
        }

    }

    public void drawingCanvasMouseReleased(MouseEvent mouseEvent) {

        if(!isImageToolEnable){
            return;
        }

        if(isCrop){
            cropLayout.handleCanvasReleased(mouseEvent);
            return;
        }

        if(currentTool!=null){
            currentTool.handleCanvasReleased(mouseEvent);
        }

        brightnessBefore = brightness;
        contrastBefore = contrast;
        zoomFactorBefore = zoomFactor;

        imageTopBefore = imageTop;
        imageLeftBefore = imageLeft;

        if(isBrightness){
            //brightnessChange();
        }
        else if(isContrast){

        }
        else if(isPan){

        }
        else if(isZoom){

        }


        isDragging = false;


        ProcessingToolsController.getInstance().brightnessSlider.setValue(brightness/ (65535.0 / 255.0));
        ProcessingToolsController.getInstance().contrastSlider.setValue(contrast);



    }


    //region ui tools

    public void handleAngleButton() {
        if(isAngleDraw){
            reset();
        }
        else{
            reset();
            isAngleDraw = true;
        }
    }

    public void handleTextButton() {
        if(isTextDraw){
            reset();
            logger.info("resetCalled");
        }
        else{
            reset();
            isTextDraw = true;
        }
    }

    public void handleDistanceButton() {
        if(isDistanceDraw){
            reset();
        }
        else{
            reset();
            isDistanceDraw = true;
        }
    }

    public void handleLButton() {
        if(isL){
            reset();
            leftRightDraw.removeAll();
            allDrawTools.remove(leftRightDraw);
            leftRightDraw = null;
            CanvasMainChange.redraw();
        }
        else{
            reset();
            if(leftRightDraw == null){
                leftRightDraw = new LeftRightDraw("LEFT", drawingCanvas);
                allDrawTools.add(leftRightDraw);
            }else {
                if(leftRightDraw.getRlIcon()==null){
                    leftRightDraw = new LeftRightDraw("LEFT", drawingCanvas);
                }
            }
            leftRightDraw.setIcon("LEFT");
            isL = true;
            leftRightDraw.redraw();
        }
    }

    public void handleRButton() {
        if(isR){
            reset();
            leftRightDraw.removeAll();
            allDrawTools.remove( leftRightDraw);
            leftRightDraw = null;
            CanvasMainChange.redraw();
        }
        else{
            reset();
            if(leftRightDraw == null){
                leftRightDraw = new LeftRightDraw("RIGHT", drawingCanvas);
                allDrawTools.add(leftRightDraw);
            }else {
                if(leftRightDraw.getRlIcon()==null){
                    leftRightDraw = new LeftRightDraw("RIGHT", drawingCanvas);
                }
            }
            leftRightDraw.setIcon("RIGHT");
            isR = true;
            leftRightDraw.redraw();
        }

    }

    public void handleSelectButton() {

        if(isSelect){
            reset();
        }
        else{
            reset();
            isSelect = true;
        }

    }

    public void handleDeleteButton() {
        for (IDrawTools tool : selectedDrawToolsList)
        {
            tool.removeAll();

            allDrawTools.remove(tool);

            if(tool instanceof AngleDraw angle)
            {
                angleDrawList.remove(angle);
            }
            if (tool instanceof TextDraw text)
            {
                textDrawList.remove(text);
            }
            if (tool instanceof DistanceDraw distance)
            {
                distanceDrawList.remove(distance);
            }
            if (tool instanceof RectangleDraw rectangle)
            {
                rectangleDrawsList.remove(rectangle);
            }
            if (tool instanceof EllipseDraw ellipse)
            {
                ellipseDrawList.remove(ellipse);
            }
        }
        selectedDrawToolsList.clear();

        CanvasMainChange.redraw();

    }

    private void makeSelectUIChange() {
        CanvasMainChange.redraw();
        for(IDrawTools tool : selectedDrawToolsList){
            if(tool instanceof AngleDraw angleDraw){
                angleDraw.selected();
            }
            else if(tool instanceof DistanceDraw distanceDraw){
                distanceDraw.selected();
            }
            else if(tool instanceof TextDraw textDraw){
                textDraw.selected();
            }
            else if(tool instanceof RectangleDraw rectangleDraw){
                rectangleDraw.selected();
            }
            else if(tool instanceof EllipseDraw ellipseDraw){
                ellipseDraw.selected();
            }
        }
    }


    //endregion


    //region processing tools

    public void handleBrightnessButton() {
        if(isBrightness){
            reset();
        }
        else{
            reset();
            isBrightness = true;
        }
    }

    public void brightnessChange() {
        if (CanvasMainChange.mainImage != null) {

            logger.info("frame type {}", CvType.typeToString(mainImageMat.type()));

            if (mainImageMat.type() == CvType.CV_8UC1 || mainImageMat.type() == CvType.CV_8UC3) {
                double offset = currentPosition.getX() - clickPosition.getX();
                brightness=(brightnessBefore + offset * 0.1);


                if (brightness >= 100) {
                    brightness=(100);
                } else if (brightness <= -100) {
                    brightness=(-100);
                }
            } else if (mainImageMat.type() == CvType.CV_16UC1 || mainImageMat.type() == CvType.CV_16UC3) {
                double offset = currentPosition.getX() - clickPosition.getX();
                brightness=(brightnessBefore / (65535.0 / 255.0) + offset * 0.1);

                brightness=(brightness * (65535.0 / 255.0)); // Convert to 16-bit range

                if (brightness >= 32767) {
                    brightness =(32767);
                } else if (brightness <= -32768) {
                    brightness=-32768;
                }
            } else {
                throw new IllegalArgumentException("Unsupported image depth: " + mainImageMat.depth());
            }


            imageProcess();
        }
    }


    public void brightnessChangeFromSlider(double sliderValue) {
        if (CanvasMainChange.mainImage != null) {
            logger.info("frame type {}", CvType.typeToString(mainImageMat.type()));

            if (mainImageMat.type() == CvType.CV_8UC1 || mainImageMat.type() == CvType.CV_8UC3) {
                brightness=(sliderValue);

                if (brightness >= 100) {
                    brightness=(100);
                } else if (brightness <= -100) {
                    brightness=(-100);
                }
                brightnessBefore=brightness;
            }
            else if (mainImageMat.type() == CvType.CV_16UC1 || mainImageMat.type() == CvType.CV_16UC3) {
                // Convert slider value to 16-bit equivalent
                brightness=(sliderValue * (65535.0 / 255.0));

                if (brightness >= 32767) {
                    brightness=(32767);
                } else if (brightness <= -32768) {
                    brightness=(-32768);
                }
                brightnessBefore=brightness;
            }
            else {
                throw new IllegalArgumentException("Unsupported image depth: " + mainImageMat.depth());
            }

            imageProcess();
        }

    }


    public void handleContrastButton() {

        if(isContrast){
            reset();
        }
        else{
            reset();
            isContrast = true;

        }
    }

    public void contrastChange(){
        if(CanvasMainChange.mainImage != null){
            double offset = currentPosition.getX() - clickPosition.getX();

            contrast = contrastBefore + offset * 0.00347;

            if(contrast >= 5){
                contrast = 5;
            }
            else if(contrast <= 0){
                contrast = 0;
            }

            imageProcess();
        }
    }


    public void contrastChangeFromSlider(double sliderValue) {
        if(CanvasMainChange.mainImage != null){

            contrast = sliderValue;

            if(contrast >= 5){
                contrast = 5;
            }
            else if(contrast <= 0){
                contrast = 0;
            }

            imageProcess();

            contrastBefore=contrast;
        }
    }

    public void handlePanButton() {
        if(isPan){
            reset();
        }
        else{
            reset();
            isPan = true;
        }
    }

    public void panChange(MouseEvent mouseEvent) {
        if (CanvasMainChange.mainImage != null) {
            // Convert scene deltas into local space (corrected for rotation)
            Point2D startLocal = canvasViewBox.sceneToLocal(startSceneX, startSceneY);
            Point2D currentLocal = canvasViewBox.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            Point2D delta = currentLocal.subtract(startLocal);

            drawingCanvas.setLayoutX(drawingCanvas.getLayoutX() + delta.getX());
            drawingCanvas.setLayoutY(drawingCanvas.getLayoutY() + delta.getY());

            textPane.setLayoutX(textPane.getLayoutX() + delta.getX());
            textPane.setLayoutY(textPane.getLayoutY() + delta.getY());

            // Update start for continuous dragging
            startSceneX = mouseEvent.getSceneX();
            startSceneY = mouseEvent.getSceneY();
        }
    }

    public void handleCropButton() {
        if(isCrop){
            cropLayout.saveCroppedImage();
            imageTop = 0;
            imageLeft = 0;
            handleResetClick();
            logger.info("rect : {}, {} {} {}", cropLayout.rectX, cropLayout.rectY,cropLayout.rectWidth,cropLayout.rectHeight);
            CanvasMainChange.redraw();

            reset();
            CanvasMainChange.disableImageToolsForCrop(false);
        }
        else{
            reset();
            isCrop = true;
            CanvasMainChange.disableImageToolsForCrop(true);
            StudyService.imageFrameResize(CanvasMainChange.mainImage);
            cropLayout.setCanvas(drawingCanvas);
            cropLayout.drawLayout();
            StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart().getBodyPart().setCrop(true);
        }
    }

    public void handleZoomButton() {
        if(isZoom){
            reset();
        }
        else{
            reset();
            isZoom = true;
        }

    }

    public void zoomChange(MouseEvent event){
        if(CanvasMainChange.mainImage != null){

            double delta = zoomStartY - event.getSceneY();
            double zoomChange = 1 + (delta / 300); // gentle zoom
            scaleFactor *= zoomChange;
            scaleFactor = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleFactor));
            zoomStartY = event.getSceneY();

            scale.setX((isHFlip ? -1 : 1)* scaleFactor);
            scale.setY((isVFlip ? -1 : 1)* scaleFactor);
            scale.setPivotX(drawingCanvas.getWidth() / 2);
            scale.setPivotY(drawingCanvas.getHeight() / 2);


            /*double delta = event.getSceneX() - zoomStartX;
            double zoomChange = 1 + (delta / 300); // gentle zoom
            scaleFactor *= zoomChange;
            scaleFactor = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleFactor));
            zoomStartX = event.getSceneX();

            scale.setX((isHFlip ? -1 : 1)* scaleFactor);
            scale.setY((isVFlip ? -1 : 1)* scaleFactor);
            scale.setPivotX(drawingCanvas.getWidth() / 2);
            scale.setPivotY(drawingCanvas.getHeight() / 2);*/
        }
    }



    public void handleHFlip(){
        isHFlip = !isHFlip;

        scale.setX((isHFlip ? -1 : 1)* scaleFactor);
        scale.setY((isVFlip ? -1 : 1)* scaleFactor);
        scale.setPivotX(drawingCanvas.getWidth() / 2);
        scale.setPivotY(drawingCanvas.getHeight() / 2);
    }

    public void handleCW(){
        //angleFactor += 90;
        //imageProcess();
        canvasViewBox.setRotate(canvasViewBox.getRotate() + 90);
        CanvasMainChange.redraw();
    }

    public void handleCCW(){
        /*angleFactor -= 90;
        imageProcess();*/
        canvasViewBox.setRotate(canvasViewBox.getRotate() - 90);
        CanvasMainChange.redraw();
    }

    public void handleInvertButton(){
        isInvert = !isInvert;
        imageProcess();
    }

    public void imageProcess(){
        Mat frame = mainImageMat.clone();

      /*  logger.info("frame type {}", CvType.typeToString(frame.type()));*/

        //logger.info("imaging params {},{},{},{}",brightness,contrast,denoise,sharpness);

        ImagingParams imagingParams = new ImagingParams();
        imagingParams.setWindowLevel(brightness);
        imagingParams.setWindowWidth(contrast);
        imagingParams.setDenoise(denoise);
        imagingParams.setSharpness(sharpness);


        frame = imageProcessService.adjustImageBrightnessAndContrast(frame, imagingParams);
        frame = imageProcessService.rotateImage(frame, -angleFactor);
        frame = imageProcessService.zoomImage(frame, zoomFactor);

        frame = imageProcessService.applyDenoisingLatest(frame, denoise);
        frame = imageProcessService.applySharpnessLatest(frame, sharpness);


        if(isInvert){
            Core.bitwise_not(frame, frame);
        }

        ProcessingToolsService.drawHistogramOnCanvas(frame);

        //Imgcodecs.imwrite("D:\\temp\\frame.png", frame);

        double width = frame.width();
        double height = frame.height();

        if(width>=height){
            height = (1024*height)/width;
            width = 1024;
        }else{
            width = (1024*width)/height;
            height = 1024;
        }

        Imgproc.resize(frame, frame, new Size(width,height));


        CanvasMainChange.displayImage = imageConversionService.matToImage(frame);
        frame.release();
        CanvasMainChange.redraw();
    }




    //endregion




    public void handleHideClick(){
        if(isHide){
            for(IDrawTools tool :  allDrawTools){
                if(tool != null) {
                    tool.unHideAll();
                }
            }
            isHide = false;
        }else{
            for(IDrawTools tool :  allDrawTools){
                if(tool != null) {
                    tool.hideAll();
                }
            }
            isHide = true;
        }
    }

    public void handleResetClick(){

        Image mainImage = CanvasMainChange.mainImage;
        Image displayImage = CanvasMainChange.displayImage;


        reset();
        allDrawTools.clear();

        CanvasMainChange.ellipses.clear();
        CanvasMainChange.lines.clear();
        CanvasMainChange.degreeLabels.clear();
        CanvasMainChange.distanceLabels.clear();
        CanvasMainChange.rectangles.clear();
        CanvasMainChange.ellipseDrawEllipses.clear();
        CanvasMainChange.textLabels.clear();
        CanvasMainChange.customFontIcon=null;

        resetAllDrawingTools();

        CanvasMainChange.disableImageTools();
        MainDrawTool.enableImageTools();

        CanvasMainChange.mainImage = mainImage;
        CanvasMainChange.displayImage = mainImage;
        CanvasMainChange.mainDrawTool.mainImageMat = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getCurrentBodyPart().getBodyPart().getImageMat();


        if(CanvasMainChange.mainImage != null){
            drawingCanvas.setLayoutX(0);
            drawingCanvas.setLayoutY(0);
            scaleFactor = 1.0;

            scale.setX((isHFlip ? -1 : 1)* scaleFactor);
            scale.setY((isVFlip ? -1 : 1)* scaleFactor);
            scale.setPivotX(drawingCanvas.getWidth() / 2);
            scale.setPivotY(drawingCanvas.getHeight() / 2);
        }


        resetImageProcessingParams();
        CanvasMainChange.redraw();

    }

    private void resetImageProcessingParams() {
        brightness = 0;
        contrast = 1;
        zoomFactor = 1;
        angleFactor = 0;
        imageLeft = 0;
        imageTop = 0;

        denoise = 3;
        sharpness = 1;
    }

    public void handleFitClick(){
        reset();

        if(CanvasMainChange.mainImage != null){
            drawingCanvas.setLayoutX(0);
            drawingCanvas.setLayoutY(0);
            scaleFactor = 1.0;

            scale.setX((isHFlip ? -1 : 1)* scaleFactor);
            scale.setY((isVFlip ? -1 : 1)* scaleFactor);
            scale.setPivotX(drawingCanvas.getWidth() / 2);
            scale.setPivotY(drawingCanvas.getHeight() / 2);
        }
    }

    public void reset(){
        isAngleDraw = false;
        isDistanceDraw = false;
        isTextDraw = false;
        isRectangle = false;
        isEllipse = false;
        isR = false;
        isL = false;
        isSelect = false;

        isBrightness = false;
        isContrast = false;
        isPan = false;
        isCrop = false;
        isZoom = false;


        if (!AngleDraw.isComplete)
        {
            AngleDraw angleDraw = angleDrawList.getLast();
            angleDraw.removeAll();
            angleDrawList.remove(angleDraw);
            allDrawTools.remove(angleDraw);

        }

        if (!DistanceDraw.isComplete)
        {
            DistanceDraw distanceDraw = distanceDrawList.getLast();
            distanceDraw.removeAll();
            distanceDrawList.remove(distanceDraw);
            allDrawTools.remove(distanceDraw);

        }

        if (!TextDraw.isComplete)
        {
            TextDraw textDraw = textDrawList.get(textDrawList.size() - 1);
            textDraw.removeAll();
            textDrawList.remove(textDraw);
            allDrawTools.remove(textDraw);
        }

        if (!RectangleDraw.isComplete)
        {
            RectangleDraw rectangleDraw = rectangleDrawsList.get(rectangleDrawsList.size() - 1);
            rectangleDraw.removeAll();
            rectangleDrawsList.remove(rectangleDraw);
            allDrawTools.remove(rectangleDraw);
        }

        if (!EllipseDraw.isComplete)
        {
            EllipseDraw ellipseDraw = ellipseDrawList.get(ellipseDrawList.size() - 1);
            ellipseDraw.removeAll();
            ellipseDrawList.remove(ellipseDraw);
            allDrawTools.remove(ellipseDraw);
        }

        currentTool = null;
        selectedDrawToolsList.clear();



        CanvasMainChange.redraw();
    }

    public void saveImageParams(PatientBodyPart patientBodyPart) {
        ImagingParams imagingParams = patientBodyPart.getImagingParams();
        if(imagingParams==null){
            imagingParams = new ImagingParams();
        }

        imagingParams.setWindowWidth(contrast);
        imagingParams.setWindowLevel(brightness);
        imagingParams.setDenoise(denoise);
        imagingParams.setSharpness(sharpness);
        //imagingParams.setAngleFactor(angleFactor);
        //imagingParams.setZoomFactor(zoomFactor);
        //imagingParams.setHFlip(isHFlip);
        //imagingParams.setVFlip(isVFlip);
        imagingParams.setInvert(isInvert);
        //imagingParams.setImageTop(imageTop);
        //imagingParams.setImageLeft(imageLeft);

        patientBodyPart.setImagingParams(imagingParams);

        CanvasMainChange.disableImageTools();


        CanvasMainChange.redraw();

    }

    public void applyAllImageParams(PatientBodyPart patientBodyPart, StepEntity stepEntity) {
        if(patientBodyPart.getImagingParams()!=null){
            ImagingParams imagingParams = patientBodyPart.getImagingParams();
            brightness=(imagingParams.getWindowLevel());
            contrast = imagingParams.getWindowWidth();
            zoomFactor= imagingParams.getZoomFactor();
            angleFactor = imagingParams.getAngleFactor();
            imageTop = imagingParams.getImageTop();
            imageLeft = imagingParams.getImageLeft();
            isVFlip = imagingParams.isVFlip();
            isHFlip = imagingParams.isHFlip();
            isInvert = imagingParams.isInvert();
            denoise = imagingParams.getDenoise();

            imageProcess();

        }else {
            logger.info("ImagingParams is null");

            if(stepEntity!=null){
                ImageTools imageTools = ImageTools.getInstance();
                ImagingParams imagingParams = new ImagingParams();

                if(stepEntity.getFlip()){
                    isHFlip = imagingParams.isHFlip();
                }else{
                    logger.info("HFlip is false, not applying HFlip");
                }

                if(stepEntity.getLrLabel()!=null){
                    String lrLabel = stepEntity.getLrLabel();
                    logger.info("L/R Label: {}", lrLabel);
                    if(lrLabel.equals("R")) {
                        imageTools.rightLabelClick();
                    }else if(lrLabel.equals("L")) {
                        imageTools.leftLabelClick();
                    }

                }else{
                    logger.info("L/R Label is null, not applying L/R");
                }

                if(stepEntity.getText()!=null){
                    String text = stepEntity.getText();
                    logger.info("Text: {}", text);
                    imageTools.textClick();


                    TextDraw textDraw = new TextDraw();
                    textDraw.setRoot(CanvasMainChange.overlayPane);
                    textDraw.setCanvas(CanvasMainChange.drawingCanvas);

                    double posVal = stepEntity.getLrPos();

                    logger.info("Position Value: {}", posVal);

                    double x,y;
                    switch ((int) posVal){
                        case 1 -> {
                            x=0;
                            y=0;
                        }
                        case 2 -> {
                            x=drawingCanvas.getWidth()-250;
                            y=0;
                        }
                        case 3 -> {
                            x=drawingCanvas.getWidth()-250;
                            y= drawingCanvas.getHeight()-100;
                        }
                        case 4 -> {
                            x=0;
                            y= drawingCanvas.getHeight()-100;
                        }
                        default -> {
                            logger.info("Invalid position value: {}", posVal);
                            x=0;
                            y=0;
                        }
                    }

                    TextLabel textLabel = new TextLabel(text, x,
                            y, Color.valueOf("#66FF00"));
                    textDraw.setTextLabel(textLabel);

                    textDraw.drawTexts();

                    MainDrawTool.textDrawList.add(textDraw);
                    MainDrawTool.allDrawTools.add(textDraw);
                    CanvasMainChange.textLabels.add(textLabel);
                }


                patientBodyPart.setImagingParams(imagingParams);
                imageProcess();
            }
            else{
                logger.info("StepEntity is null, no params to apply");
            }


        }
    }



}
