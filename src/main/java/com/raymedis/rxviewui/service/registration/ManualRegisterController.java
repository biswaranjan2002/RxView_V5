package com.raymedis.rxviewui.service.registration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.controller.NavConnect;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureService;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table.StepService;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.CandidateTagsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table.RegistrationManualService;
import com.raymedis.rxviewui.database.tables.register.localListBodyParts_table.LocalListBodyPartsEntity;
import com.raymedis.rxviewui.database.tables.register.localListBodyParts_table.LocalListBodyPartsService;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListEntity;
import com.raymedis.rxviewui.database.tables.register.patientLocalList_table.PatientLocalListService;
import com.raymedis.rxviewui.modules.study.bodypart.BodyPartNode;
import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.modules.study.study.PatientStudy;
import com.raymedis.rxviewui.modules.study.tabmanagement.TabNode;
import com.raymedis.rxviewui.service.database.DatabaseController;
import com.raymedis.rxviewui.service.study.StudyMainController;
import com.raymedis.rxviewui.service.study.StudyService;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ManualRegisterController {
    
    private static final ManualRegisterController instance = new ManualRegisterController();

    public static ManualRegisterController getInstance() {
        return instance;
    }

    public VBox bodyPositionContainer;
    public GridPane patientBodyContainer;

    public VBox selectedPositionsContainer;

    public TextField patientId;
    public TextField patientName;
    public TextField age;
    public TextField patientFirstName;
    public TextField patientMiddleName;
    public TextField patientLastName;
    public TextField weight;
    public TextField height;
    public TextField accessionNumber;
    public TextField studyDescription;
    public TextField referringPhysician;
    public TextField performingPhysician;
    public TextField readingPhysician;
    public TextField patientInstituteResidence;
    public TextField additionalPatientHistory;
    public TextField admittingDiagnosisDescription;
    public TextField patientComments;
    public TextField requestProcedurePriority;
    public TextField institution;

    public DatePicker scheduledDateTime;
    public DatePicker birthDate;
    public JFXComboBox<String> procedureComboBox;

    public Button maleButton;
    public Button femaleButton;
    public Button nonBinaryButton;

    public Button smallMan;
    public Button mediumMan;
    public Button largeMan;
    public Button extraLargeMan;

    public Parent infantPage;
    public Parent manPage;
    public Parent womanPage;

    public HBox procedureSelection;

    private final RegistrationManualService registrationManualService = RegistrationManualService.getInstance();
    private final ManualRegisterService manualRegisterService = ManualRegisterService.getInstance();
    private final NavConnect navConnect = NavConnect.getInstance();
    private final PatientLocalListService patientLocalListService = PatientLocalListService.getInstance();
    private final LocalListBodyPartsService localListBodyPartsService = LocalListBodyPartsService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ManualRegisterController.class);
    private final BodyPartSelectionController bodyPartSelectionController = BodyPartSelectionController.getInstance();
    public Map<String, ProcedureEntity> procedureMap = new HashMap<>();

    public void registerLoad(){
        maleBtn_Click();
        largeManBtn_Click();
        loadFields();
        loadProcedureData();
    }

    private final ProcedureService procedureService = ProcedureService.getInstance();
    private void loadProcedureData() {
        procedureComboBox.getItems().clear();
        procedureMap.clear();

        try {
            ArrayList<ProcedureEntity> procedureList = procedureService.findAll();
            procedureComboBox.getItems().add("None");
            procedureComboBox.getSelectionModel().selectFirst();

            for (ProcedureEntity procedure : procedureList) {
                String procedureCode = procedure.getProcedureCode();
                procedureComboBox.getItems().add(procedureCode);
                procedureMap.put(procedureCode, procedure);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading procedure data", e);
        }
    }

    public void loadAllEvents(){
        numericValidation(age);
        numericValidation(accessionNumber);
        numericValidation(height);
        numericValidation(weight);
        numericValidation(requestProcedurePriority);

        idValidation(patientId);

        alphabetsValidation(patientName);
        alphabetsValidation(patientFirstName);
        alphabetsValidation(patientMiddleName);
        alphabetsValidation(patientLastName);
        alphabetsValidation(readingPhysician);
        alphabetsValidation(referringPhysician);
        alphabetsValidation(performingPhysician);
        alphabetsValidation(patientInstituteResidence);
        alphabetsValidation(studyDescription);
        alphabetsValidation(additionalPatientHistory);
        alphabetsValidation(patientComments);
        alphabetsValidation(institution);
        alphabetsValidation(admittingDiagnosisDescription);
    }

    public void disAbleAllFields(){
        patientId.setVisible(false);
        patientName.setVisible(false);
        age.setVisible(false);
        patientFirstName.setVisible(false);
        patientMiddleName.setVisible(false);
        patientLastName.setVisible(false);
        weight.setVisible(false);
        height.setVisible(false);
        accessionNumber.setVisible(false);
        studyDescription.setVisible(false);
        referringPhysician.setVisible(false);
        performingPhysician.setVisible(false);
        readingPhysician.setVisible(false);
        patientInstituteResidence.setVisible(false);
        additionalPatientHistory.setVisible(false);
        admittingDiagnosisDescription.setVisible(false);
        patientComments.setVisible(false);
        requestProcedurePriority.setVisible(false);
        institution.setVisible(false);
        procedureSelection.setVisible(false);

        patientId.setManaged(false);
        patientName.setManaged(false);
        age.setManaged(false);
        patientFirstName.setManaged(false);
        patientMiddleName.setManaged(false);
        patientLastName.setManaged(false);
        weight.setManaged(false);
        height.setManaged(false);
        accessionNumber.setManaged(false);
        studyDescription.setManaged(false);
        referringPhysician.setManaged(false);
        performingPhysician.setManaged(false);
        readingPhysician.setManaged(false);
        patientInstituteResidence.setManaged(false);
        additionalPatientHistory.setManaged(false);
        admittingDiagnosisDescription.setManaged(false);
        patientComments.setManaged(false);
        requestProcedurePriority.setManaged(false);
        institution.setManaged(false);
        procedureSelection.setManaged(false);


        scheduledDateTime.setVisible(false);
        birthDate.setVisible(false);

        scheduledDateTime.setManaged(false);
        birthDate.setManaged(false);
    }

    public void loadFields(){
        disAbleAllFields();
        ArrayList<CandidateTagsEntity> selectedTextFields = registrationManualService.getAllSelectedCandidateTags();

        for (CandidateTagsEntity selectedField :selectedTextFields){
            String field = selectedField.getTagName().toLowerCase();
            switch (field) {
                case "patient id":
                    patientId.setVisible(true);
                    patientId.setManaged(true);
                    break;
                case "patient name":
                    patientName.setVisible(true);
                    patientName.setManaged(true);
                    break;
                case "age":
                    age.setVisible(true);
                    age.setManaged(true);
                    break;
                case "first name":
                    patientFirstName.setVisible(true);
                    patientFirstName.setManaged(true);
                    break;
                case "middle name":
                    patientMiddleName.setVisible(true);
                    patientMiddleName.setManaged(true);
                    break;
                case "last name":
                    patientLastName.setVisible(true);
                    patientLastName.setManaged(true);
                    break;
                case "weight":
                    weight.setVisible(true);
                    weight.setManaged(true);
                    break;
                case "height":
                    height.setVisible(true);
                    height.setManaged(true);
                    break;
                case "accession number":
                    accessionNumber.setVisible(true);
                    accessionNumber.setManaged(true);
                    break;
                case "study description":
                    studyDescription.setVisible(true);
                    studyDescription.setManaged(true);
                    break;
                case "referring physician":
                    referringPhysician.setVisible(true);
                    referringPhysician.setManaged(true);
                    break;
                case "performing physician":
                    performingPhysician.setVisible(true);
                    performingPhysician.setManaged(true);
                    break;
                case "reading physician":
                    readingPhysician.setVisible(true);
                    readingPhysician.setManaged(true);
                    break;
                case "patient institute residence":
                    patientInstituteResidence.setVisible(true);
                    patientInstituteResidence.setManaged(true);
                    break;
                case "additional patient history":
                    additionalPatientHistory.setVisible(true);
                    additionalPatientHistory.setManaged(true);
                    break;
                case "admitting diagnosis description":
                    admittingDiagnosisDescription.setVisible(true);
                    admittingDiagnosisDescription.setManaged(true);
                    break;
                case "patient comments":
                    patientComments.setVisible(true);
                    patientComments.setManaged(true);
                    break;
                case "request procedure priority":
                    requestProcedurePriority.setVisible(true);
                    requestProcedurePriority.setManaged(true);
                    break;
                case "institution":
                    institution.setVisible(true);
                    institution.setManaged(true);
                    break;
                case "procedure":
                    procedureSelection.setVisible(true);
                    procedureSelection.setManaged(true);
                    break;
                case "scheduled date time":
                    scheduledDateTime.setVisible(true);
                    scheduledDateTime.setManaged(true);
                    break;
                case "birth date":
                    birthDate.setVisible(true);
                    birthDate.setManaged(true);
                    break;
                case "patient institue residence":
                    patientInstituteResidence.setVisible(true);
                    patientInstituteResidence.setManaged(true);
                    break;
                default:
                     //Optional: handle unrecognized fields
                    System.out.println("Field not recognized: " + field);
                    break;
            }
        }
    }

    private void numericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 15) {
                textField.setText(oldValue);
            }
        });
    }

    private void alphabetsValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {  //Only allows letters and spaces
                textField.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
            } else if (newValue.length() > 50) {
                textField.setText(oldValue);
            }
        });
    }

    private void idValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z0-9]", ""));
            } else if (newValue.length() > 26) {
                textField.setText(oldValue);
            }
        });
    }

    public Boolean isMale = false;
    public Boolean isFemale = false;
    public Boolean isNonBinary = false;

    public Boolean isSmall = false;
    public Boolean isMedium = false;
    public Boolean isLarge = false;
    public Boolean isExtraLarge = false;

    public static PatientStudy editingPatientStudy;
    public static PatientInfo editingPatientInfo;

    public void resetGender(){
        isMale = false;
        isFemale = false;
        isNonBinary = false;

        resetButtonStyle(maleButton);
        resetButtonStyle(femaleButton);
        resetButtonStyle(nonBinaryButton);
    }

    public void resetSize() {
        isSmall = false;
        isMedium = false;
        isLarge = false;
        isExtraLarge = false;

        resetButtonStyle(smallMan);
        resetButtonStyle(mediumMan);
        resetButtonStyle(largeMan);
        resetButtonStyle(extraLargeMan);
    }

    private void resetButtonStyle(Button button) {
        button.getStyleClass().removeAll("SelectedRadioButton", "unSelectedRadioButton");
        button.getStyleClass().add("unSelectedRadioButton");
    }

    public void maleBtn_Click() {
        resetGender();
        isMale = true;

        maleButton.getStyleClass().remove("unSelectedRadioButton");
        maleButton.getStyleClass().add("SelectedRadioButton");

        if (isSmall) {
            patientBodyContainer.getChildren().setAll(infantPage);
            InfantController.getInstance().loadAllRadioButtons();
        }  else {
            patientBodyContainer.getChildren().setAll(manPage);
            ManController.getInstance().loadAllRadioButtons();
        }
    }

    public void femaleBtn_Click() {
        resetGender();
        isFemale = true;

        femaleButton.getStyleClass().remove("unSelectedRadioButton");
        femaleButton.getStyleClass().add("SelectedRadioButton");

        if (isSmall) {
            patientBodyContainer.getChildren().setAll(infantPage);
            InfantController.getInstance().loadAllRadioButtons();
        } else {
            patientBodyContainer.getChildren().setAll(womanPage);
            WomenController.getInstance().loadAllRadioButtons();
        }
    }

    public void otherBtn_Click() {
        resetGender();
        isNonBinary = true;

        nonBinaryButton.getStyleClass().remove("unSelectedRadioButton");
        nonBinaryButton.getStyleClass().add("SelectedRadioButton");

        if (isSmall) {
            patientBodyContainer.getChildren().setAll(infantPage);
            InfantController.getInstance().loadAllRadioButtons();
        } else if (isFemale) {
            patientBodyContainer.getChildren().setAll(womanPage);
            WomenController.getInstance().loadAllRadioButtons();
        } else if (isMale) {
            patientBodyContainer.getChildren().setAll(manPage);
            ManController.getInstance().loadAllRadioButtons();
        }
    }

    public void smallManBtn_Click() {
        resetSize();
        isSmall = true;

        smallMan.getStyleClass().remove("unSelectedRadioButton");
        smallMan.getStyleClass().add("SelectedRadioButton");

        patientBodyContainer.getChildren().setAll(infantPage);
        InfantController.getInstance().loadAllRadioButtons();
    }

    public void mediumMan_Click() {
        resetSize();
        isMedium = true;

        mediumMan.getStyleClass().remove("unSelectedRadioButton");
        mediumMan.getStyleClass().add("SelectedRadioButton");

        if (isMale) {
            patientBodyContainer.getChildren().setAll(manPage);
            ManController.getInstance().loadAllRadioButtons();
        } else if (isFemale) {
            patientBodyContainer.getChildren().setAll(womanPage);
            WomenController.getInstance().loadAllRadioButtons();
        }
    }

    public void largeManBtn_Click() {
        resetSize();
        isLarge = true;

        largeMan.getStyleClass().remove("unSelectedRadioButton");
        largeMan.getStyleClass().add("SelectedRadioButton");

        if (isMale) {
            patientBodyContainer.getChildren().setAll(manPage);
            ManController.getInstance().loadAllRadioButtons();
        } else if (isFemale) {
            patientBodyContainer.getChildren().setAll(womanPage);
            WomenController.getInstance().loadAllRadioButtons();
        }
    }

    public void extraLargeBtn_Click() {
        resetSize();
        isExtraLarge = true;

        extraLargeMan.getStyleClass().remove("unSelectedRadioButton");
        extraLargeMan.getStyleClass().add("SelectedRadioButton");

        if (isMale) {
            patientBodyContainer.getChildren().setAll(manPage);
            ManController.getInstance().loadAllRadioButtons();
        } else if (isFemale) {
            patientBodyContainer.getChildren().setAll(womanPage);
            WomenController.getInstance().loadAllRadioButtons();
        }
    }

    public void deleteSelectedBodyPosition() {
        for (Map.Entry<JFXButton,JFXButton> entry: bodyPartSelectionController.selectedBodyPartPositionMapForDelete.entrySet()){
            selectedPositionsContainer.getChildren().remove(entry.getValue());
            if(entry.getKey()!=null){
                entry.getKey().setDisable(false);
                bodyPartSelectionController.projectionsButonMap.remove(entry.getKey());
            }else{
                bodyPartSelectionController.projectionsButonMap.remove(entry.getValue());
            }
            bodyPositionContainer.getChildren().clear();
            ManController.getInstance().getSelectedBodyPartProjections(entry.getValue().getText().split(" ")[0],"register");
        }
        bodyPartSelectionController.selectedBodyPartPositionMapForDelete.clear();
    }

    public void studyNext() {

        if(StudyMainController.isEdit ||DatabaseController.isEdit){
            editTrigger();
            navConnect.navigateToStudy();
            resetManualRegister();
            StudyMainController.toggleEditButton(false);
            return;
        }


        int result = manualRegisterService.registerTrigger();

        switch (result){
            case 0:
                navConnect.navigateToStudy();
                resetManualRegister();
                break;
            case -2:
                showMaxStudiesAlert();
                break;
            case -1:
                showPatientDetailsAlert();
                patientName.requestFocus();
                patientId.requestFocus();
                break;
            default:
                System.out.println("Something Wrong Study Trigger");
        }

    }

    private static void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content, ButtonType.OK);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public void showPatientDetailsAlert() {
        showAlert(
                "Missing Patient Details",
                "Please fill in the required patient details before proceeding.");
    }

    public static void showMaxStudiesAlert() {
        showAlert(
                "Maximum Studies Opened",
                "The maximum number of studies are opened. Please save and close the existing studies before proceeding.");
    }

    public void saveStudyToLocalList() {

        if(patientName.getText().isEmpty() || patientName.getText()==null ||
                patientId.getText().isEmpty() || patientId.getText()==null ||
                bodyPartSelectionController.projectionsButonMap.isEmpty()){
            return;
        }

        PatientLocalListEntity patientLocalListEntity = new PatientLocalListEntity();

        patientLocalListEntity.setAccessionNumber(accessionNumber.getText());
        patientLocalListEntity.setAdditionalPatientHistory(additionalPatientHistory.getText());
        patientLocalListEntity.setAdmittingDiagnosisDescription(admittingDiagnosisDescription.getText());
        patientLocalListEntity.setDob(birthDate.getValue());
        patientLocalListEntity.setInstitution(institution.getText());
        patientLocalListEntity.setModality("RT");
        patientLocalListEntity.setPatientComments(patientComments.getText());
        patientLocalListEntity.setPatientId(patientId.getText());
        patientLocalListEntity.setPatientInstituteResidence(patientInstituteResidence.getText());
        patientLocalListEntity.setPatientName(patientName.getText());
        patientLocalListEntity.setPatientSize(getPatientSize());
        patientLocalListEntity.setPerformingPhysician(performingPhysician.getText());
        patientLocalListEntity.setReadingPhysician(readingPhysician.getText());
        patientLocalListEntity.setRegisterDateTime(LocalDateTime.now());
        patientLocalListEntity.setReferringPhysician(referringPhysician.getText());
        patientLocalListEntity.setRequestProcedurePriority(requestProcedurePriority.getText());
        patientLocalListEntity.setScheduledDateTime(LocalDateTime.now());
        patientLocalListEntity.setSex(getGender());
        patientLocalListEntity.setStudyDescription(studyDescription.getText());

        patientLocalListEntity.setAge(parseIntegerSafely(age.getText()));
        patientLocalListEntity.setHeight(parseIntegerSafely(height.getText()));
        patientLocalListEntity.setWeight(parseIntegerSafely(weight.getText()));
        patientLocalListEntity.setStudyId(null);


        try {
            patientLocalListService.save(patientLocalListEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int localListId;
        try {
            List<PatientLocalListEntity> worklist = patientLocalListService.findAll();

            if (!worklist.isEmpty()) {
                localListId = worklist.getLast().getId();
            } else {
               localListId = 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<JFXButton,String> entry:bodyPartSelectionController.projectionsButonMap.entrySet()){
            LocalListBodyPartsEntity localListBodyPartsEntity = new LocalListBodyPartsEntity();
            String bodyPartName = entry.getValue().split(" ")[0];
            String position = entry.getValue().split(" ")[1];
            localListBodyPartsEntity.setBodyPartName(bodyPartName);
            localListBodyPartsEntity.setPosition(position);
            localListBodyPartsEntity.setLocalListId(localListId);

            try {
                localListBodyPartsService.save(localListBodyPartsEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        resetManualRegister();


    }

    private String getPatientSize(){
        if(isSmall){
            return "small";
        }
        else if (isMedium) {
            return "medium";
        }
        else if (isLarge)
        {
            return "large";
        }
        else if (isExtraLarge){
            return "extraLarge";
        }
        else{
            return "unknown Size";
        }
    }

    private String getGender(){
        if(isMale){
            return "male";
        }
        else if(isFemale){
            return "female";
        }else if(isNonBinary){
            return "non binary";
        }else{
            return "unknown Gender";
        }
    }

    private int parseIntegerSafely(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void emergencyClick() {

        if(StudyMainController.isEdit || DatabaseController.isEdit){
            editTrigger();
            navConnect.navigateToStudy();
            resetManualRegister();
            StudyMainController.toggleEditButton(false);
            return;
        }


        emergencyBodyPartsLoad();
        int result = manualRegisterService.emergencyTrigger();

        switch (result){
            case 0:
                navConnect.navigateToStudy();
                resetManualRegister();
                break;
            case -2:
                showMaxStudiesAlert();
                break;
            case -1:
                navConnect.navigateToStudy();
                resetManualRegister();
                System.out.println();
                break;
            default:
                System.out.println("Something Wrong Study Trigger");
        }

    }

    private void emergencyBodyPartsLoad() {
        ArrayList<StepEntity> emergencyBodyPartList = StepService.getInstance().findByProcedureId(1);
        for (StepEntity step : emergencyBodyPartList) {
            addStepToRegister(step);
        }
    }

    public void resetManualRegister(){
        resetGender();
        resetSize();
        resetTextFields();
        resetDatePickers();
        resetSelectedPositionsContainer();
        resetBodyPositionContainer();

        registerLoad();

        if (StudyMainController.isEdit || DatabaseController.isEdit) {
            loadRegisterForEdit(null);
        }
    }

    public void resetTextFields(){
        patientId.setText("");
        patientName.setText("");
        age.setText("");
        patientFirstName.setText("");
        patientMiddleName.setText("");
        patientLastName.setText("");
        weight.setText("");
        height.setText("");
        accessionNumber.setText("");
        studyDescription.setText("");
        referringPhysician.setText("");
        performingPhysician.setText("");
        readingPhysician.setText("");
        patientInstituteResidence.setText("");
        additionalPatientHistory.setText("");
        admittingDiagnosisDescription.setText("");
        patientComments.setText("");
        requestProcedurePriority.setText("");
        institution.setText("");
    }

    public void resetDatePickers() {
        scheduledDateTime.setValue(null);
        birthDate.setValue(null);
    }

    public void resetSelectedPositionsContainer(){
        selectedPositionsContainer.getChildren().clear();
    }

    public void resetBodyPositionContainer(){
        for (Map.Entry<JFXButton, String> entry: bodyPartSelectionController.projectionsButonMap.entrySet()){
            selectedPositionsContainer.getChildren().remove(entry.getKey());
            entry.getKey().setDisable(false);
        }

        bodyPartSelectionController.projectionsButonMap.clear();
        bodyPartSelectionController.selectedBodyPartPositionMapForDelete.clear();
        bodyPositionContainer.getChildren().clear();
    }

    public void procedureSelectionClick() {
        if (!procedureComboBox.getSelectionModel().isSelected(0)) {
            selectedPositionsContainer.getChildren().clear();
            String selectedProcedureCode = procedureComboBox.getSelectionModel().getSelectedItem();
            ProcedureEntity selectedProcedureEntity = procedureMap.get(selectedProcedureCode);
            if (selectedProcedureEntity != null) {
                ArrayList<StepEntity> selectedProcedureStepList = StepService.getInstance().findByProcedureId(selectedProcedureEntity.getProcedureId());
                for (StepEntity step : selectedProcedureStepList) {
                    addStepToRegister(step);
                }
            }
        } else {
            selectedPositionsContainer.getChildren().clear();
        }


    }

    public void addStepToRegister(StepEntity step) {
        String projectionPath = Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "Styles", "Images", "Projection").toString();
        File bodyPartFolder = new File(projectionPath, step.getStepName().split(" ")[0]);
        File[] matchingFiles = findMatchingFiles(bodyPartFolder, step.getStepName());
        File imageFile = (matchingFiles != null && matchingFiles.length > 0) ? matchingFiles[0] : new File(projectionPath + "/blankBodyPart.png");
        JFXButton selectedButton = createSelectedProjectionButton(step.getStepName(), imageFile, null);

        bodyPartSelectionController.projectionsButonMap.put(selectedButton,selectedButton.getText());

        selectedPositionsContainer.getChildren().add(selectedButton);
    }

    private File[] findMatchingFiles(File folder, String stepName) {
        return folder.listFiles((dir, name) ->
                name.startsWith(stepName) && name.endsWith(".png") ||
                        name.replaceAll("\\s", "").toLowerCase().startsWith(stepName.replaceAll("\\s", "").toLowerCase())
                                && name.toLowerCase().endsWith(".png"));
    }

    private JFXButton createSelectedProjectionButton(String stepName, File imageFile, JFXButton projectionButton) {
        JFXButton button = new JFXButton(stepName);
        button.setPrefSize(215.0, 215.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(185.0);
        imageView.setFitWidth(185.0);

        if (imageFile != null) {
            imageView.setImage(new Image(imageFile.toURI().toString()));
        }

        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setAlignment(Pos.TOP_CENTER);
        button.setId("unSelectedBodyPartButton");

        button.setOnAction(event -> toggleSelectedButtonState(button, projectionButton));
        return button;
    }

    private void toggleSelectedButtonState(JFXButton button, JFXButton projectionButton) {
        if ("unSelectedBodyPartButton".equals(button.getId())) {
            button.setId("selectedBodyPartButton");
            bodyPartSelectionController.selectedBodyPartPositionMapForDelete.put(projectionButton, button);
        } else {
            button.setId("unSelectedBodyPartButton");
            bodyPartSelectionController.selectedBodyPartPositionMapForDelete.remove(button);
        }

    }


    public void loadRegisterForEdit(PatientStudy patientStudy){

        if(patientStudy==null){
            return;
        }

        if(StudyMainController.isEdit || DatabaseController.isEdit) {
            editingPatientStudy = patientStudy;
            editingPatientInfo = editingPatientStudy.getPatientInfo();

            logger.info("Editing PatientId: {}", editingPatientInfo.getPatientId());

            patientId.setText(editingPatientInfo.getPatientId());
            patientName.setText(editingPatientInfo.getPatientName());
            age.setText(String.valueOf(editingPatientInfo.getAge()));
            accessionNumber.setText(editingPatientStudy.getAccessionNum());
            studyDescription.setText(editingPatientStudy.getStudyDescription());
            performingPhysician.setText(editingPatientStudy.getPerformingPhysician());
            String procedureCode = editingPatientStudy.getProcedureCode();
            selectMatchingProcedure(procedureComboBox,procedureCode);
            additionalPatientHistory.setText(editingPatientStudy.getAdditionalPatientHistory());
            admittingDiagnosisDescription.setText(editingPatientStudy.getAdmittingDiagnosisDescription());
            birthDate.setValue(LocalDate.from(editingPatientInfo.getBirthDate()));
            height.setText(String.valueOf(editingPatientInfo.getHeight()));
            institution.setText(editingPatientStudy.getInstitution());
            patientComments.setText(editingPatientStudy.getPatientComments());
            patientInstituteResidence.setText(editingPatientInfo.getPatientInstituteResidence());
            readingPhysician.setText(editingPatientStudy.getReadingPhysician());
            referringPhysician.setText(editingPatientStudy.getReferringPhysician());
            requestProcedurePriority.setText(editingPatientStudy.getRequestProcedurePriority());
            scheduledDateTime.setValue(LocalDate.from(editingPatientStudy.getScheduledDateTime()));
            weight.setText(String.valueOf(editingPatientInfo.getWeight()));


            String sex = editingPatientInfo.getSex();

            switch (sex){
                case "male":
                    maleBtn_Click();
                    break;
                case "female":
                    femaleBtn_Click();
                    break;
                case "non binary":
                    otherBtn_Click();
                    break;
                default:
                    logger.info("unknown gender");
            }

            String patientSize = editingPatientInfo.getPatientSize().toLowerCase();

            switch (patientSize){
                case "small":
                    smallManBtn_Click();
                    break;
                case "medium":
                    mediumMan_Click();
                    break;
                case "large":
                    largeManBtn_Click();
                    break;
                case "extralarge":
                    extraLargeBtn_Click();
                    break;
                default:
                    logger.info("unknown patient size {}",patientSize);
            }

            bodyPositionContainer.setDisable(true);
            patientBodyContainer.setDisable(true);
            selectedPositionsContainer.setDisable(true);

            bodyPartSelectionController.projectionsButonMap.clear();
            selectedPositionsContainer.getChildren().clear();

            List<TabNode> allTabs = editingPatientStudy.getBodyPartHandler().getAllTabs();

            for (TabNode allTab : allTabs) {
                BodyPartNode tab = (BodyPartNode) allTab;

                String stepName = tab.getBodyPart().getBodyPartName() + " " + tab.getBodyPart().getBodyPartPosition();

                //logger.info("stepName : {}",stepName);

                String projectionPath = Paths.get(System.getProperty("user.dir"),
                        "src", "main", "resources", "Styles", "Images", "Projection").toString();

                File bodyPartFolder = new File(projectionPath, tab.getBodyPart().getBodyPartName());

                // Ensure the folder exists
                if (!bodyPartFolder.exists() && !bodyPartFolder.mkdirs()) {
                    System.out.println("Failed to create folder for body part: " + tab.getBodyPart().getBodyPartName());
                    return;
                }
                File[] matchingFiles = findMatchingFiles(bodyPartFolder, stepName);


                if (matchingFiles == null || matchingFiles.length == 0) {
                    File imageFile = new File(projectionPath+"/blankBodyPart.png");
                    selectedPositionsContainer.getChildren().add(ManController.getInstance().createSelectedProjectionButton(stepName, imageFile,null));
                } else {
                    for (File imageFile : matchingFiles) {
                        selectedPositionsContainer.getChildren().add(ManController.getInstance().createSelectedProjectionButton(stepName, imageFile,null));
                    }
                }
            }
        }

    }

    public void selectMatchingProcedure(JFXComboBox<String> procedureComboBox, String procedureCode) {
        ObservableList<String> options = procedureComboBox.getItems();

        if (options.contains(procedureCode)) {
            procedureComboBox.getSelectionModel().select(procedureCode);
        } else {
            procedureComboBox.getSelectionModel().clearSelection();
        }
    }

    public void editTrigger() {

        editingPatientInfo.setPatientId(patientId.getText());
        editingPatientInfo.setPatientName(patientName.getText());
        editingPatientInfo.setAge(Integer.parseInt(age.getText()));
        editingPatientStudy.setAccessionNum(accessionNumber.getText());
        editingPatientStudy.setStudyDescription(studyDescription.getText());
        editingPatientStudy.setPerformingPhysician(performingPhysician.getText());
        editingPatientStudy.setProcedureCode(procedureComboBox.getValue());
        editingPatientStudy.setAdditionalPatientHistory(additionalPatientHistory.getText());
        editingPatientStudy.setAdmittingDiagnosisDescription(admittingDiagnosisDescription.getText());
        editingPatientInfo.setBirthDate(birthDate.getValue().atStartOfDay());
        editingPatientInfo.setHeight(Double.parseDouble(height.getText()));
        editingPatientStudy.setInstitution(institution.getText());
        editingPatientStudy.setPatientComments(patientComments.getText());
        editingPatientInfo.setPatientInstituteResidence(patientInstituteResidence.getText());
        editingPatientStudy.setReadingPhysician(readingPhysician.getText());
        editingPatientStudy.setReferringPhysician(referringPhysician.getText());
        editingPatientStudy.setRequestProcedurePriority(requestProcedurePriority.getText());
        editingPatientStudy.setScheduledDateTime(scheduledDateTime.getValue().atStartOfDay());
        editingPatientInfo.setWeight(Double.parseDouble(weight.getText()));
        editingPatientInfo.setSex(ManualRegisterService.getInstance().getGender());
        editingPatientInfo.setPatientSize(ManualRegisterService.getInstance().getPatientSize());


    }


}
