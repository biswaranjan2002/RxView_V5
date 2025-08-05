package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXButton;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import com.raymedis.rxviewui.modules.ImageProcessing.imageToolsWrapper.CropLayoutWrapper;
import com.raymedis.rxviewui.modules.dicom.DicomRecord;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.IdGenerator;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.PatientStudyNode;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemInfoController;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.raymedis.rxviewui.service.study.StudyService.patientStudyHandler;

public class ManualRegisterService {

    private static final ManualRegisterService instance = new ManualRegisterService();
    public static ManualRegisterService getInstance(){
        return instance;
    }

    private ManualRegisterController manualRegisterController ;
    private final BodyPartSelectionController bodyPartSelectionController = BodyPartSelectionController.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(ManualRegisterService.class);


    public int registerTrigger() {
        manualRegisterController = ManualRegisterController.getInstance();

        if (isTabLimitExceeded()) return -2;

        String patientID = manualRegisterController.patientId.getText().trim();
        String patientName = manualRegisterController.patientName.getText().trim();
        if (patientID.isEmpty() || patientName.isEmpty() ||
                bodyPartSelectionController.projectionsButonMap.isEmpty()) return -1;

        PatientInfo patientInfo = createPatientInfo();
        PatientStudy study = createPatientStudy(patientInfo, "", new BodyPartHandler());
        addPatientStudyTab(study, patientName);
        processProjections(bodyPartSelectionController.projectionsButonMap, false);
        return 0;
    }

    public int emergencyTrigger() {
        manualRegisterController = ManualRegisterController.getInstance();
        if (isTabLimitExceeded()) return -2;

        PatientInfo patientInfo = createPatientInfo();

        if (patientInfo.getPatientId().isEmpty()) {
           patientInfo.setPatientId(IdGenerator.generateEmergencyPatientId());
        }


        String studyId = "";
        PatientStudy study = createPatientStudy(patientInfo,studyId, new BodyPartHandler());

        addPatientStudyTab(study, patientInfo.getPatientName());


        if (bodyPartSelectionController.projectionsButonMap.isEmpty()) return -1;
        processProjections(bodyPartSelectionController.projectionsButonMap, true);
        return 0;
    }

    public int workListTrigger(DicomRecord dicomRecord){
        manualRegisterController = ManualRegisterController.getInstance();
        if (isTabLimitExceeded()) return -2;

        PatientInfo patientInfo = createPatientInfo(dicomRecord);
        PatientStudy study = createPatientStudyForWorkList(patientInfo,"", new BodyPartHandler(),dicomRecord);
        addPatientStudyTab(study, patientInfo.getPatientName());

        if(dicomRecord.getProcedureCode().isEmpty() && dicomRecord.getBodyPartExamined().isEmpty()){
            return -1;
        }else if(dicomRecord.getViewPosition().isEmpty()){
            return -1;
        }else{
            String procedureCode = dicomRecord.getProcedureCode();
            ProcedureEntity selectedProcedureEntity = manualRegisterController.procedureMap.get(procedureCode);
            ArrayList<StepEntity> selectedProcedureStepList;
            if (selectedProcedureEntity != null) {
                selectedProcedureStepList = StepService.getInstance().findByProcedureId(selectedProcedureEntity.getProcedureId());
            } else {
                String stepName = dicomRecord.getBodyPartExamined() + " " + dicomRecord.getViewPosition();
                selectedProcedureStepList = StepService.getInstance().findByStepName(stepName);
            }

            if(selectedProcedureStepList!=null){
                for (StepEntity step : selectedProcedureStepList) {
                    manualRegisterController.addStepToRegister(step);

                    processProjections(bodyPartSelectionController.projectionsButonMap, true);
                }
            }else {
                return -1;
            }
        }

        return 0;
    }

    private PatientInfo createPatientInfo(DicomRecord dicomRecord){
        String patientID = dicomRecord.getPatientId();
        String patientName = dicomRecord.getPatientName();
        int age = 0;
        double height = 0;
        double weight = 0;
        LocalDateTime birthDate = parseDicomDob(dicomRecord.getDob()).atStartOfDay();
        String patientSize = "medium";
        String sex = dicomRecord.getSex();
        String residence = "";

        return new PatientInfo(patientID, patientName, age, height, weight, birthDate, patientSize, sex, residence);
    }

    public static LocalDate parseDicomDob(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            throw new IllegalArgumentException("DOB string cannot be null or empty");
        }

        // Define the DICOM date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            // Parse the DOB string into a LocalDate
            return LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Invalid DOB format. Expected YYYYMMDD, but got: " + dob, e);
        }
    }


    private PatientInfo createPatientInfo() {
        String patientID = manualRegisterController.patientId.getText();


        String patientName = manualRegisterController.patientName.getText();
        int age = getAge();
        double height = getHeight();
        double weight = getWeight();
        LocalDateTime birthDate = LocalDateTime.now();
        String patientSize = getPatientSize();
        String sex = getGender();
        String residence = manualRegisterController.patientInstituteResidence.getText();

        return new PatientInfo(patientID, patientName, age, height, weight, birthDate, patientSize, sex, residence);
    }

    private PatientStudy createPatientStudyForWorkList(PatientInfo patientInfo, String studyID, BodyPartHandler bodyPartHandler,DicomRecord dicomRecord) {
        return new PatientStudy(
                patientInfo,
                bodyPartHandler,
                studyID,
                dicomRecord.getAccessionNumber(),
                "DX",
                "",
                "",
                "",
                dicomRecord.getStudyDescription(),
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "",
                "",
                "",
                dicomRecord.getReferringPhysician(),
                " ",
                SystemInfoController.getInstance().institutionNameLabel.getText(),
                null,
                false
        );
    }

    private PatientStudy createPatientStudy(PatientInfo patientInfo, String studyID, BodyPartHandler bodyPartHandler) {
        return new PatientStudy(
                patientInfo,
                bodyPartHandler,
                studyID,
                manualRegisterController.accessionNumber.getText(),
                "DX",
                manualRegisterController.requestProcedurePriority.getText(),
                manualRegisterController.additionalPatientHistory.getText(),
                manualRegisterController.admittingDiagnosisDescription.getText(),
                manualRegisterController.studyDescription.getText(),
                manualRegisterController.procedureComboBox.getSelectionModel().getSelectedItem(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                manualRegisterController.patientComments.getText(),
                manualRegisterController.readingPhysician.getText(),
                manualRegisterController.performingPhysician.getText(),
                manualRegisterController.referringPhysician.getText(),
                " ",
                SystemInfoController.getInstance().institutionNameLabel.getText(),
                null,
                false
        );
    }

    private void addPatientStudyTab(PatientStudy patientStudy, String patientName) {
        String sessionId = IdGenerator.generateSessionId(3, patientName);
        patientStudyHandler.addTab(sessionId, new PatientStudyNode(patientStudy, sessionId));
    }

    private void processProjections(Map<JFXButton, String> projectionsMap, boolean isEmergency) {
        for (Map.Entry<JFXButton, String> entry : projectionsMap.entrySet()) {
            PatientBodyPart bodyPart = createPatientBodyPart(entry);
            BodyPartNode node = createBodyPartNode(bodyPart, entry, isEmergency);
            int id = patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().getAllTabs().size()+1;
            assignIdForBodyPart(bodyPart,id);
            addBodyPartToHandler(node);
        }
    }

    private PatientBodyPart createPatientBodyPart(Map.Entry<JFXButton, String> entry) {
        String[] buttonTextParts = entry.getKey().getText().split(" ");

        String bodyPartModifier = buttonTextParts.length > 1
                ? String.join(" ", Arrays.copyOfRange(buttonTextParts, 1, buttonTextParts.length))
                : "";

        return new PatientBodyPart(
                false, false,
                entry.getValue().split(" ")[0],
                bodyPartModifier,
                getImageFromButton(entry.getKey()),
                null, new XrayParams(),
                null, null, null, "", "", "", "", null, null, null,
                new CropLayoutWrapper(), null, ""
        );
    }

    private void assignIdForBodyPart(PatientBodyPart bodyPart,int id) {
        bodyPart.setSeriesId(String.valueOf(id));
        bodyPart.setInstanceId("1");
    }

    private BodyPartNode createBodyPartNode(PatientBodyPart bodyPart, Map.Entry<JFXButton, String> entry, boolean isEmergency) {
        String sessionId = isEmergency ? entry.getValue()
                : IdGenerator.generateSessionId(3,
                bodyPart.getBodyPartName().toLowerCase() + " " + bodyPart.getBodyPartPosition().toLowerCase());
        return new BodyPartNode(sessionId, bodyPart);
    }

    private void addBodyPartToHandler(BodyPartNode node) {
        if(!patientStudyHandler.getCurrentStudy().getPatientStudy().getBodyPartHandler().containsBodyPart(node.getBodyPart().getBodyPartName())){
            patientStudyHandler.getCurrentStudy()
                    .getPatientStudy()
                    .getBodyPartHandler()
                    .addTab(node.getImageSessionId(), node);
        }
    }

    public void addBodyParts() {
        processProjections(bodyPartSelectionController.projectionsButonMap, false);
        StudyService.getInstance().createBodyPartTabs();
    }





    //region utility methods

    private boolean isTabLimitExceeded() {
        return patientStudyHandler.getAllTabs().size()>=5;
    }


    public Mat getImageFromButton(JFXButton button) {
        ImageView imageView = (ImageView)button.getGraphic();
        Image fxImage = imageView.getImage();
        return convertToMat(fxImage);
    }

    private Mat convertToMat(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();

        // Extract Pixel Data
        ByteBuffer pixelBuffer = ByteBuffer.allocate(width * height * 4); // Assuming RGBA format
        fxImage.getPixelReader().getPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getByteBgraInstance(), pixelBuffer, width * 4);

        // Create OpenCV Mat and populate it
        Mat mat = new Mat(height, width, CvType.CV_8UC4); // Assuming 4 channels (BGRA)
        mat.put(0, 0, pixelBuffer.array());

        return mat;
    }

    public String getPatientSize(){
        if(ManualRegisterController.getInstance().isSmall){
            return "Infant";
        }
        else if (ManualRegisterController.getInstance().isMedium) {
            return "Small";
        }
        else if (ManualRegisterController.getInstance().isLarge)
        {
            return "Medium";
        }
        else if (ManualRegisterController.getInstance().isExtraLarge){
            return "Big";
        }
        else{
            return "unknown Size";
        }
    }

    public String getGender(){
        if(ManualRegisterController.getInstance().isMale){
            return "male";
        }
        else if(ManualRegisterController.getInstance().isFemale){
            return "female";
        }else if(ManualRegisterController.getInstance().isNonBinary){
            return "non binary";
        }else{
            return "unknown Gender";
        }
    }

    private int getAge(){
        int age = 0;
        try {
            String ageText = manualRegisterController.age.getText();
            if (ageText != null && !ageText.isEmpty()) {
                age = Integer.parseInt(ageText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format: " + e.getMessage());
        }
        return age;
    }

    private double getHeight() {
        double height=0.0d;
        try {
            String heightText=manualRegisterController.height.getText();
            if(heightText!= null && !heightText.isEmpty()){
                height=Double.parseDouble(heightText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format: " + e.getMessage());
        }
        return height;

    }

    private double getWeight(){
        double weight=0.0d;
        try {
            String weightText=manualRegisterController.weight.getText();
            if(weightText!= null && !weightText.isEmpty()){
                weight=Double.parseDouble(weightText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format: " + e.getMessage());
        }
        return weight;

    }



    //endregion

}