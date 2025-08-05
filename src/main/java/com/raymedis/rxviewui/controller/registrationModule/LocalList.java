package com.raymedis.rxviewui.controller.registrationModule;

import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.registration.LocalListController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LocalList {
    
    private final LocalListController localListController = LocalListController.getInstance();

    public JFXComboBox searchByComboBox;
    public TextField detailsText;
    public DatePicker fromDate;
    public DatePicker toDate;

    public TableView localListTableView;
    public TableColumn patientIdColumn;
    public TableColumn patientNameColumn;
    public TableColumn dobColumn;
    public TableColumn sexColumn;
    public TableColumn accessionNumberColumn;
    public TableColumn referringPhysician;
    public TableColumn studyDescriptionColumn;
    public TableColumn scheduleDateTimeColumn;
    public TableColumn registerDateColumn;
    public TableColumn registerTimeColumn;
    public TableColumn weightColumn;
    public TableColumn physicianNameColumn;
    public TableColumn modalityColumn;

    public void initialize(){

        localListController.searchByComboBox=searchByComboBox;
        localListController.detailsText=detailsText;
        localListController.fromDate=fromDate;
        localListController.toDate=toDate;
        localListController.localListTableView=localListTableView;
        localListController.patientIdColumn=patientIdColumn;
        localListController.patientNameColumn=patientNameColumn;
        localListController.dobColumn=dobColumn;
        localListController.sexColumn=sexColumn;
        localListController.accessionNumberColumn=accessionNumberColumn;
        localListController.referringPhysician=referringPhysician;
        localListController.studyDescriptionColumn=studyDescriptionColumn;
        localListController.scheduleDateTimeColumn = scheduleDateTimeColumn;
        localListController.registerDateColumn=registerDateColumn;
        localListController.registerTimeColumn=registerTimeColumn;
        localListController.weightColumn=weightColumn;
        localListController.physicianNameColumn=physicianNameColumn;
        localListController.modalityColumn=modalityColumn;
        
        localListController.loadEvents();
        localListController.loadData();
    }
    
    
    public void selectionChanged() {
        localListController.selectionChanged();
    }

    public void todayBtnClick() {
        localListController.todayBtnClick();
    }

    public void weekBtnClick() {
        localListController.weekBtnClick();
    }

    public void monthBtnClick() {
        localListController.monthBtnClick();
    }

    public void searchClick() {
        localListController.searchClick();
    }

    public void startButtonClick() {
        localListController.startButtonClick();
    }

    public void clearClick() {
        localListController.clearClick();
    }
    
}
