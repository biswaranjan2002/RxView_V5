package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomMWLController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DicomMWL {


    public TableView SCPListTable;
    public TableColumn useColumn;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;
    
    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public TextField maxPduInput;
    public TextField verificationTimeoutInput;
    public TextField refreshCycleInput;

    public JFXCheckBox maxPduCheckBox;
    
    public TableView verificationTable;
    public TableColumn timeColumn;
    public TableColumn informationColumn;
    
    public TableView tagSelectionTable;
    public TableColumn tagNameColumn;
    public TableColumn tagColumn;
    public JFXComboBox codeMappingComboBox;

    public JFXButton newButton;
    public VBox tableOuterBox;

    private DicomMWLController dicomMWLController = DicomMWLController.getInstance();

    public void initialize(){
        dicomMWLController.scpListTable =SCPListTable;
        dicomMWLController.useColumn=useColumn;
        dicomMWLController.nameColumn=nameColumn;
        dicomMWLController.aeTitleColumn=aeTitleColumn;

        dicomMWLController.nameInput=nameInput;
        dicomMWLController.aeTitleInput=aeTitleInput;
        dicomMWLController.ipAddressInput=ipAddressInput;
        dicomMWLController.portInput=portInput;
        dicomMWLController.maxPduInput=maxPduInput;
        dicomMWLController.verificationTimeoutInput=verificationTimeoutInput;
        dicomMWLController.refreshCycleInput=refreshCycleInput;

        dicomMWLController.maxPduCheckBox=maxPduCheckBox;
        dicomMWLController.codeMappingComboBox=codeMappingComboBox;

        dicomMWLController.verificationTableView =verificationTable;
        dicomMWLController.timeColumn=timeColumn;
        dicomMWLController.informationColumn=informationColumn;

        dicomMWLController.tagSelectionTable=tagSelectionTable;
        dicomMWLController.tagNameColumn=tagNameColumn;
        dicomMWLController.tagColumn=tagColumn;

        dicomMWLController.newButton = newButton;
        dicomMWLController.tableOuterBox=tableOuterBox;

        dicomMWLController.loadEvents();
        dicomMWLController.loadData();
    }


    public void newScpClick() {
        dicomMWLController.newMode();
    }

    public void deleteScpClick() {
        dicomMWLController.deleteScpClick();
    }

    public void echoClick() {
        dicomMWLController.echoClick();
    }

    public void includeExpandedModuleComboBoxClick() {
        dicomMWLController.includeExpandedModuleComboBoxClick();
    }

    public void codeMappingComboBoxClick() {
        dicomMWLController.codeMappingComboBoxClick();
    }

    public void maxPduCheckBoxClick() {
        dicomMWLController.maxPduCheckBoxClick();
    }

    public void saveClick() {
        dicomMWLController.saveClick();
    }
}
