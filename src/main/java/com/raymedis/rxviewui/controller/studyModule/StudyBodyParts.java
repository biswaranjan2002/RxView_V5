package com.raymedis.rxviewui.controller.studyModule;


import com.raymedis.rxviewui.service.study.StudyBodyPartsController;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class StudyBodyParts{

    public GridPane studyBodyPartPositionContainer;
    public GridPane patientBodyContainer;

    public Button smallMan;
    public Button mediumMan;
    public Button largeMan;
    public Button extraLargeMan;

    public FontIcon detector1;
    public FontIcon detector2;
    public FontIcon detector3;
    public ScrollPane studyBodyPartsPositionScrollBar;

    private final StudyBodyPartsController studyBodyPartsController = StudyBodyPartsController.getInstance();



    public void initialize() throws IOException {
        //fetch the current patient gender display the body in patientBodyContainer;


        studyBodyPartsController.studyPatientBodyContainer = patientBodyContainer;
        studyBodyPartsController.studyBodyPositionContainer = studyBodyPartPositionContainer;
        studyBodyPartsController.smallMan = smallMan;
        studyBodyPartsController.mediumMan = mediumMan;
        studyBodyPartsController.largeMan = largeMan;
        studyBodyPartsController.extraLargeMan = extraLargeMan;

        studyBodyPartsController.detector1 = detector1;
        studyBodyPartsController.detector2 = detector2;
        studyBodyPartsController.detector3 = detector3;
        studyBodyPartsController.studyBodyPartsPositionScrollBar=studyBodyPartsPositionScrollBar;

        studyBodyPartsController.loadStudyBodyParts();

    }




    public void smallManBtn_Click() {
        studyBodyPartsController.smallManBtn_Click();
    }


    public void mediumMan_Click() {
        studyBodyPartsController.mediumMan_Click();
    }



    public void largeManBtn_Click() {
        studyBodyPartsController.largeManBtn_Click();
    }


    public void extraLargeBtn_Click() {
        studyBodyPartsController.extraLargeBtn_Click();
    }


    public void detector1Click() {
        studyBodyPartsController.detector1Click();
    }

    public void detector2Click() {
        studyBodyPartsController.detector2Click();
    }

    public void detector3Click() {
        studyBodyPartsController.detector3Click();
    }
}