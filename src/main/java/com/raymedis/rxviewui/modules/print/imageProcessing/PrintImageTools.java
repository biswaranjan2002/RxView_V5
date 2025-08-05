package com.raymedis.rxviewui.modules.print.imageProcessing;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.modules.print.Layout.HandleLayoutImageProcessing;
import com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource.PrintMainDrawTool;
import com.raymedis.rxviewui.modules.print.printPage.PrintPageHandler;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import static com.raymedis.rxviewui.service.study.StudyService.logger;

public class PrintImageTools {

    private static final PrintImageTools instance = new PrintImageTools();
    public static PrintImageTools getInstance() {
        return instance;
    }

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

    public void disableTools(boolean b){
        Platform.runLater(()->{
            toolsBox.setDisable(b);
        });
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

    private void setHide(boolean b) {

         HandleLayoutImageProcessing handleLayoutImageProcessing = PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing();

        handleLayoutImageProcessing.getPrintMainDrawTool().handleHideClick();

        logger.info("PrintImageTools setHide");

        handleLayoutImageProcessing.getPrintCanvasMainChange().redraw();
        resetImageTools();
        handleLayoutImageProcessing.getPrintMainDrawTool().reset();

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



    public void panClick() {
        if(isPanBtn){
            resetImageTools();
            PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                    getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handlePanButton();
        }
        else{
            resetImageTools();
            isPanBtn = true;
            PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                    getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handlePanButton();
            panButton.setId("selectedImageToolsButton");
        }
    }

    public void zoomClick() {
        if(isZoomBtn){
            resetImageTools();
            PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                    getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleZoomButton();
        }
        else
        {
            resetImageTools();
            isZoomBtn = true;
            PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                    getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleZoomButton();
            zoomButton.setId("selectedImageToolsButton");
        }
    }

    public void fitClick() {
        resetImageTools();
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().
                handleFitButton();

        loadToolsForSelectedLayout(PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool());
    }

    public void hFlipClick() {
        if (isHorizontalFlipBtn) {
            isHorizontalFlipBtn = false;
            hFlipButton.setId("unSelectedImageToolsButton");
        } else {
            isHorizontalFlipBtn = true;
            hFlipButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleHFlip();
    }

    public void cwClick() {
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleCwClick();
    }

    public void ccwClick() {
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleCcwClick();
    }

    public void contrastClick() {
        if(isContrastBtn){
            resetImageTools();
        }
        else{
            resetImageTools();
            isContrastBtn = true;
            contrastButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleContrastButton();
    }

    public void brightnessClick() {
        if(isBrightnessBtn){
            resetImageTools();
        }
        else{
            resetImageTools();
            isBrightnessBtn = true;
            brightnessButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleBrightnessButton();
    }

    public void inversionClick() {
        if(isInversionBtn){
            isInversionBtn = false;
            inversionButton.setId("unSelectedImageToolsButton");
        }
        else{
            isInversionBtn = true;
            inversionButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().handleInversionButton();
    }


    public void rightLabelClick() {
        if(isR_Btn){
            resetImageTools();
        }else{
            resetImageTools();
            isR_Btn = true;
            rightLabelButton.setId("selectedImageToolsButton");
        }
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().rightLabelClick();
    }

    public void leftLabelClick() {
        if(isL_Btn){
            resetImageTools();
        }else{
            resetImageTools();
            isL_Btn = true;
            leftLabelButton.setId("selectedImageToolsButton");
        }
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().leftLabelClick();
    }

    public void angleClick() {
        if(isAngleBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isAngleBtn = true;
            angleButton.setId("selectedImageToolsButton");
        }
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().angleClick();
    }

    public void distanceClick() {
        if(isDistanceBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isDistanceBtn = true;
            distanceButton.setId("selectedImageToolsButton");
        }
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().distanceClick();
    }



    public void loadToolsForSelectedLayout(PrintMainDrawTool selectedPrintMainDrawTool){



        if(selectedPrintMainDrawTool==null || selectedPrintMainDrawTool.printCanvasMainChange.displayImage==null){
            PrintImageTools.getInstance().disableTools(true);
            return;
        }


        resetUi();
        PrintImageTools.getInstance().disableTools(false);

        if (selectedPrintMainDrawTool.isHFlip) {
            isHorizontalFlipBtn = true;
            hFlipButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isInvert){
            isInversionBtn = true;
            inversionButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isZoom){
            isZoomBtn = true;
            zoomButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isPan){
            isPanBtn = true;
            panButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isBrightness){
            isBrightnessBtn = true;
            brightnessButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isContrast){
            isContrastBtn = true;
            contrastButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isR){
            isR_Btn = true;
            rightLabelButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isAngleDraw){
            isAngleBtn = true;
            angleButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isDistanceDraw){
            isDistanceBtn = true;
            distanceButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isTextDraw){
            isTextBtn = true;
            textButton.setId("selectedImageToolsButton");
        }
        if(selectedPrintMainDrawTool.isCrop){
            isCropBtn = true;
            cropButton.setId("selectedImageToolsButton");
        }

    }


    public void textClick() {
        if(isTextBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isTextBtn = true;
            textButton.setId("selectedImageToolsButton");
        }
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().textClick();
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

    public void resetClick() {
        resetImageTools();
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().resetClick();
    }

    public void selectClick() {
        if(isSelectBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isSelectBtn = true;
            selectButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().selectClick();

    }

    public void deleteClick() {
        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().deleteClick();

    }

    public void cropClick() {
        if(isCropBtn){
            resetImageTools();
        }else{
            resetImageTools();
            isCropBtn = true;
            cropButton.setId("selectedImageToolsButton");
        }

        PrintPageHandler.getInstance().getCurrentPrintPage().getLayoutTabHandler().
                getSelectedLayout().getHandleLayoutImageProcessing().getPrintMainDrawTool().cropClick();

    }
}
