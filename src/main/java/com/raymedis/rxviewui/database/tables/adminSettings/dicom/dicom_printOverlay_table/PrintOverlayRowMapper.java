package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintOverlayRowMapper implements GenericRepository.RowMapper<PrintOverlayEntity, Integer> {



    @Override
    public PrintOverlayEntity mapRow(ResultSet rs) throws SQLException {
        PrintOverlayEntity entity = new PrintOverlayEntity();

        // Extract and map each field from the ResultSet to the entity
        entity.setId(rs.getInt("id"));
        entity.setOverlayId(rs.getString("overlayId"));
        entity.setOverlayName(rs.getString("overlayName"));
        entity.setLogoPath(rs.getString("logoPath"));

        // Map ENUM value safely
        String positionTypeString = rs.getString("positionType");
        if (positionTypeString != null) {
            entity.setPositionType(PositionType.valueOf(positionTypeString.toUpperCase()));
        }

        entity.setItemContent(rs.getString("itemContent"));
        entity.setIsSelected(rs.getBoolean("isSelected"));
        return entity;
    }

    @Override
    public Object getValue(PrintOverlayEntity entity, String columnName) {
        return switch (columnName) {
            case "overlayId" -> entity.getOverlayId();
            case "overlayName" -> entity.getOverlayName();
            case "logoPath" -> entity.getLogoPath();
            case "positionType" -> entity.getPositionType() != null ? entity.getPositionType().name() : null;
            case "itemContent" -> entity.getItemContent();
            case "isSelected"->entity.getIsSelected();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(PrintOverlayEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
