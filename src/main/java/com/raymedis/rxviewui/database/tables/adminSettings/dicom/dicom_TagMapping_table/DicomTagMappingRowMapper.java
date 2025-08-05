package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomTagMappingRowMapper implements GenericRepository.RowMapper<DicomTagMappingEntity,Integer> {
    @Override
    public DicomTagMappingEntity mapRow(ResultSet rs) throws SQLException {
        DicomTagMappingEntity dicomTagMappingEntity = new DicomTagMappingEntity();
        dicomTagMappingEntity.setId(rs.getInt("id"));
        dicomTagMappingEntity.setOriginalTagNo(rs.getString("originalTagNo"));
        dicomTagMappingEntity.setInputTagNo(rs.getString("inputTagNo"));
        dicomTagMappingEntity.setOutputTagNo(rs.getString("outputTagNo"));
        dicomTagMappingEntity.setOriginalTagName(rs.getString("originalTagName"));
        dicomTagMappingEntity.setInputTagName(rs.getString("inputTagName"));
        dicomTagMappingEntity.setOutputTagName(rs.getString("outputTagName"));

        return dicomTagMappingEntity;
    }

    @Override
    public Object getValue(DicomTagMappingEntity entity, String columnName) {
        return switch (columnName) {
            case "originalTagNo" -> entity.getOriginalTagNo();
            case "inputTagNo" -> entity.getInputTagNo();
            case "outputTagNo" -> entity.getOutputTagNo();
            case "originalTagName" -> entity.getOriginalTagName();
            case "inputTagName" -> entity.getInputTagName();
            case "outputTagName" -> entity.getOutputTagName();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(DicomTagMappingEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
