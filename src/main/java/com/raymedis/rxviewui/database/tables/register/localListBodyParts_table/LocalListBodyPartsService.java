package com.raymedis.rxviewui.database.tables.register.localListBodyParts_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalListBodyPartsService {

    private static LocalListBodyPartsService instance = new LocalListBodyPartsService();
    public static LocalListBodyPartsService getInstance(){
        return instance;
    }

    private LocalListBodyPartsDao localListBodyPartsDao;

    public LocalListBodyPartsService(){
        Map<String, String> columns = new HashMap<>();
        columns.put("bodyPartName", "VARCHAR(255)");
        columns.put("position", "VARCHAR(255)");
        columns.put("localListId", "INT");

        localListBodyPartsDao = new LocalListBodyPartsDao("localListBodyPartsEntity",columns,"id",new LocalListBodyPartsRowMapper(),true,1);
    }


    public void save(LocalListBodyPartsEntity localListBodyPartsEntity) throws SQLException {
        localListBodyPartsDao.save(localListBodyPartsEntity);
    }

    public void update(int id,LocalListBodyPartsEntity localListBodyPartsEntity) throws SQLException {
        localListBodyPartsDao.update(id,localListBodyPartsEntity);
    }

    public void delete(int id) throws SQLException {
        localListBodyPartsDao.deleteByPrimaryKey(id);
    }

    public ArrayList<LocalListBodyPartsEntity> findAll() throws SQLException {
        return (ArrayList<LocalListBodyPartsEntity>) localListBodyPartsDao.findAll();
    }


    public ArrayList<LocalListBodyPartsEntity> findByLocalListId(int localListId) throws SQLException {
        return localListBodyPartsDao.findByLocalListId(localListId);
    }
}
