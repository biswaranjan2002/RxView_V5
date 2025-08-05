package com.raymedis.rxviewui.service.fpdSdks.dao;


import com.zioxa.nativeSdkModule.sdk.innocare.InnoCareNative;

public class InnoCareDao {

    private static InnoCareNative innoCareNative = new InnoCareNative();

    public InnoCareDao() {
    }

    public static InnoCareNative getInnoCareNative() {
        return innoCareNative;
    }

    public static void setInnoCareNative(InnoCareNative innoCareNative) {
        InnoCareDao.innoCareNative = innoCareNative;
    }


}
