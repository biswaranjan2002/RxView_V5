package com.raymedis.rxviewui.service.fpdSdks.service;



import com.raymedis.rxviewui.service.fpdSdks.dao.PixgenDao;
import com.raymedis.rxviewui.service.json.DetectorDetails;
import com.zioxa.nativeSdkModule.sdk.pixgen.CBIDSystemInfo;
import com.zioxa.nativeSdkModule.sdk.pixgen.PixGenNative;

import java.util.ArrayList;

public class PixgenService implements FpdService {

    private final static PixgenService instance = new PixgenService();
    public static PixgenService getInstance(){
        return instance;
    }

    public static PixGenNative pixGenNative;


    public PixgenService() {
        pixGenNative = PixgenDao.getPixGenNative();
    }

    public boolean init(){
        return true;
    }


    public static ArrayList<String> list;
    public int findAndGetDevices(){
        list = (ArrayList<String>) pixGenNative.DiscoverAndConnectFpdPixgen();
        return list.size();
    }

    @Override
    public FpsStatus getFpdStatus() {
        return null;
        //pixGenNative.statusFpdPixgen();
    }

    @Override
    public boolean cancelOperationMode() {
        return false;
    }

    @Override
    public boolean triggerAedOneShot() {
        return false;
    }

    @Override
    public void getFpdImageToFile(String imagePath) {
        //pixGenNative.getFpdImageToFile(imagePath);
    }

    @Override
    public void getCorrectedImageToFile(String imagePath) {
        //pixGenNative.
    }

    @Override
    public String getBatteryStatus() {
        return pixGenNative.DetectorSystemInFoPixgen().getBatteryGage()+"";
    }

    @Override
    public String getSignalStrength() {
        return pixGenNative.DetectorSystemInFoPixgen().getWifiLevel()+"";
    }

    public boolean connect(){
        //return pixGenNative.connect();
        return true;
    }

    public boolean isConnected(){
        return pixGenNative.isConnected();
    }

    public DetectorDetails getFpdDetails(){
        CBIDSystemInfo info = pixGenNative.DetectorSystemInFoPixgen();

        //DetectorDetails detectorDetails = new DetectorDetails(-1, info.getProductType(), info.getSerialNumber(),
        //        FpdSDK.RAD_INNOCARE, "Wifi", "1.1.1.1");

        //return detectorDetails;

        return null;
    }
}
