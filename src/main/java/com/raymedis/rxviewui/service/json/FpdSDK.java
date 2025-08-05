package com.raymedis.rxviewui.service.json;

import java.util.ArrayList;
import java.util.List;

public enum FpdSDK {

    RAD_INNOCARE, RAD_CARERAY, RAD_PIXGEN;


    public static ArrayList<FpdSDK> getRAD() {
        return new ArrayList<>(List.of(RAD_INNOCARE, RAD_CARERAY, RAD_PIXGEN));
    }


    public static ArrayList<FpdSDK> getFluoro() {
        return null;
    }

    public static ArrayList<FpdSDK> getDental() {
        return null;
    }

    public static ArrayList<FpdSDK> getVeterinary() {
        return null;
    }
}
