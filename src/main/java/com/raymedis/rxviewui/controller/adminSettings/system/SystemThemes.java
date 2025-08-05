package com.raymedis.rxviewui.controller.adminSettings.system;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.service.adminSettings.systemSettings.SystemThemesController;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class SystemThemes {

    public JFXComboBox textFontComboBox;
    public JFXComboBox themeComboBox;

    public JFXButton restartButton;
    public JFXButton saveButton;

    public VBox customThemeBox;

    public ColorPicker colorPicker1;
    public ColorPicker colorPicker2;
    public ColorPicker colorPicker3;
    public ColorPicker colorPicker4;
    public ColorPicker colorPicker5;


    private SystemThemesController systemThemesController;
    public void initialize() throws SQLException {
        systemThemesController = SystemThemesController.getInstance();

        systemThemesController.colorPicker1 = colorPicker1;
        systemThemesController.colorPicker2 = colorPicker2;
        systemThemesController.colorPicker3 = colorPicker3;
        systemThemesController.colorPicker4 = colorPicker4;
        systemThemesController.colorPicker5 = colorPicker5;

        systemThemesController.textFontComboBox=textFontComboBox;
        systemThemesController.themeComboBox=themeComboBox;

        systemThemesController.restartButton=restartButton;
        systemThemesController.saveButton =saveButton;

        systemThemesController.customThemeBox=customThemeBox;

        systemThemesController.loadData();
    }




    public void onUpdateTheme() {
       systemThemesController.onUpdateTheme();
    }

    public void saveClick() {
        systemThemesController.saveClick();
    }


    public void restartClick() {
        systemThemesController.restartClick();
    }
}
