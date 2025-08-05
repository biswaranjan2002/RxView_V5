package com.raymedis.rxviewui.service.fpdSdks.dao;

import com.zioxa.nativeSdkModule.sdk.pixgen.PixGenNative;

public class PixgenDao {

    private static PixGenNative pixGenNative = new PixGenNative();

    public PixgenDao() {
    }

    public static PixGenNative getPixGenNative() {
        return pixGenNative;
    }

    public static void setPixGenNative(PixGenNative pixGenNative) {
        PixgenDao.pixGenNative = pixGenNative;
    }
}
