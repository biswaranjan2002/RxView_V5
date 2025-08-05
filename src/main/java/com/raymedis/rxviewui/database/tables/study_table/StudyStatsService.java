package com.raymedis.rxviewui.database.tables.study_table;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyStatsService {

    private final static  StudyStatsService instance =  new StudyStatsService();
    public static StudyStatsService getInstance(){
        return instance;
    }

    private final StudyStatsDao studyStatsDao;

    public StudyStatsService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("studyDate", "DATETIME NOT NULL");
        columns.put("studyCount", "INT NOT NULL");
        columns.put("rejectedStudyCount", "INT NOT NULL");

        studyStatsDao =  new StudyStatsDao("StudyStatsEntity", columns, "id", new StudyStatsRowMapper(),true, 1);
    }

    public void save(StudyStatsEntity studyStatsEntity) throws SQLException {
        studyStatsDao.save(studyStatsEntity);
    }

    public void update(int id,StudyStatsEntity studyStatsEntity) throws SQLException {
        studyStatsDao.update(id,studyStatsEntity);
    }

    public ArrayList<StudyStatsEntity> findAll() throws SQLException {
        return (ArrayList<StudyStatsEntity>) studyStatsDao.findAll();
    }

    public StudyStatsEntity findAllThisDayStudies(LocalDate date){
        return studyStatsDao.findTodayStudiesCount(date);
    }

    public List<StudyStatsEntity> findLatestStudyRecord(){
        return studyStatsDao.findLatestStudyRecord();
    }


}
