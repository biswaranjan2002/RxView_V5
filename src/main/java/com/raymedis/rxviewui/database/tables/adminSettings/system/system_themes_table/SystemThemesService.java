package com.raymedis.rxviewui.database.tables.adminSettings.system.system_themes_table;

import com.raymedis.rxviewui.database.GenericRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemThemesService {
    private static ArrayList<SystemThemesEntity> themesList;

    private static final SystemThemesService instance = new SystemThemesService();
    public static SystemThemesService getInstance(){
        return instance;
    }

    private final GenericRepository<SystemThemesEntity, Integer> systemThemesDao;

    public SystemThemesService(){
        Map<String, String> columns = new HashMap<>();

        // Use appropriate VARCHAR lengths for MySQL
        columns.put("backgroundColor", "VARCHAR(255)");
        columns.put("foregroundColor", "VARCHAR(255)");
        columns.put("buttonColor", "VARCHAR(255)");
        columns.put("textColor", "VARCHAR(255)");
        columns.put("additionalColor", "VARCHAR(255)");
        columns.put("fontFamily", "VARCHAR(255)");

        systemThemesDao = new GenericRepository<>("SystemThemesEntity", columns, "id", new SystemThemesRowMapper(),false, 1);
    }

    public void save(SystemThemesEntity systemThemesEntity) throws SQLException {
        systemThemesDao.save(systemThemesEntity);
    }

    public void update(int id,SystemThemesEntity systemThemesEntity) throws SQLException {
        systemThemesDao.update(id,systemThemesEntity);
    }


    public ArrayList<SystemThemesEntity>findAll() throws SQLException {
       return (ArrayList<SystemThemesEntity>) systemThemesDao.findAll();
    }



}
