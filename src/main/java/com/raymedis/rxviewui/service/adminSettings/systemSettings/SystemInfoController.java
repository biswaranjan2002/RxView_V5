package com.raymedis.rxviewui.service.adminSettings.systemSettings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table.SystemInfoService;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class SystemInfoController {

    private static final SystemInfoController instance = new SystemInfoController();

    public TextField institutionNameLabel;
    public TextField institutionAddressLabel;
    public TextField departmentLabel;
    public TextField manufacturerLabel;
    public TextField modelNameLabel;
    public TextField telephoneLabel;
    public TextField emailLabel;
    public TextField homePageLabel;
    public TextField softwareVersionLabel;
    public TextField serialNumberLabel;
    public JFXComboBox languageComboBox;
    public JFXButton configureDetailsButton;

    public static SystemInfoController getInstance(){
        return instance;
    }


    private final SystemInfoService systemInfoService = SystemInfoService.getInstance();
    private boolean isInEditMode = false;


    public void loadData() throws SQLException {
        SystemInfoEntity systemInfo=null;

        if(!SystemInfoService.getInstance().getSystemInfo().isEmpty()){
            systemInfo = SystemInfoService.getInstance().getSystemInfo().getFirst();
        }

        if(systemInfo==null){
            return;
        }

        institutionNameLabel.setText(systemInfo.getInstitutionName());
        institutionAddressLabel.setText(systemInfo.getInstitutionAddress());
        departmentLabel.setText(systemInfo.getDepartment());
        manufacturerLabel.setText(systemInfo.getManufacturer());
        modelNameLabel.setText(systemInfo.getModelName());
        telephoneLabel.setText(systemInfo.getTelephone());
        emailLabel.setText(systemInfo.getEmail());
        homePageLabel.setText(systemInfo.getHomePage());
        softwareVersionLabel.setText(systemInfo.getSoftwareVersion());
        serialNumberLabel.setText(systemInfo.getSerialNumber());

        String language = systemInfo.getLanguage().toLowerCase();
        switch (language){
            case "english":
                languageComboBox.getSelectionModel().select(0);
                break;
            case "hindi":
                languageComboBox.getSelectionModel().select(1);
                break;
            case "japanese":
                languageComboBox.getSelectionModel().select(2);
                break;
            default:
                System.out.println("something wrong with language....");

        }

        institutionNameLabel.setDisable(true);
        institutionAddressLabel.setDisable(true);
        departmentLabel.setDisable(true);

        configureDetailsButton.setText("Edit");

    }



    public void onSelectionChange() {
        SystemInfoEntity systemInfo = new SystemInfoEntity();

        systemInfo.setInstitutionName(institutionNameLabel.getText());
        systemInfo.setInstitutionAddress(institutionAddressLabel.getText());
        systemInfo.setDepartment(departmentLabel.getText());
        systemInfo.setLanguage(languageComboBox.getSelectionModel().getSelectedItem().toString());
        systemInfo.setManufacturer(manufacturerLabel.getText());
        systemInfo.setModelName(modelNameLabel.getText());
        systemInfo.setTelephone(telephoneLabel.getText());
        systemInfo.setEmail(emailLabel.getText());
        systemInfo.setHomePage(homePageLabel.getText());
        systemInfo.setSoftwareVersion(softwareVersionLabel.getText());
        systemInfo.setSerialNumber(serialNumberLabel.getText());

        try {
            systemInfoService.updateSystemInfo(0, systemInfo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public void configureDetailsClick() {
        configureDetailsButton.getParent().requestFocus();
        if (!isInEditMode) {
            configureDetailsButton.setText("Configure");
            institutionNameLabel.setDisable(false);
            institutionAddressLabel.setDisable(false);
            departmentLabel.setDisable(false);

            isInEditMode = true;
        }
        else
        {

            if(institutionNameLabel.getText().isEmpty() ||
                    institutionAddressLabel.getText().isEmpty() ||
                    departmentLabel.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill the Details to Configure...");
                alert.showAndWait();

                return;
            }

            SystemInfoEntity systemInfo = new SystemInfoEntity();

            systemInfo.setInstitutionName(institutionNameLabel.getText());
            systemInfo.setInstitutionAddress(institutionAddressLabel.getText());
            systemInfo.setDepartment(departmentLabel.getText());
            systemInfo.setLanguage(languageComboBox.getSelectionModel().getSelectedItem().toString());
            systemInfo.setManufacturer(manufacturerLabel.getText());
            systemInfo.setModelName(modelNameLabel.getText());
            systemInfo.setTelephone(telephoneLabel.getText());
            systemInfo.setEmail(emailLabel.getText());
            systemInfo.setHomePage(homePageLabel.getText());
            systemInfo.setSoftwareVersion(softwareVersionLabel.getText());
            systemInfo.setSerialNumber(serialNumberLabel.getText());

            try {
                systemInfoService.updateSystemInfo(0, systemInfo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            configureDetailsButton.setText("Edit");
            institutionNameLabel.setDisable(true);
            institutionAddressLabel.setDisable(true);
            departmentLabel.setDisable(true);

            isInEditMode = false;
        }
    }




}
