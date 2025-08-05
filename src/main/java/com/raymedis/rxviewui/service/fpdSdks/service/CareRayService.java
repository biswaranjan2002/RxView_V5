package com.raymedis.rxviewui.service.fpdSdks.service;



import com.raymedis.rxviewui.service.fpdSdks.dao.CarerayDao;
import com.raymedis.rxviewui.service.json.DetectorDetails;
import com.zioxa.nativeSdkModule.sdk.careray.CareRayNative;
import com.zioxa.nativeSdkModule.sdk.careray.CareRayStatusInfo;

public class CareRayService implements FpdService {

    private final static CareRayService instance = new CareRayService();
    public static CareRayService getInstance(){
        return instance;
    }

    public static CareRayNative careRayNative;




    public CareRayService() {
        careRayNative = CarerayDao.getCareRayNative();
    }


    public boolean init(){
        return true;
    }

    public int findAndGetDevices(){
        //
        return -1;
    }

    @Override
    public FpsStatus getFpdStatus() {
        CareRayStatusInfo status = careRayNative.getFpdStatus();
        return null;
    }

    @Override
    public boolean cancelOperationMode() {
        return careRayNative.cancelOperationMode();
    }

    @Override
    public boolean triggerAedOneShot() {
        return careRayNative.triggerAedOneShot();
    }

    @Override
    public void getFpdImageToFile(String imagePath) {
        careRayNative.getFpdImageToFile(imagePath);
    }

    @Override
    public void getCorrectedImageToFile(String imagePath) {
        careRayNative.getCorrectedImageToFile(imagePath);
    }

    @Override
    public String getBatteryStatus() {
        return careRayNative.getBatteryDetails().getRelativeStateOfCharge()+"";
    }

    @Override
    public String getSignalStrength() {
        return careRayNative.getWirelessDetails().getSignalLevel();
    }

    public boolean connect(){
        return careRayNative.connect();
    }

    public boolean isConnected(){
        return careRayNative.isConnected();
    }

    public DetectorDetails getFpdDetails(){

        //DetectorDetails detectorDetails = new DetectorDetails(-1, inxFpdSpecJava.getModel(), inxFpdSpecJava.getSerial(),
        //        FpdSDK.RAD_CARERAY, "Wifi", fpdDevJava.getFirst().getWifiIp4().toString());

        return null;
    }
}
