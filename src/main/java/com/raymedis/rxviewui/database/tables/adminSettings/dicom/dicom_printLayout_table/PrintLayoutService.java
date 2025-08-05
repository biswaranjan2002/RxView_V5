package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrintLayoutService {

    private final static PrintLayoutService instance = new PrintLayoutService();
    public static PrintLayoutService getInstance(){
        return instance;
    }

    private final GenericRepository<PrintLayoutEntity, Integer> printLayoutDao;

    public PrintLayoutService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("layoutName", "VARCHAR(255)");
        printLayoutDao = new GenericRepository<>("PrintLayoutEntity",columns,"id",new PrintLayoutRowMapper(),true,1);
    }

    public void save(PrintLayoutEntity printLayoutEntity) throws SQLException {
        printLayoutDao.save(printLayoutEntity);
    }

    public void update(int id,PrintLayoutEntity printLayoutEntity) throws SQLException {
        printLayoutDao.update(id,printLayoutEntity);
    }

    public void delete(int id) throws SQLException {
        printLayoutDao.deleteByPrimaryKey(id);
    }

    public ArrayList<PrintLayoutEntity> findAll() throws SQLException {
        return (ArrayList<PrintLayoutEntity>) printLayoutDao.findAll();
    }

}
