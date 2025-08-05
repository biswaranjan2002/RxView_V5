package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table.DicomTagMappingEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table.DicomTagMappingService;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomTagMappingController {

    private static DicomTagMappingController instance = new DicomTagMappingController();

    public static DicomTagMappingController getInstance(){
        return instance;
    }

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

    private final DicomTagMappingService dicomTagMappingService = DicomTagMappingService.getInstance();
    private ArrayList<DicomTagMappingEntity> tagMappinglist = new ArrayList<>();

    public void loadEvents(){
        internalTagColumn.setCellValueFactory(new PropertyValueFactory<>("originalTagName"));
        inputTagColumn.setCellValueFactory(new PropertyValueFactory<>("inputTagNo"));
        outputTagColumn.setCellValueFactory(new PropertyValueFactory<>("outputTagNo"));
    }

    public void loadData(){
        tagMappingTableView.getItems().clear();

        try {
            tagMappinglist = dicomTagMappingService.findAll();
            tagMappingTableView.getItems().addAll(tagMappinglist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void restoreClick() {

    }


}
