package com.raymedis.rxviewui.database.tables.bodyPartStudy_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BodyPartStudyService {

    private final static BodyPartStudyService instance = new BodyPartStudyService();
    public static BodyPartStudyService getInstance(){
        return instance;
    }

    private BodyPartStudyDao bodyPartStudyDao;

    public BodyPartStudyService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("studyId", "VARCHAR(255) NOT NULL");
        columns.put("bodyPartName", "VARCHAR(255) NOT NULL");
        columns.put("position", "VARCHAR(255) DEFAULT NULL");
        columns.put("seriesId", "VARCHAR(255) NOT NULL");
        columns.put("seriesUid", "VARCHAR(255) NOT NULL");
        columns.put("instanceId", "VARCHAR(255) NOT NULL");
        columns.put("instanceUid", "VARCHAR(255) NOT NULL");
        columns.put("isRejected", "BIT NOT NULL");
        columns.put("isExposed", "BIT NOT NULL");
        columns.put("windowWidth", "DOUBLE DEFAULT NULL");
        columns.put("windowLevel", "DOUBLE DEFAULT NULL");
        columns.put("kv", "DOUBLE DEFAULT NULL");
        columns.put("ma", "DOUBLE DEFAULT NULL");
        columns.put("mas", "DOUBLE DEFAULT NULL");
        columns.put("ms", "DOUBLE DEFAULT NULL");
        columns.put("exposedDate", "DATETIME NULL");


        bodyPartStudyDao = new BodyPartStudyDao("BodyPartStudyEntity", columns, "id", new BodyPartStudyRowMapper(), true,1);
    }

    public void save(BodyPartStudyEntity bodyPartStudyEntity) throws SQLException {
        bodyPartStudyDao.save(bodyPartStudyEntity);
    }

    private void update(int id,BodyPartStudyEntity bodyPartStudyEntity) throws SQLException {
        bodyPartStudyDao.update(id,bodyPartStudyEntity);
    }

    private void delete(int id) throws SQLException {
        bodyPartStudyDao.deleteByPrimaryKey(id);
    }

    private ArrayList<BodyPartStudyEntity> findAll() throws SQLException {
        return (ArrayList<BodyPartStudyEntity>) bodyPartStudyDao.findAll();
    }


    private ArrayList<BodyPartStudyEntity> findByStudyId(String studyId){
        return bodyPartStudyDao.findByStudyId(studyId);
    }



}
