package com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper;

import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.*;
import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageDrawWrapperHandling {

    private final Logger logger = LoggerFactory.getLogger(ImageDrawWrapperHandling.class);
    public List<AngleDrawWrapper> angleDrawWrapperList;
    public List<DistanceDrawWrapper> distanceDrawWrapperList;
    public LeftRightDrawWrapper leftRightDrawWrapper;
    public List<TextDrawWrapper> textDrawWrapperList;

    private PatientBodyPart patientBodyPart;

    public ImageDrawWrapperHandling(PatientBodyPart patientBodyPart){
        this.patientBodyPart=patientBodyPart;

        angleDrawWrapperList = new ArrayList<>();
        distanceDrawWrapperList = new ArrayList<>();
        textDrawWrapperList = new ArrayList<>();
    }

    public PatientBodyPart getPatientBodyPart() {
        return patientBodyPart;
    }

    public void setPatientBodyPart(PatientBodyPart patientBodyPart) {
        this.patientBodyPart = patientBodyPart;
    }

    public void saveAllImageParams() {
        WritableImage thumbNailImage = CanvasMainChange.drawingCanvas.snapshot(null, null);
        Mat thumNailMat = ImageConversionService.writableImageToMat(thumbNailImage);

        if(thumNailMat.channels()==4){
            Imgproc.cvtColor(thumNailMat, thumNailMat, Imgproc.COLOR_RGBA2BGR);
        }

        thumNailMat = ImageProcessService.getInstance().resizeImageToSquareShape(thumNailMat);
        Imgproc.resize(thumNailMat,thumNailMat,new Size(150,150), 0, 0, Imgproc.INTER_AREA);
        //Imgcodecs.imwrite("C:\\Users\\Rayme\\OneDrive\\Desktop\\FX_Logo\\thumNailMat.png", thumNailMat);

        patientBodyPart.setThumbNail(thumNailMat);


        // Ensure lists are initialized
        if (patientBodyPart.getDistanceDrawList() == null) {
            patientBodyPart.setDistanceDrawList(new ArrayList<>());
        } else {
            patientBodyPart.getDistanceDrawList().clear();
        }

        if (patientBodyPart.getAngleDrawList() == null) {
            patientBodyPart.setAngleDrawList(new ArrayList<>());
        } else {
            patientBodyPart.getAngleDrawList().clear();
        }

        if(patientBodyPart.getTextDrawList()==null){
            patientBodyPart.setTextDrawList(new ArrayList<>());
        } else{
            patientBodyPart.getTextDrawList().clear();
        }


        // Clear local lists
        angleDrawWrapperList.clear();
        distanceDrawWrapperList.clear();
        textDrawWrapperList.clear();
        leftRightDrawWrapper = null;

        CropLayoutWrapper cropLayoutWrapper = new CropLayoutWrapper();

        // Rebuild and save Angle Drawings
        for (AngleDraw angleDraw : MainDrawTool.angleDrawList) {
            List<EllipseWrapper> ellipseWrapperList = new ArrayList<>();
            List<LineWrapper> lineWrapperList = new ArrayList<>();
            for (Ellipse ellipse : angleDraw.getEllipses()) {
                ellipseWrapperList.add(new EllipseWrapper(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY()));
            }
            for (Line line : angleDraw.getLines()) {
                lineWrapperList.add(new LineWrapper(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            }

            DegreeLabel degreeLabel = angleDraw.getDegreeLabel();
            LabelWrapper labelWrapper = new LabelWrapper(degreeLabel.getAngle(), degreeLabel.getX(), degreeLabel.getY());

            angleDrawWrapperList.add(new AngleDrawWrapper(ellipseWrapperList, lineWrapperList, labelWrapper));

            //System.out.println("Saving Angle Draw Wrapper: " + labelWrapper);
        }

        // Rebuild and save Distance Drawings
        for (DistanceDraw distanceDraw : MainDrawTool.distanceDrawList) {


            List<EllipseWrapper> ellipseWrapperList = new ArrayList<>();
            for (Ellipse ellipse : distanceDraw.getEllipses()) {
                ellipseWrapperList.add(new EllipseWrapper(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY()));
            }

            LineWrapper lineWrapper = new LineWrapper(distanceDraw.getLine().getStartX(), distanceDraw.getLine().getStartY(), distanceDraw.getLine().getEndX(), distanceDraw.getLine().getEndY());
            DistanceLabel distanceLabel = distanceDraw.getDistanceLabel();
            LabelWrapper labelWrapper = new LabelWrapper(distanceLabel.getDistance(), distanceLabel.getX(), distanceLabel.getY());

            distanceDrawWrapperList.add(new DistanceDrawWrapper(ellipseWrapperList, lineWrapper, labelWrapper));

            //System.out.println("Saving Distance Draw Wrapper: " + labelWrapper);
        }

        for(TextDraw textDraw:MainDrawTool.textDrawList){
            if(textDraw.getTextLabel()!=null){
                TextDrawWrapper textDrawWrapper = new TextDrawWrapper(textDraw.getTextLabel().getText(),textDraw.getTextLabel().getX(),textDraw.getTextLabel().getY());
                textDrawWrapperList.add(textDrawWrapper);
            }
        }


        if(MainDrawTool.leftRightDraw!=null){
            if(MainDrawTool.leftRightDraw.getRlIcon()!=null){
                leftRightDrawWrapper = new LeftRightDrawWrapper();
                leftRightDrawWrapper.setOffsetX(MainDrawTool.leftRightDraw.getRlIcon().getX());
                leftRightDrawWrapper.setOffsetY(MainDrawTool.leftRightDraw.getRlIcon().getY());
                leftRightDrawWrapper.setText(MainDrawTool.leftRightDraw.getText());

                patientBodyPart.setLeftRightDrawWrapper(leftRightDrawWrapper);

                MainDrawTool.leftRightDraw.removeAll();

                logger.info("saved label  {} ",patientBodyPart.getLeftRightDrawWrapper());
            }
        }else{
            patientBodyPart.setLeftRightDrawWrapper(null);
        }

        if(MainDrawTool.cropLayout!=null){
            MainDrawTool.cropLayout.setCanvas(CanvasMainChange.drawingCanvas);
            double rectX = CropLayout.getRectX();
            double rectY = CropLayout.getRectY();
            double rectWidth = CropLayout.getRectWidth();
            double rectHeight = CropLayout.getRectHeight();

            logger.info("========================================================================");
            logger.info("Crop Layout X: {}, Y: {}, Width: {}, Height: {}", rectX, rectY, rectWidth, rectHeight);

            cropLayoutWrapper.setRectX(rectX);
            cropLayoutWrapper.setRectY(rectY);
            cropLayoutWrapper.setRectWidth(rectWidth);
            cropLayoutWrapper.setRectHeight(rectHeight);
            patientBodyPart.setCropLayoutWrapper(cropLayoutWrapper);

            Mat mat = StudyService.imageFrameResize(CanvasMainChange.mainImage);
            MainDrawTool.cropLayout.reset(mat.cols(),mat.rows());
        }


        // Replace lists instead of modifying references
        patientBodyPart.setAngleDrawList(new ArrayList<>(angleDrawWrapperList));
        patientBodyPart.setDistanceDrawList(new ArrayList<>(distanceDrawWrapperList));
        patientBodyPart.setTextDrawList(new ArrayList<>(textDrawWrapperList));

        CanvasMainChange.redraw();



        //logger.info("saving text wrapper list {} ",patientBodyPart.getTextDrawList());
    }

    private final Rect cropRect = new Rect();
    public void applyAllDrawingTools(){

        if(CanvasMainChange.displayImage==null){
            logger.warn("Display image is null, cannot apply drawing tools.");
            return;
        }

        // Clear local lists
        angleDrawWrapperList.clear();
        distanceDrawWrapperList.clear();
        textDrawWrapperList.clear();
        leftRightDrawWrapper = null;


        angleDrawWrapperList = patientBodyPart.getAngleDrawList();
        distanceDrawWrapperList = patientBodyPart.getDistanceDrawList();
        leftRightDrawWrapper = patientBodyPart.getLeftRightDrawWrapper();
        textDrawWrapperList = patientBodyPart.getTextDrawList();



        if(angleDrawWrapperList!=null){
            for(AngleDrawWrapper angleDrawWrapper : angleDrawWrapperList){
                AngleDraw angle = new AngleDraw();
                angle.setCanvas(CanvasMainChange.drawingCanvas);
                angle.setGc(CanvasMainChange.drawingCanvas.getGraphicsContext2D());
                angle.setClickCount(3);
                angle.setSelectedEllipse(null);

                ObservableList<Ellipse> ellipseList = FXCollections.observableArrayList();
                ObservableList<Line> lineList = FXCollections.observableArrayList();

                AngleDraw.isComplete=true;

                for (EllipseWrapper ellipseWrapper : angleDrawWrapper.getEllipses()){
                    Ellipse ellipse = new Ellipse(ellipseWrapper.getCenterX(),ellipseWrapper.getCenterY(),ellipseWrapper.getRadiusX(),ellipseWrapper.getRadiusY());
                    ellipse.setFill(Color.BLUE);
                    CanvasMainChange.ellipses.add(ellipse);
                    ellipseList.add(ellipse);
                }

                for (LineWrapper lineWrapper : angleDrawWrapper.getLines()){
                    Line line = new Line(lineWrapper.getStartX(),lineWrapper.getStartY(),lineWrapper.getEndX(),lineWrapper.getEndY());
                    CanvasMainChange.lines.add(line);
                    lineList.add(line);
                }

                LabelWrapper labelWrapper = angleDrawWrapper.getDegreeLabel();
                DegreeLabel degreeLabel = new DegreeLabel(labelWrapper.getValue(), labelWrapper.getX(), labelWrapper.getY(), Color.rgb(255, 235, 42));
                CanvasMainChange.addDegreeLabel(degreeLabel);


                angle.setEllipses(ellipseList);
                angle.setLines(lineList);
                angle.setDegreeLabel(degreeLabel);

                MainDrawTool.angleDrawList.add(angle);
                MainDrawTool.allDrawTools.add(angle);
            }
        }

        if(distanceDrawWrapperList!=null){
            for(DistanceDrawWrapper distanceDrawWrapper : distanceDrawWrapperList){
                DistanceDraw distanceDraw = new DistanceDraw();
                distanceDraw.setCanvas(CanvasMainChange.drawingCanvas);
                distanceDraw.setGc(CanvasMainChange.drawingCanvas.getGraphicsContext2D());
                distanceDraw.setClickCount(2);
                distanceDraw.setSelectedEllipse(null);

                ObservableList<Ellipse> ellipseList = FXCollections.observableArrayList();

                DistanceDraw.isComplete=true;

                for (EllipseWrapper ellipseWrapper : distanceDrawWrapper.getEllipses()){
                    Ellipse ellipse = new Ellipse(ellipseWrapper.getCenterX(),ellipseWrapper.getCenterY(),ellipseWrapper.getRadiusX(),ellipseWrapper.getRadiusY());
                    ellipse.setFill(Color.BLUE);
                    CanvasMainChange.ellipses.add(ellipse);
                    ellipseList.add(ellipse);
                }

                Line line = new Line(distanceDrawWrapper.getLines().getStartX(),distanceDrawWrapper.getLines().getStartY(),distanceDrawWrapper.getLines().getEndX(),distanceDrawWrapper.getLines().getEndY());
                CanvasMainChange.lines.add(line);

                LabelWrapper labelWrapper = distanceDrawWrapper.getLabelWrapper();
                DistanceLabel distanceLabel = new DistanceLabel(labelWrapper.getValue(), labelWrapper.getX(), labelWrapper.getY(), Color.rgb(255, 235, 42));
                CanvasMainChange.addDistanceLabel(distanceLabel);


                distanceDraw.setEllipses(ellipseList);
                distanceDraw.setLine(line);
                distanceDraw.setDistanceLabel(distanceLabel);

                MainDrawTool.distanceDrawList.add(distanceDraw);
                MainDrawTool.allDrawTools.add(distanceDraw);
            }
        }

        if(leftRightDrawWrapper!=null){
            MainDrawTool.leftRightDraw = new LeftRightDraw(leftRightDrawWrapper.getText(),MainDrawTool.drawingCanvas);
            MainDrawTool.leftRightDraw.getRlIcon().setX(leftRightDrawWrapper.getOffsetX());
            MainDrawTool.leftRightDraw.getRlIcon().setY(leftRightDrawWrapper.getOffsetY());
            MainDrawTool.leftRightDraw.redraw();
            logger.info("apply label  {} ",patientBodyPart.getLeftRightDrawWrapper());
        }

        if(textDrawWrapperList!=null){
            for(TextDrawWrapper textDrawWrapper:textDrawWrapperList){
                TextDraw textDraw = new TextDraw();
                textDraw.setRoot(CanvasMainChange.overlayPane);
                textDraw.setCanvas(CanvasMainChange.drawingCanvas);
                TextLabel textLabel = new TextLabel(textDrawWrapper.getText(), textDrawWrapper.getX(),
                        textDrawWrapper.getY(), Color.valueOf("#66FF00"));
                textDraw.setTextLabel(textLabel);

                textDraw.drawTexts();

                MainDrawTool.textDrawList.add(textDraw);
                MainDrawTool.allDrawTools.add(textDraw);
                CanvasMainChange.textLabels.add(textLabel);
            }
        }


        CropLayoutWrapper cropLayoutWrapper = patientBodyPart.getCropLayoutWrapper();
        if(cropLayoutWrapper!=null){
            logger.info("crop rect before {} and multiple is {}",patientBodyPart.getCropLayoutWrapper(), DynamicCanvasElementsResize.getMultiple());

            if(patientBodyPart.isCrop()){

                logger.info("crop is requested, applying crop layout settings.");

                CropLayout.setRectX(cropLayoutWrapper.getRectX());
                CropLayout.setRectY(cropLayoutWrapper.getRectY());
                CropLayout.setRectWidth(cropLayoutWrapper.getRectWidth());
                CropLayout.setRectHeight(cropLayoutWrapper.getRectHeight());
            }

            //cropRect = new Rect((int) CropLayout.rectX, (int)CropLayout.rectY,(int) CropLayout.rectWidth,(int) CropLayout.rectHeight);

            cropRect.x= (int)CropLayout.rectX;
            cropRect.y=(int)CropLayout.rectY;
            cropRect.width=(int)CropLayout.rectWidth;
            cropRect.height=(int)CropLayout.rectHeight;



            //crop issues so commented out
           /* if(cropRect.height>= CanvasMainChange.displayImage.getHeight() || cropRect.width>=CanvasMainChange.displayImage.getWidth()){
                cropRect.x=0;
                cropRect.y=0;
                cropRect.width= (int) CanvasMainChange.displayImage.getWidth();
                cropRect.height= (int) CanvasMainChange.displayImage.getHeight();
            }*/


            //logger.info(" crop rect after {} and width and height is {} {}",cropRect, CanvasMainChange.displayImage.getHeight(),CanvasMainChange.displayImage.getWidth());



            // Crop the image
            Mat croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(CanvasMainChange.displayImage), cropRect);
            StudyService.imageFrameResize(ImageConversionService.getInstance().matToImage(croppedMat));
        }
        else{
            logger.info("crop layout is null");
        }



        //logger.info("Applying text wrapper list {} ",patientBodyPart.getTextDrawList());

        CanvasMainChange.redraw();

    }


    public void applyOnlyCrop(boolean b) {
        if(b){
            CropLayoutWrapper cropLayoutWrapper = patientBodyPart.getCropLayoutWrapper();
            if(cropLayoutWrapper!=null){
                logger.info(" crop rect before {} and multiple is {}",patientBodyPart.getCropLayoutWrapper(), DynamicCanvasElementsResize.getMultiple());

                if(patientBodyPart.isCrop()){
                    CropLayout.setRectX(cropLayoutWrapper.getRectX());
                    CropLayout.setRectY(cropLayoutWrapper.getRectY());
                    CropLayout.setRectWidth(cropLayoutWrapper.getRectWidth());
                    CropLayout.setRectHeight(cropLayoutWrapper.getRectHeight());
                }

                Rect cropRect = new Rect((int) CropLayout.rectX, (int)CropLayout.rectY,(int) CropLayout.rectWidth,(int) CropLayout.rectHeight);

                logger.info(" crop rect after {} and multiple is {}",patientBodyPart.getCropLayoutWrapper(), DynamicCanvasElementsResize.getMultiple());

                // Crop the image
                Mat croppedMat = new Mat(ImageConversionService.getInstance().imageToMat(CanvasMainChange.displayImage), cropRect);
                StudyService.imageFrameResize(ImageConversionService.getInstance().matToImage(croppedMat));
            }
            else{
                logger.info("crop layout is null");
            }



            //logger.info("Applying text wrapper list {} ",patientBodyPart.getTextDrawList());

            CanvasMainChange.redraw();
        }
    }
}
