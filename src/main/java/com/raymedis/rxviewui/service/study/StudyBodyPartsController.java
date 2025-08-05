package com.raymedis.rxviewui.service.study;

import com.raymedis.rxviewui.modules.study.patient.PatientInfo;
import com.raymedis.rxviewui.service.registration.InfantController;
import com.raymedis.rxviewui.service.registration.ManController;
import com.raymedis.rxviewui.service.registration.WomenController;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyBodyPartsController {

    private static final StudyBodyPartsController instance = new StudyBodyPartsController();

    public GridPane studyPatientBodyContainer;
    public GridPane studyBodyPositionContainer;

    public FontIcon detector1;
    public FontIcon detector2;
    public FontIcon detector3;
    public ScrollPane studyBodyPartsPositionScrollBar;


    public static StudyBodyPartsController getInstance(){
        return instance;
    }



    public Button smallMan;
    public Button mediumMan;
    public Button largeMan;
    public Button extraLargeMan;


    private String sex = "";

    private final Logger logger = LoggerFactory.getLogger(StudyBodyPartsController.class);

    public void loadStudyBodyParts(){
        if(StudyService.patientStudyHandler.getCurrentStudy()==null){
            studyPatientBodyContainer.getChildren().clear();
            studyBodyPositionContainer.getChildren().clear();
            return;
        }

        PatientInfo currentPatientInfo = StudyService.patientStudyHandler.getCurrentStudy().getPatientStudy().getPatientInfo();

        String patientSize = currentPatientInfo.getPatientSize();
         sex = currentPatientInfo.getSex();

         switch (patientSize){
             case "Infant":
                 smallManBtn_Click();
                 break;
             case "Small":
                 mediumMan_Click();
                 break;
             case "Medium":
                 largeManBtn_Click();
                 break;
             case "Big":
                 extraLargeBtn_Click();
                 break;
             default:
                 logger.info("Unknown Anatomy Size");
         }
    }


    public void resetSize() {
        resetButtonStyle(smallMan);
        resetButtonStyle(mediumMan);
        resetButtonStyle(largeMan);
        resetButtonStyle(extraLargeMan);
    }

    private void resetButtonStyle(Button button) {
        button.getStyleClass().removeAll("SelectedRadioButton", "unSelectedRadioButton");
        button.getStyleClass().add("unSelectedRadioButton");
    }


    public void smallManBtn_Click() {
        resetSize();

        smallMan.getStyleClass().remove("unSelectedRadioButton");
        smallMan.getStyleClass().add("SelectedRadioButton");

        studyPatientBodyContainer.getChildren().setAll(InfantController.getInstance().infantPageStudy);
        InfantController.getInstance().loadAllRadioButtons();
    }


    public void mediumMan_Click() {
        resetSize();

        mediumMan.getStyleClass().remove("unSelectedRadioButton");
        mediumMan.getStyleClass().add("SelectedRadioButton");

        if (sex.equals("male")) {
            studyPatientBodyContainer.getChildren().setAll(ManController.getInstance().manPageStudy);
            ManController.getInstance().loadAllRadioButtons();
        } else if (sex.equals("female")) {
            studyPatientBodyContainer.getChildren().setAll(WomenController.getInstance().womenPageStudy);
            WomenController.getInstance().loadAllRadioButtons();
        }else{
            //implement for non-binary
        }

    }



    public void largeManBtn_Click() {
        resetSize();

        largeMan.getStyleClass().remove("unSelectedRadioButton");
        largeMan.getStyleClass().add("SelectedRadioButton");

        if (sex.equals("male")) {
            studyPatientBodyContainer.getChildren().setAll(ManController.getInstance().manPageStudy);
            ManController.getInstance().loadAllRadioButtons();

        } else if (sex.equals("female")) {
            studyPatientBodyContainer.getChildren().setAll(WomenController.getInstance().womenPageStudy);
            WomenController.getInstance().loadAllRadioButtons();
        }else{
            //implement for non-binary
        }
    }


    public void extraLargeBtn_Click() {
        resetSize();

        extraLargeMan.getStyleClass().remove("unSelectedRadioButton");
        extraLargeMan.getStyleClass().add("SelectedRadioButton");

        if (sex.equals("male")) {
            studyPatientBodyContainer.getChildren().setAll(ManController.getInstance().manPageStudy);
            ManController.getInstance().loadAllRadioButtons();
        } else if (sex.equals("female")) {
            studyPatientBodyContainer.getChildren().setAll(WomenController.getInstance().womenPageStudy);
            WomenController.getInstance().loadAllRadioButtons();
        }else{
            //implement for non-binary
        }
    }

    public void resetDetectors(){
        detector1.getStyleClass().removeAll("selectedDetector", "unSelectedDetector");
        detector1.getStyleClass().add("unSelectedDetector");

        detector2.getStyleClass().removeAll("selectedDetector", "unSelectedDetector");
        detector2.getStyleClass().add("unSelectedRadioButton");

        detector3.getStyleClass().removeAll("selectedDetector", "unSelectedDetector");
        detector3.getStyleClass().add("unSelectedDetector");
    }

    public void detector1Click() {
        resetDetectors();

        detector1.getStyleClass().remove("unSelectedDetector");
        detector1.getStyleClass().add("selectedDetector");
        //System.out.println("detector1  is selected...");
    }

    public void detector2Click() {
        resetDetectors();

        detector2.getStyleClass().remove("unSelectedDetector");
        detector2.getStyleClass().add("selectedDetector");
        //System.out.println("detector2  is selected...");
    }

    public void detector3Click() {
        resetDetectors();

        detector3.getStyleClass().remove("unSelectedDetector");
        detector3.getStyleClass().add("selectedDetector");
        //System.out.println("detector3  is selected...");
    }


}
