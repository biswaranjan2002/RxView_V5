package com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegistrationGeneralService {

    public static RegistrationGeneralService instance =new RegistrationGeneralService();
    public static RegistrationGeneralService getInstance(){
        return instance;
    }

    private final GenericRepository<DefaultTabEntity, Integer> registrationGeneralDao;

    public RegistrationGeneralService(){
        Map<String, String> columns = new HashMap<>();
        columns.put("isManual", "BIT NOT NULL");
        columns.put("isWorkList", "BIT NOT NULL");
        columns.put("isLocalList", "BIT NOT NULL");
        registrationGeneralDao = new GenericRepository<>("DefaultTabs",columns,"id", new DefaultTabRowMapper(),false, 1);
    }

    public void saveDefaultTab(DefaultTabEntity defaultTabEntity) throws SQLException {
        registrationGeneralDao.save(defaultTabEntity);
    }

    public ArrayList<DefaultTabEntity> getDefaultTab() throws SQLException {
        return (ArrayList<DefaultTabEntity>) registrationGeneralDao.findAll();
    }


    public void updateDefaultTab(int id,DefaultTabEntity defaultTab) throws SQLException {
        registrationGeneralDao.update(id,defaultTab);
    }

}
