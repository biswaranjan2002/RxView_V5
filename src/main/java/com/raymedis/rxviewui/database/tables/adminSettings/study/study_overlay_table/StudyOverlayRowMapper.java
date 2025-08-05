package com.raymedis.rxviewui.database.tables.adminSettings.study.study_overlay_table;

import com.raymedis.rxviewui.database.GenericRepository;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table.PositionType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudyOverlayRowMapper implements GenericRepository.RowMapper<PrintOverlayEntity, Integer> {
    @Override
    public PrintOverlayEntity mapRow(ResultSet rs) throws SQLException {
        PrintOverlayEntity entity = new PrintOverlayEntity();

        entity.setId(rs.getInt("id"));
        String positionTypeString = rs.getString("positionType");
        if (positionTypeString != null) {
            entity.setPositionType(PositionType.valueOf(positionTypeString.toUpperCase()));
        }
        entity.setItemContent(rs.getString("itemContent"));
        return entity;
    }

    @Override
    public Object getValue(PrintOverlayEntity entity, String columnName) {
        return switch (columnName) {
            case "positionType" -> entity.getPositionType() != null ? entity.getPositionType().name() : null;
            case "itemContent" -> entity.getItemContent();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(PrintOverlayEntity entity, String primaryKeyName) {
        return entity.getId();
    }


}
