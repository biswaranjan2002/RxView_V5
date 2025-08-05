package com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemAccountsService {

    public static SystemAccountsService instance = new SystemAccountsService();

    public static SystemAccountsService getInstance() {
        return instance;
    }

    private final SystemAccountsDao systemAccountsDao;

    public SystemAccountsService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("userName", "VARCHAR(255)");
        columns.put("password", "VARCHAR(255)");
        columns.put("userGroup", "VARCHAR(255)");

        systemAccountsDao = new SystemAccountsDao("SystemAccountsEntity", columns, "userId", new SystemAccountsRowMapper(),false, "");
    }


    public void updateSystemAccount(String userId,SystemAccountsEntity systemAccountsEntity) throws SQLException {
        for (SystemAccountsEntity account : findAllAccounts()){
            if(account.getUserId().equals(userId)){
                systemAccountsDao.update(userId,systemAccountsEntity);
                return;
            }
        }
       systemAccountsDao.save(systemAccountsEntity);
    }

    public void deleteSystemAccount(String userId) throws SQLException {
        systemAccountsDao.deleteByPrimaryKey(userId);
    }

    public ArrayList<SystemAccountsEntity> findAllAccounts() throws SQLException {
        return (ArrayList<SystemAccountsEntity>)systemAccountsDao.findAll();
    }

    public SystemAccountsEntity findByUserName(String userName) {
        return systemAccountsDao.findByUserName(userName);
    }

    public SystemAccountsEntity findByUserId(String userId) {
        return systemAccountsDao.findByUserId(userId);
    }

}
