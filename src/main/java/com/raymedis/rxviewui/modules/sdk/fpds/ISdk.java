package com.raymedis.rxviewui.modules.sdk.fpds;

public interface ISdk {

    boolean init();
    Object findNumberOfDevice();
    boolean connect(String connectionType, int index);
    boolean isConnected();
    //getting details will be individual class method
    boolean triggerAED();
    boolean disConnect();
    boolean clearAll();
}
