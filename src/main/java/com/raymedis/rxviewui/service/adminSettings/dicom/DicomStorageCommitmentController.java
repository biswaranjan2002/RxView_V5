package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table.DicomStorageCommitmentEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table.DicomStorageCommitmentService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomStorageCommitmentController {

    private static DicomStorageCommitmentController instance = new DicomStorageCommitmentController();
    public static DicomStorageCommitmentController getInstance(){
        return instance;
    }


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
    private int selectedItemId = 0;

    private final DicomStorageCommitmentService dicomStorageCommitmentService = DicomStorageCommitmentService.getInstance();
    private ArrayList<DicomStorageCommitmentEntity> storageCommitmentList = new ArrayList<>();

    public void loadEvents(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        aeTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aeTitle"));

        useColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
        useColumn.setCellFactory(new Callback<TableColumn<DicomStorageCommitmentEntity, Boolean>, TableCell<DicomStorageCommitmentEntity, Boolean>>() {
            @Override
            public TableCell<DicomStorageCommitmentEntity, Boolean> call(TableColumn<DicomStorageCommitmentEntity, Boolean> param) {
                return new CheckBoxTableCell<DicomStorageCommitmentEntity, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            DicomStorageCommitmentEntity dicomMwlEntity = getTableRow().getItem();

                            if (dicomMwlEntity != null) {
                                // Set the initial state of the checkbox
                                checkBox.setSelected(dicomMwlEntity.getIsSelected());

                                // Add a new listener for checkbox changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Uncheck all other rows
                                        for (Object entity : StorageCommitmentTableView.getItems()) {
                                            DicomStorageCommitmentEntity entity1= (DicomStorageCommitmentEntity) entity;
                                            if (!entity1.equals(dicomMwlEntity)) {
                                                entity1.setIsSelected(false);
                                            }
                                        }

                                        // Update the current entity
                                        dicomMwlEntity.setIsSelected(true);

                                        // Refresh the table view to update the UI
                                        StorageCommitmentTableView.refresh();

                                        // Persist the change
                                        try {
                                            dicomStorageCommitmentService.setAllItemsSelectionToFalse(dicomMwlEntity.getId());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        dicomMwlEntity.setIsSelected(false);
                                    }
                                });

                                // Set the checkbox graphic
                                setGraphic(checkBox);
                            } else {
                                setGraphic(null);
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        numericValidation(portInput);

        StorageCommitmentTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DicomStorageCommitmentEntity>() {
            @Override
            public void changed(ObservableValue<? extends DicomStorageCommitmentEntity> observable, DicomStorageCommitmentEntity oldValue, DicomStorageCommitmentEntity newValue) {
                if (newValue != null) {
                    editMode();
                }
            }
        });

        tableOuterBox.setOnMouseClicked(event -> {
            if (!StorageCommitmentTableView.isHover()) {
                StorageCommitmentTableView.getSelectionModel().clearSelection();
                newClick();
            }
        });
    }

    public void loadData(){
        StorageCommitmentTableView.getItems().clear();
        StorageCommitmentTableView.getItems().clear();

        try {
            storageCommitmentList = dicomStorageCommitmentService.findAll();
            StorageCommitmentTableView.getItems().addAll(storageCommitmentList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        maxPduCheckBoxClick();
        newClick();
    }

    private void numericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 5) {
                textField.setText(oldValue);
            }
        });
    }

    public void newClick() {
        newButton.setText("New");

        nameInput.setText("");
        aeTitleInput.setText("");
        ipAddressInput.setText("");
        portInput.setText("");
        maxPduCheckBox.setSelected(false);
        maxPduInput.setText("");
        verificationTimeoutInput.setText("");

        selectedItemId = 0;
    }

    private void editMode(){
        DicomStorageCommitmentEntity dicomStorageCommitmentEntity = (DicomStorageCommitmentEntity) StorageCommitmentTableView.getSelectionModel().getSelectedItem();
        selectedItemId = dicomStorageCommitmentEntity.getId();

        newButton.setText("Edit");

        nameInput.setText(dicomStorageCommitmentEntity.getName());
        aeTitleInput.setText(dicomStorageCommitmentEntity.getAeTitle());
        ipAddressInput.setText(dicomStorageCommitmentEntity.getIpAddress());
        portInput.setText(dicomStorageCommitmentEntity.getPort() + "");

        int port = dicomStorageCommitmentEntity.getMaxPdu();
        if(port==0){
            maxPduCheckBox.setSelected(false);
            maxPduInput.setText("0");
        }else{
            maxPduCheckBox.setSelected(true);
            maxPduInput.setText(port + "");
            maxPduInput.setDisable(false);
        }

        verificationTimeoutInput.setText(dicomStorageCommitmentEntity.getVerificationTimeout()+"");
    }

    public void saveClick() {
        String name = nameInput.getText();
        String aeTitle = aeTitleInput.getText();
        String ipAddress = ipAddressInput.getText();
        int port = Integer.parseInt(portInput.getText());
        int maxPdu;
        if (maxPduCheckBox.isSelected()) {
            maxPdu = Integer.parseInt(maxPduInput.getText());
        } else {
            maxPdu = 0;
        }
        int verificationTimeout = Integer.parseInt(verificationTimeoutInput.getText());

        DicomStorageCommitmentEntity dicomStorageCommitmentEntity = new DicomStorageCommitmentEntity();

        dicomStorageCommitmentEntity.setName(name);
        dicomStorageCommitmentEntity.setAeTitle(aeTitle);
        dicomStorageCommitmentEntity.setIpAddress(ipAddress);
        dicomStorageCommitmentEntity.setPort(port);
        dicomStorageCommitmentEntity.setMaxPdu(maxPdu);
        dicomStorageCommitmentEntity.setVerificationTimeout(verificationTimeout);


        try {
            if (selectedItemId == 0) {
                dicomStorageCommitmentEntity.setIsSelected(false);
                dicomStorageCommitmentService.save(dicomStorageCommitmentEntity);
            } else {
                dicomStorageCommitmentService.update(selectedItemId, dicomStorageCommitmentEntity);
            }


            StorageCommitmentTableView.getItems().clear();
            storageCommitmentList = dicomStorageCommitmentService.findAll();
            StorageCommitmentTableView.getItems().addAll(storageCommitmentList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteClick() {
        if(selectedItemId!=0){
            try {
                dicomStorageCommitmentService.delete(selectedItemId);
                StorageCommitmentTableView.getItems().clear();
                storageCommitmentList = dicomStorageCommitmentService.findAll();
                StorageCommitmentTableView.getItems().addAll(storageCommitmentList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            newClick();
        }
    }

    public void maxPduCheckBoxClick() {
        if(maxPduCheckBox.isSelected()){
            maxPduInput.setDisable(false);
            maxPduInput.setText("0");
        }else{
            maxPduInput.setDisable(true);
        }
    }

    public void echoClick() {

    }


}
