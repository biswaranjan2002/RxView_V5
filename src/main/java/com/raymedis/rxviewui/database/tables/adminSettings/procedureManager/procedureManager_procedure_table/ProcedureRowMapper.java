package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcedureRowMapper implements GenericRepository.RowMapper<ProcedureEntity, Integer> {

    private static final ProcedureRowMapper instance = new ProcedureRowMapper();
    public static ProcedureRowMapper getInstance() {
        return instance;
    }

    @Override
    public ProcedureEntity mapRow(ResultSet rs) throws SQLException {
        return new ProcedureEntity(
                rs.getInt("procedureId"),
                rs.getString("procedureCode"),
                rs.getString("procedureName"),
                rs.getString("procedureDescription")
        );
    }

    @Override
    public Object getValue(ProcedureEntity entity, String columnName) {
        return switch (columnName) {
            case "procedureId" -> entity.getProcedureId();
            case "procedureCode" -> entity.getProcedureCode();
            case "procedureName" -> entity.getProcedureName();
            case "procedureDescription" -> entity.getProcedureDescription();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }



    @Override
    public Object getPrimaryKeyValue(ProcedureEntity entity, String primaryKeyName) {
        return entity.getProcedureId();
    }
}
