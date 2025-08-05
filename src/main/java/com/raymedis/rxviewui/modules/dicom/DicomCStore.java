package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.dicom.SetOfDicomFiles;
import com.pixelmed.network.StorageSOPClassSCU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class DicomCStore {

    private String remoteEntityHostName; //their hostname or IP address
    private int remoteEntityPort; //the port their entity is listening on
    private String calledAETitle; //their Application Entity Title
    private String callingAETitle; //our Application Entity Title

    private Logger logger = LoggerFactory.getLogger(DicomCStore.class);

    public DicomCStore(String remoteEntityHostName, int remoteEntityPort, String calledAETitle, String callingAETitle) {
        this.remoteEntityHostName = remoteEntityHostName;
        this.remoteEntityPort = remoteEntityPort;
        this.calledAETitle = calledAETitle;
        this.callingAETitle = callingAETitle;
    }

    public String getRemoteEntityHostName() {
        return remoteEntityHostName;
    }

    public void setRemoteEntityHostName(String remoteEntityHostName) {
        this.remoteEntityHostName = remoteEntityHostName;
    }

    public int getRemoteEntityPort() {
        return remoteEntityPort;
    }

    public void setRemoteEntityPort(int remoteEntityPort) {
        this.remoteEntityPort = remoteEntityPort;
    }

    public String getCalledAETitle() {
        return calledAETitle;
    }

    public void setCalledAETitle(String calledAETitle) {
        this.calledAETitle = calledAETitle;
    }

    public String getCallingAETitle() {
        return callingAETitle;
    }

    public void setCallingAETitle(String callingAETitle) {
        this.callingAETitle = callingAETitle;
    }

    public boolean store(String... dicomFilePaths){

        if(!OurTransferUpdateStatusHandler.isTransferComplete){
            return false;
        }

        try{
            SetOfDicomFiles dicomFilesToTransmit = new SetOfDicomFiles();
            Arrays.stream(dicomFilePaths).forEach(dicomFilesToTransmit::add);
            logger.info("\nInitiating C-STORE operation...");

            OurTransferUpdateStatusHandler.isTransferComplete = false;


            StorageSOPClassSCU storage = new StorageSOPClassSCU(remoteEntityHostName,
                    remoteEntityPort,
                    calledAETitle,
                    callingAETitle,
                    dicomFilesToTransmit,
                    0, //compression level
                    new OurTransferUpdateStatusHandler());

            return true;
        }
        catch (Exception e){
            logger.error("error {}",e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }
}

