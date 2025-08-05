package com.raymedis.rxviewui.modules.ImageProcessing;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.MainDrawTool;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ImageTools {

    private static final ImageTools instance = new ImageTools();

    public JFXButton selectButton;
    public JFXButton zoomButton;
    public JFXButton panButton;
    public JFXButton brightnessButton;
    public JFXButton contrastButton;
    public JFXButton hFlipButton;
    public JFXButton inversionButton;
    public JFXButton clockwiseRotationButton;
    public JFXButton counterClockWiseRotationButton;
    public JFXButton fitButton;
    public JFXButton distanceButton;
    public JFXButton angleButton;
    public JFXButton rightLabelButton;
    public JFXButton leftLabelButton;
    public JFXButton hideButton;
    public JFXButton deleteButton;
    public JFXButton textButton;
    public VBox toolsBox;
    public JFXButton cropButton;
    public JFXButton resetButton;
    public ViewBox canvasViewBox;
    public Pane canvasPane;


    public void disableTools(boolean b){
        Platform.runLater(()->{
            toolsBox.setDisable(b);
        });
    }


    public Boolean isSelectBtn = false;
    public Boolean isZoomBtn = false;
    public Boolean isPanBtn= false;
    public Boolean isBrightnessBtn= false;
    public Boolean isContrastBtn = false;
    public Boolean isCropBtn = false;
    public Boolean isHorizontalFlipBtn= false;
    public Boolean isInversionBtn= false;
    public Boolean isCWBtn= false;
    public Boolean isCCWBtn= false;
    public Boolean isFitBtn= false;
    public Boolean isDistanceBtn= false;
    public Boolean isAngleBtn= false;
    public Boolean isR_Btn= false;
    public Boolean isL_Btn= false;
    public Boolean isHideBtn= false;
    public Boolean isDeleteBtn= false;
    public Boolean isTextBtn= false;


    public StackPane canvasStackPane;
    public Canvas drawingCanvas;
    public Pane textPane;



    public MainDrawTool mainDrawTool ;

    public void setUiTools(){
        mainDrawTool = new MainDrawTool(drawingCanvas,canvasStackPane,textPane, canvasPane);
        mainDrawTool.canvasViewBox=canvasViewBox;
        CanvasMainChange.mainDrawTool = mainDrawTool;
        CanvasMainChange.imageTools = this;
    }

    public static ImageTools getInstance() {
        return instance;
    }

    private void resetImageTools() {
        isZoomBtn = false;
        isPanBtn = false;
        isBrightnessBtn = false;
        isContrastBtn = false;
        isCropBtn=false;
        isDistanceBtn = false;
        isAngleBtn = false;
        isTextBtn = false;
        isR_Btn = false;
        isL_Btn = false;
        isSelectBtn = false;

        zoomButton.setId("unSelectedImageToolsButton");
        panButton.setId("unSelectedImageToolsButton");
        brightnessButton.setId("unSelectedImageToolsButton");
        contrastButton.setId("unSelectedImageToolsButton");
        cropButton.setId("unSelectedImageToolsButton");
        distanceButton.setId("unSelectedImageToolsButton");
        angleButton.setId("unSelectedImageToolsButton");
        textButton.setId("unSelectedImageToolsButton");
        rightLabelButton.setId("unSelectedImageToolsButton");
        leftLabelButton.setId("unSelectedImageToolsButton");
        selectButton.setId("unSelectedImageToolsButton");
    }


    public void resetUiForCrop(boolean isCropping){
        isSelectBtn = false;
        isZoomBtn = false;
        isPanBtn= false;
        isFitBtn= false;
        isBrightnessBtn= false;
        isContrastBtn = false;
        isInversionBtn= false;
        isCWBtn= false;
        isCCWBtn= false;
        isHorizontalFlipBtn= false;
        isDistanceBtn= false;
        isAngleBtn= false;
        isR_Btn= false;
        isL_Btn= false;
        isTextBtn= false;
        isHideBtn= false;
        isDeleteBtn= false;

        selectButton.setId("unSelectedImageToolsButton");
        zoomButton.setId("unSelectedImageToolsButton");
        panButton.setId("unSelectedImageToolsButton");
        fitButton.setId("unSelectedImageToolsButton");
        brightnessButton.setId("unSelectedImageToolsButton");
        contrastButton.setId("unSelectedImageToolsButton");
        inversionButton.setId("unSelectedImageToolsButton");
        clockwiseRotationButton.setId("unSelectedImageToolsButton");
        counterClockWiseRotationButton.setId("unSelectedImageToolsButton");
        hFlipButton.setId("unSelectedImageToolsButton");
        distanceButton.setId("unSelectedImageToolsButton");
        angleButton.setId("unSelectedImageToolsButton");
        rightLabelButton.setId("unSelectedImageToolsButton");
        leftLabelButton.setId("unSelectedImageToolsButton");
        textButton.setId("unSelectedImageToolsButton");
        hideButton.setId("unSelectedImageToolsButton");
        deleteButton.setId("unSelectedImageToolsButton");

        selectButton.setDisable(isCropping);
        zoomButton.setDisable(isCropping);
        panButton.setDisable(isCropping);
        fitButton.setDisable(isCropping);
        brightnessButton.setDisable(isCropping);
        contrastButton.setDisable(isCropping);
        inversionButton.setDisable(isCropping);
        clockwiseRotationButton.setDisable(isCropping);
        counterClockWiseRotationButton.setDisable(isCropping);
        hFlipButton.setDisable(isCropping);
        distanceButton.setDisable(isCropping);
        angleButton.setDisable(isCropping);
        rightLabelButton.setDisable(isCropping);
        leftLabelButton.setDisable(isCropping);
        textButton.setDisable(isCropping);
        hideButton.setDisable(isCropping);
        deleteButton.setDisable(isCropping);

    }




    public void resetUi(){
        isSelectBtn = false;
        isZoomBtn = false;
        isPanBtn= false;
        isFitBtn= false;
        isBrightnessBtn= false;
        isContrastBtn=false;
        isCropBtn = false;
        isInversionBtn= false;
        isCWBtn= false;
        isCCWBtn= false;
        isHorizontalFlipBtn= false;
        isDistanceBtn= false;
        isAngleBtn= false;
        isR_Btn= false;
        isL_Btn= false;
        isTextBtn= false;
        isHideBtn= false;
        isDeleteBtn= false;

        selectButton.setId("unSelectedImageToolsButton");
        zoomButton.setId("unSelectedImageToolsButton");
        panButton.setId("unSelectedImageToolsButton");
        fitButton.setId("unSelectedImageToolsButton");
        brightnessButton.setId("unSelectedImageToolsButton");
        contrastButton.setId("unSelectedImageToolsButton");
        cropButton.setId("unSelectedImageToolsButton");
        inversionButton.setId("unSelectedImageToolsButton");
        clockwiseRotationButton.setId("unSelectedImageToolsButton");
        counterClockWiseRotationButton.setId("unSelectedImageToolsButton");
        hFlipButton.setId("unSelectedImageToolsButton");
        distanceButton.setId("unSelectedImageToolsButton");
        angleButton.setId("unSelectedImageToolsButton");
        rightLabelButton.setId("unSelectedImageToolsButton");
        leftLabelButton.setId("unSelectedImageToolsButton");
        textButton.setId("unSelectedImageToolsButton");
        hideButton.setId("unSelectedImageToolsButton");
        deleteButton.setId("unSelectedImageToolsButton");
    }

    public void textClick() {
        if(isTextBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isTextBtn = true;
            textButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleTextButton();
    }

    public void angleClick() {
        if(isAngleBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isAngleBtn = true;
            angleButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleAngleButton();
    }

    public void distanceClick() {
        if(isDistanceBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isDistanceBtn = true;
            distanceButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleDistanceButton();
    }


    public void rightLabelClick() {
        if(isR_Btn){
            resetImageTools();
        }else{
            resetImageTools();
            isR_Btn = true;
            rightLabelButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleRButton();
    }

    public void leftLabelClick() {
        if(isL_Btn){
            resetImageTools();
        }else{
            resetImageTools();
            isL_Btn = true;
            leftLabelButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleLButton();
    }

    public void selectClick() {
        if(isSelectBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isSelectBtn = true;
            selectButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleSelectButton();
    }


    public void deleteClick() {
        mainDrawTool.handleDeleteButton();
    }


    public void imageBrightness() {
        if(isBrightnessBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isBrightnessBtn = true;
            brightnessButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleBrightnessButton();
    }

    public void cropClick() {
        if(isCropBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isCropBtn = true;
            cropButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleCropButton();
    }

    public void panClick() {
        if(isPanBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isPanBtn = true;
            panButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handlePanButton();
    }

    public void zoomClick() {
        if(isZoomBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isZoomBtn = true;
            zoomButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleZoomButton();
    }

    public void inversionClick() {
        if (isInversionBtn) {
            isInversionBtn = false;
            inversionButton.setId("unSelectedImageToolsButton");
        } else {
            isInversionBtn = true;
            inversionButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleInvertButton();
    }

    public void hFlipClick() {
        if (isHorizontalFlipBtn) {
            isHorizontalFlipBtn = false;
            hFlipButton.setId("unSelectedImageToolsButton");
        } else {
            isHorizontalFlipBtn = true;
            hFlipButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleHFlip();
    }

    public void hideClick() {
        if (isHideBtn) {
            isHideBtn = false;
            hideButton.setId("unSelectedImageToolsButton");
            setHide(false);
        } else {
            isHideBtn = true;
            hideButton.setId("selectedImageToolsButton");
            setHide(true);
        }
    }

    private void setHide(boolean b) {
        mainDrawTool.handleHideClick();
        CanvasMainChange.redraw();
        resetImageTools();
        mainDrawTool.reset();

        selectButton.setDisable(b);
        zoomButton.setDisable(b);
        panButton.setDisable(b);
        brightnessButton.setDisable(b);
        cropButton.setDisable(b);
        contrastButton.setDisable(b);
        hFlipButton.setDisable(b);
        inversionButton.setDisable(b);
        clockwiseRotationButton.setDisable(b);
        counterClockWiseRotationButton.setDisable(b);
        fitButton.setDisable(b);
        distanceButton.setDisable(b);
        angleButton.setDisable(b);
        rightLabelButton.setDisable(b);
        leftLabelButton.setDisable(b);
        textButton.setDisable(b);
        deleteButton.setDisable(b);
        resetButton.setDisable(b);
    }

    public void clockwiseRotation() {
        mainDrawTool.handleCW();
    }

    public void counterClockwiseRotation() {
        mainDrawTool.handleCCW();
    }

    public void fitClick() {
        mainDrawTool.handleFitClick();
        resetUi();
    }

    public void contrastClick() {
        if(isContrastBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isContrastBtn = true;
            contrastButton.setId("selectedImageToolsButton");
        }
        mainDrawTool.handleContrastButton();
    }

    public void resetClick() {
        mainDrawTool.handleResetClick();
    }
}
