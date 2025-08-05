package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.dicom.DicomPrintController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DicomPrint {

    private final DicomPrintController dicomPrintController = DicomPrintController.getInstance();
    
    public TableView printTableView;
    public TableColumn useColumn;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;
    
    public JFXButton newButton;
    
    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public TextField maxPduInput;
    
    public JFXCheckBox maxPduCheckBox;
    public TextField timeoutInput;
    public TextField verificationTimeoutInput;
    public JFXCheckBox filmPixelPitchCheckBox;
    
    public TableView verificationTableView;
    public TableColumn timeColumn;
    public TableColumn informationColumn;

    public JFXComboBox sizeComboBox;
    public JFXComboBox orientationComboBox;
    public JFXComboBox magnificationTypeComboBox;
    public JFXComboBox mediumTypeComboBox;
    public JFXComboBox priorityComboBox;
    public JFXComboBox trimComboBox;
    public JFXComboBox minDensityComboBox;
    public JFXComboBox maxDensityComboBox;
    public JFXComboBox filmDestinationComboBox;
    public JFXComboBox requestResolutionComboBox;
    public JFXComboBox numOfCopyComboBox;

    public VBox outerTableBox;


    public void initialize(){

        dicomPrintController.printTableView=printTableView;
        dicomPrintController.useColumn=useColumn;
        dicomPrintController.nameColumn=nameColumn;
        dicomPrintController.aeTitleColumn=aeTitleColumn;

        dicomPrintController.newButton=newButton;

        dicomPrintController.nameInput=nameInput;
        dicomPrintController.aeTitleInput=aeTitleInput;
        dicomPrintController.ipAddressInput=ipAddressInput;
        dicomPrintController.portInput=portInput;
        dicomPrintController.maxPduInput=maxPduInput;

        dicomPrintController.maxPduCheckBox=maxPduCheckBox;
        dicomPrintController.timeoutInput=timeoutInput;
        dicomPrintController.verificationTimeoutInput=verificationTimeoutInput;
        dicomPrintController.filmPixelPitchCheckBox=filmPixelPitchCheckBox;

        dicomPrintController.verificationTableView=verificationTableView;
        dicomPrintController.timeColumn=timeColumn;
        dicomPrintController.informationColumn=informationColumn;

        dicomPrintController.sizeComboBox=sizeComboBox;
        dicomPrintController.orientationComboBox=orientationComboBox;
        dicomPrintController.magnificationTypeComboBox=magnificationTypeComboBox;
        dicomPrintController.mediumTypeComboBox=mediumTypeComboBox;
        dicomPrintController.priorityComboBox=priorityComboBox;
        dicomPrintController.trimComboBox=trimComboBox;
        dicomPrintController.minDensityComboBox=minDensityComboBox;
        dicomPrintController.maxDensityComboBox=maxDensityComboBox;
        dicomPrintController.filmDestinationComboBox=filmDestinationComboBox;
        dicomPrintController.requestResolutionComboBox=requestResolutionComboBox;
        dicomPrintController.numOfCopyComboBox=numOfCopyComboBox;

        dicomPrintController.outerTableBox=outerTableBox;

        dicomPrintController.loadEvents();
        dicomPrintController.loadData();

    }

    public void saveClick() {
        dicomPrintController.saveClick();
    }


    public void newClick() {
        dicomPrintController.newClick();
    }


    public void deleteClick() {
        dicomPrintController.deleteClick();
    }

    public void maxPduCheckBoxClick() {
        dicomPrintController.maxPduCheckBoxClick();
    }

    public void echoClick() {
        dicomPrintController.echoClick();
    }

    public void sizeCheckBoxClick() {
        dicomPrintController.sizeCheckBoxClick();
    }

    public void orientationCheckBoxClick() {
        dicomPrintController.orientationCheckBoxClick();
    }

    public void magnificationTypeCheckBoxClick() {
        dicomPrintController.magnificationTypeCheckBoxClick();
    }

    public void mediumTypeCheckBoxClick() {
        dicomPrintController.mediumTypeCheckBoxClick();
    }

    public void priorityCheckBoxClick() {
        dicomPrintController.priorityCheckBoxClick();
    }

    public void trimCheckBoxClick() {
        dicomPrintController.trimCheckBoxClick();
    }

    public void minDensityClick() {
        dicomPrintController.minDensityClick();
    }

    public void maxDensityClick() {
        dicomPrintController.maxDensityClick();
    }

    public void filmDestinationClick() {
        dicomPrintController.filmDestinationClick();
    }

    public void requestResolutionCheckBox() {
        dicomPrintController.requestResolutionCheckBox();
    }



}
