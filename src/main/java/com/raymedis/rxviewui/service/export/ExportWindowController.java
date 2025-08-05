package com.raymedis.rxviewui.service.export;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.raymedis.rxviewui.ViewBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageService;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import com.raymedis.rxviewui.modules.dicom.DicomCreator;
import com.raymedis.rxviewui.modules.dicom.DicomMetadata;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportWindowController {

    public HBox imageFormat;
    public Label imageFormatMandatory;
    public JFXButton dicomButton;
    public JFXButton bmpButton;
    public JFXButton pngButton;
    public JFXButton jpegButton;
    public HBox exportPathBox;
    public Label exportMandatory;
    public TextField selectedPath;
    public JFXButton pathButton;
    public Label statusLabel;
    public JFXButton startButton;
    public JFXButton closeButton;
    public ProgressBar progressBar;
    public JFXCheckBox burnAnnotationsCheckBox;
    public JFXCheckBox burnInformationCheckBox;


    public JFXCheckBox burnCropCheckBox;
    public ViewBox viewBox;

    private boolean isDicom=false;
    private boolean isBmp = false;
    private boolean isPng = false;
    private boolean isJpeg = false;

    private static final ExportWindowController instace = new ExportWindowController();
    public static ExportWindowController getInstance(){
        return instace;
    }


    private static PatientStudy currentStudy;
    private static final List<PatientBodyPart> currentStudyMatList = new ArrayList<>();
    private final SystemInfoService systemInfoService = SystemInfoService.getInstance();
    private final DicomStorageService dicomStorageService = DicomStorageService.getInstance();
    private SystemInfoEntity systemInfoEntity;
    private DicomStorageEntity dicomStorageEntity ;
    private String mode = "";

    public static String rootDirectory;

    static {
        rootDirectory = System.getProperty("user.dir");
    }

    private final Logger logger = LoggerFactory.getLogger(ExportWindowController.class);

    public void loadData(PatientStudy patientStudy, String mode){
        currentStudy = patientStudy;
        for (TabNode bodyPart : currentStudy.getBodyPartHandler().getAllTabs()){
            PatientBodyPart patientBodyPart = ((BodyPartNode)bodyPart).getBodyPart();
            Mat mat = patientBodyPart.getImageMat();
            if(mat!=null){
                currentStudyMatList.add(patientBodyPart);
            }
        }

        if(mode.equals("database")){
            viewBox.setId("gridPane3");
        }else{
            viewBox.setId("gridPane2");
        }
        this.mode=mode;

    }



    public void loadEvents(){
        dicomStorageEntity = dicomStorageService.findSelected();
        ArrayList<SystemInfoEntity> systemInfoEntityList;
        try {
            systemInfoEntityList = systemInfoService.getSystemInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(!systemInfoEntityList.isEmpty()){
            systemInfoEntity = systemInfoEntityList.getFirst();
        }



        pngClick();
        selectedPath.setText("D:\\");
        burnCropCheckBox.setSelected(true);
        burnAnnotationsCheckBox.setSelected(true);
        burnInformationCheckBox.setSelected(true);
    }



    public void resetImageFormat(){
        isDicom = false;
        isBmp = false;
        isPng = false;
        isJpeg = false;

        dicomButton.setId("unSelectedButton");
        bmpButton.setId("unSelectedButton");
        pngButton.setId("unSelectedButton");
        jpegButton.setId("unSelectedButton");
    }

    public void dicomClick() {
        dicomButton.getParent().requestFocus();
        resetImageFormat();
        isDicom=true;
        dicomButton.setId("selectedButton");
    }

    public void bmpClick() {
        bmpButton.getParent().requestFocus();
        resetImageFormat();
        isBmp=true;
        bmpButton.setId("selectedButton");
    }

    public void pngClick() {
        pngButton.getParent().requestFocus();
        resetImageFormat();
        isPng=true;
        pngButton.setId("selectedButton");
    }

    public void jpegClick() {
        jpegButton.getParent().requestFocus();
        resetImageFormat();
        isJpeg=true;
        jpegButton.setId("selectedButton");
    }

    public void dialogBoxClick() {
        pathButton.getParent().requestFocus();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        // Show open directory dialog
        Stage stage = (Stage) selectedPath.getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);

        if (directory != null) {
            selectedPath.setText(directory.getAbsolutePath());
        }
    }

    public void startExport() {
        startButton.getParent().requestFocus();
        //startButton.setDisable(true);

        try {

            if (!isImageFormatValid()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select an Image Format.");
                return;
            }

            if (currentStudyMatList.isEmpty()) {
                return;
            }

            String destinationFolder = createTimestampedFolder();
            if (destinationFolder == null) return;

            statusLabel.setText("Converting Images");
            imageConversion(currentStudy, destinationFolder);

            statusLabel.setText("Export Complete");
            showAlert(Alert.AlertType.NONE, "Success", "Export completed successfully!");

        } catch (Exception e) {
            logger.error("e: ", e);
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during export: " + e.getMessage());
        }
    }

    private void imageConversion(PatientStudy patientStudy, String destinationFolder) {
        ExportStudyService exportStudyService = ExportStudyService.getInstance();
        List<ExportData> exportDataList = exportStudyService.exportStudy(patientStudy,burnAnnotationsCheckBox.isSelected(),burnInformationCheckBox.isSelected(),burnCropCheckBox.isSelected(),destinationFolder);

       /* if (image.empty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read image ");
            return;
        }

        ExportStudy exportStudy = new ExportStudy(CanvasMainChange.drawingCanvas);


        if (shouldUseOriginalImage()) {
            image = exportStudy.applyCropAndImagingParams(patientBodyPart,burnCropCheckBox.isSelected());
        }
        else if(burnAnnotationsCheckBox.isSelected() && burnInformationCheckBox.isSelected()){
            Mat mat = exportStudy.saveWithAnnotationBurn(patientBodyPart,burnCropCheckBox.isSelected());
            image =  exportStudy.saveWithInformationBurn(mat);
        }
        else if(burnAnnotationsCheckBox.isSelected()){
            image = exportStudy.saveWithAnnotationBurn(patientBodyPart,burnCropCheckBox.isSelected());
        }
        else if(burnInformationCheckBox.isSelected()){
            Mat mat = exportStudy.applyCropAndImagingParams(patientBodyPart,burnCropCheckBox.isSelected());
            image = exportStudy.saveWithInformationBurn(mat);
        }
        else if(burnCropCheckBox.isSelected()) {
            image = exportStudy.applyCropAndImagingParams(patientBodyPart,burnCropCheckBox.isSelected());
        }*/

        for(ExportData exportData : exportDataList) {

           String outputFilePath = exportData.getPath();
            Mat image = exportData.getImage();
            PatientBodyPart patientBodyPart = exportData.getPatientBodyPart();

            if (isDicom) {
                //logger.info("while calling dicom");
                outputFilePath = outputFilePath + ".dcm";
                try {
                    createDicomFile(outputFilePath,patientBodyPart, image);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (isPng) {
                outputFilePath = outputFilePath + ".png";
                Imgcodecs.imwrite(outputFilePath, image);
            }
            else if (isJpeg) {
                outputFilePath = outputFilePath + ".jpg";
                Imgcodecs.imwrite(outputFilePath, image);
            }
            else if (isBmp) {
                outputFilePath = outputFilePath + ".bmp";
                Imgcodecs.imwrite(outputFilePath, image);
            }

        }



    }



    private void createDicomFile(String outputDicomPath, PatientBodyPart patientBodyPart, Mat image) throws Exception {

        logger.info("inside createDicomFile");

        Mat mat = rgbaToRgb(image);

        logger.info("images ready");

        DicomMetadata metadata = buildDicomMetadata(patientBodyPart);

        logger.info("meta data is ready");

        DicomCreator dicomCreator = new DicomCreator(Collections.singletonList(mat), outputDicomPath, metadata);

        logger.info("dicom file ready");

        mat.release();
        updateBodyPartUIds(patientBodyPart, dicomCreator);
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

    private DicomMetadata buildDicomMetadata(PatientBodyPart patientBodyPart) {

        DicomMetadata dicomMetadata = new DicomMetadata();

        String patientNameText = currentStudy.getPatientInfo().getPatientName().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientName(patientNameText);

        String patientIdText =  currentStudy.getPatientInfo().getPatientId().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientId(patientIdText);

        String patientDOB = String.format("%04d%02d%02d", currentStudy.getPatientInfo().getBirthDate().getYear(),  currentStudy.getPatientInfo().getBirthDate().getMonthValue(),  currentStudy.getPatientInfo().getBirthDate().getDayOfMonth());
        dicomMetadata.setPatientDOB(patientDOB);

        String patientSex =  currentStudy.getPatientInfo().getSex().trim().replaceAll(" +", "^");
        dicomMetadata.setPatientSex(patientSex);

        String accessionNumberText =  currentStudy.getAccessionNum().trim().replaceAll(" +", "^");
        dicomMetadata.setAccessionNumber(accessionNumberText);

        LocalDateTime exposureDateTime = patientBodyPart.getExposureDateTime();

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


        String referringPhysician = currentStudy.getReferringPhysician().trim().replaceAll(" +", "^");
        dicomMetadata.setReferringPhysician(referringPhysician);


        String kVp = String.valueOf(patientBodyPart.getXrayParams().getKV());
        dicomMetadata.setkVp(kVp);

        String xrayTubeCurrent = String.valueOf(patientBodyPart.getXrayParams().getmA());
        dicomMetadata.setXrayTubeCurrent(xrayTubeCurrent);

        String dap = String.valueOf(patientBodyPart.getXrayParams().getmAs());
        dicomMetadata.setAreaDoseProduct(dap);

        String viewPosition = String.valueOf(patientBodyPart.getBodyPartPosition());
        dicomMetadata.setViewPosition(viewPosition);

        String studyId = String.valueOf(currentStudy.getStudyId());
        dicomMetadata.setStudyId(studyId);

        String seriesNumber = String.valueOf(patientBodyPart.getSeriesId());
        dicomMetadata.setSeriesNumber(seriesNumber);

        String instanceNumber = String.valueOf(patientBodyPart.getInstanceId());
        dicomMetadata.setInstanceNumber(instanceNumber);

        String modality = String.valueOf(currentStudy.getModality());
        dicomMetadata.setModality(modality);

        String sopClass = "1.2.840.10008.5.1.4.1.1.1.1"; // radiography SOP Class UID
        dicomMetadata.setSopClass(sopClass);

        String hospitalNameText = systemInfoEntity.getInstitutionName().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalName(hospitalNameText);

        String hospitalAddressText = systemInfoEntity.getInstitutionAddress().trim().replaceAll(" +", "^");
        dicomMetadata.setHospitalAddress(hospitalAddressText);

        String studyDescription = String.valueOf(currentStudy.getStudyDescription());
        dicomMetadata.setStudyDescription(studyDescription);

        String manufacturer = String.valueOf(systemInfoEntity.getManufacturer());
        dicomMetadata.setManufacturer(manufacturer);

        String bodyPartName = String.valueOf(patientBodyPart.getBodyPartName());
        dicomMetadata.setBodyPart(bodyPartName);

        String procedureCodeSequence = String.valueOf(currentStudy.getProcedureCode());
        dicomMetadata.setProcedureCode(procedureCodeSequence);

        dicomMetadata.setBurnedInAnnotation(burnAnnotationsCheckBox.isSelected());
        return dicomMetadata;
    }

    private void updateBodyPartUIds(PatientBodyPart patientBodyPart, DicomCreator dicomCreator) {
        String seriesInstanceUID = dicomCreator.getSeriesInstanceUID() == null ? "": dicomCreator.getSeriesInstanceUID();
        String sOPInstanceUID = dicomCreator.getsOPInstanceUID() == null ? "": dicomCreator.getsOPInstanceUID();
        String studyInstanceUID = dicomCreator.getStudyInstanceUID() == null ? "": dicomCreator.getStudyInstanceUID();

        patientBodyPart.setSeriesUid(seriesInstanceUID);
        patientBodyPart.setInstanceUid(sOPInstanceUID);
        patientBodyPart.setSeriesUid(studyInstanceUID);
    }

    private String createTimestampedFolder() {
        if (selectedPath.getText() == null || selectedPath.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a directory.");
            return null;
        }

        statusLabel.setText("Creating Folder");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy__HH_mm_ss");
        String timestamp = dtf.format(LocalDateTime.now());
        String destinationFolder = selectedPath.getText() + File.separator + "RxView Export" + File.separator + timestamp;

        File destinationDir = new File(destinationFolder);
        if (!destinationDir.exists() && !destinationDir.mkdirs()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create destination folder.");
            return null;
        }

        return destinationFolder;
    }

    private boolean isImageFormatValid() {
        return imageFormat.isDisabled() || (isDicom || isBmp || isPng || isJpeg);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        // Find the secondary screen
        Screen secondaryScreen = Screen.getScreens().stream()
                .filter(screen -> !screen.equals(Screen.getPrimary()))
                .findFirst()
                .orElse(Screen.getPrimary());

        Rectangle2D screenBounds = secondaryScreen.getVisualBounds();

        // Create the alert
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);

        // Set the alert position to the center of the secondary screen
        alert.initOwner(getCurrentStage()); // Use the main application stage as the owner
        alert.setOnShown(e -> {
            // Center alert on the secondary screen
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - alertStage.getWidth()) / 2);
            alertStage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - alertStage.getHeight()) / 2);
        });

        // Show the alert
        alert.showAndWait();
    }

    private Stage getCurrentStage() {
        return (Stage) selectedPath.getScene().getWindow();
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        if (!mode.equals("database")) {
            StudyService.getInstance().createBodyPartTabs();
        }

    }

}
