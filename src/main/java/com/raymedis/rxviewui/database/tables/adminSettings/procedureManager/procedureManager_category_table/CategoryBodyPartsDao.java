package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_category_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class CategoryBodyPartsDao extends GenericRepository<CategoryBodyPartsEntity,String> {

    public CategoryBodyPartsDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<CategoryBodyPartsEntity, String> row_mapper, boolean isAutoIncrement, String dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }


    public CategoryBodyPartsEntity findByBodyPartName(String bodyPart) {
        String query = "SELECT * FROM BodyPartsEntity WHERE LOWER(bodyPartName) LIKE LOWER(?)";
        ArrayList<CategoryBodyPartsEntity> results = list(query, "%" + bodyPart + "%");

        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }


    private ArrayList<CategoryBodyPartsEntity> list(String query, Object... params) {
        ArrayList<CategoryBodyPartsEntity> resultList = new ArrayList<>();
        CategoryBodyPartsRowMapper rowMapper = CategoryBodyPartsRowMapper.getInstance();

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


    public ArrayList<CategoryBodyPartsEntity> getAllStepExistsBodyParts() {
        String query = "SELECT * FROM BodyPartsEntity WHERE isStepExist = true";
        return list(query);
    }

}
