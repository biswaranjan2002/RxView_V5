package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table.DicomMppsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table.DicomMppsService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomMppsController {

    private static DicomMppsController instance = new DicomMppsController();

    public static DicomMppsController getInstance(){
        return instance;
    }


    public TableView mppsTableView;
    public TableColumn<DicomMppsEntity,Boolean> useColumn;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;

    public JFXButton newButton;

    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public TextField maxPduInput;
    public JFXCheckBox maxPDUCheckBox;
    public TextField verificationTimeoutInput;
    public JFXComboBox languageComboBox;

    public TableView verificationTable;
    public TableColumn timeColumn;
    public TableColumn informationColumn;

    public VBox tableOuterBox;

    private DicomMppsService dicomMppsService = DicomMppsService.getInstance();
    private ArrayList<DicomMppsEntity> mppsList = new ArrayList<>();

    private int selectedItemId=0;

    public void loadEvents() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        aeTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aeTitle"));

        useColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));

        useColumn.setCellFactory(new Callback<TableColumn<DicomMppsEntity, Boolean>, TableCell<DicomMppsEntity, Boolean>>() {
            @Override
            public TableCell<DicomMppsEntity, Boolean> call(TableColumn<DicomMppsEntity, Boolean> param) {
                return new CheckBoxTableCell<DicomMppsEntity, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            DicomMppsEntity dicomMwlEntity = getTableRow().getItem();

                            if (dicomMwlEntity != null) {
                                // Set the initial state of the checkbox
                                checkBox.setSelected(dicomMwlEntity.getIsSelected());

                                // Add a new listener for checkbox changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Uncheck all other rows
                                        for (Object entity : mppsTableView.getItems()) {
                                            DicomMppsEntity entity1= (DicomMppsEntity) entity;
                                            if (!entity1.equals(dicomMwlEntity)) {
                                                entity1.setIsSelected(false);
                                            }
                                        }

                                        // Update the current entity
                                        dicomMwlEntity.setIsSelected(true);

                                        // Refresh the table view to update the UI
                                        mppsTableView.refresh();

                                        // Persist the change
                                        try {
                                            dicomMppsService.setAllItemsSelectionToFalse(dicomMwlEntity.getId());
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

        mppsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DicomMppsEntity>() {
            @Override
            public void changed(ObservableValue<? extends DicomMppsEntity> observable, DicomMppsEntity oldValue, DicomMppsEntity newValue) {
                if (newValue != null) {
                    editMode();
                }
            }
        });

        tableOuterBox.setOnMouseClicked(event -> {
            if (!mppsTableView.isHover()) {
                mppsTableView.getSelectionModel().clearSelection();
                newMode();
            }
        });
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

    private void newMode(){
        newButton.setText("New");

        nameInput.setText("");
        aeTitleInput.setText("");
        ipAddressInput.setText("");
        portInput.setText("");
        maxPDUCheckBox.setSelected(false);
        maxPduInput.setText("");
        verificationTimeoutInput.setText("");
        // set to default item of languageComboBox

        selectedItemId = 0;
    }

    private void editMode(){
        DicomMppsEntity dicomMwlEntity = (DicomMppsEntity) mppsTableView.getSelectionModel().getSelectedItem();
        selectedItemId = dicomMwlEntity.getId();

        newButton.setText("Edit");

        nameInput.setText(dicomMwlEntity.getName());
        aeTitleInput.setText(dicomMwlEntity.getAeTitle());
        ipAddressInput.setText(dicomMwlEntity.getIpAddress());
        portInput.setText(dicomMwlEntity.getPort() + "");

        int port = dicomMwlEntity.getMaxPdu();
        if(port==0){
            maxPDUCheckBox.setSelected(false);
            maxPduInput.setText("0");
        }else{
            maxPDUCheckBox.setSelected(true);
            maxPduInput.setText(port + "");
            maxPduInput.setDisable(false);
        }

        verificationTimeoutInput.setText(dicomMwlEntity.getVerificationTimeout()+"");
        String language = dicomMwlEntity.getLanguage();
        //use switch case for language comboBox
    }

    public void loadData(){
        mppsTableView.getItems().clear();
        try {
            mppsList = dicomMppsService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mppsTableView.getItems().addAll(mppsList);

        newMode();
    }

    public void newClick() {
        newMode();
    }

    public void deleteClick() {
        if(selectedItemId!=0){
            try {
                dicomMppsService.delete(selectedItemId);
                mppsTableView.getItems().clear();
                mppsList = dicomMppsService.findAll();
                mppsTableView.getItems().addAll(mppsList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            newMode();
        }
    }

    public void saveClick() {
        String name = nameInput.getText();
        String aeTitle = aeTitleInput.getText();
        String ipAddress = ipAddressInput.getText();
        int port = Integer.parseInt(portInput.getText());
        int maxPdu;
        if (maxPDUCheckBox.isSelected()) {
            maxPdu = Integer.parseInt(maxPduInput.getText());
        } else {
            maxPdu = 0;
        }
        int verificationTimeout = Integer.parseInt(verificationTimeoutInput.getText());
        String language = ""; //languageComboBox.getSelectionModel().getSelectedItem().toString();

        DicomMppsEntity dicomMppsEntity = new DicomMppsEntity();

        dicomMppsEntity.setName(name);
        dicomMppsEntity.setAeTitle(aeTitle);
        dicomMppsEntity.setIpAddress(ipAddress);
        dicomMppsEntity.setPort(port);
        dicomMppsEntity.setMaxPdu(maxPdu);
        dicomMppsEntity.setVerificationTimeout(verificationTimeout);
        dicomMppsEntity.setLanguage(language);

        try {
            if (selectedItemId == 0) {
                dicomMppsService.save(dicomMppsEntity);
            } else {
                DicomMppsEntity selectedItem = (DicomMppsEntity) mppsTableView.getSelectionModel().getSelectedItem();
                dicomMppsService.update(selectedItem.getId(), dicomMppsEntity);
            }
            mppsTableView.getItems().clear();
            mppsList = dicomMppsService.findAll();
            mppsTableView.getItems().addAll(mppsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void maxPduCheckBoxClick() {
        if(maxPDUCheckBox.isSelected()){
            maxPduInput.setDisable(false);
            maxPduInput.setText("0");
        }else{
            maxPduInput.setDisable(true);
        }
    }

    public void echoClick() {

    }



}


