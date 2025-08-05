package com.raymedis.rxviewui.controller.adminSettings.registration.general;

import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationGeneralController;
import javafx.event.ActionEvent;

import java.sql.SQLException;

public class RegistrationGeneral {

    public JFXRadioButton manualRadioButton;
    public JFXRadioButton workListRadioButton;
    public JFXRadioButton localListRadioButton;




    private RegistrationGeneralController registrationGeneralController ;

    public void initialize() throws SQLException {
        registrationGeneralController = RegistrationGeneralController.getInstance();

        registrationGeneralController.manualRadioButton = manualRadioButton;
        registrationGeneralController.workListRadioButton = workListRadioButton;
        registrationGeneralController.localListRadioButton = localListRadioButton;

        registrationGeneralController.loadData();


    }



    public void defaultTabSaveClick(ActionEvent actionEvent) throws SQLException {
        registrationGeneralController.defaultTabSaveClick(actionEvent);
    }
}
