package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class StepDao  extends GenericRepository<StepEntity,Integer> {

    public StepDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<StepEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<StepEntity> list(String query, Object... params) {
        ArrayList<StepEntity> resultList = new ArrayList<>();
        StepRowMapper rowMapper = StepRowMapper.getInstance();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resultList.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public ArrayList<StepEntity> findByStepName(String stepName) {
        String query = "SELECT * FROM StepEntity WHERE stepName LIKE ?";
        String searchPattern = "%" + stepName + "%";
        return list(query, searchPattern);
    }


    public ArrayList<StepEntity> findByProcedureId(int procedureId) {
        String query = "SELECT * FROM StepEntity WHERE procedureId = ?";
        return list(query, procedureId);
    }

    public ArrayList<StepEntity> findStepByPatientSize(String patientSize, String selectedBodyPart) {
        String query = "SELECT * FROM StepEntity WHERE patientSize = ? AND stepName LIKE ?";
        String searchPattern = "%" + selectedBodyPart + "%";
        return list(query, patientSize, searchPattern);
    }


    public void deleteByProjection(String projection) {
        String query = "DELETE FROM StepEntity WHERE stepName = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, projection);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
