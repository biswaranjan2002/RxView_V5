package com.raymedis.rxviewui.database.tables.register.localListBodyParts_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class LocalListBodyPartsDao extends GenericRepository<LocalListBodyPartsEntity, Integer> {

    public LocalListBodyPartsDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<LocalListBodyPartsEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<LocalListBodyPartsEntity> list(String query, Object... params) {
        ArrayList<LocalListBodyPartsEntity> resultList = new ArrayList<>();
        LocalListBodyPartsRowMapper rowMapper = LocalListBodyPartsRowMapper.getInstance();
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


    public ArrayList<LocalListBodyPartsEntity> findByLocalListId(int localListId) {
        String query = "SELECT * FROM localListBodyPartsEntity WHERE localListId = ?";
        return list(query,localListId);
    }

}
