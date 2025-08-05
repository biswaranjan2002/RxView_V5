package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.raymedis.rxviewui.service.adminSettings.dicom.DicomTagMappingController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DicomTagMapping {


    public TableView tagMappingTableView;
    public TableColumn internalTagColumn;
    public TableColumn inputTagColumn;
    public TableColumn outputTagColumn;

    public TextField internalTagNameInput;
    public TextField inputTagNameInput;
    public TextField outputTagNameInput;
    public TextField internalTagNoInput;
    public TextField inputTagNoInput;
    public TextField outputTagNoInput;


    private DicomTagMappingController dicomTagMappingController = DicomTagMappingController.getInstance();

    public void initialize(){

        dicomTagMappingController.tagMappingTableView=tagMappingTableView;
        dicomTagMappingController.internalTagColumn=internalTagColumn;
        dicomTagMappingController.inputTagColumn=inputTagColumn;
        dicomTagMappingController.outputTagColumn=outputTagColumn;
        dicomTagMappingController.internalTagNameInput=internalTagNameInput;
        dicomTagMappingController.inputTagNameInput=inputTagNameInput;
        dicomTagMappingController.outputTagNameInput=outputTagNameInput;
        dicomTagMappingController.internalTagNoInput=internalTagNoInput;
        dicomTagMappingController.inputTagNoInput=inputTagNoInput;
        dicomTagMappingController.outputTagNoInput=outputTagNoInput;

        dicomTagMappingController.loadEvents();
        dicomTagMappingController.loadData();

    }


    public void restoreClick() {
        dicomTagMappingController.restoreClick();
    }
}
