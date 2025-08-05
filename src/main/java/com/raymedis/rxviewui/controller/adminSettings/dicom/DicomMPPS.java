package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomMppsController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DicomMPPS {


    public TableView mppsTableView;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;

    public JFXButton newButton;

    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public TextField maxPduInput;
    public TextField verificationTimeoutInput;
    public JFXComboBox languageComboBox;

    public TableView verificationTable;
    public TableColumn timeColumn;
    public TableColumn informationColumn;
    public TableColumn useColumn;
    public VBox tableOuterBox;
    public JFXCheckBox maxPduCheckBox;

    private DicomMppsController dicomMppsController = DicomMppsController.getInstance();

    public void initialize(){

        dicomMppsController.mppsTableView=mppsTableView;
        dicomMppsController.useColumn=useColumn;
        dicomMppsController.nameColumn = nameColumn;
        dicomMppsController.aeTitleColumn = aeTitleColumn;

        dicomMppsController.newButton=newButton;

        dicomMppsController.nameInput = nameInput;
        dicomMppsController.aeTitleInput=aeTitleInput;
        dicomMppsController.ipAddressInput=ipAddressInput;
        dicomMppsController.portInput=portInput;
        dicomMppsController.maxPduInput=maxPduInput;
        dicomMppsController.maxPDUCheckBox=maxPduCheckBox;
        dicomMppsController.verificationTimeoutInput=verificationTimeoutInput;
        dicomMppsController.languageComboBox=languageComboBox;

        dicomMppsController.verificationTable=verificationTable;
        dicomMppsController.timeColumn=timeColumn;
        dicomMppsController.informationColumn=informationColumn;

        dicomMppsController.tableOuterBox = tableOuterBox;

        dicomMppsController.loadEvents();
        dicomMppsController.loadData();
    }

    public void newClick() {
        dicomMppsController.newClick();
    }

    public void deleteClick() {
        dicomMppsController.deleteClick();
    }

    public void saveClick() {
        dicomMppsController.saveClick();
    }

    public void maxPduCheckBoxClick(){
        dicomMppsController.maxPduCheckBoxClick();
    }

    public void echoClick() {
        dicomMppsController.echoClick();
    }
}
