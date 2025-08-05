package com.raymedis.rxviewui.service.login;

import com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table.SystemAccountsEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table.SystemAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Authentication {

    private final Logger logger =  LoggerFactory.getLogger(Authentication.class);

    private final static Authentication instance = new Authentication();

    public static Authentication getInstance() {
        return instance;
    }


    public SystemAccountsEntity authenticate(String userId, String password) {
        SystemAccountsEntity systemAccounts = SystemAccountsService.getInstance().findByUserId(userId);

        if(systemAccounts!=null){
            logger.info("User found: {}", systemAccounts.getUserName());
            logger.info("userEntity: {}", systemAccounts);
            if(Hashing.checkPassword(password,systemAccounts.getPassword())){
                return systemAccounts;
            }else{
                logger.info("Incorrect password for user: {}", userId);
            }
        }else{
            logger.info("User not found: {}", userId);
        }
        return null;
    }

}



