package com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageAlertsRowMapper implements GenericRepository.RowMapper<StorageAlertsEntity, Integer>{

    @Override
    public StorageAlertsEntity mapRow(ResultSet rs) throws SQLException {
        StorageAlertsEntity entity = new StorageAlertsEntity();
        entity.setId(rs.getInt("id"));
        entity.setWarningValue(rs.getDouble("warningValue"));
        entity.setCriticalValue(rs.getDouble("criticalValue"));
        return entity;
    }


    @Override
    public Object getValue(StorageAlertsEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "warningValue" -> entity.getWarningValue();
            case "criticalValue" -> entity.getCriticalValue();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(StorageAlertsEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
