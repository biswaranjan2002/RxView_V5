package com.raymedis.rxviewui.modules.sdk;


import com.raymedis.rxviewui.modules.sdk.fpds.ISdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SdkService {

    private Logger logger = LoggerFactory.getLogger(SdkService.class);

    private ISdk firstDetector;
    private ISdk secondDetector;
    private ISdk thirdDetector;

    private ISdk selectedDetector;

    private static SdkService instance = new SdkService();

    public SdkService() {
    }

    public static SdkService getInstance(){
        return instance;
    }

    public ISdk getFirstDetector() {
        return firstDetector;
    }

    public void setFirstDetector(ISdk firstDetector) {
        this.firstDetector = firstDetector;
    }

    public ISdk getSecondDetector() {
        return secondDetector;
    }

    public void setSecondDetector(ISdk secondDetector) {
        this.secondDetector = secondDetector;
    }

    public ISdk getThirdDetector() {
        return thirdDetector;
    }

    public void setThirdDetector(ISdk thirdDetector) {
        this.thirdDetector = thirdDetector;
    }

    public ISdk getSelectedDetector() {
        return selectedDetector;
    }

    public ISdk selectDetector(int number){
        selectedDetector = null;
        if(number == 0){
            selectedDetector = firstDetector;
        }
        else if(number == 1){
            selectedDetector = secondDetector;
        }
        else if(number == 2){
            selectedDetector = thirdDetector;
        }
        else{
            logger.info("Please Enter either of 0,1 or 2.");
        }
        return selectedDetector;
    }
}
