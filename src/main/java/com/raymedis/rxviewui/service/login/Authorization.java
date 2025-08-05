package com.raymedis.rxviewui.service.login;

import com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table.SystemAccountsEntity;

public class Authorization {

    private final static Authorization instance = new Authorization();
    public static Authorization getInstance(){
        return instance;
    }


    public String authorize(SystemAccountsEntity systemAccount) {
        if(systemAccount == null){
            return "null";
        }
        return systemAccount.getUserGroup();
    }
}
