package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.network.MultipleInstanceTransferStatusHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class OurTransferUpdateStatusHandler extends MultipleInstanceTransferStatusHandler {

    private Logger logger = LoggerFactory.getLogger(OurTransferUpdateStatusHandler.class);

    public static boolean isTransferComplete = true;

    public static ArrayList<String> statusMap = new ArrayList<>();

    public static int failCount = 0;

    @Override
    public void updateStatus(int nRemaining,
                             int nCompleted,
                             int nFailed,
                             int nWarning,
                             String sopInstanceUID) {

        logger.info("SOP Instance ID being transferred:{}", sopInstanceUID);
        logger.info("Items in Queue:{}", nRemaining);
        logger.info("Transfers Completed:{}", nCompleted);
        logger.info("Failed Transmits:{}", nFailed);
        logger.info("Warnings:{}", nWarning);


        if(nRemaining == 0){
            isTransferComplete = true;
            DicomService.isStoreComplete();
        }

        if(failCount!=nFailed){
            failCount = nFailed;
            statusMap.add(sopInstanceUID);
        }


        logger.info("isTransferComplete: {}", isTransferComplete);

    }





}
