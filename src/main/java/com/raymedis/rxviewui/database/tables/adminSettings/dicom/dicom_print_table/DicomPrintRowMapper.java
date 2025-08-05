package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomPrintRowMapper implements GenericRepository.RowMapper<DicomPrintEntity,Integer> {
    @Override
    public DicomPrintEntity mapRow(ResultSet rs) throws SQLException {
        DicomPrintEntity dicomPrintEntity = new DicomPrintEntity();
        dicomPrintEntity.setId(rs.getInt("id"));
        dicomPrintEntity.setName(rs.getString("name"));
        dicomPrintEntity.setAeTitle(rs.getString("aeTitle"));
        dicomPrintEntity.setIpAddress(rs.getString("ipAddress"));
        dicomPrintEntity.setPort(rs.getInt("port"));
        dicomPrintEntity.setMaxPdu(rs.getInt("maxPdu"));
        dicomPrintEntity.setTimeout(rs.getInt("timeout"));
        dicomPrintEntity.setVerificationTimeout(rs.getInt("verificationTimeout"));
        dicomPrintEntity.setPrintLayout(rs.getString("printLayout"));
        dicomPrintEntity.setIsSelected(rs.getBoolean("isSelected"));
        return dicomPrintEntity;
    }

    @Override
    public Object getValue(DicomPrintEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "name" -> entity.getName();
            case "aeTitle" -> entity.getAeTitle();
            case "ipAddress" -> entity.getIpAddress();
            case "port" -> entity.getPort();
            case "maxPdu" -> entity.getMaxPdu();
            case "timeout" -> entity.getTimeout();
            case "verificationTimeout" -> entity.getVerificationTimeout();
            case "printLayout" -> entity.getPrintLayout();
            case "isSelected" -> entity.getIsSelected();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(DicomPrintEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
