package com.raymedis.rxviewui.service.fpdSdks.dao;


import com.zioxa.nativeSdkModule.sdk.careray.CareRayNative;

public class CarerayDao {

    private static CareRayNative careRayNative = new CareRayNative();

    public CarerayDao() {

    }

    public static CareRayNative getCareRayNative() {
        return careRayNative;
    }

    public static void setCareRayNative(CareRayNative careRayNative) {
        CarerayDao.careRayNative = careRayNative;
    }

}
