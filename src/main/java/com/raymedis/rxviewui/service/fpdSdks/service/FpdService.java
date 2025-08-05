package com.raymedis.rxviewui.service.fpdSdks.service;

import com.raymedis.rxviewui.service.json.DetectorDetails;

public interface FpdService {


    boolean init();

    boolean connect();

    boolean isConnected();

    DetectorDetails getFpdDetails();

    int findAndGetDevices();

    FpsStatus getFpdStatus();

    boolean cancelOperationMode();

    boolean triggerAedOneShot();

    void getFpdImageToFile(String imagePath);

    void getCorrectedImageToFile(String imagePath);

    String getBatteryStatus();

    String getSignalStrength();
}
