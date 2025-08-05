package com.raymedis.rxviewui.database.tables.adminSettings.study.study_storageAlerts_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageAlertsService {

    private final static StorageAlertsService instance = new StorageAlertsService();
    public static StorageAlertsService getInstance(){
        return instance;
    }

    private final GenericRepository<StorageAlertsEntity, Integer> storageAlertsDao;

    public StorageAlertsService(){

        Map<String, String> columns = new HashMap<>();
        columns.put("criticalValue","DOUBLE NOT NULL");
        columns.put("warningValue","DOUBLE NOT NULL");

        storageAlertsDao = new GenericRepository<>("StorageAlertsEntity",columns,"id",new StorageAlertsRowMapper(),false,1);
    }

    public void save(StorageAlertsEntity storageAlerts) throws SQLException {
        storageAlertsDao.save(storageAlerts);
    }

    public void update(int id,StorageAlertsEntity storageAlerts) throws SQLException {
        storageAlertsDao.update(id,storageAlerts);
    }

    public ArrayList<StorageAlertsEntity> findAll() throws SQLException {
        return (ArrayList<StorageAlertsEntity>) storageAlertsDao.findAll();
    }

}
