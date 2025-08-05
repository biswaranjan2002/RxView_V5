package com.raymedis.rxviewui.database.tables.adminSettings.system.system_themes_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemThemesRowMapper implements GenericRepository.RowMapper<SystemThemesEntity, Integer> {
    @Override
    public SystemThemesEntity mapRow(ResultSet rs) throws SQLException {
        SystemThemesEntity systemThemesEntity = new SystemThemesEntity();

        systemThemesEntity.setId(rs.getInt("id"));
        systemThemesEntity.setBackgroundColor(rs.getString("backgroundColor"));
        systemThemesEntity.setForegroundColor(rs.getString("foregroundColor"));
        systemThemesEntity.setButtonColor(rs.getString("buttonColor"));
        systemThemesEntity.setTextColor(rs.getString("textColor"));
        systemThemesEntity.setAdditionalColor(rs.getString("additionalColor"));
        systemThemesEntity.setFontFamily(rs.getString("fontFamily"));

        return systemThemesEntity;
    }

    @Override
    public Object getValue(SystemThemesEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        switch (columnName) {
            case "backgroundColor":
                return entity.getBackgroundColor();
            case "foregroundColor":
                return entity.getForegroundColor();
            case "buttonColor":
                return entity.getButtonColor();
            case "textColor":
                return entity.getTextColor();
            case "additionalColor":
                return entity.getAdditionalColor();
            case "fontFamily":
                return entity.getFontFamily();
            default:
                System.out.println("Unknown column name: " + columnName);
                return null;
        }
    }


    @Override
    public Object getPrimaryKeyValue(SystemThemesEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
