package com.raymedis.rxviewui.service.export;

import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.PrintOverlayEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table.StudyOverlayService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.*;
import com.raymedis.rxviewui.modules.ImageProcessing.DynamicCanvasElementsResize;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageConversionService;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.*;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.ImagingParams;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class ExportStudyService {

    private final StudyOverlayService studyOverlayService = StudyOverlayService.getInstance();
    private final static ExportStudyService instance = new ExportStudyService();
    public static ExportStudyService getInstance(){
        return instance;
    }

    private final static Logger logger = LoggerFactory.getLogger(ExportStudyService.class);

    private final ImageConversionService imageConversionService = ImageConversionService.getInstance();
    private final ImageProcessService imageProcessService = ImageProcessService.getInstance();

    private VBox topLeftOverlayBox;
    private VBox topRightOverlayBox;

    private VBox bottomLeftOverlayBox;
    private VBox bottomRightOverlayBox;

    private GridPane overlayGridpane = new GridPane();

    private boolean isAnnotations;
    private boolean isInformation;
    private boolean isCrop;

    private String destinationFolder;

    private PatientStudy patientStudy;

    public double brightness = 0;
    public double contrast = 1;
    public double zoomFactor = 1;
    public double angleFactor = 0;
    public boolean isInvert = false;
    public double denoise = 3;
    public double sharpness = 1;

    private Rect rect = new Rect();

    public List<ExportData> exportStudy(PatientStudy patientStudy, boolean isAnnotations, boolean isInformation, boolean isCrop, String destinationFolder) {

        loadStudyData();
        this.patientStudy = patientStudy;
        this.isAnnotations =isAnnotations;
        this.isInformation = isInformation;
        this.isCrop = isCrop;
        this.destinationFolder = destinationFolder;


        List<TabNode> bodyPartNodeList = patientStudy.getBodyPartHandler().getAllTabs();

        Scene scene = new Scene(overlayGridpane, (int)overlayGridpane.getWidth(), (int)overlayGridpane.getHeight());

        List<ExportData> exportDataList = new ArrayList<>();

        for(TabNode tabNode : bodyPartNodeList){
            BodyPartNode bodyPartNode = (BodyPartNode) tabNode;
            logger.info("Exporting body part: {}", bodyPartNode.getBodyPart().getBodyPartName());

            if(bodyPartNode.getBodyPart() != null && bodyPartNode.getBodyPart().getImageMat() != null) {

                String imageName = bodyPartNode.getBodyPart().getBodyPartName() + "_" + bodyPartNode.getBodyPart().getBodyPartPosition() + "_" + bodyPartNode.getBodyPart().getSeriesId() + "_" + bodyPartNode.getBodyPart().getInstanceId();
                String outputFilePath = destinationFolder + "/" + imageName;

                Mat orgImageMat = bodyPartNode.getBodyPart().getImageMat().clone();

                orgImageMat = applyImageProcessing(orgImageMat,bodyPartNode.getBodyPart());

                if(isCrop){
                    orgImageMat = getCroppedImage(bodyPartNode.getBodyPart(),orgImageMat);
                }

                if(isAnnotations){
                    orgImageMat = getAnnotatedImage(bodyPartNode.getBodyPart(),orgImageMat);
                }

                if(isInformation){
                    orgImageMat = getInformationImage(bodyPartNode.getBodyPart(),orgImageMat);
                }

                ExportData exportData = new ExportData(outputFilePath, orgImageMat, bodyPartNode.getBodyPart());
                exportDataList.add(exportData);

                Imgcodecs.imwrite("D:\\Work\\Temp\\orgMat"+bodyPartNode.getBodyPart().getBodyPartName()+"_"+bodyPartNode.getBodyPart().getBodyPartPosition()+"finalOutput.png", orgImageMat);


            }
            else {
                logger.warn("No image found for body part: {}", bodyPartNode.getBodyPart().getBodyPartName());
            }
        }

        return exportDataList;

    }


    private Mat getCroppedImage(PatientBodyPart bodyPart, Mat orgImageMat) {
        setupForAnnotations(bodyPart);

        Mat croppedMat = new Mat(orgImageMat, rect);

        //Imgcodecs.imwrite("D:\\Work\\Temp\\orgMat"+bodyPart.getBodyPartName()+"_"+bodyPart.getBodyPartPosition()+"_cropped.png", croppedMat);

        return croppedMat;
    }


    private Mat applyImageProcessing(Mat orgImageMat,PatientBodyPart patientBodyPart) {

        if(patientBodyPart.getImagingParams()!=null) {
            ImagingParams imagingParams = patientBodyPart.getImagingParams();
            brightness = imagingParams.getWindowLevel();
            contrast = imagingParams.getWindowWidth();
            zoomFactor = imagingParams.getZoomFactor();
            angleFactor = imagingParams.getAngleFactor();
            isInvert = imagingParams.isInvert();
            denoise = imagingParams.getDenoise();
            sharpness = imagingParams.getSharpness();



            return imageProcess(orgImageMat);
        }


        return orgImageMat;
    }

    public Mat imageProcess(Mat mainImageMat) {
        Mat frame = mainImageMat;

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

        return frame;
    }

    public void loadStudyData() {
        logger.info("ExportStudyService initialized.");

        overlayGridpane.setPrefSize(2500,3052);
        overlayGridpane.setMaxSize(2500,3052);
        overlayGridpane.setMinSize(2500,3052);
        overlayGridpane=createOverlayGridPane();
    }

    private GridPane createOverlayGridPane() {
        // Create GridPane
        GridPane overlayGridPane = new GridPane();
        overlayGridPane.setAlignment(Pos.CENTER);
        overlayGridPane.setPrefSize(3052.0, 3052.0);
        overlayGridPane.setMinSize(3052.0, 3052.0);
        overlayGridPane.setMaxSize(3052.0, 3052.0);

        // Create ViewBox
        ViewBox viewBox = new ViewBox(); // Assuming ViewBox extends Region or Pane
        viewBox.setAlignment(Pos.CENTER);
        viewBox.setPrefSize(3052.0, 3052.0);
        viewBox.setMinSize(3052.0, 3052.0);
        viewBox.setMaxSize(3052.0, 3052.0);

        // Outer VBox inside ViewBox
        VBox mainVBox = new VBox();
        mainVBox.setPrefSize(3052.0, 3052.0);
        mainVBox.setMinSize(3052.0, 3052.0);
        mainVBox.setMaxSize(3052.0, 3052.0);

        // Top HBox
        HBox topHBox = new HBox();
        topHBox.setPrefSize(3052.0, 1526.0);
        topHBox.setMinSize(3052.0, 1526.0);
        topHBox.setMaxSize(3052.0, 1526.0);

        // Top-left VBox
        topLeftOverlayBox = new VBox();
        topLeftOverlayBox.setPadding(new Insets(15.0, 0, 0, 15.0));
        topLeftOverlayBox.setPrefSize(1526.0, 1526.0);
        topLeftOverlayBox.setMinSize(1526.0, 1526.0);
        topLeftOverlayBox.setMaxSize(1526.0, 1526.0);

        // Top-right VBox
        topRightOverlayBox = new VBox();
        topRightOverlayBox.setAlignment(Pos.TOP_RIGHT);
        topRightOverlayBox.setPadding(new Insets(15.0, 15.0, 0, 0));
        topRightOverlayBox.setPrefSize(1526.0, 1526.0);
        topRightOverlayBox.setMinSize(1526.0, 1526.0);
        topRightOverlayBox.setMaxSize(1526.0, 1526.0);

        topHBox.getChildren().addAll(topLeftOverlayBox, topRightOverlayBox);

        // Bottom HBox
        HBox bottomHBox = new HBox();
        bottomHBox.setLayoutX(10.0);
        bottomHBox.setLayoutY(10.0);
        bottomHBox.setPrefSize(3052.0, 1526.0);
        bottomHBox.setMinSize(3052.0, 1526.0);
        bottomHBox.setMaxSize(3052.0, 1526.0);

        // Bottom-left VBox
        bottomLeftOverlayBox = new VBox();
        bottomLeftOverlayBox.setAlignment(Pos.BOTTOM_LEFT);
        bottomLeftOverlayBox.setPadding(new Insets(0, 0, 15.0, 15.0));
        bottomLeftOverlayBox.setPrefSize(1526.0, 1526.0);
        bottomLeftOverlayBox.setMinSize(1526.0, 1526.0);
        bottomLeftOverlayBox.setMaxSize(1526.0, 1526.0);

        // Bottom-right VBox
        bottomRightOverlayBox = new VBox();
        bottomRightOverlayBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightOverlayBox.setPadding(new Insets(0, 15.0, 15.0, 0));
        bottomRightOverlayBox.setPrefSize(1526.0, 1526.0);
        bottomRightOverlayBox.setMinSize(1526.0, 1526.0);
        bottomRightOverlayBox.setMaxSize(1526.0, 1526.0);

        bottomHBox.getChildren().addAll(bottomLeftOverlayBox, bottomRightOverlayBox);

        // Build structure
        mainVBox.getChildren().addAll(topHBox, bottomHBox);
        viewBox.getChildren().add(mainVBox);
        overlayGridPane.getChildren().add(viewBox);

        return overlayGridPane;
    }

    private Canvas createDrawingCanvas(){
        return new Canvas( 2500,3052);
    }

    public void applyStudyOverLay(PatientBodyPart patientBodyPart) {
        topLeftOverlayBox.getChildren().clear();
        topRightOverlayBox.getChildren().clear();
        bottomLeftOverlayBox.getChildren().clear();
        bottomRightOverlayBox.getChildren().clear();


         ArrayList<PrintOverlayEntity> studyOverlayList = null;
         SystemInfoEntity systemInfo =null;

        try {
            studyOverlayList = studyOverlayService.findAll();
            systemInfo = SystemInfoService.getInstance().getSystemInfo().getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(patientStudy !=null){
            for(PrintOverlayEntity overlay : studyOverlayList){
                String contentVal = "";

                switch (overlay.getItemContent().toLowerCase()) {
                    case "patient id":
                        contentVal = patientStudy.getPatientInfo().getPatientId();
                        break;
                    case "patient name":
                        contentVal = patientStudy.getPatientInfo().getPatientName();
                        break;
                    case "birth date":
                        contentVal = patientStudy.getPatientInfo().getBirthDate().toString();
                        break;
                    case "sex":
                        contentVal = patientStudy.getPatientInfo().getSex();
                        break;
                    case "age":
                        contentVal = patientStudy.getPatientInfo().getAge() + "";
                        break;
                    case "gray value":
                        //contentVal = patientStudy.getGrayValue() + "";
                        break;
                    case "histogram":
                        //contentVal = patientStudy.getHistogram();
                        break;
                    case "dose kvp":
                        //contentVal = patientStudy.getDoseKvp() + "";
                        break;
                    case "dose ma":
                        contentVal = patientBodyPart.getXrayParams().getmA() + "";
                        break;
                    case "dose ms":
                        contentVal = patientBodyPart.getXrayParams().getMs() + "";
                        break;
                    case "dose mas":
                         contentVal = patientBodyPart.getXrayParams().getmAs() + "";
                        break;
                    case "exposure index":
                        // contentVal = patientStudy.getExposureIndex() + "";
                        break;
                    case "exposure indec(ugy)":
                        //contentVal = patientStudy.getExposureIndecUgy() + "";
                        break;
                    case "deviation index":
                        //contentVal = patientStudy.getDeviationIndex() + "";
                        break;
                    case "target exposure index":
                        //contentVal = patientStudy.getTargetExposureIndex() + "";
                        break;
                    case "target exposure index(ugy)":
                        // contentVal = patientStudy.getTargetExposureIndexUgy() + "";
                        break;
                    case "dap(dgy.cm^2)":
                        //contentVal = patientStudy.getDapDgyCm2() + "";
                        break;
                    case "dap(mgy.cm^2)":
                        // contentVal = patientStudy.getDapMgyCm2() + "";
                        break;
                    case "dap(ugy.m^2)":
                        //contentVal = patientStudy.getDapUgyM2() + "";
                        break;
                    case "exposure date":
                        contentVal = patientBodyPart.getExposureDateTime()+ "";
                        break;
                    case "exposure time":
                        contentVal = patientBodyPart.getExposureDateTime()+ "1";
                        break;
                    case "performing physcian":
                        contentVal = patientStudy.getPerformingPhysician();
                        break;
                    case "refering physcian":
                        contentVal = patientStudy.getReferringPhysician();
                        break;
                    case "series number - instance number":
                        contentVal  = patientBodyPart.getSeriesId()+ "-" + patientBodyPart.getInstanceId();
                        break;
                    case "bodypart projection":
                        contentVal = patientBodyPart.getBodyPartName() + " " + patientBodyPart.getBodyPartPosition();
                        break;
                    case "study date", "study time":
                        contentVal = patientStudy.getStudyDateTime()+"";
                        break;
                    case "screen zoom":
                        //contentVal = patientStudy.getScreenZoom() + "";
                        break;
                    case "pixel zoom":
                        // contentVal = study.getPixelZoom() + "";
                        break;
                    case "accession number":
                        contentVal = patientStudy.getAccessionNum();
                        break;
                    case "laterality":
                        // contentVal = patientStudy.getLaterality();
                        break;
                    case "modality":
                        contentVal = patientStudy.getModality();
                        break;
                    case "height":
                        contentVal = patientStudy.getPatientInfo().getHeight() + "";
                        break;
                    case "weight":
                        contentVal = patientStudy.getPatientInfo().getWeight() + "";
                        break;
                    case "institutional name":
                        contentVal = systemInfo.getInstitutionName();
                        break;
                    case "institutional adress":
                        contentVal = systemInfo.getInstitutionAddress();
                        break;
                    case "institutional dept name":
                        contentVal = systemInfo.getDepartment();
                        break;
                    case "manufacturer":
                        contentVal = systemInfo.getManufacturer();
                        break;
                    case "manufacturer model name":
                        contentVal = systemInfo.getModelName();
                        break;
                    case "station name":
                        contentVal = systemInfo.getSerialNumber();
                        break;
                    case "other patient id":
                        //contentVal = patientStudy.getPatientInfo().getOtherPatientId();
                        break;
                    case "image orientation":
                        //contentVal = patientStudy.getImageOrientation();
                        break;
                    default:
                        logger.info("Invalid Value: {}", overlay.getItemContent().toLowerCase());
                        contentVal = "";
                        break;
                }

                if(contentVal.isEmpty() || contentVal.equals("0.0") || contentVal.equals("0")){
                    continue;
                }

                String content = overlay.getItemContent() + " : " + contentVal;
                double fontSize = 45;//DynamicCanvasElementsResize.getInfoLabelFontSize();

                String fontFamily = "Century Gothic";

                Label label = new Label(content);
                label.setStyle(
                        "-fx-font-family: '" + fontFamily + "';" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: " + fontSize + "px;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 0;"
                );



                switch (overlay.getPositionType()) {
                    case PositionType.TOP_LEFT:
                        topLeftOverlayBox.getChildren().add(label);
                        //logger.info("label {} added to TOP_LEFT", label.getText());
                        break;
                    case PositionType.TOP_RIGHT:
                        topRightOverlayBox.getChildren().add(label);
                        //logger.info("label {} added to TOP_RIGHT", label.getText());
                        break;
                    case PositionType.BOTTOM_LEFT:
                        bottomLeftOverlayBox.getChildren().add(label);
                        //logger.info("label {} added to BOTTOM_LEFT", label.getText());
                        break;
                    case PositionType.BOTTOM_RIGHT:
                        bottomRightOverlayBox.getChildren().add(label);
                        //logger.info("label {} added to BOTTOM_RIGHT", label.getText());
                        break;
                    default:
                        logger.info("");
                }
            }
        }
    }

    public Mat getInformationImage(PatientBodyPart bodyPart,Mat orgMat) {
        applyStudyOverLay(bodyPart);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = overlayGridpane.snapshot(parameters, null);
        Mat informationMat = ImageConversionService.writableImageToMat(image);

        double w;
        double h;
        if(orgMat.width()>=orgMat.height()){
            w = 3052;
            h = (orgMat.height() * 3052) / orgMat.width();
        }else{
            h = 3052;
            w = (orgMat.width() * 3052) / orgMat.height();
        }

        Imgproc.resize(orgMat, orgMat, new Size(w,h));


        logger.info("orgMat size {} , type {} ", orgMat.size(), CvType.typeToString(orgMat.type()));



        if(orgMat.width() < 3052 || orgMat.height() < 3052){
            Mat paddedAnnotationMat = new Mat(3052, 3052, orgMat.type(), new Scalar(0, 0, 0, 65535));
            orgMat.copyTo(paddedAnnotationMat.submat(new Rect((3052-orgMat.width())/2, (3052-orgMat.height())/2, orgMat.width(), orgMat.height())));
            orgMat = paddedAnnotationMat;
        }




        // Check if the depth of orgMat and informationMat are the same
        if(orgMat.depth() != informationMat.depth()){
            logger.info("Type or Depth Issue in informationMat : {} : {}", CvType.typeToString(orgMat.type()), CvType.typeToString(informationMat.type()));

            if (orgMat.depth() == CvType.CV_8U) {
                informationMat.convertTo(informationMat, CvType.CV_8UC4);
            } else if (orgMat.depth() == CvType.CV_16U) {
                informationMat.convertTo(informationMat, CvType.CV_16UC4,256.0);
            }
        }



        if(informationMat.width() < 3052 || informationMat.height() < 3052){
            Mat paddedInformationMat = new Mat(3052, 3052, informationMat.type(), new Scalar(0, 0, 0, 0));
            informationMat.copyTo(paddedInformationMat.submat(new Rect((3052-informationMat.width())/2, (3052-informationMat.height())/2, informationMat.width(), informationMat.height())));
            informationMat = paddedInformationMat;
        }


        convertToBGRA(orgMat);

        Mat outputMat = new Mat(informationMat.size(), orgMat.type(), new Scalar(0, 0, 65535, 0));

        // Split channels
        List<Mat> orgChannels = new ArrayList<>();
        List<Mat> infoChannels = new ArrayList<>();
        Core.split(orgMat, orgChannels);
        Core.split(informationMat, infoChannels);

        // Normalize alpha to float [0,1]
        Mat orgAlphaF = new Mat();
        Mat infoAlphaF = new Mat();
        orgChannels.get(3).convertTo(orgAlphaF, CvType.CV_32F, 1.0 / 65535.0);
        infoChannels.get(3).convertTo(infoAlphaF, CvType.CV_32F, 1.0 / 65535.0);

        // For each RGB channel: blend using alpha
        List<Mat> resultChannels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Mat orgF = new Mat();
            Mat annoF = new Mat();
            orgChannels.get(i).convertTo(orgF, CvType.CV_32F);
            infoChannels.get(i).convertTo(annoF, CvType.CV_32F);

            Mat blended = new Mat();

            // blended = anno * alpha_anno + org * (1 - alpha_anno)
            Mat oneMinusAlpha = new Mat();
            Core.subtract(Mat.ones(infoAlphaF.size(), CvType.CV_32F), infoAlphaF, oneMinusAlpha);

            Mat part1 = new Mat(), part2 = new Mat();
            Core.multiply(annoF, infoAlphaF, part1);
            Core.multiply(orgF, oneMinusAlpha, part2);
            Core.add(part1, part2, blended);

            // Convert back to 16-bit
            Mat blended16 = new Mat();
            blended.convertTo(blended16, CvType.CV_16U);
            resultChannels.add(blended16);
        }

        // Blend alpha (optional: use max, or blend)
        Mat blendedAlpha = new Mat();
        Core.max(orgChannels.get(3), infoChannels.get(3), blendedAlpha);
        resultChannels.add(blendedAlpha);

        // Merge channels back
        Core.merge(resultChannels, outputMat);


       // Imgcodecs.imwrite("D:\\Work\\Temp\\infoOutput"+  bodyPart.getBodyPartName() + "_" + bodyPart.getBodyPartPosition() + ".png", outputMat);

        return outputMat;
    }

    private void convertToBGRA(Mat mat) {
        if (mat.empty()) {
            System.err.println("convertToBGRA: Input Mat is empty!");
            return;
        }

        if (mat.channels() == 1) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGRA);
        } else if (mat.channels() == 3) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
        }
        else  if(mat.channels()==4){
            //do nothing
        }else {
            throw new IllegalArgumentException("Unsupported number of channels: " + mat.channels());
        }
    }


    public Mat getAnnotatedImage(PatientBodyPart bodyPart, Mat orgMat) {

        //logger.info("orgMat size: {}, type: {}", bodyPart.getImageMat().size(), CvType.typeToString(orgMat.type()));
        setupForAnnotations(bodyPart);
        Canvas drawingCanvas = createDrawingCanvas();
        if(isCrop){
            drawingCanvas.setWidth(rect.width);
            drawingCanvas.setHeight(rect.height);
        }

        applyAnnotations(drawingCanvas, bodyPart);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage writableImage = drawingCanvas.snapshot(parameters, null);

        Mat annotationMat = ImageConversionService.writableImageToMat(writableImage);

        // Check if the depth of orgMat and informationMat are the same
        if(orgMat.depth() != annotationMat.depth()){
            logger.info("Type or Depth Issue in annotationMat : {} : {}", CvType.typeToString(orgMat.type()), CvType.typeToString(annotationMat.type()));

            if (orgMat.depth() == CvType.CV_8U) {
                annotationMat.convertTo(annotationMat, CvType.CV_8UC4);
            } else if (orgMat.depth() == CvType.CV_16U) {
                annotationMat.convertTo(annotationMat, CvType.CV_16UC4,256.0);
            }
        }

        //Imgcodecs.imwrite("D:\\Work\\Temp\\annotationMat" + bodyPart.getBodyPartName() + "_" + bodyPart.getBodyPartPosition() + ".png", annotationMat);


       logger.info("annotationMat size {} , type {} ", annotationMat.size(), CvType.typeToString(annotationMat.type()));
       logger.info("org Mat size {} , type {} ", orgMat.size(), CvType.typeToString(orgMat.type()));
        if(orgMat.width() < annotationMat.width() || orgMat.height() < annotationMat.height()){
            Mat paddedOrgMat = new Mat(annotationMat.height(), annotationMat.width(), orgMat.type(), new Scalar(0, 0, 0, 0));
            orgMat.copyTo(paddedOrgMat.submat(new Rect((annotationMat.width()-orgMat.width())/2, (annotationMat.height()-orgMat.height())/2, orgMat.width(), orgMat.height())));
            orgMat = paddedOrgMat;
        }

        //Imgcodecs.imwrite("D:\\Work\\Temp\\resizedOrg" + bodyPart.getBodyPartName() + "_" + bodyPart.getBodyPartPosition() + ".png", orgMat);

        convertToBGRA(orgMat);
        convertToBGRA(annotationMat);


        //Imgcodecs.imwrite("D:\\Work\\Temp\\orgMat"+  bodyPart.getBodyPartName() + "_" + bodyPart.getBodyPartPosition() + ".png", orgMat);

        //logger.info("orgMat Type {} ",CvType.typeToString(orgMat.type()));
        //logger.info("annotationMat Type {} ",CvType.typeToString(annotationMat.type()));

        if (orgMat.type() != CvType.CV_16UC4 || annotationMat.type() != CvType.CV_16UC4) {
            throw new IllegalArgumentException("Both input images must be CV_16UC4");
        }


        Mat outputMat = new Mat(orgMat.size(), orgMat.type(), new Scalar(0, 0, 65535, 0));

        // Split channels
        List<Mat> orgChannels = new ArrayList<>();
        List<Mat> annoChannels = new ArrayList<>();
        Core.split(orgMat, orgChannels);
        Core.split(annotationMat, annoChannels);

        // Normalize alpha to float [0,1]
        Mat orgAlphaF = new Mat();
        Mat annoAlphaF = new Mat();
        orgChannels.get(3).convertTo(orgAlphaF, CvType.CV_32F, 1.0 / 65535.0);
        annoChannels.get(3).convertTo(annoAlphaF, CvType.CV_32F, 1.0 / 65535.0);

        // For each RGB channel: blend using alpha
        List<Mat> resultChannels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Mat orgF = new Mat();
            Mat annoF = new Mat();
            orgChannels.get(i).convertTo(orgF, CvType.CV_32F);
            annoChannels.get(i).convertTo(annoF, CvType.CV_32F);

            Mat blended = new Mat();

            // blended = anno * alpha_anno + org * (1 - alpha_anno)
            Mat oneMinusAlpha = new Mat();
            Core.subtract(Mat.ones(annoAlphaF.size(), CvType.CV_32F), annoAlphaF, oneMinusAlpha);

            Mat part1 = new Mat(), part2 = new Mat();
            Core.multiply(annoF, annoAlphaF, part1);
            Core.multiply(orgF, oneMinusAlpha, part2);
            Core.add(part1, part2, blended);

            // Convert back to 16-bit
            Mat blended16 = new Mat();
            blended.convertTo(blended16, CvType.CV_16U);
            resultChannels.add(blended16);
        }

        // Blend alpha (optional: use max, or blend)
        Mat blendedAlpha = new Mat();
        Core.max(orgChannels.get(3), annoChannels.get(3), blendedAlpha);
        resultChannels.add(blendedAlpha);

        // Merge channels back
        Core.merge(resultChannels, outputMat);

        return outputMat;

    }


    private void setupForAnnotations(PatientBodyPart patientBodyPart){

        //based on the cropped 1024 image and cropped org image calculate the multiple
        Mat org = patientBodyPart.getImageMat().clone();

        double wof1024 = org.width();
        double hof1024 = org.height();

        if(wof1024>=hof1024){
            wof1024 = 1024;
        }
        else{
            wof1024 = (1024*wof1024)/hof1024;
        }

        double cropMultiple =  (double) org.width() /wof1024;

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

        //logger.info("export mat.rows() * width : {} {}", croppedMat.width(), width);

        applyExportValues(imageToolMultiple);

    }

    private double dynamicLineWidth;
    private double dynamicLabelFontSize;
    private double dynamicMultiple;
    private double dynamicCustomFontIconSize;


    public void applyExportValues(double multiple){

        dynamicMultiple = multiple;
        dynamicLineWidth = 2 * multiple;
        dynamicLabelFontSize = 14 * multiple;
        dynamicCustomFontIconSize = 200 * multiple;

        DynamicCanvasElementsResize.setCustomFontIconSize(200*multiple);
       /*  DynamicCanvasElementsResize.setLineWidth(2 * multiple);
        DynamicCanvasElementsResize.setEllipseRadiusX(8*multiple);
        DynamicCanvasElementsResize.setEllipseRadiusY(8*multiple);
        DynamicCanvasElementsResize.setLabelFontSize(14*multiple);
        DynamicCanvasElementsResize.setRectWidth(90*multiple);
        DynamicCanvasElementsResize.setRectHeight(30*multiple);
        DynamicCanvasElementsResize.setMultiple(multiple);
        DynamicCanvasElementsResize.setInfoLabelFontSize(45);*/

    }

    private void applyAnnotations(Canvas drawingCanvas, PatientBodyPart patientBodyPart) {
        List<AngleDrawWrapper> angleDrawWrapperList = patientBodyPart.getAngleDrawList();
        List<DistanceDrawWrapper> distanceDrawWrapperList = patientBodyPart.getDistanceDrawList();
        LeftRightDrawWrapper leftRightDrawWrapper = patientBodyPart.getLeftRightDrawWrapper();
        List<TextDrawWrapper> textDrawWrapperList = patientBodyPart.getTextDrawList();
        LeftRightDraw leftRightDraw = null;
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());


        double cropX= 0;
        double cropY = 0;
        if(!isCrop){
             cropX= patientBodyPart.getCropLayoutWrapper().getRectX();
             cropY = patientBodyPart.getCropLayoutWrapper().getRectY();
        }

        logger.info("Crop X: {}, Crop Y: {}", cropX, cropY);





        if(angleDrawWrapperList!=null){
            for(AngleDrawWrapper angleDrawWrapper : angleDrawWrapperList){

                for (EllipseWrapper ellipseWrapper : angleDrawWrapper.getEllipses()){
                    gc.setFill(Color.ORANGERED);
                    gc.fillOval((dynamicMultiple*(cropX+ ellipseWrapper.getCenterX() - ellipseWrapper.getRadiusX())),
                            (dynamicMultiple*(cropY+ ellipseWrapper.getCenterY() - ellipseWrapper.getRadiusY())),
                            dynamicMultiple*ellipseWrapper.getRadiusX() * 2,
                            dynamicMultiple*ellipseWrapper.getRadiusY() * 2);
                }

                for (LineWrapper lineWrapper : angleDrawWrapper.getLines()){
                    gc.setLineDashes();
                    gc.setStroke(Color.ORANGERED);
                    gc.setLineWidth(dynamicLineWidth);
                    gc.strokeLine(dynamicMultiple*(cropX+lineWrapper.getStartX()),dynamicMultiple*(cropY+lineWrapper.getStartY()), dynamicMultiple*(cropX+lineWrapper.getEndX()), dynamicMultiple*(cropY+lineWrapper.getEndY()));

                }

                LabelWrapper labelWrapper = angleDrawWrapper.getDegreeLabel();
                double angleDeg = labelWrapper.getValue();
                double px =  dynamicMultiple*(cropX+ labelWrapper.getX());
                double py =  dynamicMultiple*(cropY+ labelWrapper.getY());


                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * dynamicLabelFontSize));
                gc.strokeText(String.format("%.2f°", angleDeg), px, py+ 2*dynamicLabelFontSize);


                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * dynamicLabelFontSize));
                gc.fillText(String.format("%.2f°", angleDeg), px, py+ 2*dynamicLabelFontSize);

            }
        }

        if(distanceDrawWrapperList!=null){
            for(DistanceDrawWrapper distanceDrawWrapper : distanceDrawWrapperList){

                for (EllipseWrapper ellipseWrapper : distanceDrawWrapper.getEllipses()){
                    //Ellipse ellipse = new Ellipse(ellipseWrapper.getCenterX(),ellipseWrapper.getCenterY(),ellipseWrapper.getRadiusX(),ellipseWrapper.getRadiusY());

                    gc.setFill(Color.ORANGERED);
                    gc.fillOval((dynamicMultiple*(cropX+ ellipseWrapper.getCenterX() - ellipseWrapper.getRadiusX())),
                            (dynamicMultiple*(cropY+ ellipseWrapper.getCenterY() - ellipseWrapper.getRadiusY())),
                            dynamicMultiple*ellipseWrapper.getRadiusX() * 2,
                            dynamicMultiple*ellipseWrapper.getRadiusY() * 2);
                }

                //Line line = new Line(distanceDrawWrapper.getLines().getStartX(),distanceDrawWrapper.getLines().getStartY(),distanceDrawWrapper.getLines().getEndX(),distanceDrawWrapper.getLines().getEndY());
                gc.setLineDashes();
                gc.setStroke(Color.ORANGERED);
                gc.setLineWidth(dynamicLineWidth);
                gc.strokeLine(dynamicMultiple*(cropX+distanceDrawWrapper.getLines().getStartX()),dynamicMultiple*(cropY+distanceDrawWrapper.getLines().getStartY()), dynamicMultiple*(cropX+distanceDrawWrapper.getLines().getEndX()), dynamicMultiple*(cropY+distanceDrawWrapper.getLines().getEndY()));




                LabelWrapper labelWrapper = distanceDrawWrapper.getLabelWrapper();
                //DistanceLabel label = new DistanceLabel(labelWrapper.getValue(), labelWrapper.getX(), labelWrapper.getY(), Color.rgb(255, 235, 42));
                double distanceInmm = labelWrapper.getValue();
                double px = dynamicMultiple*(cropX+labelWrapper.getX());
                double py = dynamicMultiple*(cropY+labelWrapper.getY());

                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * dynamicLabelFontSize));
                gc.strokeText(String.format("%.2f mm", distanceInmm),  px, py+ 2*dynamicLabelFontSize);


                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 2 * dynamicLabelFontSize));
                gc.fillText(String.format("%.2f mm", distanceInmm),  px, py+ 2*dynamicLabelFontSize);
            }
        }


        if(leftRightDrawWrapper!=null){
            leftRightDraw = new LeftRightDraw(leftRightDrawWrapper.getText(),new Canvas());
            CustomFontIcon customFontIcon = leftRightDraw.getRlIcon();

            gc.drawImage(customFontIcon.getCurrentImage(),   (dynamicMultiple*(cropX+leftRightDrawWrapper.getOffsetX())),   (dynamicMultiple*(cropY+leftRightDrawWrapper.getOffsetY())));
        }


        if(textDrawWrapperList!=null){
            for(TextDrawWrapper textDrawWrapper:textDrawWrapperList){
                String text = textDrawWrapper.getText();
                double px = (dynamicMultiple*(cropX+textDrawWrapper.getX()));
                double py = (dynamicMultiple*(cropY+textDrawWrapper.getY()));

                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * dynamicLabelFontSize));
                gc.strokeText(text, px, py+ 2*dynamicLabelFontSize);

                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Century Gothic", FontWeight.BOLD, 2 * dynamicLabelFontSize));
                gc.fillText(text, px, py+ 2*dynamicLabelFontSize);
            }
        }


    }


}
