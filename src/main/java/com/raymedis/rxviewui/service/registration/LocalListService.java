package com.raymedis.rxviewui.service.registration;

import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyEntity;
import com.raymedis.rxviewui.database.tables.patientStudy_table.PatientStudyService;
import com.raymedis.rxviewui.database.tables.register.localListBodyParts_table.LocalListBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.register.localListBodyParts_table.LocalListBodyPartsService;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListEntity;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListService;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartHandler;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.bodypart.PatientBodyPart;
import com.raymedis.rxviewui.modules.study.params.XrayParams;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.IdGenerator;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.study.PatientStudyNode;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemInfoController;
import com.raymedis.rxviewui.service.serialization.KryoSerializer;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.raymedis.rxviewui.service.study.StudyService.patientStudyHandler;

public class LocalListService {


    private static LocalListService instance = new LocalListService();
    public static LocalListService getInstance(){
        return instance;
    }

    private LocalListBodyPartsService localListBodyPartsService = LocalListBodyPartsService.getInstance();
    private PatientLocalListService patientLocalListService = PatientLocalListService.getInstance();

    public int localListTrigger(PatientLocalListEntity patientLocalListEntity) {
        List<TabNode> allTabs = patientStudyHandler.getAllTabs();

        if(allTabs.size()>=5) {
            return -2;
        }

        String studyID = patientLocalListEntity.getStudyId();
        //System.out.println(studyID);
        String sessionId = patientLocalListEntity.getStudySessionId();

        if((sessionId!=null && !sessionId.isEmpty() || (studyID != null && !studyID.isEmpty()))){
            //System.out.println("sessionId Exists...");
            if(sessionId!=null && !sessionId.isEmpty()){
                for (TabNode tab : allTabs){
                    PatientStudyNode studyTab = (PatientStudyNode) tab;
                    String studySessionId = studyTab.getStudySessionId();
                    if(studySessionId.equals(sessionId))
                    {
                        //System.out.println("study is already opened but not saved");
                        StudyService.patientStudyHandler.selectPatientStudy(studySessionId);
                        return 0;
                    }
                }
            }

            if(studyID!=null && !studyID.isEmpty()){
                PatientStudyEntity patientStudyEntity = PatientStudyService.getInstance().findPatientByStudyId(studyID);
                if(patientStudyEntity!=null){
                    //System.out.println("study exists");
                    for (TabNode tab : allTabs) {
                        PatientStudyNode studyTab = (PatientStudyNode) tab;
                        if(studyTab.getPatientStudy().getStudyId().equals(patientStudyEntity.getStudyId())){
                            //System.out.println("study is already opened");
                            String studySessionID = studyTab.getStudySessionId();
                            StudyService.patientStudyHandler.selectPatientStudy(studySessionID);
                            patientLocalListEntity.setStudySessionId(studySessionID);
                            try {
                                patientLocalListService.update(patientLocalListEntity.getId(),patientLocalListEntity);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            return 0;
                        }
                    }
                    //System.out.println("opening study from database");
                    openSelectedStudy(patientStudyEntity);
                    return 1;
                }
            }
        }

        //System.out.println("creating a new study");

        String patientID = patientLocalListEntity.getPatientId();
        String patientName = patientLocalListEntity.getPatientName();

        ArrayList<LocalListBodyPartsEntity> bodyPartsList;
        try {
            bodyPartsList = localListBodyPartsService.findByLocalListId(patientLocalListEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int age = patientLocalListEntity.getAge();
        double height = patientLocalListEntity.getHeight();
        double weight = patientLocalListEntity.getWeight();
        LocalDateTime birthDate = LocalDateTime.now();

        String patientSize = patientLocalListEntity.getPatientSize();
        String readingPhysician = patientLocalListEntity.getReadingPhysician();
        String performingPhysician = patientLocalListEntity.getPerformingPhysician();
        String referringPhysician = patientLocalListEntity.getReferringPhysician();
        String patientInstituteResidence = patientLocalListEntity.getPatientInstituteResidence();


        String accessionNumber = patientLocalListEntity.getAccessionNumber();
        String modality = "RT";
        String requestProcedurePriority = patientLocalListEntity.getRequestProcedurePriority();
        String additionalPatientHistory = patientLocalListEntity.getAdditionalPatientHistory();
        String admittingDiagnosisDescription = patientLocalListEntity.getAdmittingDiagnosisDescription();
        String studyDescription = patientLocalListEntity.getStudyDescription();
        String procedure = " ";
        String sex = patientLocalListEntity.getSex();

        LocalDateTime scheduledDateTime =  patientLocalListEntity.getScheduledDateTime();
        String patientComments = patientLocalListEntity.getPatientComments();

        LocalDateTime studyDateTime = LocalDateTime.now();
        LocalDateTime registerDateTime = LocalDateTime.now();

        String institution = SystemInfoController.getInstance().institutionNameLabel.getText();

        PatientInfo patientInfo = new PatientInfo(
                patientID,
                patientName,
                age,
                height,
                weight,
                birthDate,
                patientSize,
                sex,
                patientInstituteResidence
        );

        BodyPartHandler bodyPartHandler=new BodyPartHandler();

        String studyUid = " ";
        PatientStudy patientStudy = new PatientStudy(
                patientInfo,
                bodyPartHandler,
                studyID,
                accessionNumber,
                modality,
                requestProcedurePriority,
                additionalPatientHistory,
                admittingDiagnosisDescription,
                studyDescription,
                procedure,
                studyDateTime,
                registerDateTime,
                scheduledDateTime,
                patientComments,
                readingPhysician,
                performingPhysician,
                referringPhysician,
                studyUid,
                institution,
                null
                ,false
        );

        sessionId = IdGenerator.generateSessionId(3,patientStudy.getPatientInfo().getPatientName());
        PatientStudyNode patientStudyNode = new PatientStudyNode(patientStudy, sessionId);
        patientStudyHandler.addTab(sessionId, patientStudyNode);

        patientLocalListEntity.setStudySessionId(sessionId);
        try {
            PatientLocalListService.getInstance().update(patientLocalListEntity.getId(),patientLocalListEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (LocalListBodyPartsEntity bodyPartEntity : bodyPartsList){
            String projectionName = bodyPartEntity.getPosition();
            Image image = bodyPartProjectionImage(bodyPartEntity.getBodyPartName(),bodyPartEntity.getPosition());
            PatientBodyPart patientBodyPart=new PatientBodyPart(false,false,bodyPartEntity.getBodyPartName(),projectionName,
                    convertToMat(image),null,new XrayParams(),
                    null,null,null,"","","","",null,null,null,null,null,"");


            String imageSessionId = IdGenerator.generateSessionId(3,bodyPartEntity.getBodyPartName(),bodyPartEntity.getPosition());
            BodyPartNode patientBodyPartNode = new BodyPartNode(imageSessionId, patientBodyPart);

            patientStudyHandler.getCurrentStudy()
                    .getPatientStudy()
                    .getBodyPartHandler()
                    .addTab(imageSessionId, patientBodyPartNode);

        }

        return 0;
    }

    private void openSelectedStudy(PatientStudyEntity selectedPatient) {
        String baseDir = System.getProperty("user.dir");
        String patientId = selectedPatient.getPatientId();
        String directoryPath = String.format("%s/output/%s", baseDir, patientId);
        String fileName = selectedPatient.getStudyId() + ".rxv";
        File file = new File(directoryPath, fileName);


        if (file.exists()) {
            try {
                PatientStudy patientStudy = KryoSerializer.getInstance().deserializeFromFile(file);
                String sessionId = StudyService.patientStudyHandler.containsStudyWithStudyId(patientStudy.getStudyId());
                if(sessionId==null){
                    sessionId = IdGenerator.generateSessionId(3,patientStudy.getPatientInfo().getPatientName());
                    PatientStudyNode patientStudyNode = new PatientStudyNode(patientStudy, sessionId);
                    StudyService.patientStudyHandler.addTab(sessionId,patientStudyNode);
                    StudyService.patientStudyHandler.selectPatientStudy(sessionId);
                }else{
                    StudyService.patientStudyHandler.selectPatientStudy(sessionId);
                    System.out.println(StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getPatientInfo());
                }

                NavConnect.getInstance().navigateToStudy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("File not found: " + file.getAbsolutePath());
        }
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

    private Image bodyPartProjectionImage(String bodyPart, String position) {
        String projectionPath = Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "Styles", "Images", "Projection").toString();

        File bodyPartFolder = new File(projectionPath, bodyPart);


        if (!bodyPartFolder.exists() && !bodyPartFolder.mkdirs()) {
            System.out.println("Failed to create folder for body part: " + bodyPart);
            return null;
        }
        String imageName = bodyPart + " " + position + ".png";
        File imageFile = new File(bodyPartFolder, imageName);


        if (!imageFile.exists()) {
            System.out.println("Image not found: " + imageFile.getPath());
            return null;
        }

        return new Image(imageFile.toURI().toString());
    }

}
