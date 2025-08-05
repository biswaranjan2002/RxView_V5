package com.raymedis.rxviewui.database.tables.adminSettings.backup_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class BackupDao extends GenericRepository<BackupEntity,String> {

    public BackupDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<BackupEntity, String> row_mapper, boolean isAutoIncrement, String dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<BackupEntity> list(String query, Object... params) {
        ArrayList<BackupEntity> resultList = new ArrayList<>();
        BackupRowMapper rowMapper = BackupRowMapper.getInstance();
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

    public ArrayList<BackupEntity> findByStudyId(String studyId) {
        String query = "SELECT * FROM BackupEntity WHERE studyId = ?";
        return list(query,studyId);
    }
}
