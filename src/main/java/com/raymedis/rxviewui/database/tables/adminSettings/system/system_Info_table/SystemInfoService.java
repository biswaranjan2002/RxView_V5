package com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemInfoService {
    public static SystemInfoService instance = new SystemInfoService();

    public static SystemInfoService getInstance() {
        return instance;
    }

    private final GenericRepository<SystemInfoEntity, Integer> systemInfoDao;

    public SystemInfoService() {
        Map<String, String> columns = new HashMap<>();

        // Use appropriate VARCHAR lengths for MySQL
        columns.put("institutionName", "VARCHAR(255)");
        columns.put("institutionAddress", "VARCHAR(255)");
        columns.put("department", "VARCHAR(255)");
        columns.put("manufacturer", "VARCHAR(255)");
        columns.put("modelName", "VARCHAR(255)");
        columns.put("telephone", "VARCHAR(50)");
        columns.put("email", "VARCHAR(100)");
        columns.put("homePage", "VARCHAR(255)");
        columns.put("softwareVersion", "VARCHAR(50)");
        columns.put("serialNumber", "VARCHAR(100)");
        columns.put("language", "VARCHAR(50)");

        systemInfoDao = new GenericRepository<>("SystemInfoEntity", columns, "id", new SystemInfoRowMapper(),false, 1);
    }


    public void saveSystemInfo(SystemInfoEntity systemInfoEntity) throws SQLException {
        systemInfoDao.save(systemInfoEntity);
    }

    public void updateSystemInfo(int id,SystemInfoEntity systemInfoEntity) throws SQLException {
        systemInfoDao.update(id,systemInfoEntity);
    }

    public ArrayList<SystemInfoEntity> getSystemInfo() throws SQLException {
        return (ArrayList<SystemInfoEntity>) systemInfoDao.findAll();
    }



}
