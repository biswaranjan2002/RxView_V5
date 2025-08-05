package com.raymedis.rxviewui.database.tables.study_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudyStatsDao extends GenericRepository<StudyStatsEntity,Integer> {
    public StudyStatsDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<StudyStatsEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }


    public StudyStatsEntity findTodayStudiesCount(LocalDate date) {
        List<StudyStatsEntity> studyStatsEntity = new ArrayList<>();
        String query = "SELECT * FROM StudyStatsEntity WHERE studyDate = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, date); //using setObject for date type
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    studyStatsEntity.add(StudyStatsRowMapper.getInstance().mapRow(resultSet));
                    return studyStatsEntity.getFirst();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<StudyStatsEntity> findLatestStudyRecord() {
        List<StudyStatsEntity> studyStatsEntity = new ArrayList<>();
        String query = "SELECT * FROM StudyStatsEntity ORDER BY studyDate LIMIT 1";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                studyStatsEntity.add(StudyStatsRowMapper.getInstance().mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyStatsEntity;
    }



}
