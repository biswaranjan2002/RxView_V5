package com.raymedis.rxviewui.modules.sdk.fpds.innocare;

import com.raymedis.rxviewui.modules.sdk.fpds.ISdk;

public class InnoCareSdk implements ISdk {

    // private InnoCareNative innoCareNative;
    //  public InnoCareSdk(){
    //        innoCareNative = new InnoCareNative();
    //    }


    @Override
    public boolean init() {
        return false;
    }

    @Override
    public Object findNumberOfDevice() {
        return null;
    }

    @Override
    public boolean connect(String connectionType, int index) {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean triggerAED() {
        return false;
    }

    @Override
    public boolean disConnect() {
        return false;
    }

    @Override
    public boolean clearAll() {
        return false;
    }
}
