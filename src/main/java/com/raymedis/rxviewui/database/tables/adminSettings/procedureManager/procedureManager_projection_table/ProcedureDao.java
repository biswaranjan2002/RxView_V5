package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table.ProcedureRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class ProcedureDao extends GenericRepository<ProcedureEntity,Integer> {

    public ProcedureDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<ProcedureEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }


    private ArrayList<ProcedureEntity> list(String query, Object... params) {
        ArrayList<ProcedureEntity> resultList = new ArrayList<>();
        ProcedureRowMapper rowMapper = ProcedureRowMapper.getInstance();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultList.add(rowMapper.mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Database query execution failed: " + e.getMessage());
        }

        return resultList;
    }


    public ArrayList<ProcedureEntity> findByProcedureId(int procedureId) {
        String query = "SELECT * FROM procedureEntity WHERE procedureId = ?";
        return list(query, procedureId);
    }



}
