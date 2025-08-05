package com.raymedis.rxviewui.controller.adminSettings.registration.physician;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.registration.RegistrationPhysicianController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class RegistrationPhysician {

    public JFXButton addButton;
    public TextField physicianName;
    public TextField physicianId;

    public JFXComboBox physicianGroupComboBox;

    public TableView physicianDataGrid;
    public TableColumn physicianNameColumn;
    public TableColumn physicianGroupColumn;
    public VBox physicianOuterBox;

    private RegistrationPhysicianController registrationPhysicianController;

    public void initialize() throws SQLException {
        registrationPhysicianController = RegistrationPhysicianController.getInstance();


        registrationPhysicianController.addButton=addButton;
        registrationPhysicianController.physicianNameLabel =physicianName;
        registrationPhysicianController.physicianGroupComboBox=physicianGroupComboBox;
        registrationPhysicianController.physicianIdLabel =physicianId;
        registrationPhysicianController.physicianDataGrid=physicianDataGrid;
        registrationPhysicianController.physicianNameColumn = physicianNameColumn;
        registrationPhysicianController.physicianGroupColumn = physicianGroupColumn;
        registrationPhysicianController.physicianOuterBox = physicianOuterBox;

        registrationPhysicianController.loadEvents();
        registrationPhysicianController.loadData();
    }


    public void addDataClick() {
        registrationPhysicianController.addDataClick();
    }


    public void deleteClick() {
        registrationPhysicianController.deleteClick();
    }
}
