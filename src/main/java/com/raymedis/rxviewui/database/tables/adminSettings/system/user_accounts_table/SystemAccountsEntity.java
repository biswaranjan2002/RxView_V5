package com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table;

public class SystemAccountsEntity {

    private String userName;
    private String userId;
    private String password;
    private String userGroup;

    //constructors
    public SystemAccountsEntity() {
    }

    public SystemAccountsEntity(String userName, String userId, String password, String userGroup) {
        this.userName = userName;
        this.userId = userId;
        this.password = password;
        this.userGroup = userGroup;
    }


    //getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public String toString() {
        return "SystemAccountsEntity{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", userGroup='" + userGroup + '\'' +
                '}';
    }
}
