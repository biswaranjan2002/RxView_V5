package com.raymedis.rxviewui.controller.adminSettings.dicom;

import com.raymedis.rxviewui.service.adminSettings.dicom.DicomQueueController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DicomQueue {

    public TableView storageQueueTable;
    public TableColumn storageQueueStateColumn;
    public TableColumn storageQueuePatientNameColumn;
    public TableColumn storageQueuePatientIdColumn;


    public TableView printQueueTable;
    public TableColumn printQueuePatientIdColumn;
    public TableColumn printQueuePatientNameColumn;
    public TableColumn printQueueStateColumn;

    private DicomQueueController dicomQueueController = DicomQueueController.getInstance();

    public void initialize(){
        dicomQueueController.storageQueueTable=storageQueueTable;
        dicomQueueController.storageQueueStateColumn=storageQueueStateColumn;
        dicomQueueController.storageQueuePatientNameColumn=storageQueuePatientNameColumn;
        dicomQueueController.storageQueuePatientIdColumn=storageQueuePatientIdColumn;
        dicomQueueController.printQueueTable=printQueueTable;
        dicomQueueController.printQueuePatientIdColumn=printQueuePatientIdColumn;
        dicomQueueController.printQueuePatientNameColumn=printQueuePatientNameColumn;
        dicomQueueController.printQueueStateColumn=printQueueStateColumn;

        dicomQueueController.loadEvents();
        dicomQueueController.loadData();
    }

    public void storageQueueRetryClick() {
        dicomQueueController.storageQueueRetryClick();
    }

    public void storageQueueDeleteClick() {
        dicomQueueController.storageQueueDeleteClick();
    }

    public void printQueueRetryClick() {
        dicomQueueController.printQueueRetryClick();
    }

    public void printQueueDeleteClick() {
        dicomQueueController.printQueueDeleteClick();
    }
}
