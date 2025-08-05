package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomMppsRowMapper implements GenericRepository.RowMapper<DicomMppsEntity,Integer> {
    @Override
    public DicomMppsEntity mapRow(ResultSet rs) throws SQLException {
        DicomMppsEntity dicomMppsEntity = new DicomMppsEntity();
        dicomMppsEntity.setId(rs.getInt("id"));
        dicomMppsEntity.setName(rs.getString("name"));
        dicomMppsEntity.setAeTitle(rs.getString("aeTitle"));
        dicomMppsEntity.setIpAddress(rs.getString("ipAddress"));
        dicomMppsEntity.setPort(rs.getInt("port"));
        dicomMppsEntity.setMaxPdu(rs.getInt("maxPdu"));
        dicomMppsEntity.setVerificationTimeout(rs.getInt("verificationTimeout"));
        dicomMppsEntity.setLanguage(rs.getString("language"));
        dicomMppsEntity.setIsSelected(rs.getBoolean("isSelected"));
        return dicomMppsEntity;
    }

    @Override
    public Object getValue(DicomMppsEntity entity, String columnName) {
        return switch (columnName) {
            case "name" -> entity.getName();
            case "aeTitle" -> entity.getAeTitle();
            case "ipAddress" -> entity.getIpAddress();
            case "port" -> entity.getPort();
            case "maxPdu" -> entity.getMaxPdu();
            case "verificationTimeout" -> entity.getVerificationTimeout();
            case "language" -> entity.getLanguage();
            case "isSelected" -> entity.getIsSelected();
            default -> throw new IllegalArgumentException("Unknown column: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(DicomMppsEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
