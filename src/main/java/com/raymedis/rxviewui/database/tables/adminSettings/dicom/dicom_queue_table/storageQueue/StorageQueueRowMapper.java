package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.storageQueue;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class StorageQueueRowMapper implements GenericRepository.RowMapper<StorageQueueEntity, Integer> {

    @Override
    public StorageQueueEntity mapRow(ResultSet rs) throws SQLException {
        StorageQueueEntity storageQueueEntity = new StorageQueueEntity();

        storageQueueEntity.setId(rs.getInt("id"));
        storageQueueEntity.setState(rs.getString("state"));
        storageQueueEntity.setExamDateTime(rs.getObject("examDateTime", LocalDateTime.class));
        storageQueueEntity.setPatientName(rs.getString("patientName"));
        storageQueueEntity.setPatientId(rs.getString("patientId"));

        return storageQueueEntity;
    }

    @Override
    public Object getValue(StorageQueueEntity entity, String columnName) {
        return switch (columnName) {
            case "state" -> entity.getState();
            case "examDateTime" -> entity.getExamDateTime();
            case "patientName" -> entity.getPatientName();
            case "patientId" -> entity.getPatientId();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(StorageQueueEntity entity, String primaryKeyName) {
        return entity.getId();
    }


}
