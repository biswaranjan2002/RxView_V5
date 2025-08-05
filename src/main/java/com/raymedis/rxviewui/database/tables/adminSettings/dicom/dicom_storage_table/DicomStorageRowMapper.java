package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomStorageRowMapper implements GenericRepository.RowMapper<DicomStorageEntity, Integer> {

    @Override
    public DicomStorageEntity mapRow(ResultSet rs) throws SQLException {
        DicomStorageEntity dicomStorageEntity = new DicomStorageEntity();

        dicomStorageEntity.setId(rs.getInt("id"));
        dicomStorageEntity.setName(rs.getString("name"));
        dicomStorageEntity.setAeTitle(rs.getString("aeTitle"));
        dicomStorageEntity.setIpAddress(rs.getString("ipAddress"));
        dicomStorageEntity.setPort(rs.getInt("port"));
        dicomStorageEntity.setMaxPdu(rs.getInt("maxPdu"));
        dicomStorageEntity.setTimeout(rs.getInt("timeout"));
        dicomStorageEntity.setVerificationTimeout(rs.getInt("verificationTimeout"));
        dicomStorageEntity.setIsSelected(rs.getBoolean("isSelected"));
        dicomStorageEntity.setBurnedInAnnotation(rs.getBoolean("BurnedInAnnotation"));
        dicomStorageEntity.setBurnedInInformation(rs.getBoolean("BurnedInInformation"));
        dicomStorageEntity.setBurnedWithCrop(rs.getBoolean("BurnedWithCrop"));
        dicomStorageEntity.setLut(rs.getInt("lut"));
        dicomStorageEntity.setCompression(rs.getInt("compression"));
        dicomStorageEntity.setTransferSyntax(rs.getInt("transferSyntax"));
        dicomStorageEntity.setDapUnitType(rs.getInt("dapUnitType"));
        dicomStorageEntity.setDapUnitType(rs.getInt("softwareCollimation"));

        return dicomStorageEntity;
    }

    @Override
    public Object getValue(DicomStorageEntity entity, String columnName) {
        if (entity == null || columnName == null || columnName.isEmpty()) {
            return null;
        }

        return switch (columnName) {
            case "id" -> entity.getId();
            case "name" -> entity.getName();
            case "aeTitle" -> entity.getAeTitle();
            case "ipAddress" -> entity.getIpAddress();
            case "port" -> entity.getPort();
            case "maxPdu" -> entity.getMaxPdu();
            case "timeout" -> entity.getTimeout();
            case "verificationTimeout" -> entity.getVerificationTimeout();
            case "isSelected" -> entity.getIsSelected();
            case "burnedInAnnotation" ->entity.isBurnedInAnnotation();
            case "burnedInInformation" -> entity.isBurnedInInformation();
            case "BurnedWithCrop" -> entity.isBurnedWithCrop();
            case "lut"->entity.getLut();
            case "compression" ->entity.getCompression();
            case "transferSyntax" ->entity.getTransferSyntax();
            case "dapUnitType"-> entity.getDapUnitType();
            case "softwareCollimation" -> entity.getSoftwareCollimation();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(DicomStorageEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
