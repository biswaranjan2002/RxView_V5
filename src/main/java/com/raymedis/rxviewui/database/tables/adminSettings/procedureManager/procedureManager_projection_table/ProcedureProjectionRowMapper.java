package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcedureProjectionRowMapper implements GenericRepository.RowMapper<ProcedureProjectionEntity, Integer> {

    @Override
    public ProcedureProjectionEntity mapRow(ResultSet rs) throws SQLException {
        ProcedureProjectionEntity entity = new ProcedureProjectionEntity();
        entity.setId(rs.getInt("id"));
        entity.setMeaning(rs.getString("meaning"));
        entity.setAlias(rs.getString("alias"));
        entity.setHide(rs.getBoolean("hide"));
        entity.setCodeValue(rs.getString("codeValue"));
        entity.setDesignator(rs.getString("designator"));
        entity.setVersion(rs.getString("version"));
        entity.setViewPosition(rs.getString("viewPosition"));
        return entity;
    }


    @Override
    public Object getValue(ProcedureProjectionEntity entity, String columnName) {
        if (entity == null || columnName == null || columnName.isEmpty()) {
            return null;
        }

        return switch (columnName) {
            case "id" -> entity.getId();
            case "meaning" -> entity.getMeaning();
            case "alias" -> entity.getAlias();
            case "hide" -> entity.getHide();
            case "codeValue" -> entity.getCodeValue();
            case "designator" -> entity.getDesignator();
            case "version" -> entity.getVersion();
            case "viewPosition" -> entity.getViewPosition();
            default -> throw new IllegalArgumentException("Unknown column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(ProcedureProjectionEntity entity, String primaryKeyName) {
        return entity.getId();
    }

}
