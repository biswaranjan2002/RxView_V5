package com.raymedis.rxviewui.service.adminSettings.studySettings;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table.AutoDeleteEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table.AutoDeleteService;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table.StorageAlertsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table.StorageAlertsService;
import com.raymedis.rxviewui.service.login.LoginController;
import javafx.scene.control.TextField;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class StudyDeleteController {
    
    private final static StudyDeleteController instance = new StudyDeleteController();
    public static StudyDeleteController getInstance(){
        return instance;
    }

    public JFXCheckBox enableAutoDelete;
    public JFXCheckBox enableTimeLimit;
    public JFXComboBox weekComboBox;
    public JFXCheckBox enableStorageLimit;
    public JFXComboBox storageSizeComboBox;
    public JFXCheckBox enableAllStudies;
    public JFXCheckBox enableSentAndPrintedStudies;
    public JFXCheckBox enableRejectedStudies;

    public JFXSlider warningSlider;
    public JFXSlider criticalSlider;

    public TextField warningLabel;
    public TextField criticalLabel;

    private final StorageAlertsService storageAlertsService = StorageAlertsService.getInstance();
    private final AutoDeleteService autoDeleteService = AutoDeleteService.getInstance();
    private static AutoDeleteEntity autoDeleteEntityMain = null;


    public void loadData(){
        StorageAlertsEntity storageAlerts;

        try {
            storageAlerts = storageAlertsService.findAll().getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(storageAlerts!=null){
            double warningValue = storageAlerts.getWarningValue();
            double criticalValue = storageAlerts.getCriticalValue();

            warningLabel.setText(String.format("%.1f", warningValue));
            criticalLabel.setText(String.format("%.1f", criticalValue));

            File currentDir = new File(".");
            double totalSpace = LoginController.formatSize(currentDir.getTotalSpace());
            criticalSlider.setMax(totalSpace);
            warningSlider.setMax(totalSpace);

            warningSlider.setValue(warningValue);
            criticalSlider.setValue(criticalValue);
        }

        List<AutoDeleteEntity> autoDeleteEntities = null;

        try {
            autoDeleteEntities = autoDeleteService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (autoDeleteEntities != null && !autoDeleteEntities.isEmpty()) {
             autoDeleteEntityMain = autoDeleteEntities.getFirst();
        }


        if(autoDeleteEntityMain !=null){
            enableAutoDelete.setSelected(autoDeleteEntityMain.isAutoDelete());
            enableTimeLimit.setSelected(autoDeleteEntityMain.isTimeEnabled());
            enableStorageLimit.setSelected(autoDeleteEntityMain.isStorageEnabled());
            enableAllStudies.setSelected(autoDeleteEntityMain.isAllStudies());
            enableSentAndPrintedStudies.setSelected(autoDeleteEntityMain.isSentOrPrintedStudies());
            enableRejectedStudies.setSelected(autoDeleteEntityMain.isRejectedStudies());

            weekComboBox.setValue(String.valueOf(autoDeleteEntityMain.getWeekDuration()));
            storageSizeComboBox.setValue(String.valueOf(autoDeleteEntityMain.getStorageSize()));
        }

    }
    
    public void loadEvents(){
        syncSliderAndTextField(warningSlider, warningLabel);
        syncSliderAndTextField(criticalSlider, criticalLabel);
    }




    private void syncSliderAndTextField(JFXSlider slider, TextField textField) {
        // Sync Slider value to TextField
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(String.format("%.1f", newValue.doubleValue()));
        });

        // Sync TextField value to Slider
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && newValue.matches("\\d*\\.?\\d*")) {
                try {
                    double value = Double.parseDouble(newValue);
                    slider.setValue(value);
                } catch (NumberFormatException e) {
                    // Handle invalid input if needed
                    textField.setText(oldValue);
                }
            }
        });
    }

    public void saveStorageAlerts() {
        StorageAlertsEntity storageAlerts = null;
        try {
            storageAlerts = storageAlertsService.findAll().getFirst();

            storageAlerts.setCriticalValue(criticalSlider.getValue());
            storageAlerts.setWarningValue(warningSlider.getValue());

            storageAlertsService.update(storageAlerts.getId(),storageAlerts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveAutoDeleteOptions() throws SQLException {
        AutoDeleteEntity autoDeleteEntity = new AutoDeleteEntity();
        autoDeleteEntity.setAutoDelete(enableAutoDelete.isSelected());
        autoDeleteEntity.setTimeEnabled(enableTimeLimit.isSelected());
        autoDeleteEntity.setStorageEnabled(enableStorageLimit.isSelected());
        autoDeleteEntity.setAllStudies(enableAllStudies.isSelected());
        autoDeleteEntity.setSentOrPrintedStudies(enableSentAndPrintedStudies.isSelected());
        autoDeleteEntity.setRejectedStudies(enableRejectedStudies.isSelected());

        autoDeleteEntity.setWeekDuration(Integer.parseInt(weekComboBox.getValue().toString()));
        autoDeleteEntity.setStorageSize(Integer.parseInt(storageSizeComboBox.getValue().toString()));
        autoDeleteEntity.setSavedDate(LocalDateTime.now());


        if(autoDeleteEntityMain==null){
            autoDeleteService.save(autoDeleteEntity);
        }else{
            autoDeleteService.update(1,autoDeleteEntity);
        }
    }





}
