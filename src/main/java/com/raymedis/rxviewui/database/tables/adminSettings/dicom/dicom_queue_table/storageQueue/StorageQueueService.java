package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.storageQueue;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageQueueService {

    private final static StorageQueueService instance = new StorageQueueService();
    public static StorageQueueService getInstance(){
        return instance;
    }

    private final GenericRepository<StorageQueueEntity, Integer> dicomQueueDao;

    public StorageQueueService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("state", "VARCHAR(255)");
        columns.put("examDateTime", "DATETIME NOT NULL");
        columns.put("patientName","VARCHAR(255)");
        columns.put("patientId","VARCHAR(255)");

        dicomQueueDao = new GenericRepository<>("storageQueueEntity",columns,"id",new StorageQueueRowMapper(),true,1);
    }

    public void save(StorageQueueEntity storageQueueEntity) throws SQLException {
        dicomQueueDao.save(storageQueueEntity);
    }


    public void update(int id, StorageQueueEntity storageQueueEntity) throws SQLException {
        dicomQueueDao.update(id, storageQueueEntity);
    }

    public void delete(int id) throws SQLException {
        dicomQueueDao.deleteByPrimaryKey(id);
    }

    public ArrayList<StorageQueueEntity> findAll() throws SQLException {
        return (ArrayList<StorageQueueEntity>) dicomQueueDao.findAll();
    }


}
