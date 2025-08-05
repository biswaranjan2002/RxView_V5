package com.raymedis.rxviewui.database.tables.adminSettings.study.study_autoDelete_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutoDeleteService {

    private final static AutoDeleteService instance = new AutoDeleteService();

    public static AutoDeleteService getInstance(){
        return instance;
    }

    private final GenericRepository<AutoDeleteEntity, Integer> autoDeleteDao;

    public AutoDeleteService(){
        Map<String, String> columns = new HashMap<>();
        columns.put("isAutoDelete", "BOOLEAN NOT NULL");
        columns.put("isTimeEnabled", "BOOLEAN NOT NULL");
        columns.put("isStorageEnabled", "BOOLEAN NOT NULL");
        columns.put("allStudies", "BOOLEAN NOT NULL");
        columns.put("sentOrPrintedStudies", "BOOLEAN NOT NULL");
        columns.put("rejectedStudies", "BOOLEAN NOT NULL");
        columns.put("weekDuration", "INT NOT NULL");
        columns.put("storageSize", "INT NOT NULL");
        columns.put("savedDate","DATETIME NOT NULL");

        autoDeleteDao = new GenericRepository<>("AutoDeleteEntity",columns,"id",new AutoDeleteRowMapper(),true,1);
    }

    public void save(AutoDeleteEntity autoDeleteEntity) throws SQLException {
        autoDeleteDao.save(autoDeleteEntity);
    }

    public void delete(int id) throws SQLException {
        autoDeleteDao.deleteByPrimaryKey(id);
    }

    public void update(int id,AutoDeleteEntity autoDeleteEntity) throws SQLException {
        autoDeleteDao.update(id,autoDeleteEntity);
    }

    public ArrayList<AutoDeleteEntity> findAll() throws SQLException {
        return (ArrayList<AutoDeleteEntity>) autoDeleteDao.findAll();
    }


}
