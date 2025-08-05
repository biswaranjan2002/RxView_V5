package com.raymedis.rxviewui.modules.dicom;

import com.raymedis.rxviewui.controller.ExportStudy;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource.CanvasMainChange;
import com.raymedis.rxviewui.modules.ImageProcessing.ImageProcessService;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.export.ExportData;
import com.raymedis.rxviewui.service.export.ExportStudyService;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DicomService {

    private static final DicomService instance = new DicomService();
    public static DicomService getInstance(){
        return instance;
    }

    private final DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();
    private final DicomStorageService dicomStorageService = DicomStorageService.getInstance();
    private final SystemInfoService systemInfoService = SystemInfoService.getInstance();

    private DicomStorageEntity dicomStorageEntity;
    private SystemInfoEntity systemInfoEntity;
    private  DicomMain dicomMain = null;
    private static PatientStudy currentPatient;
    public static boolean isUploadTaskComplete = true;
    private static final Logger logger = LoggerFactory.getLogger(DicomService.class);


    public static String rootDirectory;

    static {
        rootDirectory = System.getProperty("user.dir");
    }

    public static void isStoreComplete(){
        isUploadTaskComplete = true;
    }


    public void loadData(PatientStudy patientStudy) throws SQLException {
        OurTransferUpdateStatusHandler.isTransferComplete = true;

        ArrayList<DicomGeneralEntity> dicomGeneralList =  dicomGeneralService.findAll();
        dicomStorageEntity =dicomStorageService.findSelected();
        if(!dicomGeneralList.isEmpty() && dicomStorageEntity!=null){
            DicomGeneralEntity dicomGeneralEntity = dicomGeneralList.getFirst();

            String ip = dicomGeneralEntity.getStationName();
            String calledAE = dicomStorageEntity.getAeTitle();
            String callingAE = dicomGeneralEntity.getStationAETitle();
            int port = Integer.parseInt(dicomGeneralEntity.getStationPort());
            dicomMain= new DicomMain(ip,port,calledAE,callingAE);
        }else{
            logger.info("something is null");
        }

        currentPatient = patientStudy;
        ArrayList<SystemInfoEntity> systemInfoEntityList = systemInfoService.getSystemInfo();

        if(systemInfoEntityList.isEmpty()){
            logger.info("studyEntity is null");
        }else{
            systemInfoEntity = systemInfoEntityList.getFirst();
        }

        logger.info("DicomService loaded with patient: {}", currentPatient.getPatientInfo().getPatientId());
    }


    public void uploadToStorageServer(){
        if (!OurTransferUpdateStatusHandler.isTransferComplete || !isUploadTaskComplete) {
            updateValidationLabelBasedOnStatus();
            return;
        }

        initializeUploadProcess();

        NavConnect.totalUploads++;

        // Create a new Task for the upload process
       /* Task<Void> uploadTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Update UI from background thread
                    logger.info("Creating DICOM File/s...");

                    List<TabNode> bodyPartsTab = currentPatient.getBodyPartHandler().getAllTabs();
                    List<String> validFiles = processBodyParts(bodyPartsTab);

                    // Update progress - could also use updateProgress() method
                    logger.info("Uploading to server...");

                    // Perform the actual upload
                    dicomMain.store(validFiles.toArray(new String[0]));

                    return null;
                } catch (Exception e) {
                    // Handle error but let the task framework know it failed
                    Platform.runLater(() -> handleUploadError(e));
                    throw e;
                }
            }
        };

        setupTaskHandlers(uploadTask);
        new Thread(uploadTask).start();*/

        try {
            // Update UI from background thread
            logger.info("Creating DICOM File/s...");

            List<TabNode> bodyPartsTab = currentPatient.getBodyPartHandler().getAllTabs();
            List<String> validFiles = processBodyParts(bodyPartsTab);

            // Update progress - could also use updateProgress() method
            logger.info("Uploading to server...");

            // Perform the actual upload
            dicomMain.store(validFiles.toArray(new String[0]));
        } catch (Exception e) {
            // Handle error but let the task framework know it failed
            Platform.runLater(() -> handleUploadError(e));
            throw e;
        }

    }

    private void updateValidationLabelBasedOnStatus() {
        if(!OurTransferUpdateStatusHandler.isTransferComplete){
            logger.info("Status : Uploading dicom/s... Please wait");
        }
        if( !isUploadTaskComplete){
            logger.info("Status : Creating dicom/s... Please wait");
        }
    }

    private void initializeUploadProcess() {
        isUploadTaskComplete = false;
        logger.info("Status : Uploading Begins");
    }

    private List<String> processBodyParts(List<TabNode> bodyPartsTab) {
        List<String> validFiles = new ArrayList<>();

        for (TabNode tab : bodyPartsTab) {
            String filePath = createObjects((BodyPartNode) tab);
            if (filePath != null && !filePath.isEmpty() && !filePath.equals("err")) {
                validFiles.add(filePath);
            }
        }

        return validFiles;
    }

    private void setupTaskHandlers(Task<Void> uploadTask) {
        // Handle successful completion
        uploadTask.setOnSucceeded(event -> Platform.runLater(() -> {
            logger.info("Upload completed successfully!", "green");
            isUploadTaskComplete = true;

            NavConnect.getInstance().uploadSuccess();
            StudyService.getInstance().createBodyPartTabs();
        }));

        // Handle failures
        uploadTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                Throwable exception = uploadTask.getException();
                isUploadTaskComplete = false;
                // Log the error for debugging
                logger.error("Upload task failed: {}", exception);
                NavConnect.getInstance().uploadFailed();

                StudyService.getInstance().createBodyPartTabs();
            });
        });

        // Handle progress updates (if your Task reports progress)
        uploadTask.progressProperty().addListener((observable, oldValue, newValue) -> {
            double progress = newValue.doubleValue();
            int percentage = (int) (progress * 100);
            logger.info("Uploading: {} %" , percentage );
        });
    }

    private String createObjects(BodyPartNode bodyPartNode) {
        try {
            if (bodyPartNode.getBodyPart().getImageMat() == null) {
                return null;
            }


            List<Mat> imageList = new ArrayList<>();
            ExportStudyService exportStudyService = ExportStudyService.getInstance();
            String destinationFolder = System.getProperty("user.dir")+  "\\temp\\";
            List<ExportData> exportDataList = exportStudyService.exportStudy(currentPatient,needsAnnotations(),needsStudyInformation(),shouldUseCropImage(),destinationFolder);

            for(ExportData data : exportDataList){
                Mat mat = data.getImage();
                imageList.add(mat);
            }


            return createDicomFile(bodyPartNode, imageList);
        } catch (Exception e) {
            logger.error("DICOM File creation failed: {}", e);
            handleUploadError(e);
            return "err";
        }
    }

    private void handleUploadError(Exception e) {
        Platform.runLater(()->{
            logger.error("Failed to create dicom File {}", e);
            logger.info("Status : Upload Failed");
            isUploadTaskComplete = true;
        });
    }

    private Mat handleOnlyCrop(BodyPartNode bodyPartNode){
        ExportStudy exportStudy = new ExportStudy(CanvasMainChange.drawingCanvas);
        //Mat mat2 = exportStudy.applyCropAndImageParams(bodyPartNode.getBodyPart(),shouldUseCropImage());

        Mat mat = exportStudy.applyImageProperties(bodyPartNode.getBodyPart());
        mat = exportStudy.applyOnlyCrop(mat,bodyPartNode.getBodyPart());

        return mat;
    }

    private Mat handleStudyInformationImage(BodyPartNode bodyPartNode) {
        ExportStudy exportStudy = new ExportStudy(CanvasMainChange.drawingCanvas);
        Mat mat = exportStudy.applyImageParams(bodyPartNode.getBodyPart());
        //logger.info("orgMat type {}", CvType.typeToString(mat.type()));

        return exportStudy.saveWithInformationBurn(mat);
    }

    private void convertToBGRA(Mat mat) {
        if (mat.channels() != 4) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);
        }
    }

    public Mat handleAnnotatedImage(BodyPartNode bodyPartNode) {
        ExportStudy exportStudy = new ExportStudy(CanvasMainChange.drawingCanvas);

        Mat orgMat = exportStudy.applyImageParams(bodyPartNode.getBodyPart());
        orgMat = ImageProcessService.getInstance().resizeImageToSquareShape(orgMat);

        //Imgcodecs.imwrite("D:\\temp\\imageMat.png", orgMat);

        Mat annotationMat = exportStudy.saveWithAnnotationBurn(bodyPartNode.getBodyPart(), shouldUseCropImage());




        annotationMat.convertTo(annotationMat, CvType.CV_16UC4, 256.0);

        //Imgcodecs.imwrite("D:\\temp\\annotationMat.png", annotationMat);

        // Ensure working copy is BGRA
        convertToBGRA(orgMat);
        convertToBGRA(annotationMat);

        /*logger.info("orgMat type {}", CvType.typeToString(orgMat.type()));
        logger.info("annotationMat type {}", CvType.typeToString(annotationMat.type()));*/

        Mat resultMat = new Mat(orgMat.size(), orgMat.type(), new Scalar(0, 0, 65535, 0));

        Mat alphaMask = new Mat();
        Core.extractChannel(orgMat, alphaMask, 3);
        Mat mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8UC1, 1.0 / 256.0);
        orgMat.copyTo(resultMat, mask8U);

        alphaMask = new Mat();
        Core.extractChannel(annotationMat, alphaMask, 3);
        mask8U = new Mat();
        alphaMask.convertTo(mask8U, CvType.CV_8U, 1.0 / 256.0);
        annotationMat.copyTo(resultMat, mask8U);


        Imgcodecs.imwrite("D:\\temp\\output.png", resultMat);

        return resultMat;
    }



    private Mat handleAnnotationAndInformation(BodyPartNode bodyPartNode) {
        ExportStudy exportStudy = new ExportStudy(CanvasMainChange.drawingCanvas);
        Mat mat = exportStudy.saveWithAnnotationBurn(bodyPartNode.getBodyPart(),shouldUseCropImage());
        return exportStudy.saveWithInformationBurn(mat);
    }


    //region helper functions

    private boolean needsStudyInformation() {
        return dicomStorageEntity.isBurnedInInformation();
    }

    private boolean needsAnnotations() {
        return dicomStorageEntity.isBurnedInAnnotation();
    }

    private boolean shouldUseOriginalImage() {
        return !dicomStorageEntity.isBurnedInAnnotation() && !dicomStorageEntity.isBurnedInInformation() && !dicomStorageEntity.isBurnedWithCrop();
    }

    private boolean shouldUseCropImage(){
        return dicomStorageEntity.isBurnedWithCrop();
    }

    //endregion


    private String createDicomFile(BodyPartNode bodyPartNode, List<Mat> imageList) throws Exception {
        String outputDicomPath = buildAndCreateOutputPath(bodyPartNode.getBodyPart());

        for (int i = 0; i < imageList.size(); i++) {
            Mat mat = imageList.get(i);

            //logger.info("mat type {}", CvType.typeToString(mat.type()));

            /*String path = "D:\\temp\\createDicomFile.png";
            Imgcodecs.imwrite(path, mat);*/

            Mat updatedMat = rgbaToRgb(mat);

            //logger.info("updatedMat type {}", CvType.typeToString(updatedMat.type()));

            // Update the list with the new Mat
            imageList.set(i, updatedMat);
        }


        DicomMetadata metadata = buildDicomMetadata(bodyPartNode);
        DicomCreator dicomCreator = new DicomCreator(imageList, outputDicomPath, metadata);

        cleanupResources(imageList);
        updateBodyPartUIds(bodyPartNode, dicomCreator);

        return outputDicomPath;
    }

    private void updateBodyPartUIds(BodyPartNode bodyPartNode, DicomCreator dicomCreator) {
        String seriesInstanceUID = dicomCreator.getSeriesInstanceUID() == null ? "": dicomCreator.getSeriesInstanceUID();
        String sOPInstanceUID = dicomCreator.getsOPInstanceUID() == null ? "": dicomCreator.getsOPInstanceUID();
        String studyInstanceUID = dicomCreator.getStudyInstanceUID() == null ? "": dicomCreator.getStudyInstanceUID();

        bodyPartNode.getBodyPart().setSeriesUid(seriesInstanceUID);
        bodyPartNode.getBodyPart().setInstanceUid(sOPInstanceUID);
        bodyPartNode.getBodyPart().setSeriesUid(studyInstanceUID);
    }

    private DicomMetadata buildDicomMetadata(BodyPartNode bodyPartNode) {

        DicomMetadata dicomMetadata = new DicomMetadata();

        String patientNameText = currentPatient.getPatientInfo().getPatientName().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientName(patientNameText);

        String patientIdText =  currentPatient.getPatientInfo().getPatientId().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientId(patientIdText);

        String patientDOB = String.format("%04d%02d%02d", currentPatient.getPatientInfo().getBirthDate().getYear(),  currentPatient.getPatientInfo().getBirthDate().getMonthValue(),  currentPatient.getPatientInfo().getBirthDate().getDayOfMonth());
        dicomMetadata.setPatientDOB(patientDOB);

        String patientSex =  currentPatient.getPatientInfo().getSex().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientSex(patientSex);

        String accessionNumberText =  currentPatient.getAccessionNum().trim().replaceAll(" +", "^");
        dicomMetadata.setAccessionNumber(accessionNumberText);

        LocalDateTime exposureDateTime = bodyPartNode.getBodyPart().getExposureDateTime();

        if (exposureDateTime == null) {
            logger.warn("ExposureDateTime is null. Using current time as default.");
            exposureDateTime = LocalDateTime.now();
        }

        Date studyDateTime = Date.from(
                exposureDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
        dicomMetadata.setStudyDateTime(studyDateTime);


        Date acquisitionDateTime = Date.from(
                exposureDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
        dicomMetadata.setAcquisitionDateTime(acquisitionDateTime);


        String referringPhysician = currentPatient.getReferringPhysician().trim().replaceAll(" +", "^");
        dicomMetadata.setReferringPhysician(referringPhysician);


        String kVp = String.valueOf(bodyPartNode.getBodyPart().getXrayParams().getKV());
        dicomMetadata.setkVp(kVp);

        String xrayTubeCurrent = String.valueOf(bodyPartNode.getBodyPart().getXrayParams().getmA());
        dicomMetadata.setXrayTubeCurrent(xrayTubeCurrent);

        String dap = String.valueOf(bodyPartNode.getBodyPart().getXrayParams().getmAs());
        dicomMetadata.setAreaDoseProduct(dap);

        String viewPosition = String.valueOf(bodyPartNode.getBodyPart().getBodyPartPosition());
        dicomMetadata.setViewPosition(viewPosition);

        String studyId = String.valueOf(currentPatient.getStudyId());
        dicomMetadata.setStudyId(studyId);

        String seriesNumber = String.valueOf(bodyPartNode.getBodyPart().getSeriesId());
        dicomMetadata.setSeriesNumber(seriesNumber);

        String instanceNumber = String.valueOf(bodyPartNode.getBodyPart().getInstanceId());
        dicomMetadata.setInstanceNumber(instanceNumber);

        String modality = String.valueOf(currentPatient.getModality());
        dicomMetadata.setModality(modality);

        String sopClass = "1.2.840.10008.5.1.4.1.1.1.1"; // radiography SOP Class UID
        dicomMetadata.setSopClass(sopClass);

        String hospitalNameText = systemInfoEntity.getInstitutionName().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalName(hospitalNameText);

        String hospitalAddressText = systemInfoEntity.getInstitutionAddress().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalAddress(hospitalAddressText);

        String studyDescription = String.valueOf(currentPatient.getStudyDescription());
        dicomMetadata.setStudyDescription(studyDescription);

        String manufacturer = String.valueOf(systemInfoEntity.getManufacturer());
        dicomMetadata.setManufacturer(manufacturer);

        String bodyPartName = String.valueOf(bodyPartNode.getBodyPart().getBodyPartName());
        dicomMetadata.setBodyPart(bodyPartName);

        String procedureCodeSequence = String.valueOf(currentPatient.getProcedureCode());
        dicomMetadata.setProcedureCode(procedureCodeSequence);

        boolean burnedInAnnotation = dicomStorageEntity.isBurnedInAnnotation();
        dicomMetadata.setBurnedInAnnotation(burnedInAnnotation);


        return dicomMetadata;
    }

    private void cleanupResources(List<Mat> imageList) {
        for (Mat mat :imageList){
            mat.release();
        }
    }

    public static String buildAndCreateOutputPath(PatientBodyPart patientBodyPart) {
        String outputDicomPath = rootDirectory + "/dcmOutput";
        Path path = Paths.get(outputDicomPath);

        // Create the directory if it doesn't exist
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            else{
                logger.info("path exits");
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }


        outputDicomPath = outputDicomPath + "/" + currentPatient.getPatientInfo().getPatientName() + patientBodyPart.getSeriesId() +  patientBodyPart.getInstanceId()  + ".dcm";
        logger.info(outputDicomPath);
        return outputDicomPath;
    }

    private Mat rgbaToRgb(Mat original) {
        // Create a working copy to avoid modifying original
        Mat converted = new Mat();

        switch (original.channels()) {
            case 4: // RGBA/BGRA case
                // Remove alpha channel and convert to 3-channel
                Imgproc.cvtColor(original, converted, Imgproc.COLOR_BGRA2BGR);
                break;

            case 3: // Already color (BGR/RGB)
                original.copyTo(converted);
                break;

            case 1: // Grayscale
                original.copyTo(converted);
                break;

            default:
                throw new IllegalArgumentException("Unsupported channel count: " + original.channels());
        }

        // Ensure 8U or 16U depth
        if (original.depth() != CvType.CV_8U && original.depth() != CvType.CV_16U) {
            double minVal, maxVal;
            Core.MinMaxLocResult mm = Core.minMaxLoc(original);
            minVal = mm.minVal;
            maxVal = mm.maxVal;

            // Normalize to 16-bit unsigned if dynamic range exceeds 8-bit
            if (maxVal > 255 || minVal < 0) {
                converted.convertTo(converted, CvType.CV_16UC(converted.channels()),
                        (65535.0/(maxVal - minVal)),
                        (-minVal * (65535.0/(maxVal - minVal))));
            } else {
                converted.convertTo(converted, CvType.CV_8UC(converted.channels()));
            }
        }

        original.release();
        return converted;
    }


}
