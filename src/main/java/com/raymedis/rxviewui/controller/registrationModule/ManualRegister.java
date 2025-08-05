package com.raymedis.rxviewui.controller.registrationModule;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.registration.InfantController;
import com.raymedis.rxviewui.service.registration.ManController;
import com.raymedis.rxviewui.service.registration.ManualRegisterController;
import com.raymedis.rxviewui.service.registration.WomenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class ManualRegister {

    public Button maleButton;
    public Button femaleButton;
    public Button nonBinaryButton;


    public Button smallMan;
    public Button mediumMan;
    public Button largeMan;
    public Button extraLargeMan;

    public VBox bodyPartPositionContainer;
    public GridPane patientBodyContainer;
    
    
    public TextField patientId;
    public TextField patientName;
    public TextField age;
    public TextField patientFirstName;
    public TextField patientMiddleName;
    public TextField patientLastName;
    public DatePicker birthDate;
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
    public DatePicker scheduledDateTime;
    public TextField institution;
    public VBox selectedPositionsContainer;
    public JFXComboBox<String> procedureComboBox;
    public HBox procedureSelection;


    Parent infantPage;
    Parent manPage;
    Parent womanPage;

    Parent infantPageStudy;
    Parent manPageStudy;
    Parent womenPageStudy;


    private final ManualRegisterController manualRegisterController = ManualRegisterController.getInstance();

    public void initialize() throws IOException {

        //Loading Anatomy
        FXMLLoader infant = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/patientSize/Infant.fxml"));
        infantPage = infant.load();

        FXMLLoader man = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/patientSize/Man.fxml"));
        manPage = man.load();

        FXMLLoader women = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/patientSize/Women.fxml"));
        womanPage = women.load();

        FXMLLoader infantStudy = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/study/patientSize/InfantStudy.fxml"));
        infantPageStudy = infantStudy.load();

        FXMLLoader manStudy = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/study/patientSize/ManStudy.fxml"));
        manPageStudy = manStudy.load();

        FXMLLoader womenStudy = new FXMLLoader(getClass().getResource("/com/raymedis/rxviewui/controllerUi/study/patientSize/WomenStudy.fxml"));
        womenPageStudy = womenStudy.load();

        manualRegisterController.bodyPositionContainer = bodyPartPositionContainer;
        manualRegisterController.selectedPositionsContainer =selectedPositionsContainer;

        manualRegisterController.patientId = patientId;
        manualRegisterController.patientName = patientName ;
        manualRegisterController.age = age;
        manualRegisterController.patientFirstName = patientFirstName;
        manualRegisterController.patientMiddleName = patientMiddleName;
        manualRegisterController.patientLastName = patientLastName;
        manualRegisterController.birthDate=birthDate;
        manualRegisterController.weight = weight;
        manualRegisterController.height = height;
        manualRegisterController.accessionNumber = accessionNumber;
        manualRegisterController.studyDescription = studyDescription;
        manualRegisterController.referringPhysician = referringPhysician;
        manualRegisterController.performingPhysician = performingPhysician;
        manualRegisterController.readingPhysician = readingPhysician;
        manualRegisterController.patientInstituteResidence = patientInstituteResidence;
        manualRegisterController.additionalPatientHistory = additionalPatientHistory;
        manualRegisterController.admittingDiagnosisDescription = admittingDiagnosisDescription;
        manualRegisterController.patientComments = patientComments ;
        manualRegisterController.requestProcedurePriority = requestProcedurePriority;
        manualRegisterController.scheduledDateTime=scheduledDateTime;
        manualRegisterController.institution = institution;


        manualRegisterController.maleButton=maleButton;
        manualRegisterController.femaleButton=femaleButton;
        manualRegisterController.nonBinaryButton=nonBinaryButton;

        manualRegisterController.smallMan=smallMan;
        manualRegisterController.mediumMan=mediumMan;
        manualRegisterController.largeMan=largeMan;
        manualRegisterController.extraLargeMan=extraLargeMan;

        manualRegisterController.patientBodyContainer=patientBodyContainer;
        manualRegisterController.infantPage=infantPage;
        manualRegisterController.manPage=manPage;
        manualRegisterController.womanPage=womanPage;
        manualRegisterController.procedureComboBox=procedureComboBox;
        manualRegisterController.procedureSelection=procedureSelection;

        manualRegisterController.loadAllEvents();
        manualRegisterController.registerLoad();

        InfantController.getInstance().infantPageStudy = infantPageStudy;
        ManController.getInstance().manPageStudy = manPageStudy;
        WomenController.getInstance().womenPageStudy = womenPageStudy;
    }


    public void maleBtn_Click() {
        manualRegisterController.maleBtn_Click();

    }


    public void femaleBtn_Click() {
        manualRegisterController.femaleBtn_Click();
    }


    public void otherBtn_Click() {
       manualRegisterController.otherBtn_Click();
    }





    public void smallManBtn_Click() {
        manualRegisterController.smallManBtn_Click();
    }


    public void mediumMan_Click() {
        manualRegisterController.mediumMan_Click();
    }



    public void largeManBtn_Click() {
       manualRegisterController.largeManBtn_Click();
    }


    public void extraLargeBtn_Click() {
        manualRegisterController.extraLargeBtn_Click();
    }




    public void studyNextClick() {
        manualRegisterController.studyNext();
    }

    public void saveToWorkListClick() {
        manualRegisterController.saveStudyToLocalList();
    }


    public void deleteSelectedBodyPosition() {
        manualRegisterController.deleteSelectedBodyPosition();
    }

    public void procedureSelectionClick() {
        manualRegisterController.procedureSelectionClick();
    }
}
