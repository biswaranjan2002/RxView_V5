package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomMwlRowMapper implements GenericRepository.RowMapper<DicomMwlEntity,Integer> {

    @Override
    public DicomMwlEntity mapRow(ResultSet rs) throws SQLException {
        DicomMwlEntity dicomMwlEntity = new DicomMwlEntity();
        dicomMwlEntity.setId(rs.getInt("id"));
        dicomMwlEntity.setName(rs.getString("name"));
        dicomMwlEntity.setAeTitle(rs.getString("aeTitle"));
        dicomMwlEntity.setIpAddress(rs.getString("ipAddress"));
        dicomMwlEntity.setPort(rs.getInt("port"));
        dicomMwlEntity.setMaxPdu(rs.getInt("maxPdu"));
        dicomMwlEntity.setVerificationTimeout(rs.getInt("verificationTimeout"));
        dicomMwlEntity.setCodeMapping(rs.getString("codeMapping"));
        dicomMwlEntity.setRefreshCycle(rs.getString("refreshCycle"));
        dicomMwlEntity.setIsSelected(rs.getBoolean("isSelected"));
        return dicomMwlEntity;
    }

    @Override
    public Object getValue(DicomMwlEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "name" -> entity.getName();
            case "aeTitle" -> entity.getAeTitle();
            case "ipAddress" -> entity.getIpAddress();
            case "port" -> entity.getPort();
            case "maxPdu" -> entity.getMaxPdu();
            case "verificationTimeout" -> entity.getVerificationTimeout();
            case "codeMapping" -> entity.getCodeMapping();
            case "refreshCycle" -> entity.getRefreshCycle();
            case "isSelected" -> entity.getIsSelected();
            default -> null;
        };
    }



    @Override
    public Object getPrimaryKeyValue(DicomMwlEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
