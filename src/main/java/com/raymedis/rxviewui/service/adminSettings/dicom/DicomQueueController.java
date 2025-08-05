package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.printQueue.PrintQueueEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.printQueue.PrintQueueService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.storageQueue.StorageQueueEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.storageQueue.StorageQueueService;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomQueueController {

    private static DicomQueueController instance = new DicomQueueController();
    public static DicomQueueController getInstance(){
        return instance;
    }

    public TableView storageQueueTable;
    public TableColumn storageQueueStateColumn;
    public TableColumn storageQueuePatientNameColumn;
    public TableColumn storageQueuePatientIdColumn;

    public TableView printQueueTable;
    public TableColumn printQueuePatientIdColumn;
    public TableColumn printQueuePatientNameColumn;
    public TableColumn printQueueStateColumn;

    private PrintQueueService printQueueService = PrintQueueService.getInstance();
    private StorageQueueService storageQueueService = StorageQueueService.getInstance();

    private ArrayList<PrintQueueEntity> printQueueList = new ArrayList<>();
    private ArrayList<StorageQueueEntity> storageQueueList = new ArrayList<>();

    public void loadEvents(){
        storageQueuePatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        storageQueuePatientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        storageQueueStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        printQueuePatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        printQueuePatientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        printQueueStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
    }

    public void loadData(){
        try {
            printQueueTable.getItems().clear();
            storageQueueTable.getItems().clear();

            printQueueList = printQueueService.findAll();
            storageQueueList = storageQueueService.findAll();

            printQueueTable.getItems().addAll(printQueueList);
            storageQueueTable.getItems().addAll(storageQueueList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void storageQueueRetryClick() {

    }

    public void storageQueueDeleteClick() {
        StorageQueueEntity storageQueueEntity = (StorageQueueEntity) storageQueueTable.getSelectionModel().getSelectedItem();

        if(storageQueueEntity!=null){
            try {
                storageQueueService.delete(storageQueueEntity.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void printQueueRetryClick() {

    }

    public void printQueueDeleteClick() {
        PrintQueueEntity printQueueEntity = (PrintQueueEntity) printQueueTable.getSelectionModel().getSelectedItem();

        if(printQueueEntity!=null){
            try {
                printQueueService.delete(printQueueEntity.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
