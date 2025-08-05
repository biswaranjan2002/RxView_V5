package com.raymedis.rxviewui.service.fpdSdks.controller;

import com.raymedis.rxviewui.service.fpdSdks.service.*;
import com.raymedis.rxviewui.service.json.DetectorDetails;
import com.raymedis.rxviewui.service.json.FpdSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FpdController {

    private final InnoCareService innoCareService = InnoCareService.getInstance();
    private final CareRayService careRayService = CareRayService.getInstance();
    private final PixgenService pixgenService =  PixgenService.getInstance();

    private final static FpdController instance = new FpdController();
    public static FpdController getInstance() {
        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(FpdController.class);

    private DetectorDetails detectorDetails;
    private FpdService fpdService = null;

    public void loadDetector(DetectorDetails detectorDetails) {
        this.detectorDetails = detectorDetails;

        FpdSDK fpdSDK  = detectorDetails.getSdkType();

        logger.info("Loading detector: {}, SDK type: {}", detectorDetails.getModel(), fpdSDK);

        switch (fpdSDK){
            case  RAD_INNOCARE:
                fpdService= innoCareService;
                break;
            case RAD_CARERAY:
                fpdService= careRayService;
                break;
            case RAD_PIXGEN:
                fpdService = pixgenService;
                break;
            default:
                logger.info("Unknown SDK type: {}", fpdSDK);}
    }

    public boolean init() {
        if(fpdService == null) {
            noDetectorLoaded();
            return false;
        }
        return fpdService.init();
    }


    public int findAndGetDevices() {
        if (fpdService == null) {
            noDetectorLoaded();
            return -1;
        }
        int count = fpdService.findAndGetDevices();
        if (count <= 0) {
            logger.error("No devices found");
        } else {
            logger.info("Found {} devices", count);
        }
        return count;

    }

    public boolean connect() {
        if (fpdService == null) {
            noDetectorLoaded();
            return false;
        }
        return fpdService.connect();
    }


    public boolean isConnected() {
        if (fpdService == null) {
            noDetectorLoaded();
            return false;
        }
        return fpdService.isConnected();
    }


    public FpsStatus getFpdStatus() {
        if (fpdService == null) {
            noDetectorLoaded();
            return null;
        }
       return fpdService.getFpdStatus();
    }


    public boolean cancelOperationMode() {
        if (fpdService == null) {
            noDetectorLoaded();
            return false;
        }

        return fpdService.cancelOperationMode();
    }


    public boolean triggerAedOneShot() {
        if (fpdService == null) {
            noDetectorLoaded();
            return false;
        }

        return fpdService.triggerAedOneShot();
    }


    public void getFpdImageToFile(String imagePath) {
        if (fpdService == null) {
            noDetectorLoaded();
            return;
        }


        if (!fpdService.isConnected()) {
            logger.error("Detector is not connected");
            return;
        }

        // Call the method to get the image to file
        fpdService.getFpdImageToFile(imagePath);
    }


    public void noDetectorLoaded() {
        logger.error("No detector loaded");
        fpdService = null;
        detectorDetails = null;
    }


    public void getCorrectedImageToFile(String imagePath) {
        if (fpdService == null) {
            noDetectorLoaded();
            return;
        }

        // Call the method to get the corrected image to file
        fpdService.getCorrectedImageToFile(imagePath);
    }

    public String getBatteryStatus() {
        if (fpdService == null) {
            noDetectorLoaded();
            return "0";
        }

        // Call the method to get the battery status
        String batteryLevel = fpdService.getBatteryStatus();
        logger.info("Battery Status: {}", batteryLevel);
        return batteryLevel;
    }

    public String signalStrength() {
        if (fpdService == null) {
            noDetectorLoaded();
            return "0";
        }

        // Call the method to get the signal strength
        String signalStrength = fpdService.getSignalStrength();
        logger.info("Signal Strength: {}", signalStrength);
        return signalStrength;
    }



}
