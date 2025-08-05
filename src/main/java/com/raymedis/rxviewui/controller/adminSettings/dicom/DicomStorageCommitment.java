package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomStorageCommitmentController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DicomStorageCommitment {

    public TableView StorageCommitmentTableView;
    public TableColumn useColumn;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;

    public JFXButton newButton;

    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public JFXCheckBox maxPduCheckBox;
    public TextField maxPduInput;
    public TextField verificationTimeoutInput;

    public TableView verificationTableView;
    public TableColumn timeColumn;
    public TableColumn informationColumn;
    public VBox tableOuterBox;


    private DicomStorageCommitmentController dicomStorageCommitmentController = DicomStorageCommitmentController.getInstance();

    public void initialize(){

        dicomStorageCommitmentController.StorageCommitmentTableView=StorageCommitmentTableView;
        dicomStorageCommitmentController.useColumn=useColumn;
        dicomStorageCommitmentController.nameColumn=nameColumn;
        dicomStorageCommitmentController.aeTitleColumn=aeTitleColumn;

        dicomStorageCommitmentController.newButton=newButton;

        dicomStorageCommitmentController.nameInput=nameInput;
        dicomStorageCommitmentController.aeTitleInput=aeTitleInput;
        dicomStorageCommitmentController.ipAddressInput=ipAddressInput;
        dicomStorageCommitmentController.portInput=portInput;
        dicomStorageCommitmentController.maxPduCheckBox=maxPduCheckBox;
        dicomStorageCommitmentController.maxPduInput=maxPduInput;
        dicomStorageCommitmentController.verificationTimeoutInput=verificationTimeoutInput;

        dicomStorageCommitmentController.verificationTableView=verificationTableView;
        dicomStorageCommitmentController.timeColumn=timeColumn;
        dicomStorageCommitmentController.informationColumn=informationColumn;
        dicomStorageCommitmentController.tableOuterBox=tableOuterBox;


        dicomStorageCommitmentController.loadEvents();
        dicomStorageCommitmentController.loadData();

    }

    public void saveClick() {
        dicomStorageCommitmentController.saveClick();
    }

    public void newClick() {
        dicomStorageCommitmentController.newClick();
    }

    public void deleteClick() {
        dicomStorageCommitmentController.deleteClick();
    }

    public void maxPduCheckBoxClick() {
        dicomStorageCommitmentController.maxPduCheckBoxClick();
    }

    public void echoClick() {
        dicomStorageCommitmentController.echoClick();
    }
}
