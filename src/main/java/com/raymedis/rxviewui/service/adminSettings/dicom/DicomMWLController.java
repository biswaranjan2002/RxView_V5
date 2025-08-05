package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table.DicomTagMappingEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table.DicomTagMappingService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table.DicomMwlEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table.DicomMwlService;
import com.raymedis.rxviewui.modules.dicom.DicomMain;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomMWLController {

    private static final DicomMWLController instance = new DicomMWLController();


    public static DicomMWLController getInstance(){
        return instance;
    }

    public TableView scpListTable;
    public TableColumn<DicomMwlEntity, Boolean> useColumn;
    public TableColumn nameColumn;
    public TableColumn aeTitleColumn;

    public TextField nameInput;
    public TextField aeTitleInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public TextField maxPduInput;
    public TextField verificationTimeoutInput;
    public TextField refreshCycleInput;

    public JFXCheckBox maxPduCheckBox;
    public JFXComboBox codeMappingComboBox;

    @FXML public TableView<VerificationResult> verificationTableView;
    @FXML public TableColumn<VerificationResult, String> timeColumn;
    @FXML public TableColumn<VerificationResult, String> informationColumn;

    public TableView tagSelectionTable;
    public TableColumn tagNameColumn;
    public TableColumn tagColumn;

    public JFXButton newButton;
    public VBox tableOuterBox;

    private final DicomMwlService dicomMwlService = DicomMwlService.getInstance();
    private final DicomTagMappingService dicomTagMappingService = DicomTagMappingService.getInstance();

    private ArrayList<DicomMwlEntity> dicomMwlList = new ArrayList<>();
    private ArrayList<DicomTagMappingEntity> dicomTagMappingList = new ArrayList<>();


    private int selectedItemId = 0;

    public void loadEvents() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        aeTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aeTitle"));
        tagNameColumn.setCellValueFactory(new PropertyValueFactory<>("originalTagName"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("inputTagName"));


        useColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));

        useColumn.setCellFactory(new Callback<TableColumn<DicomMwlEntity, Boolean>, TableCell<DicomMwlEntity, Boolean>>() {
            @Override
            public TableCell<DicomMwlEntity, Boolean> call(TableColumn<DicomMwlEntity, Boolean> param) {
                return new CheckBoxTableCell<DicomMwlEntity, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            DicomMwlEntity dicomMwlEntity = getTableRow().getItem();

                            if (dicomMwlEntity != null) {
                                // Set the initial state of the checkbox
                                checkBox.setSelected(dicomMwlEntity.getIsSelected());

                                // Add a new listener for checkbox changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Uncheck all other rows
                                        for (Object entity : scpListTable.getItems()) {
                                            DicomMwlEntity entity1= (DicomMwlEntity) entity;
                                            if (!entity1.equals(dicomMwlEntity)) {
                                                entity1.setIsSelected(false);
                                            }
                                        }

                                        // Update the current entity
                                        dicomMwlEntity.setIsSelected(true);

                                        // Refresh the table view to update the UI
                                        scpListTable.refresh();

                                        // Persist the change
                                        try {
                                            dicomMwlService.setAllItemsSelectionToFalse(dicomMwlEntity.getId());
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

        scpListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DicomMwlEntity>() {
            @Override
            public void changed(ObservableValue<? extends DicomMwlEntity> observable, DicomMwlEntity oldValue, DicomMwlEntity newValue) {
                if (newValue != null) {
                    editMode();
                }
            }
        });

        tableOuterBox.setOnMouseClicked(event -> {
            if (!scpListTable.isHover()) {
                scpListTable.getSelectionModel().clearSelection();
                newMode();
            }
        });

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        informationColumn.setCellValueFactory(new PropertyValueFactory<>("information"));
    }

    public void newMode(){
        newButton.setText("New");

        nameInput.setText("");
        aeTitleInput.setText("");
        ipAddressInput.setText("");
        portInput.setText("");
        maxPduCheckBox.setSelected(false);
        maxPduInput.setText("");
        verificationTimeoutInput.setText("");
        refreshCycleInput.setText("");
        // set to default item of codeMappingComboBox

        selectedItemId = 0;
    }

    private void editMode(){
        DicomMwlEntity dicomMwlEntity = (DicomMwlEntity) scpListTable.getSelectionModel().getSelectedItem();
        selectedItemId = dicomMwlEntity.getId();

        newButton.setText("Edit");

        nameInput.setText(dicomMwlEntity.getName());
        aeTitleInput.setText(dicomMwlEntity.getAeTitle());
        ipAddressInput.setText(dicomMwlEntity.getIpAddress());
        portInput.setText(dicomMwlEntity.getPort() + "");

        int port = dicomMwlEntity.getMaxPdu();
        if(port==0){
            maxPduCheckBox.setSelected(false);
            maxPduInput.setText("0");
        }else{
            maxPduCheckBox.setSelected(true);
            maxPduInput.setText(port + "");
            maxPduInput.setDisable(false);
        }

        verificationTimeoutInput.setText(dicomMwlEntity.getVerificationTimeout()+"");
        refreshCycleInput.setText(dicomMwlEntity.getRefreshCycle());
        String codeMapping = dicomMwlEntity.getCodeMapping();
        //use switch case for code mapping comboBox
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

    public void loadData(){
        tagSelectionTable.getItems().clear();
        scpListTable.getItems().clear();

        try {
            dicomTagMappingList = dicomTagMappingService.findAll();
            dicomMwlList = dicomMwlService.findAll();

            tagSelectionTable.getItems().addAll(dicomTagMappingList);
            scpListTable.getItems().addAll(dicomMwlList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        newMode();
        maxPduCheckBoxClick();
    }


    public void deleteScpClick() {
        if(selectedItemId!=0){
            try {
                dicomMwlService.delete(selectedItemId);
                scpListTable.getItems().clear();
                dicomMwlList = dicomMwlService.findAll();
                scpListTable.getItems().addAll(dicomMwlList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            newMode();
        }
    }

    private final DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();
    public void echoClick() {
        DicomMwlEntity dicomMwlEntity = (DicomMwlEntity) scpListTable.getSelectionModel().getSelectedItem();
        ArrayList<DicomGeneralEntity> dicomGeneralEntityArrayList = null;
        try {
            dicomGeneralEntityArrayList = dicomGeneralService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DicomGeneralEntity dicomGeneralEntity = dicomGeneralEntityArrayList.getFirst();
        if(dicomMwlEntity!=null){

            DicomMain dicomMain = new DicomMain(dicomMwlEntity.getIpAddress(), dicomMwlEntity.getPort(), dicomMwlEntity.getAeTitle(), dicomGeneralEntity.getStationAETitle());

            // Start timer
            long startTime = System.currentTimeMillis();

            // Perform DICOM echo
            boolean status = dicomMain.echo();

            // End timer
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Format the output
            String output = String.format("Echo : %s", status ? "Success" : "Fail");
            String time = String.format("%d ms", duration);

            // Add the result to the TableView
            Platform.runLater(() -> {
                verificationTableView.getItems().clear();
                verificationTableView.getItems().add(new VerificationResult(time, output));
            });

        }
    }

    public void includeExpandedModuleComboBoxClick() {

    }

    public void codeMappingComboBoxClick() {

    }

    public void maxPduCheckBoxClick() {
        if(maxPduCheckBox.isSelected()){
            maxPduInput.setDisable(false);
            maxPduInput.setText("0");
        }else{
            maxPduInput.setDisable(true);
        }
    }

    public void saveClick() {
        String name = nameInput.getText();
        String aeTitle = aeTitleInput.getText();
        String ipAddress = ipAddressInput.getText();
        int port  = Integer.parseInt(portInput.getText());
        int maxPdu ;
        if(maxPduCheckBox.isSelected()){
            maxPdu = Integer.parseInt(maxPduInput.getText());
        }else{
            maxPdu = 0;
        }
        int verificationTimeout = Integer.parseInt(verificationTimeoutInput.getText());
        String refreshCycle = refreshCycleInput.getText();
        String codeMapping = "" ;//codeMappingComboBox.getSelectionModel().getSelectedItem().toString();

        DicomMwlEntity dicomMwlEntity = new DicomMwlEntity();

        dicomMwlEntity.setName(name);
        dicomMwlEntity.setAeTitle(aeTitle);
        dicomMwlEntity.setIpAddress(ipAddress);
        dicomMwlEntity.setPort(port);
        dicomMwlEntity.setMaxPdu(maxPdu);
        dicomMwlEntity.setVerificationTimeout(verificationTimeout);
        dicomMwlEntity.setRefreshCycle(refreshCycle);
        dicomMwlEntity.setCodeMapping(codeMapping);

        try {
            if(selectedItemId==0){
                dicomMwlService.save(dicomMwlEntity);
            }else{
                DicomMwlEntity selectedItem = (DicomMwlEntity) scpListTable.getSelectionModel().getSelectedItem();
                int id = selectedItem.getId();
                dicomMwlService.update(id,dicomMwlEntity);
            }
            scpListTable.getItems().clear();
            dicomMwlList = dicomMwlService.findAll();
            scpListTable.getItems().addAll(dicomMwlList);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
