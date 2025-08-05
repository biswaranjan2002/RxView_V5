package com.raymedis.rxviewui.database.tables.study_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class StudyStatsRowMapper implements GenericRepository.RowMapper<StudyStatsEntity, Integer>{

    private static StudyStatsRowMapper instance = new StudyStatsRowMapper();

    public static StudyStatsRowMapper getInstance() {
        return instance;
    }

    @Override
    public StudyStatsEntity mapRow(ResultSet rs) throws SQLException {
        StudyStatsEntity studyStats = new StudyStatsEntity();
        studyStats.setId(rs.getInt("id"));
        studyStats.setStudyDate(rs.getObject("studyDate", LocalDate.class));
        studyStats.setStudyCount(rs.getInt("studyCount"));
        studyStats.setRejectedStudyCount(rs.getInt("rejectedStudyCount"));
        return studyStats;
    }


    @Override
    public Object getValue(StudyStatsEntity entity, String columnName) {
        return switch (columnName) {
            case "studyDate" -> entity.getStudyDate();
            case "studyCount" -> entity.getStudyCount();
            case "rejectedStudyCount" -> entity.getRejectedStudyCount();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(StudyStatsEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
