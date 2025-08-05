package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table.DicomPrintEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table.DicomPrintService;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomPrintController {

    private static DicomPrintController instance = new DicomPrintController();
    public static DicomPrintController getInstance(){
        return instance;
    }

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

    private final DicomPrintService dicomPrintService = DicomPrintService.getInstance();
    private ArrayList<DicomPrintEntity> printList =  new ArrayList<>();
    private int selectedItemId;

    public void loadEvents(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        aeTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aeTitle"));

        useColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
        useColumn.setCellFactory(new Callback<TableColumn<DicomPrintEntity, Boolean>, TableCell<DicomPrintEntity, Boolean>>() {
            @Override
            public TableCell<DicomPrintEntity, Boolean> call(TableColumn<DicomPrintEntity, Boolean> param) {
                return new CheckBoxTableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            DicomPrintEntity dicomPrintEntity = getTableRow().getItem();

                            if (dicomPrintEntity != null) {
                                // Set the initial state of the checkbox
                                checkBox.setSelected(dicomPrintEntity.getIsSelected());

                                // Add a new listener for checkbox changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Uncheck all other rows
                                        for (Object entity : printTableView.getItems()) {
                                            DicomPrintEntity entity1 = (DicomPrintEntity) entity;
                                            if (!entity1.equals(dicomPrintEntity)) {
                                                entity1.setIsSelected(false);
                                            }
                                        }

                                        // Update the current entity
                                        dicomPrintEntity.setIsSelected(true);

                                        // Refresh the table view to update the UI
                                        printTableView.refresh();

                                        // Persist the change
                                        try {
                                            dicomPrintService.setAllItemsSelectionToFalse(dicomPrintEntity.getId());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        dicomPrintEntity.setIsSelected(false);
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

        printTableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<DicomPrintEntity>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                editMode();
            }
        });

        outerTableBox.setOnMouseClicked(event -> {
            if (!printTableView.isHover()) {
                printTableView.getSelectionModel().clearSelection();
                newClick();
            }
        });
    }

    public void loadData(){
        printTableView.getItems().clear();
        printTableView.getItems().clear();

        try {
            printList = dicomPrintService.findAll();
            printTableView.getItems().addAll(printList);

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


    private void editMode(){
        DicomPrintEntity dicomPrintEntity = (DicomPrintEntity) printTableView.getSelectionModel().getSelectedItem();

        if(dicomPrintEntity == null){
            return;
        }

        selectedItemId = dicomPrintEntity.getId();
        newButton.setText("Edit");

        nameInput.setText(dicomPrintEntity.getName());
        aeTitleInput.setText(dicomPrintEntity.getAeTitle());
        ipAddressInput.setText(dicomPrintEntity.getIpAddress());
        portInput.setText(dicomPrintEntity.getPort() + "");

        int port = dicomPrintEntity.getMaxPdu();
        if(port==0){
            maxPduCheckBox.setSelected(false);
            maxPduInput.setText("0");
        }else{
            maxPduCheckBox.setSelected(true);
            maxPduInput.setDisable(false);
            maxPduInput.setText(port + "");
        }
        timeoutInput.setText(dicomPrintEntity.getTimeout()+"");
        verificationTimeoutInput.setText(dicomPrintEntity.getVerificationTimeout()+"");

    }

    public void saveClick() {
        String name = nameInput.getText();
        String aeTitle = aeTitleInput.getText();
        String ipAddress = ipAddressInput.getText();
        int port = Integer.parseInt(portInput.getText());


        if (!maxPduCheckBox.isSelected()) {
            maxPduInput.setText("0");
        }
        int maxPdu = Integer.parseInt(maxPduInput.getText());

        if (verificationTimeoutInput.getText() == null || verificationTimeoutInput.getText().trim().isEmpty()) {
            verificationTimeoutInput.setText("0");
        }
        int verificationTimeout = Integer.parseInt(verificationTimeoutInput.getText());

        if(timeoutInput.getText() == null || timeoutInput.getText().trim().isEmpty()) {
            timeoutInput.setText("0");
        }
        int timeOut = Integer.parseInt(timeoutInput.getText());


        DicomPrintEntity dicomPrintEntity = new DicomPrintEntity();

        dicomPrintEntity.setName(name);
        dicomPrintEntity.setAeTitle(aeTitle);
        dicomPrintEntity.setIpAddress(ipAddress);
        dicomPrintEntity.setPort(port);
        dicomPrintEntity.setMaxPdu(maxPdu);
        dicomPrintEntity.setVerificationTimeout(verificationTimeout);
        dicomPrintEntity.setTimeout(timeOut);
        dicomPrintEntity.setPrintLayout("");


        try {
            if (selectedItemId == 0) {
                dicomPrintEntity.setIsSelected(false);
                dicomPrintService.save(dicomPrintEntity);
            } else {
                dicomPrintService.update(selectedItemId, dicomPrintEntity);
            }


            printTableView.getItems().clear();
            printList = dicomPrintService.findAll();
            printTableView.getItems().addAll(printList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        timeoutInput.setText("");


        selectedItemId = 0;

    }


    public void deleteClick() {
        if(selectedItemId!=0){
            try {
                dicomPrintService.delete(selectedItemId);
                printTableView.getItems().clear();
                printList = dicomPrintService.findAll();
                printTableView.getItems().addAll(printList);
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

    public void sizeCheckBoxClick() {

    }

    public void orientationCheckBoxClick() {

    }

    public void magnificationTypeCheckBoxClick() {

    }

    public void mediumTypeCheckBoxClick() {

    }

    public void priorityCheckBoxClick() {

    }

    public void trimCheckBoxClick() {

    }

    public void minDensityClick() {

    }

    public void maxDensityClick() {

    }

    public void filmDestinationClick() {

    }

    public void requestResolutionCheckBox() {

    }




}
