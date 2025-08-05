package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_queue_table.printQueue;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrintQueueService {

    private final static PrintQueueService instance = new PrintQueueService();
    public static PrintQueueService getInstance(){
        return instance;
    }

    private final GenericRepository<PrintQueueEntity, Integer> printQueueDao;

    public PrintQueueService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("state", "VARCHAR(255)");
        columns.put("examDateTime", "DATETIME NOT NULL");
        columns.put("patientName","VARCHAR(255)");
        columns.put("patientId","VARCHAR(255)");

        printQueueDao = new GenericRepository<>("printQueueEntity",columns,"id",new PrintQueueRowMapper(),true,1);
    }

    public void save(PrintQueueEntity printQueueEntity) throws SQLException {
        printQueueDao.save(printQueueEntity);
    }


    public void update(int id, PrintQueueEntity printQueueEntity) throws SQLException {
        printQueueDao.update(id, printQueueEntity);
    }

    public void delete(int id) throws SQLException {
        printQueueDao.deleteByPrimaryKey(id);
    }

    public ArrayList<PrintQueueEntity> findAll() throws SQLException {
        return (ArrayList<PrintQueueEntity>) printQueueDao.findAll();
    }

}
