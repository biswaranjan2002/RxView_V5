package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomStorageCommitmentRowMapper implements GenericRepository.RowMapper<DicomStorageCommitmentEntity,Integer> {
    @Override
    public DicomStorageCommitmentEntity mapRow(ResultSet rs) throws SQLException {
        DicomStorageCommitmentEntity dicomStorageCommitmentEntity = new DicomStorageCommitmentEntity();
        dicomStorageCommitmentEntity.setId(rs.getInt("id"));
        dicomStorageCommitmentEntity.setName(rs.getString("name"));
        dicomStorageCommitmentEntity.setAeTitle(rs.getString("aeTitle"));
        dicomStorageCommitmentEntity.setIpAddress(rs.getString("ipAddress"));
        dicomStorageCommitmentEntity.setPort(rs.getInt("port"));
        dicomStorageCommitmentEntity.setMaxPdu(rs.getInt("maxPdu"));
        dicomStorageCommitmentEntity.setVerificationTimeout(rs.getInt("verificationTimeout"));
        dicomStorageCommitmentEntity.setIsSelected(rs.getBoolean("isSelected"));
        return dicomStorageCommitmentEntity;
    }

    @Override
    public Object getValue(DicomStorageCommitmentEntity entity, String columnName) {
        return switch (columnName) {
            case "name" -> entity.getName();
            case "aeTitle" -> entity.getAeTitle();
            case "ipAddress" -> entity.getIpAddress();
            case "port" -> entity.getPort();
            case "maxPdu" -> entity.getMaxPdu();
            case "verificationTimeout" -> entity.getVerificationTimeout();
            case "isSelected" -> entity.getIsSelected();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(DicomStorageCommitmentEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
