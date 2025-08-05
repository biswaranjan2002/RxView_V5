package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.print.imageProcessing.PrintImageTools;
import com.raymedis.rxviewui.modules.print.printInput.*;
import com.raymedis.rxviewui.modules.print.Layout.LayoutTab;


import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PrintMainDrawTool {


    public PrintCanvasMainChange printCanvasMainChange;

    private final Canvas drawingCanvas;
    private final StackPane canvasStackPane;
    private final Pane textPane;
    private final Pane canvasPane;
   // public ViewBox canvasViewBox;

    public boolean isPan = false;
    public boolean isZoom = false;
    public boolean isContrast = false;
    public boolean isBrightness = false;
    public boolean isR = false;
    public boolean isL = false;

    public boolean isHFlip = false;
    public boolean isVFlip = false;
    public boolean isInvert = false;

    public boolean isAngleDraw=false;
    public boolean isDistanceDraw=false;
    public boolean isTextDraw=false;

    private boolean isHide = false;
    public  boolean isImageToolEnable = false;
    private boolean isSelect = false;
    public boolean isCrop = false;

    private Point2D clickPosition;
    private Point2D currentPosition;

    private PrintInterfaceDrawTools currentTool;

    public final List<PrintAngleDraw> angleDrawList = new ArrayList<>();
    public final List<PrintDistanceDraw> distanceDrawList = new ArrayList<>();
    public final PrintCropLayout cropLayout = new PrintCropLayout();
    public final List<PrintTextDraw> textDrawList = new ArrayList<>();
    private final List<PrintInterfaceDrawTools> selectedDrawToolsList = new ArrayList<>();
    public final List<PrintInterfaceDrawTools> allDrawTools = new ArrayList<>();
    public PrintLeftRightDraw leftRightDraw = null;


    private double startSceneX, startSceneY;

    private double viewportWidth;
    private double viewportHeight;

    private double zoomStartY;
    private double scaleFactor = 1.0;

    private final double MIN_SCALE = 0.5;
    private final double MAX_SCALE = 5.0;

    public double imageLeft = 0;
    public double imageTop = 0;

    public double brightnessBefore = 0;
    public double contrastBefore = 1;
    public double imageLeftBefore;
    public double imageTopBefore = 0;
    public double zoomFactorBefore = 0;

    public double brightness = 0;
    public double contrast = 1;
    public double zoomFactor = 1;
    public double angleFactor = 0;
    public double denoise = 3;
    public double sharpness = 1;


    private Mat mainImageMat;
    private boolean isDragging = false;
    private final ImageProcessService imageProcessService = new ImageProcessService();

    private final ImageConversionService imageConversionService = new ImageConversionService();
    private final Logger logger = LoggerFactory.getLogger(PrintMainDrawTool.class);


    private LayoutTab layoutTab;

    public Group canvasGroup;
    private Scale scale;

    public PrintMainDrawTool(LayoutTab layoutTab,PrintCanvasMainChange printCanvasMainChange) {

        this.layoutTab=layoutTab;
        this.drawingCanvas = layoutTab.getDrawingCanvas();
        this.canvasStackPane = layoutTab.getCanvasStackPane();
        this.textPane = layoutTab.getTextPane();
        this.printCanvasMainChange = printCanvasMainChange;
        this.canvasPane = layoutTab.getCanvasPane();

        attachCanvasFunction();


        viewportWidth = layoutTab.getGridPaneWidth();
        viewportHeight = layoutTab.getGridPaneHeight();
        canvasGroup = new Group(drawingCanvas);

        Rectangle clip = new Rectangle(0,0,viewportWidth, viewportHeight);
        canvasGroup.setClip(clip);
        canvasPane.getChildren().add(canvasGroup);

        scale = new Scale();
        drawingCanvas.getTransforms().add(scale);
    }

    public void attachCanvasFunction() {
        drawingCanvas.setOnMouseReleased(this::drawingCanvasMouseReleased);
        drawingCanvas.setOnMousePressed(this::drawingCanvasMousePressed);
        drawingCanvas.setOnMouseDragged(this::drawingCanvasMouseDragged);
    }

    private void makeSelectUIChange() {
        printCanvasMainChange.redraw();
        for(PrintInterfaceDrawTools tool : selectedDrawToolsList){
            if(tool instanceof PrintAngleDraw angleDraw){
                angleDraw.selected(printCanvasMainChange);
            }
            else if(tool instanceof PrintDistanceDraw distanceDraw){
                distanceDraw.selected(printCanvasMainChange);
            }
            else if(tool instanceof PrintTextDraw textDraw){
                textDraw.selected(printCanvasMainChange);
            }
        }
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

        if(isPan){
            startSceneX = mouseEvent.getSceneX();
            startSceneY = mouseEvent.getSceneY();
            return;
        }
        else if(isZoom){
            zoomStartY = mouseEvent.getSceneY();
            return;
        }
        else if(isContrast){
            return;
        }
        else if(isBrightness){
            return;
        }
        //endregion


        if(currentTool!=null){
            currentTool.handleMousePressed(mouseEvent);
            //logger.info("current tool pressed");
        }

        Ellipse isEllipseClick = printCanvasMainChange.isEllipse(clickPoint);
        Line isLineClicked = printCanvasMainChange.isLine(clickPoint);
        PrintTextLabel isTextLabelClick = printCanvasMainChange.isTextLabel(clickPoint);
        PrintCustomFontIcon isCustomFontIconClicked = printCanvasMainChange.isCustomFontIcon(clickPoint);


        if(isSelect){
            if(isEllipseClick!=null || isLineClicked != null|| isTextLabelClick != null){

                for(PrintAngleDraw angleDraw : angleDrawList){
                    if(angleDraw.containsElement(isEllipseClick) || angleDraw.containsElement(isLineClicked)){
                        if(!selectedDrawToolsList.contains(angleDraw)){
                            selectedDrawToolsList.add(angleDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(angleDraw);
                        }
                    }
                }

                for(PrintDistanceDraw distanceDraw : distanceDrawList){
                    if(distanceDraw.containsElement(isEllipseClick)|| distanceDraw.containsElement(isLineClicked)){
                        if(!selectedDrawToolsList.contains(distanceDraw)){
                            selectedDrawToolsList.add(distanceDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(distanceDraw);
                        }

                    }
                }

                for(PrintTextDraw textDraw : textDrawList){
                    if(textDraw.containsElement(isTextLabelClick)){
                        if(!selectedDrawToolsList.contains(textDraw)){
                            selectedDrawToolsList.add(textDraw);
                        }
                        else{
                            selectedDrawToolsList.remove(textDraw);
                        }
                    }
                }


            }
            else {
                selectedDrawToolsList.clear();
            }

            makeSelectUIChange();
            return;
        }


        selectedDrawToolsList.clear();


        if(isAngleDraw) {
            if(printCanvasMainChange.isAngleDrawComplete && (isEllipseClick==null) && (isLineClicked == null)){
                printCanvasMainChange.redraw();
                currentTool = new PrintAngleDraw(drawingCanvas,clickPoint,printCanvasMainChange);
                angleDrawList.add((PrintAngleDraw) currentTool);
                allDrawTools.add( currentTool);
                selectedDrawToolsList.add(currentTool);
            }
        }
        else if(isDistanceDraw){
            if(printCanvasMainChange.isDistanceDrawComplete && (isEllipseClick==null) && (isLineClicked == null)){
                printCanvasMainChange.redraw();
                currentTool = new PrintDistanceDraw(drawingCanvas,clickPoint,printCanvasMainChange);
                distanceDrawList.add((PrintDistanceDraw) currentTool);
                allDrawTools.add(currentTool);
                selectedDrawToolsList.add(currentTool);
            }
        }
        else if(isTextDraw){
            if(printCanvasMainChange.isTextDrawComplete && (isTextLabelClick==null)){
                currentTool = new PrintTextDraw(drawingCanvas,clickPoint, textPane,printCanvasMainChange);
                textDrawList.add((PrintTextDraw) currentTool);
                allDrawTools.add(currentTool);
                selectedDrawToolsList.add(currentTool);
                printCanvasMainChange.redraw();
            }
        }
        else if(isR){
            currentTool = leftRightDraw;
            allDrawTools.add(currentTool);
            printCanvasMainChange.redraw();
        }
        else if (isL) {
            currentTool = leftRightDraw;
            allDrawTools.add(currentTool);
            printCanvasMainChange.redraw();
        }


        if(printCanvasMainChange.isAngleDrawComplete && printCanvasMainChange.isDistanceDrawComplete &&
                printCanvasMainChange.isTextDrawComplete && (isTextLabelClick != null || isEllipseClick!=null  || isLineClicked != null ||isCustomFontIconClicked != null)){
            if(isAngleDraw){
                for(PrintAngleDraw angleDraw : angleDrawList){
                    if(angleDraw.containsElement(isEllipseClick) || angleDraw.containsElement(isLineClicked)){
                        currentTool = angleDraw;
                        angleDraw.redraw(printCanvasMainChange);
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isDistanceDraw){
                for(PrintDistanceDraw distanceDraw : distanceDrawList){
                    if(distanceDraw.containsElement(isEllipseClick) || distanceDraw.containsElement(isLineClicked)){
                        currentTool = distanceDraw;
                        distanceDraw.redraw(printCanvasMainChange);
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isTextDraw){
                for(PrintTextDraw textDraw : textDrawList){
                    if(textDraw.containsElement(isTextLabelClick)){
                        currentTool = textDraw;
                        textDraw.drawTexts();
                        currentTool.handleMousePressed(mouseEvent);
                        return;
                    }
                }
            }
            else if(isR){
                if(isCustomFontIconClicked != null) {
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
        else if(printCanvasMainChange.isAngleDrawComplete  && printCanvasMainChange.isDistanceDrawComplete && printCanvasMainChange.isTextDrawComplete){
            currentTool = null;
            printCanvasMainChange.redraw();
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
        if(isPan){
            panChange(mouseEvent);
        }
        else if(isZoom){
            zoomChange(mouseEvent);
        }
        else if(isContrast){
            contrastChange();
        } else if (isBrightness) {
            brightnessChange();
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

        isDragging = false;

    }


    //region ui tools

    public void handlePanButton() {
        if(isPan){
            reset();
        }
        else{
            reset();
            isPan = true;
            isImageToolEnable=true;
        }
    }

    public void handleZoomButton() {
        if(isZoom){
            reset();
        }
        else{
            reset();
            isZoom = true;
            isImageToolEnable=true;
        }
    }

    public void panChange(MouseEvent mouseEvent) {

        // Convert scene deltas into local space (corrected for rotation)
        Point2D startLocal = layoutTab.getDrawingCanvas().sceneToLocal(startSceneX, startSceneY);
        Point2D currentLocal = layoutTab.getDrawingCanvas().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        Point2D delta = currentLocal.subtract(startLocal);

        drawingCanvas.setLayoutX(drawingCanvas.getLayoutX() + delta.getX());
        drawingCanvas.setLayoutY(drawingCanvas.getLayoutY() + delta.getY());

        // Update start for continuous dragging
        startSceneX = mouseEvent.getSceneX();
        startSceneY = mouseEvent.getSceneY();

        //logger.info("dragged startSceneX : {} , startSceneY : {}", startSceneX, startSceneY);

    }

    private void contrastChange() {
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

    private void brightnessChange() {
        double offset = currentPosition.getX() - clickPosition.getX();
        brightness = brightnessBefore + offset * 0.1;
        if(brightness >= 100){
            brightness = 100;
        }
        else if(brightness <= -100){
            brightness = -100;
        }
        imageProcess();
    }

    public void zoomChange(MouseEvent event){
        double delta = zoomStartY - event.getSceneY();
        double zoomChange = 1 + (delta / 300); // gentle zoom
        scaleFactor *= zoomChange;
        scaleFactor = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleFactor));
        zoomStartY = event.getSceneY();

        scale.setX((isHFlip ? -1 : 1)* scaleFactor);
        scale.setY((isVFlip ? -1 : 1)* scaleFactor);
        scale.setPivotX(drawingCanvas.getWidth() / 2);
        scale.setPivotY(drawingCanvas.getHeight() / 2);
    }

    public void handleFitButton() {
        drawingCanvas.setLayoutX((layoutTab.getGridPaneWidth()-drawingCanvas.getWidth() ) / 2);
        drawingCanvas.setLayoutY((layoutTab.getGridPaneHeight()-drawingCanvas.getHeight() ) / 2);
        scaleFactor = 1.0;

        scale.setX((isHFlip ? -1 : 1)* scaleFactor);
        scale.setY((isVFlip ? -1 : 1)* scaleFactor);
        scale.setPivotX(drawingCanvas.getWidth() / 2);
        scale.setPivotY(drawingCanvas.getHeight() / 2);

    }

    public void handleHFlip() {
        isHFlip = !isHFlip;

        scale.setX((isHFlip ? -1 : 1)* scaleFactor);
        scale.setY((isVFlip ? -1 : 1)* scaleFactor);
        scale.setPivotX(drawingCanvas.getWidth() / 2);
        scale.setPivotY(drawingCanvas.getHeight() / 2);
    }

    public void handleCwClick() {
        drawingCanvas.setRotate(drawingCanvas.getRotate() + 90);
        printCanvasMainChange.redraw();
    }

    public void handleCcwClick() {
        drawingCanvas.setRotate(drawingCanvas.getRotate() - 90);
        printCanvasMainChange.redraw();
    }

    public void handleContrastButton() {
        if(isContrast){
            reset();
        }
        else{
            reset();
            isContrast = true;
            isImageToolEnable=true;
        }
    }

    public void handleBrightnessButton() {
        if(isBrightness){
            reset();
        }
        else{
            reset();
            isBrightness = true;
            isImageToolEnable=true;
        }
    }

    public void handleInversionButton() {
        isInvert = !isInvert;
        imageProcess();
    }

    public void reset(){
        isPan = false;
        isZoom = false;
        isImageToolEnable = false;
        isContrast = false;
        isBrightness=false;
        isR = false;
        isL= false;

        isAngleDraw=false;
        isDistanceDraw=false;
        isTextDraw=false;
        isCrop=false;

        isSelect= false;


        if (!printCanvasMainChange.isAngleDrawComplete)
        {
            PrintAngleDraw angleDraw = angleDrawList.getLast();
            angleDraw.removeAll();
            angleDrawList.remove(angleDraw);
            allDrawTools.remove(angleDraw);
            currentTool = null;
        }

        if (!printCanvasMainChange.isDistanceDrawComplete)
        {
            PrintDistanceDraw distanceDraw = distanceDrawList.getLast();
            distanceDraw.removeAll();
            distanceDrawList.remove(distanceDraw);
            allDrawTools.remove(distanceDraw);
            currentTool = null;
        }

        if (!printCanvasMainChange.isTextDrawComplete)
        {
            PrintTextDraw textDraw = textDrawList.getLast();
            textDraw.removeAll();
            textDrawList.remove(textDraw);
            allDrawTools.remove(textDraw);
        }


        currentTool = null;
        selectedDrawToolsList.clear();
        printCanvasMainChange.redraw();
    }


    public double getImageLeft() {
        return imageLeft;
    }

    public double getImageTop() {
        return imageTop;
    }

    public void setMainImageMat(Mat newOrg) {
        mainImageMat = newOrg;
    }

    public Mat getMainImageMat() {
        return mainImageMat;
    }



    public void imageProcess(){
        Mat frame = mainImageMat.clone();

        ImagingParams imagingParams = new ImagingParams();
        imagingParams.setWindowLevel(brightness);
        imagingParams.setWindowWidth(contrast);
        imagingParams.setDenoise(denoise);
        imagingParams.setSharpness(sharpness);


        frame = imageProcessService.adjustImageBrightnessAndContrast(frame, imagingParams);
        frame = imageProcessService.rotateImage(frame, -angleFactor);
        frame = imageProcessService.zoomImage(frame, zoomFactor);


        if(isInvert){
            Core.bitwise_not(frame, frame);
        }

        if(frame != null){
            printCanvasMainChange.setDisplayImage(imageConversionService.matToImage(frame));
            frame.release();
        }

        printCanvasMainChange.redraw();
    }

    public void rightLabelClick() {

        if(isR){
            reset();
            leftRightDraw.removeAll();
            allDrawTools.remove( leftRightDraw);
            leftRightDraw = null;
            printCanvasMainChange.redraw();
        }
        else{
            reset();
            if(leftRightDraw == null){
                leftRightDraw = new PrintLeftRightDraw("RIGHT", drawingCanvas,printCanvasMainChange);
                allDrawTools.add(leftRightDraw);
            }else {
                if(leftRightDraw.getRlIcon()==null){
                    leftRightDraw = new PrintLeftRightDraw("RIGHT", drawingCanvas,printCanvasMainChange);
                }
            }
            leftRightDraw.setIcon("RIGHT");
            isR = true;
            leftRightDraw.redraw();


            isImageToolEnable=true;
        }

    }

    public void leftLabelClick() {
        if(isL){
            reset();
            leftRightDraw.removeAll();
            allDrawTools.remove(leftRightDraw);
            leftRightDraw = null;
            printCanvasMainChange.redraw();
        }
        else{
            reset();
            if(leftRightDraw == null){
                leftRightDraw = new PrintLeftRightDraw("LEFT", drawingCanvas,printCanvasMainChange);
                allDrawTools.add(leftRightDraw);
            }
            else {
                if(leftRightDraw.getRlIcon()==null){
                    leftRightDraw = new PrintLeftRightDraw("LEFT", drawingCanvas,printCanvasMainChange);
                }
            }
            leftRightDraw.setIcon("LEFT");
            isL = true;
            leftRightDraw.redraw();

            isImageToolEnable=true;
        }
    }

    public void angleClick() {
        if(isAngleDraw){
            reset();
        }
        else{
            reset();
            isAngleDraw = true;
            isImageToolEnable=true;
        }
    }

    public void distanceClick() {
        if(isDistanceDraw){
            reset();
        }
        else{
            reset();
            isDistanceDraw = true;
            isImageToolEnable=true;
        }
    }

    public void textClick() {
        if(isTextDraw){
            reset();
        }
        else{
            reset();
            isTextDraw = true;
            isImageToolEnable=true;
        }
    }


    public void handleHideClick() {
        if(isHide){
            for(PrintInterfaceDrawTools tool :  allDrawTools){
                if(tool != null) {
                    tool.unHideAll();
                }
            }
            isHide = false;
        }else{
            for(PrintInterfaceDrawTools tool :  allDrawTools){
                if(tool != null) {
                    tool.hideAll(printCanvasMainChange);
                }
            }
            isHide = true;
        }
    }

    public void resetClick() {

/*
        reset();*/
        allDrawTools.clear();

        printCanvasMainChange.ellipses.clear();
        printCanvasMainChange.lines.clear();
        printCanvasMainChange.degreeLabels.clear();
        printCanvasMainChange.distanceLabels.clear();
        printCanvasMainChange.rectangles.clear();
        printCanvasMainChange.ellipseDrawEllipses.clear();
        printCanvasMainChange.textLabels.clear();
        printCanvasMainChange.customFontIcon=null;

        resetAllDrawingTools();

        isImageToolEnable = true;

        printCanvasMainChange.mainImage = imageConversionService.matToImage(mainImageMat);
        printCanvasMainChange.displayImage = imageConversionService.matToImage(mainImageMat);;


        printCanvasMainChange.redraw();
    }

    public void resetAllDrawingTools(){
        currentTool=null;
        leftRightDraw=null;
        distanceDrawList.clear();
        angleDrawList.clear();
        textDrawList.clear();
        allDrawTools.clear();
        printCanvasMainChange.clearCanvas();
        reset();
    }


    public void selectClick() {
        if(isSelect){
            reset();
        }
        else{
            reset();
            isSelect = true;
            isImageToolEnable=true;
        }
    }

    public void deleteClick() {

        for (PrintInterfaceDrawTools tool : selectedDrawToolsList)
        {
            tool.removeAll();

            allDrawTools.remove(tool);

            if(tool instanceof PrintAngleDraw angle)
            {
                angleDrawList.remove(angle);
            }
            if (tool instanceof PrintTextDraw text)
            {
                textDrawList.remove(text);
            }
            if (tool instanceof PrintDistanceDraw distance)
            {
                distanceDrawList.remove(distance);
            }

        }
        selectedDrawToolsList.clear();
        printCanvasMainChange.redraw();

    }


    public Rect finalcropRect = new Rect();
    public double imageCropDisplayWidth = 0;

    public void cropClick() {
        Rect rect = layoutTab.getRect();
        double cropMultiple1024_final=1;
        if(isCrop){
            cropLayout.saveCroppedImage();

            double displayToOrgMultiple = mainImageMat.width()/imageCropDisplayWidth;


            rect.x = (int) (finalcropRect.x*displayToOrgMultiple);
            rect.y = (int) (finalcropRect.y*displayToOrgMultiple);
            rect.width = (int) (finalcropRect.width*displayToOrgMultiple);
            rect.height = (int) (finalcropRect.height*displayToOrgMultiple);


            double ratio = layoutTab.getGridPaneWidth() / layoutTab.getGridPaneHeight();
            double targetRatio = (double) rect.width /rect.height;


            double scaleX = 1;
            double scaleY = 1;

            if (targetRatio < ratio) {
                scaleX = layoutTab.getGridPaneHeight() / rect.height;
                scaleY = layoutTab.getGridPaneHeight() / rect.height;
            } else if (targetRatio > ratio) {
                scaleX = layoutTab.getGridPaneWidth() / rect.width;
                scaleY = layoutTab.getGridPaneWidth() / rect.width;
            }
            else if (targetRatio == ratio) {
                scaleX = layoutTab.getGridPaneWidth() / rect.width;
                scaleY = layoutTab.getGridPaneWidth() / rect.width;
            }

            double width = rect.width*scaleX;
            double height = rect.height*scaleY;





            layoutTab.scaleX = scaleX;
            layoutTab.scaleY = scaleY;


            layoutTab.applyImageFrameResize(width,height);


            imageTop = 0;
            imageLeft = 0;
            resetClick();
            reset();
            PrintImageTools.getInstance().resetUiForCrop(false);
        }
        else{
            handleFitButton();
            reset();
            isCrop = true;
            isImageToolEnable=true;
            PrintImageTools.getInstance().resetUiForCrop(true);
            Mat temp =  new Mat();


            //get the new w and h which fits in layout grid pane

            double ratio = layoutTab.getGridPaneWidth() / layoutTab.getGridPaneHeight();
            double targetRatio = (double) mainImageMat.width() /mainImageMat.height();


            double scaleX = 1;
            double scaleY = 1;

            if (targetRatio < ratio) {
                scaleX = layoutTab.getGridPaneHeight() / mainImageMat.height();
                scaleY = layoutTab.getGridPaneHeight() / mainImageMat.height();
            } else if (targetRatio > ratio) {
                scaleX = layoutTab.getGridPaneWidth() / mainImageMat.width();
                scaleY = layoutTab.getGridPaneWidth() / mainImageMat.width();
            }
            else if (targetRatio == ratio) {
                scaleX = layoutTab.getGridPaneWidth() / mainImageMat.width();
                scaleY = layoutTab.getGridPaneWidth() / mainImageMat.width();
            }

            imageCropDisplayWidth = mainImageMat.width()*scaleX;
            double height = mainImageMat.height()*scaleY;


            Imgproc.resize(mainImageMat.clone(),temp, new Size(imageCropDisplayWidth,height),Imgproc.INTER_AREA);
            printCanvasMainChange.displayImage = ImageConversionService.getInstance().matToImage(temp);

            rect.width= (int) imageCropDisplayWidth;
            rect.height= (int) height;
            rect.x = 0;
            rect.y = 0;

            layoutTab.applyImageFrameResize(imageCropDisplayWidth,height);

            Rect rect1024 = new Rect((int) printCanvasMainChange.getLayoutTab().patientPrintImageData.getRectX(), (int) printCanvasMainChange.getLayoutTab().patientPrintImageData.getRectY(), (int) printCanvasMainChange.getLayoutTab().patientPrintImageData.getRectWidth(), (int) printCanvasMainChange.getLayoutTab().patientPrintImageData.getRectHeight());

            cropMultiple1024_final = printCanvasMainChange.getDisplayImage().getWidth() /printCanvasMainChange.getLayoutTab().getPatientPrintImageData().getEditedMat().width();


            finalcropRect.x = (int)(rect1024.x*cropMultiple1024_final);
            finalcropRect.y=(int)(rect1024.y*cropMultiple1024_final);
            finalcropRect.width = (int)(rect1024.width*cropMultiple1024_final);
            finalcropRect.height = (int)(rect1024.height*cropMultiple1024_final);


            cropLayout.setCanvas(drawingCanvas);
            cropLayout.drawLayout(printCanvasMainChange);
            layoutTab.getPatientPrintImageData().setCrop(true);

        }
    }

    public void applyAllImageParams(PatientPrintImageData patientPrintImageData) {
        if(patientPrintImageData!=null){
            brightness = patientPrintImageData.getWindowLevel();
            contrast = patientPrintImageData.getWindowWidth();
            zoomFactor= patientPrintImageData.getZoomFactor();
            angleFactor = patientPrintImageData.getAngleFactor();

            denoise = patientPrintImageData.getDenoise();
            sharpness = patientPrintImageData.getSharpness();


            isVFlip = patientPrintImageData.isVFlip();
            isHFlip = patientPrintImageData.isHFlip();
            isInvert = patientPrintImageData.isInvert();


            imageProcess();
        }
        else
        {
            logger.info("PatientPrintImageData is null");
        }
    }

    public void saveImageParams() {
        layoutTab.getPatientPrintImageData().setWindowWidth(contrast);
        layoutTab.getPatientPrintImageData().setWindowLevel(brightness);
        layoutTab.getPatientPrintImageData().setSharpness(sharpness);
        layoutTab.getPatientPrintImageData().setDenoise(denoise);

        layoutTab.getPatientPrintImageData().setInvert(isInvert);

        printCanvasMainChange.disableImageTools();
        printCanvasMainChange.redraw();

    }


    private final List<PatientPrintAngleDrawWrapper> angleDrawWrapperList = new ArrayList<>();
    private final List<PatientPrintDistanceDrawWrapper> distanceDrawWrapperList = new ArrayList<>();
    private final List<PatientPrintTextDrawWrapper> textDrawWrapperList = new ArrayList<>();

    public void saveImageAnnotations() {
        angleDrawWrapperList.clear();
        distanceDrawWrapperList.clear();
        textDrawWrapperList.clear();

        for(PrintAngleDraw angleDraw : this.angleDrawList){

            PatientPrintAngleDrawWrapper patientPrintAngleDrawWrapper = new PatientPrintAngleDrawWrapper();

            List<PatientPrintLineWrapper> lineWrapperList = new ArrayList<>();
            List<PatientPrintEllipseWrapper> ellipseWrapperList = new ArrayList<>();

            for(Line line : angleDraw.getLines()){
                lineWrapperList.add(new PatientPrintLineWrapper((line.getStartX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (line.getStartY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (line.getEndX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (line.getEndY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple())));
            }

            for(Ellipse ellipse : angleDraw.getEllipses()){
                ellipseWrapperList.add(new PatientPrintEllipseWrapper((ellipse.getCenterX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (ellipse.getCenterY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), ellipse.getRadiusX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple(), ellipse.getRadiusY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));
            }

            PatientPrintLabelWrapper patientPrintLabelWrapper = new PatientPrintLabelWrapper(angleDraw.getDegreeLabel().getAngle(), (angleDraw.getDegreeLabel().getX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (angleDraw.getDegreeLabel().getY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));

            patientPrintAngleDrawWrapper.setEllipsesWrapperList(ellipseWrapperList);
            patientPrintAngleDrawWrapper.setLineWrapperList(lineWrapperList);
            patientPrintAngleDrawWrapper.setLabelWrapper(patientPrintLabelWrapper);

            angleDrawWrapperList.add(patientPrintAngleDrawWrapper);
        }

        for(PrintDistanceDraw distanceDraw:this.distanceDrawList){
            PatientPrintDistanceDrawWrapper patientPrintDistanceDrawWrapper = new PatientPrintDistanceDrawWrapper();


            PatientPrintLineWrapper patientPrintLineWrapper = new PatientPrintLineWrapper((distanceDraw.getLine().getStartX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (distanceDraw.getLine().getStartY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (distanceDraw.getLine().getEndX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (distanceDraw.getLine().getEndY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));

            List<PatientPrintEllipseWrapper> ellipseWrapperList = new ArrayList<>();
            for(Ellipse ellipse : distanceDraw.getEllipses()){
                ellipseWrapperList.add(new PatientPrintEllipseWrapper((ellipse.getCenterX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (ellipse.getCenterY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (ellipse.getRadiusX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), ellipse.getRadiusY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));
            }

            PatientPrintLabelWrapper patientPrintLabelWrapper = new PatientPrintLabelWrapper(distanceDraw.getPrintDistanceLabel().getDistance(), (distanceDraw.getPrintDistanceLabel().getX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (distanceDraw.getPrintDistanceLabel().getY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));

            patientPrintDistanceDrawWrapper.setEllipses(ellipseWrapperList);
            patientPrintDistanceDrawWrapper.setLines(patientPrintLineWrapper);
            patientPrintDistanceDrawWrapper.setLabelWrapper(patientPrintLabelWrapper);

            distanceDrawWrapperList.add(patientPrintDistanceDrawWrapper);
        }

        for(PrintTextDraw textDraw : this.textDrawList){
            PatientPrintTextDrawWrapper labelWrapper = new PatientPrintTextDrawWrapper(textDraw.getTextLabel().getText(), (textDraw.getTextLabel().getX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()), (textDraw.getTextLabel().getY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));
            textDrawWrapperList.add(labelWrapper);
        }

        if(leftRightDraw!=null){
            layoutTab.getPatientPrintImageData().setOffsetX((leftRightDraw.getRlIcon().getX()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));
            layoutTab.getPatientPrintImageData().setOffsetY((leftRightDraw.getRlIcon().getY()/printCanvasMainChange.printDynamicCanvasElementsResize.getMultiple()));
            layoutTab.getPatientPrintImageData().setText(leftRightDraw.getText());
        }
        else{
            layoutTab.getPatientPrintImageData().setText(null);
        }

        layoutTab.getPatientPrintImageData().setPatientPrintAngleDrawWrappers(angleDrawWrapperList);
        layoutTab.getPatientPrintImageData().setPatientPrintDistanceDrawWrappers(distanceDrawWrapperList);
        layoutTab.getPatientPrintImageData().setPatientPrintTextDrawWrappers(textDrawWrapperList);




        layoutTab.getPatientPrintImageData().setRectX(layoutTab.getRect().x/layoutTab.cropMultiple);
        layoutTab.getPatientPrintImageData().setRectY(layoutTab.getRect().y/layoutTab.cropMultiple);
        layoutTab.getPatientPrintImageData().setRectWidth(  layoutTab.getRect().width/layoutTab.cropMultiple);
        layoutTab.getPatientPrintImageData().setRectHeight( layoutTab.getRect().height/layoutTab.cropMultiple);

    }
}

