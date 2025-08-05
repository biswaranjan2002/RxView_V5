package com.raymedis.rxviewui.service.login;

public class LoginService {

    private final static LoginService instance = new LoginService();
    public static LoginService getInstance(){
        return instance;
    }

    private final Authorization authorization = Authorization.getInstance();
    private final Authentication authentication = Authentication.getInstance();

    public String loginButtonClick(String userId, String password) {
       return authorization.authorize(authentication.authenticate(userId,password));
    }

}
