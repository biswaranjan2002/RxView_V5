package com.raymedis.rxviewui.database.tables.bodyPartStudy_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class BodyPartStudyDao extends GenericRepository<BodyPartStudyEntity,Integer> {

    public BodyPartStudyDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<BodyPartStudyEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }


    public ArrayList<BodyPartStudyEntity> findByStudyId(String studyId) {
        String query = "SELECT * FROM BodyPartStudyEntity WHERE studyId = ?";
        ArrayList<BodyPartStudyEntity> bodyPartStudyEntities = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, studyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BodyPartStudyEntity entity = new BodyPartStudyRowMapper().mapRow(resultSet);
                bodyPartStudyEntities.add(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving BodyPartStudyEntity by studyId", e);
        }

        return bodyPartStudyEntities;
    }

}
