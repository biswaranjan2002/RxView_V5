package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomStorageController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DicomStorage {

    public TableView storageTableView;
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
    public TextField timeoutInput;
    public TextField verificationTimeInput;
    
    public TableView verificationTableView;
    public TableColumn timeColumn;
    public TableColumn informationColumn;
    
    public JFXComboBox lutComboBox;
    public JFXComboBox modalityComboBox;
    public JFXComboBox dapUnitTypeComboBox;
    public JFXComboBox softwareCollimationComboBox;

    public JFXRadioButton burnedInAnnotationYes;
    public JFXRadioButton burnedInAnnotationNo;
    public JFXRadioButton burnedInInformationYes;
    public JFXRadioButton burnedInInformationNo;

    public JFXComboBox transferSyntaxComboBox;
    public JFXComboBox compressionComboBox;
    public VBox tableOuterBox;

    public JFXRadioButton burnedWithCropNo;
    public JFXRadioButton burnedWithCropYes;


    private DicomStorageController dicomStorageController = DicomStorageController.getInstance();

    public void initialize(){
        dicomStorageController.storageTableView=storageTableView;
        dicomStorageController.useColumn=useColumn;
        dicomStorageController.nameColumn=nameColumn;
        dicomStorageController.aeTitleColumn=aeTitleColumn;

        dicomStorageController.newButton=newButton;

        dicomStorageController.nameInput=nameInput;
        dicomStorageController.aeTitleInput=aeTitleInput;
        dicomStorageController.ipAddressInput=ipAddressInput;
        dicomStorageController.portInput=portInput;
        dicomStorageController.maxPduCheckBox=maxPduCheckBox;
        dicomStorageController.maxPduInput=maxPduInput;
        dicomStorageController.timeoutInput=timeoutInput;
        dicomStorageController.verificationTimeInput=verificationTimeInput;

        dicomStorageController.verificationTableView=verificationTableView;
        dicomStorageController.timeColumn=timeColumn;
        dicomStorageController.informationColumn=informationColumn;

        dicomStorageController.lutComboBox=lutComboBox;
        dicomStorageController.modalityComboBox=modalityComboBox;
        dicomStorageController.dapUnitTypeComboBox=dapUnitTypeComboBox;
        dicomStorageController.softwareCollimationComboBox=softwareCollimationComboBox;

        dicomStorageController.burnedInAnnotationYes = burnedInAnnotationYes;
        dicomStorageController.burnedInAnnotationNo = burnedInAnnotationNo;
        dicomStorageController.burnedInInformationNo = burnedInInformationNo;
        dicomStorageController.burnedInInformationYes =burnedInInformationYes;
        dicomStorageController.burnedWithCropYes = burnedWithCropYes;
        dicomStorageController.burnedWithCropNo = burnedWithCropNo;

        dicomStorageController.transferSyntaxComboBox=transferSyntaxComboBox;
        dicomStorageController.compressionComboBox=compressionComboBox;
        dicomStorageController.tableOuterBox=tableOuterBox;

        dicomStorageController.loadEvents();
        dicomStorageController.loadData();
    }



    public void newClick() {
        dicomStorageController.newClick();
    }

    public void deleteClick() {
        dicomStorageController.deleteClick();
    }

    public void saveClick() {
    dicomStorageController.saveClick();
    }

    public void maxPduChangeClick() {
        dicomStorageController.maxPduChangeClick();
    }

    public void echoClick() {
        dicomStorageController.echoClick();
    }
}
