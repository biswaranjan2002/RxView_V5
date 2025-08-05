package com.raymedis.rxviewui.service.login;

import org.mindrot.jbcrypt.BCrypt;

public class Hashing{

    public static String hashPassword(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public static boolean checkPassword(String candidate, String hashed) {
       return BCrypt.checkpw(candidate, hashed);
    }
}