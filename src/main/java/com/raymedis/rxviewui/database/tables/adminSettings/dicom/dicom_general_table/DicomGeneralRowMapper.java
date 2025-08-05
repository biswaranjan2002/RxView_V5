package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DicomGeneralRowMapper  implements GenericRepository.RowMapper<DicomGeneralEntity, Integer> {
    @Override
    public DicomGeneralEntity mapRow(ResultSet rs) throws SQLException {
        DicomGeneralEntity dicomGeneralEntity = new DicomGeneralEntity();
        dicomGeneralEntity.setId(rs.getInt("id"));
        dicomGeneralEntity.setStationName(rs.getString("stationName"));
        dicomGeneralEntity.setStationAETitle(rs.getString("stationAETitle"));
        dicomGeneralEntity.setStationPort(rs.getString("stationPort"));
        return dicomGeneralEntity;
    }

    @Override
    public Object getValue(DicomGeneralEntity entity, String columnName) {
        return switch (columnName) {
            case "stationName" -> entity.getStationName();
            case "stationAETitle" -> entity.getStationAETitle();
            case "stationPort" -> entity.getStationPort();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(DicomGeneralEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
