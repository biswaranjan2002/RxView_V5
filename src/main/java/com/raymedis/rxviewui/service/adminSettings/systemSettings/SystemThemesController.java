package com.raymedis.rxviewui.service.adminSettings.systemSettings;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_themes_table.SystemThemesEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.system_themes_table.SystemThemesService;
import javafx.application.Platform;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class SystemThemesController {

    private final static SystemThemesController instance = new SystemThemesController();
    public ColorPicker colorPicker5;
    public ColorPicker colorPicker4;
    public ColorPicker colorPicker3;
    public ColorPicker colorPicker1;
    public ColorPicker colorPicker2;

    public JFXComboBox textFontComboBox;
    public JFXComboBox themeComboBox;

    public JFXButton restartButton;
    public JFXButton saveButton;

    public VBox customThemeBox;

    private final SystemThemesService systemThemesService = SystemThemesService.getInstance();
    public static SystemThemesController getInstance(){
        return instance;
    }


    public void loadData() throws SQLException {

        SystemThemesEntity systemThemesEntity=null;

        if(!systemThemesService.findAll().isEmpty()){
            systemThemesEntity = systemThemesService.findAll().getFirst();
        }

        if(systemThemesEntity==null){
            return;
        }

        colorPicker1.setValue(Color.web(systemThemesEntity.getBackgroundColor()));
        colorPicker2.setValue(Color.web(systemThemesEntity.getForegroundColor()));
        colorPicker3.setValue(Color.web(systemThemesEntity.getButtonColor()));
        colorPicker4.setValue(Color.web(systemThemesEntity.getTextColor()));
        colorPicker5.setValue(Color.web(systemThemesEntity.getAdditionalColor()));

        String fontFamily = systemThemesEntity.getFontFamily().toLowerCase();

        switch (fontFamily){
            case "century gothic":
                textFontComboBox.getSelectionModel().select(0);
                break;
            case "system":
                textFontComboBox.getSelectionModel().select(1);
                break;
            case "times new roman":
                textFontComboBox.getSelectionModel().select(2);
                break;
            default:
                System.out.println("unknown font family");
        }
    }





    private String colorToHexString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("#%02x%02x%02x", r, g, b);
    }



    public void onUpdateTheme() {
        String selectedTheme = themeComboBox.getSelectionModel().getSelectedItem().toString().toLowerCase();

        switch (selectedTheme) {
            case "custom" -> customThemeBox.setDisable(false);
            case "light" -> {
                customThemeBox.setDisable(true);

                colorPicker1.setValue(Color.web("#ffffff"));
                colorPicker2.setValue(Color.web("#f0f0f0"));
                colorPicker3.setValue(Color.web("#0078d4"));
                colorPicker4.setValue(Color.web("#000000"));
                colorPicker5.setValue(Color.web("#606060"));


            }
            case "dark" -> {
                customThemeBox.setDisable(true);

                colorPicker1.setValue(Color.web("#121212"));
                colorPicker2.setValue(Color.web("#1e1e1e"));
                colorPicker3.setValue(Color.web("#1a73e8"));
                colorPicker4.setValue(Color.web("#ffffff"));
                colorPicker5.setValue(Color.web("#a0a0a0"));
            }
            case "default" -> {
                customThemeBox.setDisable(true);

                colorPicker1.setValue(Color.web("#3a3a3a"));
                colorPicker2.setValue(Color.web("#575656"));
                colorPicker3.setValue(Color.web("#ec641c"));
                colorPicker4.setValue(Color.web("#ffffff"));
                colorPicker5.setValue(Color.web("#808080"));
            }
        }
    }

    public void saveClick() {
        String fontFamily = (String) textFontComboBox.getValue();

        // Convert colors to hex strings
        String primary = colorToHexString(colorPicker1.getValue());
        String secondary = colorToHexString(colorPicker2.getValue());
        String accent = colorToHexString(colorPicker3.getValue());
        String text = colorToHexString(colorPicker4.getValue());
        String additional = colorToHexString(colorPicker5.getValue());

        // Update SystemThemesEntity
        SystemThemesEntity systemThemesEntity = new SystemThemesEntity();
        systemThemesEntity.setBackgroundColor(primary);
        systemThemesEntity.setForegroundColor(secondary);
        systemThemesEntity.setButtonColor(accent);
        systemThemesEntity.setTextColor(text);
        systemThemesEntity.setAdditionalColor(additional);
        systemThemesEntity.setFontFamily(fontFamily);

        // Prepare CSS content
        String fontsCssContent = String.format(
                ".fontStyle {\n    -fx-font-family: \"%s\";\n}\n",
                fontFamily
        );

        String themeColorsCssContent = String.format(
                ".root {\n    -primary: %s;\n    -secondary: %s;\n    -accent: %s;\n    -text: %s;\n    -additional: %s;\n}\n",
                primary, secondary, accent, text, additional
        );

        // File paths
        Path fontsCssFilePath = Paths.get("src/main/resources/Styles/Css/Fonts.css");
        Path themeColorCssFilePath = Paths.get("src/main/resources/Styles/Css/ThemeColor.css");

        // Write content to files
        try {
            writeToFile(fontsCssFilePath, fontsCssContent);
            writeToFile(themeColorCssFilePath, themeColorsCssContent);

            systemThemesService.update(0, systemThemesEntity);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method for writing content to files
    private void writeToFile(Path filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(content);
        }
    }


    public void restartClick() {
        Platform.exit();
        System.exit(0);
    }


}
