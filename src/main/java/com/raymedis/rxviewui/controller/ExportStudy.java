package com.raymedis.rxviewui.controller;


import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CropLayout;
import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.CropLayoutWrapper;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.ImageDrawWrapperHandling;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.service.study.StudyMainController;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class ExportStudy {

    private final Logger logger = LoggerFactory.getLogger(ExportStudy.class);

    private final Canvas drawingCanvas;

    public ExportStudy(Canvas drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
    }

    public Mat getAnnotationMat(PatientBodyPart patientBodyPart,boolean isStorageCropSelected){

        StudyMainController.getInstance().newStudyProjectionUi();


        //based on the cropped 1024 image and cropped org image calculate the multiple
        Mat org = patientBodyPart.getImageMat();
        logger.info("org.width(): {} patientBodyPart.getEditedMat().width(): {}", org.width(), patientBodyPart.getEditedMat().width());

        double cropMultiple = (double) org.width() /patientBodyPart.getEditedMat().width();
        logger.info("saveWithAnnotationBurn Crop Multiple: {}", cropMultiple);
        Rect rect = new Rect();

        rect.x = (int)(patientBodyPart.getCropLayoutWrapper().getRectX()*cropMultiple);
        rect.y=(int)(patientBodyPart.getCropLayoutWrapper().getRectY()*cropMultiple);
        rect.width = (int)(patientBodyPart.getCropLayoutWrapper().getRectWidth()*cropMultiple);
        rect.height = (int)(patientBodyPart.getCropLayoutWrapper().getRectHeight()*cropMultiple);

        if(rect.width> org.width()){
            rect.width = org.width();
        }
        if(rect.height> org.height()){
            rect.height = org.height();
        }

        logger.info("Crop Rect: {}", rect);
        logger.info("org width: {}, height: {}", org.width(), org.height());

        Mat croppedMat = new Mat(org, rect);
        Mat croppedMatOld = new Mat(patientBodyPart.getEditedMat(),new Rect((int)patientBodyPart.getCropLayoutWrapper().getRectX(), (int)patientBodyPart.getCropLayoutWrapper().getRectY(), (int)patientBodyPart.getCropLayoutWrapper().getRectWidth(), (int)patientBodyPart.getCropLayoutWrapper().getRectHeight()));


        double width = croppedMatOld.width();
        double height = croppedMatOld.height();

        if(width>=height){
            width = 1024;
        }else{
            width = (1024*width)/height;
        }

        double imageToolMultiple = croppedMat.width()/width;



        StudyService.applyExportValues(imageToolMultiple);



        drawingCanvas.setVisible(true);

        Image temp = ImageConversionService.getInstance().matToImage(patientBodyPart.getImageMat());
        CanvasMainChange.mainImage = temp;
        CanvasMainChange.displayImage = temp;
        CanvasMainChange.mainDrawTool.mainImageMat = patientBodyPart.getImageMat();


        ImageDrawWrapperHandling imageDrawWrapperHandling = new ImageDrawWrapperHandling(patientBodyPart);
        imageDrawWrapperHandling.applyAllDrawingTools();
        applyImageParams(patientBodyPart);

        if(isStorageCropSelected){
            resizeDrawingCanvas(1,rect.width,rect.height);

            CropLayout.setRectX(patientBodyPart.getCropLayoutWrapper().getRectX()*cropMultiple);
            CropLayout.setRectY(patientBodyPart.getCropLayoutWrapper().getRectY()*cropMultiple);
            CropLayout.setRectHeight(patientBodyPart.getCropLayoutWrapper().getRectHeight()*cropMultiple);
            CropLayout.setRectWidth(patientBodyPart.getCropLayoutWrapper().getRectWidth()*cropMultiple);
        }
        else{
            resizeDrawingCanvas(1,patientBodyPart.getImageMat().cols(),patientBodyPart.getImageMat().rows());
            CropLayout.setRectX(0);
            CropLayout.setRectY(0);
            CropLayout.setRectWidth(patientBodyPart.getImageMat().cols());
            CropLayout.setRectHeight(patientBodyPart.getImageMat().rows());
        }


        //logger.info("CropLayout: {}", CropLayout);

        CanvasMainChange.displayImage=null;
        CanvasMainChange.redraw();


        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage snapshot = drawingCanvas.snapshot(parameters, null);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
        Mat mat = ImageConversionService.bufferedImageToMat(image);

        // -> convert the mat into square resizeImageToSquareShape
        mat = ImageProcessService.getInstance().resizeImageToSquareShape(mat);

        StudyMainController.getInstance().applyStudyOverLay();

        String path = "D:\\temp\\annotations.bmp";
        Imgcodecs.imwrite(path, mat);

        return mat;
    }



    public Mat saveWithAnnotationBurn(PatientBodyPart patientBodyPart,boolean isStorageCropSelected){

        StudyMainController.getInstance().newStudyProjectionUi();


        //based on the cropped 1024 image and cropped org image calculate the multiple
        Mat org = patientBodyPart.getImageMat();
        //logger.info("org.width(): {} patientBodyPart.getEditedMat().width(): {}", org.width(), patientBodyPart.getEditedMat().width());

        double cropMultiple = (double) org.width() /patientBodyPart.getEditedMat().width();
       // logger.info("saveWithAnnotationBurn Crop Multiple: {}", cropMultiple);
        Rect rect = new Rect();

        rect.x = (int)(patientBodyPart.getCropLayoutWrapper().getRectX()*cropMultiple);
        rect.y=(int)(patientBodyPart.getCropLayoutWrapper().getRectY()*cropMultiple);
        rect.width = (int)(patientBodyPart.getCropLayoutWrapper().getRectWidth()*cropMultiple);
        rect.height = (int)(patientBodyPart.getCropLayoutWrapper().getRectHeight()*cropMultiple);

        if(rect.width> org.width()){
            rect.width = org.width();
        }
        if(rect.height> org.height()){
            rect.height = org.height();
        }

       /* logger.info("Crop Rect: {}", rect);
        logger.info("org width: {}, height: {}", org.width(), org.height());*/

        Mat croppedMat = new Mat(org, rect);
        Mat croppedMatOld = new Mat(patientBodyPart.getEditedMat(),new Rect((int)patientBodyPart.getCropLayoutWrapper().getRectX(), (int)patientBodyPart.getCropLayoutWrapper().getRectY(), (int)patientBodyPart.getCropLayoutWrapper().getRectWidth(), (int)patientBodyPart.getCropLayoutWrapper().getRectHeight()));

        double width = croppedMatOld.width();
        double height = croppedMatOld.height();

        if(width>=height){
            width = 1024;
        }
        else{
            width = (1024*width)/height;
        }

        double imageToolMultiple = croppedMat.width()/width;
        StudyService.applyExportValues(imageToolMultiple);
        drawingCanvas.setVisible(true);

        Image temp = ImageConversionService.getInstance().matToImage(patientBodyPart.getImageMat());
        CanvasMainChange.mainImage = temp;
        CanvasMainChange.displayImage = temp;
        CanvasMainChange.mainDrawTool.mainImageMat = patientBodyPart.getImageMat();


        ImageDrawWrapperHandling imageDrawWrapperHandling = new ImageDrawWrapperHandling(patientBodyPart);
        imageDrawWrapperHandling.applyAllDrawingTools();
        applyImageParams(patientBodyPart);
        logger.info("isStorageCropSelected: {}", isStorageCropSelected);


        if(isStorageCropSelected){
            resizeDrawingCanvas(1,rect.width,rect.height);

            CropLayout.setRectX(patientBodyPart.getCropLayoutWrapper().getRectX()*cropMultiple);
            CropLayout.setRectY(patientBodyPart.getCropLayoutWrapper().getRectY()*cropMultiple);
            CropLayout.setRectHeight(patientBodyPart.getCropLayoutWrapper().getRectHeight()*cropMultiple);
            CropLayout.setRectWidth(patientBodyPart.getCropLayoutWrapper().getRectWidth()*cropMultiple);
        }
        else{
            resizeDrawingCanvas(1,patientBodyPart.getImageMat().cols(),patientBodyPart.getImageMat().rows());
            CropLayout.setRectX(0);
            CropLayout.setRectY(0);
            CropLayout.setRectWidth(patientBodyPart.getImageMat().cols());
            CropLayout.setRectHeight(patientBodyPart.getImageMat().rows());
        }

        CanvasMainChange.displayImage=null;
        CanvasMainChange.redraw();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage snapshot = drawingCanvas.snapshot(parameters, null);
        Mat mat = ImageConversionService.writableImageToMat(snapshot);


        // -> convert the mat into square resizeImageToSquareShape
        mat = ImageProcessService.getInstance().resizeImageToSquareShape(mat);

        StudyMainController.getInstance().applyStudyOverLay();


        Imgcodecs.imwrite( "D:\\Work\\Temp\\annotationMat.png", mat);
        return mat;
    }


    public Mat saveWithInformationBurn(Mat  orgCloneMat) {

        // Capture overlay and convert to Mat

        Size overlayGridSize = new Size(StudyMainController.getInstance().overlayGridPane.getWidth(),StudyMainController.getInstance().overlayGridPane.getWidth());

        StudyMainController.getInstance().overlayGridPane.setMinSize(orgCloneMat.width(), orgCloneMat.height());
        StudyMainController.getInstance().overlayGridPane.setMaxSize(orgCloneMat.width(), orgCloneMat.height());
        StudyMainController.getInstance().overlayGridPane.setPrefSize(orgCloneMat.width(), orgCloneMat.height());


        WritableImage snapshot = captureOverlaySnapshot(StudyMainController.getInstance().overlayViewBox);
        Mat informationMat = ImageConversionService.writableImageToMat(snapshot);

        //Imgcodecs.imwrite("D:\\temp\\informationMat0.png", informationMat);
        //Imgcodecs.imwrite("D:\\temp\\orgCloneMat0.png", orgCloneMat);

        int orgDepth = orgCloneMat.depth();
        int infoDepth = informationMat.depth();



        // Case 2: orgCloneMat is 8-bit and informationMat is 16-bit → convert informationMat to 8-bit
        if (orgDepth == CvType.CV_8U && infoDepth == CvType.CV_16U) {
            informationMat.convertTo(informationMat, CvType.CV_8U, 1.0 / 256.0); // Normalize from 0-65535 to 0-255
        }
        // Case 3: orgCloneMat is 16-bit and informationMat is 8-bit → convert informationMat to 16-bit
        else if (orgDepth == CvType.CV_16U && infoDepth == CvType.CV_8U) {
            informationMat.convertTo(informationMat, CvType.CV_16U, 256.0); // Expand from 0-255 to 0-65535
        }



        //logger.info("informationMat type {}", CvType.typeToString(informationMat.type()));

        //Imgcodecs.imwrite("D:\\temp\\informationMat.png", informationMat);
        //Imgcodecs.imwrite("D:\\temp\\orgCloneMat.png", orgCloneMat);

        Imgproc.resize(informationMat, informationMat, orgCloneMat.size(), 0, 0, Imgproc.INTER_AREA);

        convertToBGRA(orgCloneMat);
        convertToBGRA(informationMat);

        Mat resultMat = new Mat(orgCloneMat.size(), orgCloneMat.type(), new Scalar(0, 0, 65535, 0));

        Mat alphaMask = new Mat();
        Core.extractChannel(orgCloneMat, alphaMask, 3);
        Mat mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8UC1, 1.0 / 256.0);
        orgCloneMat.copyTo(resultMat, mask8U);

        alphaMask = new Mat();
        Core.extractChannel(informationMat, alphaMask, 3);
        mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
        informationMat.copyTo(resultMat, mask8U);


        //Imgcodecs.imwrite("D:\\temp\\resultMat.png", resultMat);
        //logger.info("resultMat type {}", CvType.typeToString(resultMat.type()));

        StudyMainController.getInstance().overlayGridPane.setMinSize(overlayGridSize.width, overlayGridSize.height);
        StudyMainController.getInstance().overlayGridPane.setMaxSize(overlayGridSize.width, overlayGridSize.height);
        StudyMainController.getInstance().overlayGridPane.setPrefSize(overlayGridSize.width, overlayGridSize.height);


        return resultMat;
    }

    private WritableImage captureOverlaySnapshot(Node gridPane) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return gridPane.snapshot(parameters, null);
    }

    private void convertToBGRA(Mat mat) {
        if (mat.channels() != 4) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
        }
    }


    public void resizeDrawingCanvas(double multiple,double cropWidth, double cropHeight){
        double width = cropWidth*multiple;
        double height = cropHeight*multiple;

        StudyService.applyImageFrameResize(width,height);

    }

    public Mat applyImageParams(PatientBodyPart patientBodyPart) {
        ImagingParams imagingParams = patientBodyPart.getImagingParams();

        Mat frame = patientBodyPart.getImageMat().clone();
        frame = ImageProcessService.getInstance().adjustImageBrightnessAndContrast(frame, imagingParams);
        if (imagingParams.isVFlip()) frame = ImageProcessService.getInstance().verticalFlip(frame);
        if (imagingParams.isHFlip()) frame = ImageProcessService.getInstance().horizontalFlip(frame);
        frame = ImageProcessService.getInstance().rotateImage(frame, -imagingParams.getAngleFactor());
        frame = ImageProcessService.getInstance().zoomImage(frame, imagingParams.getZoomFactor());

        if(imagingParams.isInvert()){
            Core.bitwise_not(frame, frame);
        }
        if(frame != null){
            CanvasMainChange.displayImage = ImageConversionService.getInstance().matToImage0(frame);
        }

        return frame;
    }

    public Mat applyCropAndImageParams(PatientBodyPart patientBodyPart, boolean isStorageCrop) {
        StudyMainController.getInstance().newStudyProjectionUi();

        StudyService.dynamicExportValues(patientBodyPart.getImageMat());

        drawingCanvas.setVisible(true);

        Image temp = ImageConversionService.getInstance().matToImage(patientBodyPart.getImageMat());
        CanvasMainChange.mainImage = temp;
        CanvasMainChange.displayImage = temp;
        CanvasMainChange.mainDrawTool.mainImageMat = patientBodyPart.getImageMat();

        ImageDrawWrapperHandling imageDrawWrapperHandling = new ImageDrawWrapperHandling(patientBodyPart);
        if(isStorageCrop)
        {
            imageDrawWrapperHandling.applyOnlyCrop(true);
            CanvasMainChange.mainDrawTool.applyAllImageParams(patientBodyPart,null);
            resizeDrawingCanvas(DynamicCanvasElementsResize.getMultiple(),patientBodyPart.cropLayoutWrapper.getRectWidth(),patientBodyPart.cropLayoutWrapper.getRectHeight());

            CropLayout.setRectX(patientBodyPart.getCropLayoutWrapper().getRectX()*DynamicCanvasElementsResize.getMultiple());
            CropLayout.setRectY(patientBodyPart.getCropLayoutWrapper().getRectY()*DynamicCanvasElementsResize.getMultiple());
            CropLayout.setRectHeight(patientBodyPart.getCropLayoutWrapper().getRectHeight()*DynamicCanvasElementsResize.getMultiple());
            CropLayout.setRectWidth(patientBodyPart.getCropLayoutWrapper().getRectWidth()*DynamicCanvasElementsResize.getMultiple());
        }
        else
        {
            imageDrawWrapperHandling.applyOnlyCrop(false);
            CanvasMainChange.mainDrawTool.applyAllImageParams(patientBodyPart,null);

            resizeDrawingCanvas(1,patientBodyPart.getImageMat().cols(),patientBodyPart.getImageMat().rows());
            CropLayout.setRectX(0);
            CropLayout.setRectY(0);
            CropLayout.setRectHeight(patientBodyPart.getImageMat().rows());
            CropLayout.setRectWidth(patientBodyPart.getImageMat().cols());
        }


        CanvasMainChange.redraw();
        //logger.info("after last redraw");

        WritableImage snapshot = drawingCanvas.snapshot(null, null);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
        Mat mat = ImageConversionService.bufferedImageToMat(image);

        // -> convert the mat into square        resizeImageToSquareShape
        mat = ImageProcessService.getInstance().resizeImageToSquareShape(mat);

        StudyMainController.getInstance().applyStudyOverLay();


        return mat;
    }

    public Mat applyOnlyCrop(Mat mat, PatientBodyPart patientBodyPart) {

        StudyService.dynamicExportValues(patientBodyPart.getImageMat());
        CropLayoutWrapper cropLayoutWrapper = patientBodyPart.getCropLayoutWrapper();
        Rect cropRect = new Rect((int)(cropLayoutWrapper.getRectX()*DynamicCanvasElementsResize.getMultiple()), (int)(cropLayoutWrapper.getRectY()*DynamicCanvasElementsResize.getMultiple()),(int) (cropLayoutWrapper.getRectWidth()*DynamicCanvasElementsResize.getMultiple()),(int) (cropLayoutWrapper.getRectHeight()*DynamicCanvasElementsResize.getMultiple()));
        logger.info("applyOnlyCrop {} ",cropRect);

        return new Mat(mat, cropRect);
    }

    public Mat applyImageProperties(PatientBodyPart patientBodyPart) {
        ImagingParams imagingParams = patientBodyPart.getImagingParams();

        Mat frame = patientBodyPart.getImageMat().clone();
        frame = ImageProcessService.getInstance().adjustImageBrightnessAndContrast(frame, imagingParams);
        if (imagingParams.isVFlip()) frame = ImageProcessService.getInstance().verticalFlip(frame);
        if (imagingParams.isHFlip()) frame = ImageProcessService.getInstance().horizontalFlip(frame);
        frame = ImageProcessService.getInstance().rotateImage(frame, -imagingParams.getAngleFactor());
        frame = ImageProcessService.getInstance().zoomImage(frame, imagingParams.getZoomFactor());

        if (imagingParams.isInvert()) {
            Core.bitwise_not(frame, frame);
        }

        return frame;
    }


}
