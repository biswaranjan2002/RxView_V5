package com.raymedis.rxviewui.service.fpdSdks.service;


import com.raymedis.rxviewui.service.fpdSdks.dao.InnoCareDao;
import com.raymedis.rxviewui.service.json.DetectorDetails;
import com.raymedis.rxviewui.service.json.FpdSDK;
import com.zioxa.nativeSdkModule.sdk.innocare.*;

import java.util.ArrayList;

public class InnoCareService implements FpdService {

    private final static InnoCareService instance = new InnoCareService();
    public static InnoCareService getInstance(){
        return instance;
    }

    public static InnoCareNative innoCareNative;

    public static ArrayList<InxFpdDevJava> fpdDevJava;

    public InnoCareService() {
        innoCareNative = InnoCareDao.getInnoCareNative();
    }

    public boolean init(){
        return innoCareNative.init();
    }

    public int findAndGetDevices(){
        fpdDevJava = innoCareNative.findAndGetDevices();
        return fpdDevJava.size();
    }

    @Override
    public FpsStatus getFpdStatus() {
        InxFpdOperStatusJava status = innoCareNative.getFpdStatus();
        FpdStatusMode mode = FpdStatusMode.fromValue(status.getMode() & 0xFF);

        return switch (mode) {
            case FpdStatusMode.UNKNOWN_MODE -> FpsStatus.UNKNOWN_MODE;
            case FpdStatusMode.NON_OPERATION_STATE -> FpsStatus.NON_OPERATION_STATE;
            case FpdStatusMode.OPERATION_IDLE -> FpsStatus.OPERATION_IDLE;
            case FpdStatusMode.RESET_MODE -> FpsStatus.RESET_MODE;
            case FpdStatusMode.DIAGNOSIS_MODE -> FpsStatus.DIAGNOSIS_MODE;
            case FpdStatusMode.DISCHARGE_MODE -> FpsStatus.DISCHARGE_MODE;
            case FpdStatusMode.AED_ONE_SHOT_MODE -> FpsStatus.AED_ONE_SHOT_MODE;
            case FpdStatusMode.AED_REPEAT_MODE -> FpsStatus.AED_REPEAT_MODE;
            case FpdStatusMode.SOFTWARE_SYNC_MODE -> FpsStatus.SOFTWARE_SYNC_MODE;
            case FpdStatusMode.SYSTEM_UP -> FpsStatus.SYSTEM_UP;
            case FpdStatusMode.SYSTEM_DOWN -> FpsStatus.SYSTEM_DOWN;
            case FpdStatusMode.ACQUIRE_MODE -> FpsStatus.ACQUIRE_MODE;
        };


    }

    public boolean cancelOperationMode() {
        return innoCareNative.cancelOperationMode();
    }

    @Override
    public boolean triggerAedOneShot() {
        return InnoCareService.innoCareNative.triggerAedOneShot();
    }

    @Override
    public void getFpdImageToFile(String imagePath) {
        innoCareNative.getFpdImageToFile(imagePath);
    }

    @Override
    public void getCorrectedImageToFile(String imagePath) {
        innoCareNative.getCorrectedImageToFile(imagePath);
    }


    /*

    Add this to the Inno care native to get battery

    typedef struct InxDiagBattInfo_ {
        unsigned char capacityP;			// battery remaining capacity: 1 %
        char temperatureC;					// battery temperature: 1 degreeC
        unsigned short voltageMv;			// charging/discharging voltage in mV
        short currentMa;					// charging/discharging current in mA
        unsigned short totalMah;			// full capacity (FFFFh if not available)
        unsigned short cycle;				// cycle time (FFFFh if not available)
        char device[20 + 1];				// device name
    } InxDiagBattInfo;

    */
    @Override
    public String getBatteryStatus() {
        return "100"; // InnoCare does not provide battery status in the current SDK
    }


    /*
    add this to Inno care native to get the signal strength
     typedef struct InxScanInfo_ {
        unsigned char bssid[6];			 BSSID
        char ssid[33];					 string of SSID name, max length = 32, padded with '\0'
        unsigned char channel;			 bound channel
        unsigned char auth;				 authentication method, see #FpdWifiAuth
        short signal;					 signal strength, -128 ~ 127 dBm
    } InxScanInfo;

    */

    @Override
    public String getSignalStrength() {
        return "100"; // InnoCare does not provide signal strength in the current SDK
    }


    public boolean connect(){
        return innoCareNative.connect("Wifi", 0);
    }

    public boolean isConnected(){
        return innoCareNative.isConnected();
    }

    public DetectorDetails getFpdDetails(){
        InxFpdSpecJava inxFpdSpecJava = innoCareNative.getFpdDetails();

        DetectorDetails detectorDetails = new DetectorDetails(1, inxFpdSpecJava.getModel(), inxFpdSpecJava.getSerial(),
                FpdSDK.RAD_INNOCARE, "Wifi", formatIpArray(fpdDevJava.getFirst().getWifiIp4()));

        return detectorDetails;
    }

    public static String formatIpArray(int[] ipArray) {
        if (ipArray == null || ipArray.length != 4) {
            return "Invalid IP";
        }
        return ipArray[0] + "." + ipArray[1] + "." + ipArray[2] + "." + ipArray[3];
    }

}
