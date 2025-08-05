package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcedureBodyPartRowMapper implements GenericRepository.RowMapper<ProcedureBodyPartEntity, Integer> {
    @Override
    public ProcedureBodyPartEntity mapRow(ResultSet rs) throws SQLException {
        ProcedureBodyPartEntity entity = new ProcedureBodyPartEntity();
        entity.setId(rs.getInt("id"));
        entity.setMeaning(rs.getString("meaning"));
        entity.setAlias(rs.getString("alias"));
        entity.setHide(rs.getBoolean("hide"));
        entity.setCodeValue(rs.getString("codeValue"));
        entity.setDesignator(rs.getString("designator"));
        entity.setVersion(rs.getString("version"));
        entity.setBodyPartExamination(rs.getString("bodyPartExamination"));
        return entity;
    }

    @Override
    public Object getValue(ProcedureBodyPartEntity entity, String columnName) {
        return switch (columnName) {
            case "meaning" -> entity.getMeaning();
            case "alias" -> entity.getAlias();
            case "hide" -> entity.getHide();
            case "codeValue" -> entity.getCodeValue();
            case "designator" -> entity.getDesignator();
            case "version" -> entity.getVersion();
            case "bodyPartExamination" -> entity.getBodyPartExamination();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(ProcedureBodyPartEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
