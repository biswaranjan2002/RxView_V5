package com.raymedis.rxviewui.database.tables.adminSettings.system.system_themes_table;

public class SystemThemesEntity {

    private int id;
    private String backgroundColor;
    private String foregroundColor;
    private String buttonColor;
    private String textColor;
    private String additionalColor;

    private String fontFamily;

    //constructors
    public SystemThemesEntity() {
    }

    public SystemThemesEntity(int id, String backgroundColor, String foregroundColor, String buttonColor,
                              String textColor, String additionalColor, String fontFamily) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.buttonColor = buttonColor;
        this.textColor = textColor;
        this.additionalColor = additionalColor;
        this.fontFamily = fontFamily;
    }


    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getAdditionalColor() {
        return additionalColor;
    }

    public void setAdditionalColor(String additionalColor) {
        this.additionalColor = additionalColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }


}
