package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.network.VerificationSOPClassSCU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DicomCEcho {
    private String remoteEntityHostName; //their hostname or IP address
    private int remoteEntityPort; //the port their entity is listening on
    private String calledAETitle; //their Application Entity Title
    private String callingAETitle; //our Application Entity Title


    public DicomCEcho(String remoteEntityHostName, int remoteEntityPort, String calledAETitle, String callingAETitle) {
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

    private Logger logger = LoggerFactory.getLogger(DicomCEcho.class);

    public boolean verify(){

        try{
            boolean secureTransport = false;
            logger.info("\nInitiating C-ECHO operation...");

            VerificationSOPClassSCU echo = new VerificationSOPClassSCU(remoteEntityHostName,
                    remoteEntityPort,
                    calledAETitle,
                    callingAETitle,
                    secureTransport,4);

            String val = echo == null ? "not " : "";
            logger.error("VerificationSOPClass: was {} successful", val);

            return true;
        }
        catch (Exception e){
            e.printStackTrace(System.err);
            return false;
        }
    }
}
