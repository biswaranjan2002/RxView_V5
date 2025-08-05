package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.printQueue;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PrintQueueRowMapper implements GenericRepository.RowMapper<PrintQueueEntity, Integer> {
    @Override
    public PrintQueueEntity mapRow(ResultSet rs) throws SQLException {
        PrintQueueEntity printQueueEntity = new PrintQueueEntity();

        printQueueEntity.setId(rs.getInt("id"));
        printQueueEntity.setState(rs.getString("state"));
        printQueueEntity.setExamDateTime(rs.getObject("examDateTime", LocalDateTime.class));
        printQueueEntity.setPatientName(rs.getString("patientName"));
        printQueueEntity.setPatientId(rs.getString("patientId"));

        return printQueueEntity;
    }

    @Override
    public Object getValue(PrintQueueEntity entity, String columnName) {
        return switch (columnName) {
            case "state" -> entity.getState();
            case "examDateTime" -> entity.getExamDateTime();
            case "patientName" -> entity.getPatientName();
            case "patientId" -> entity.getPatientId();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(PrintQueueEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
