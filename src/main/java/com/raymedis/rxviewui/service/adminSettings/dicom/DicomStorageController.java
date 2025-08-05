package com.raymedis.rxviewui.service.adminSettings.dicom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table.DicomStorageService;
import com.raymedis.rxviewui.modules.dicom.DicomMain;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class DicomStorageController {

    private static DicomStorageController instance = new DicomStorageController();
    public static DicomStorageController getInstance(){
        return instance;
    }

    @FXML ToggleGroup burnedInAnnotationToggle = new ToggleGroup();
    @FXML ToggleGroup burnedInInformationToggle = new ToggleGroup();
    @FXML ToggleGroup burnedWithCropToggle = new ToggleGroup();

    public TableView storageTableView;
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
    public TextField timeoutInput;
    public TextField verificationTimeInput;

    @FXML public TableView<VerificationResult> verificationTableView;
    @FXML public TableColumn<VerificationResult, String> timeColumn;
    @FXML public TableColumn<VerificationResult, String> informationColumn;

    public JFXComboBox lutComboBox;
    public JFXComboBox modalityComboBox;
    public JFXComboBox dapUnitTypeComboBox;
    public JFXComboBox softwareCollimationComboBox;

    public JFXRadioButton burnedInAnnotationYes;
    public JFXRadioButton burnedInAnnotationNo;
    public JFXRadioButton burnedInInformationNo;
    public JFXRadioButton burnedInInformationYes;
    public JFXRadioButton burnedWithCropNo;
    public JFXRadioButton burnedWithCropYes;

    public JFXComboBox transferSyntaxComboBox;
    public JFXComboBox compressionComboBox;
    public VBox tableOuterBox;
    private int selectedItemId = 0;

    private final DicomStorageService dicomStorageService = DicomStorageService.getInstance();
    private ArrayList<DicomStorageEntity> storageList = new ArrayList<>();
    private final DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();

    private Logger logger = LoggerFactory.getLogger(DicomStorageController.class);


    public void loadEvents(){
        burnedInAnnotationYes.setToggleGroup(burnedInAnnotationToggle);
        burnedInAnnotationNo.setToggleGroup(burnedInAnnotationToggle);

        burnedInInformationNo.setToggleGroup(burnedInInformationToggle);
        burnedInInformationYes.setToggleGroup(burnedInInformationToggle);

        burnedWithCropYes.setToggleGroup(burnedWithCropToggle);
        burnedWithCropNo.setToggleGroup(burnedWithCropToggle);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        aeTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aeTitle"));

        useColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
        useColumn.setCellFactory(new Callback<TableColumn<DicomStorageEntity, Boolean>, TableCell<DicomStorageEntity, Boolean>>() {
            @Override
            public TableCell<DicomStorageEntity, Boolean> call(TableColumn<DicomStorageEntity, Boolean> param) {
                return new CheckBoxTableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            DicomStorageEntity dicomMwlEntity = getTableRow().getItem();

                            if (dicomMwlEntity != null) {
                                // Set the initial state of the checkbox
                                checkBox.setSelected(dicomMwlEntity.getIsSelected());

                                // Add a new listener for checkbox changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Uncheck all other rows
                                        for (Object entity : storageTableView.getItems()) {
                                            DicomStorageEntity entity1 = (DicomStorageEntity) entity;
                                            if (!entity1.equals(dicomMwlEntity)) {
                                                entity1.setIsSelected(false);
                                            }
                                        }

                                        // Update the current entity
                                        dicomMwlEntity.setIsSelected(true);

                                        // Refresh the table view to update the UI
                                        storageTableView.refresh();

                                        // Persist the change
                                        try {
                                            dicomStorageService.setAllItemsSelectionToFalse(dicomMwlEntity.getId());
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

        storageTableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<DicomStorageEntity>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                editMode();
            }
        });

        tableOuterBox.setOnMouseClicked(event -> {
            if (!storageTableView.isHover()) {
                storageTableView.getSelectionModel().clearSelection();
                newClick();
            }
        });


        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        informationColumn.setCellValueFactory(new PropertyValueFactory<>("information"));

    }

    public void loadData(){
        storageTableView.getItems().clear();
        storageTableView.getItems().clear();

        try {
            storageList = dicomStorageService.findAll();
            storageTableView.getItems().addAll(storageList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        maxPduChangeClick();
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
        verificationTimeInput.setText("");
        timeoutInput.setText("");

        burnedInAnnotationToggle.selectToggle(null);
        burnedInInformationToggle.selectToggle(null);
        burnedWithCropToggle.selectToggle(null);

        compressionComboBox.getSelectionModel().clearSelection();
        dapUnitTypeComboBox.getSelectionModel().clearSelection();
        lutComboBox.getSelectionModel().clearSelection();
        softwareCollimationComboBox.getSelectionModel().clearSelection();
        transferSyntaxComboBox.getSelectionModel().clearSelection();

        selectedItemId = 0;
    }

    private void editMode() {
        DicomStorageEntity dicomStorageEntity = (DicomStorageEntity) storageTableView.getSelectionModel().getSelectedItem();
        if (dicomStorageEntity == null) {
            return; // Exit if no selection is made
        }

        selectedItemId = dicomStorageEntity.getId();
        newButton.setText("Edit");

        nameInput.setText(dicomStorageEntity.getName() != null ? dicomStorageEntity.getName() : "");
        aeTitleInput.setText(dicomStorageEntity.getAeTitle() != null ? dicomStorageEntity.getAeTitle() : "");
        ipAddressInput.setText(dicomStorageEntity.getIpAddress() != null ? dicomStorageEntity.getIpAddress() : "");
        portInput.setText(String.valueOf(dicomStorageEntity.getPort()));



        Optional.of(dicomStorageEntity.getLut())
                .filter(index -> index >= 0 && index < lutComboBox.getItems().size())
                .ifPresent(index -> lutComboBox.getSelectionModel().select(index));

        Optional.of(dicomStorageEntity.getTransferSyntax())
                .ifPresent(transferSyntaxComboBox.getSelectionModel()::select);

        Optional.of(dicomStorageEntity.getSoftwareCollimation())
                .ifPresent(softwareCollimationComboBox.getSelectionModel()::select);

        Optional.of(dicomStorageEntity.getDapUnitType())
                .ifPresent(dapUnitTypeComboBox.getSelectionModel()::select);

        Optional.of(dicomStorageEntity.getCompression())
                .ifPresent(compressionComboBox.getSelectionModel()::select);

        burnedInInformationYes.setSelected(Boolean.TRUE.equals(dicomStorageEntity.isBurnedInInformation()));
        burnedInInformationNo.setSelected(!Boolean.TRUE.equals(dicomStorageEntity.isBurnedInInformation()));

        burnedWithCropYes.setSelected(Boolean.TRUE.equals(dicomStorageEntity.isBurnedWithCrop()));
        burnedWithCropNo.setSelected(!Boolean.TRUE.equals(dicomStorageEntity.isBurnedWithCrop()));

        burnedInAnnotationYes.setSelected(Boolean.TRUE.equals(dicomStorageEntity.isBurnedInAnnotation()));
        burnedInAnnotationNo.setSelected(!Boolean.TRUE.equals(dicomStorageEntity.isBurnedInAnnotation()));

        int maxPdu = dicomStorageEntity.getMaxPdu();
        if (maxPdu == 0) {
            maxPduCheckBox.setSelected(false);
            maxPduInput.setText("0");
        } else {
            maxPduCheckBox.setSelected(true);
            maxPduInput.setDisable(false);
            maxPduInput.setText(String.valueOf(maxPdu));
        }

        verificationTimeInput.setText(String.valueOf(dicomStorageEntity.getVerificationTimeout()));
        timeoutInput.setText(String.valueOf(dicomStorageEntity.getTimeout()));
    }



    public void deleteClick() {
        if(selectedItemId!=0){
            try {
                dicomStorageService.delete(selectedItemId);
                storageTableView.getItems().clear();
                storageList = dicomStorageService.findAll();
                storageTableView.getItems().addAll(storageList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            newClick();
        }
    }

    public void saveClick() {
        String name = nameInput.getText();
        String aeTitle = aeTitleInput.getText();
        String ipAddress = ipAddressInput.getText();
        String portText = portInput.getText();
        int dapUnitType = dapUnitTypeComboBox.getSelectionModel().getSelectedIndex();
        int compression = compressionComboBox.getSelectionModel().getSelectedIndex();
        int transferSyntax = transferSyntaxComboBox.getSelectionModel().getSelectedIndex();
        int softwareCollimation = softwareCollimationComboBox.getSelectionModel().getSelectedIndex();
        int lut = lutComboBox.getSelectionModel().getSelectedIndex();

        int port =0;
        if(!portText.isEmpty()){
            port = Integer.parseInt(portText);
        }


        int maxPdu;
        if (maxPduCheckBox.isSelected()) {
            maxPdu = Integer.parseInt(maxPduInput.getText());
        } else {
            maxPdu = 0;
        }
        int verificationTimeout;
        int timeout;
        try{
            String verificationTimeText = verificationTimeInput.getText().trim();
            verificationTimeout = verificationTimeText.isEmpty() ? 0 : Integer.parseInt(verificationTimeText);

            String timeoutText = timeoutInput.getText().trim();
            timeout = timeoutText.isEmpty() ? 0 : Integer.parseInt(timeoutText);
        } catch (NumberFormatException e) {
            logger.error("Error : {} ",e.getMessage());
            return;
        }

        if(name.isEmpty() || port==0 || ipAddress.isEmpty() || aeTitle.isEmpty()){
            return;
        }

        DicomStorageEntity dicomStorageEntity = new DicomStorageEntity();

        dicomStorageEntity.setName(name);
        dicomStorageEntity.setAeTitle(aeTitle);
        dicomStorageEntity.setIpAddress(ipAddress);
        dicomStorageEntity.setPort(port);
        dicomStorageEntity.setMaxPdu(maxPdu);
        dicomStorageEntity.setVerificationTimeout(verificationTimeout);
        dicomStorageEntity.setTimeout(timeout);
        dicomStorageEntity.setBurnedInInformation(burnedInInformationYes.isSelected());
        dicomStorageEntity.setBurnedInAnnotation(burnedInAnnotationYes.isSelected());
        dicomStorageEntity.setBurnedWithCrop(burnedWithCropYes.isSelected());
        dicomStorageEntity.setDapUnitType(dapUnitType);
        dicomStorageEntity.setSoftwareCollimation(softwareCollimation);
        dicomStorageEntity.setTransferSyntax(transferSyntax);
        dicomStorageEntity.setLut(lut);
        dicomStorageEntity.setCompression(compression);


        try {
            if (selectedItemId == 0) {
                dicomStorageEntity.setIsSelected(false);
                dicomStorageService.save(dicomStorageEntity);
            } else {
                 dicomStorageService.update(selectedItemId, dicomStorageEntity);
            }
            storageTableView.getItems().clear();
            storageList = dicomStorageService.findAll();
            storageTableView.getItems().addAll(storageList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void maxPduChangeClick() {
        if(maxPduCheckBox.isSelected()){
            maxPduInput.setDisable(false);
            maxPduInput.setText("0");
        }else{
            maxPduInput.setDisable(true);
        }
    }

    public void echoClick() {
        DicomStorageEntity dicomStorageEntity = (DicomStorageEntity) storageTableView.getSelectionModel().getSelectedItem();
        ArrayList<DicomGeneralEntity> dicomGeneralEntityArrayList = null;
        try {
            dicomGeneralEntityArrayList = dicomGeneralService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DicomGeneralEntity dicomGeneralEntity = dicomGeneralEntityArrayList.getFirst();
        if(dicomStorageEntity!=null){

            DicomMain dicomMain = new DicomMain(dicomGeneralEntity.getStationName(), Integer.parseInt(dicomGeneralEntity.getStationPort()), dicomStorageEntity.getAeTitle(), dicomGeneralEntity.getStationAETitle());

            // Start timer
            long startTime = System.currentTimeMillis();

            // Perform DICOM echo
            boolean status = false;
            try {
                status = dicomMain.echo();
            }catch (Exception e){
                logger.error("Exception : {}",e.getMessage());
            }


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


}
