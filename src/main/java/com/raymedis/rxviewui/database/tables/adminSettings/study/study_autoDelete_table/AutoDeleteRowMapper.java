package com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class AutoDeleteRowMapper implements GenericRepository.RowMapper<AutoDeleteEntity, Integer>{

    @Override
    public AutoDeleteEntity mapRow(ResultSet rs) throws SQLException {
        AutoDeleteEntity entity = new AutoDeleteEntity();
        entity.setId(rs.getInt("id"));
        entity.setAutoDelete(rs.getBoolean("isAutoDelete"));
        entity.setTimeEnabled(rs.getBoolean("isTimeEnabled"));
        entity.setStorageEnabled(rs.getBoolean("isStorageEnabled"));
        entity.setAllStudies(rs.getBoolean("allStudies"));
        entity.setSentOrPrintedStudies(rs.getBoolean("sentOrPrintedStudies"));
        entity.setRejectedStudies(rs.getBoolean("rejectedStudies"));
        entity.setWeekDuration(rs.getInt("weekDuration"));
        entity.setStorageSize(rs.getInt("storageSize"));
        entity.setSavedDate(rs.getObject("savedDate", LocalDateTime.class));

        return entity;
    }

    @Override
    public Object getValue(AutoDeleteEntity entity, String columnName) {
        if (columnName == null || entity == null) {
            return null;
        }

        return switch (columnName) {
            case "isAutoDelete" -> entity.isAutoDelete();
            case "isTimeEnabled" -> entity.isTimeEnabled();
            case "isStorageEnabled" -> entity.isStorageEnabled();
            case "allStudies" -> entity.isAllStudies();
            case "sentOrPrintedStudies" -> entity.isSentOrPrintedStudies();
            case "rejectedStudies" -> entity.isRejectedStudies();
            case "weekDuration"->entity.getWeekDuration();
            case "storageSize" ->entity.getStorageSize();
            case "savedDate" -> entity.getSavedDate();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(AutoDeleteEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
