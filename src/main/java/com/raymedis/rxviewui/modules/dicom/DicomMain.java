package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;

import java.util.ArrayList;

public class DicomMain {

    private String remoteEntityHostName; //their hostname or IP address
    private int remoteEntityPort; //the port their entity is listening on
    private String calledAETitle; //their Application Entity Title
    private String callingAETitle; //our Application Entity Title

    private DicomCEcho dicomCEcho;
    private DicomCFind dicomCFind;
    private DicomCStore dicomCStore;
    private DicomCreator dicomCreator;
    private DicomMPPSSender dicomMPPSSender;


    public DicomMain(String remoteEntityHostName, int remoteEntityPort, String calledAETitle, String callingAETitle) {
        this.remoteEntityHostName = remoteEntityHostName;
        this.remoteEntityPort = remoteEntityPort;
        this.calledAETitle = calledAETitle;
        this.callingAETitle = callingAETitle;

        this.dicomCEcho = new DicomCEcho(remoteEntityHostName, remoteEntityPort, calledAETitle, callingAETitle);
        this.dicomCFind = new DicomCFind(remoteEntityHostName, remoteEntityPort, calledAETitle, callingAETitle);
        this.dicomCStore = new DicomCStore(remoteEntityHostName, remoteEntityPort, calledAETitle, callingAETitle);
        this.dicomMPPSSender= new DicomMPPSSender(remoteEntityHostName, remoteEntityPort, calledAETitle, callingAETitle);
    }

    public String getRemoteEntityHostName() {
        return remoteEntityHostName;
    }

    public void setRemoteEntityHostName(String remoteEntityHostName) {
        this.remoteEntityHostName = remoteEntityHostName;
        this.dicomCEcho.setRemoteEntityHostName(remoteEntityHostName);
        this.dicomCFind.setRemoteEntityHostName(remoteEntityHostName);
        this.dicomCStore.setRemoteEntityHostName(remoteEntityHostName);
    }

    public int getRemoteEntityPort() {
        return remoteEntityPort;
    }

    public void setRemoteEntityPort(int remoteEntityPort) {
        this.remoteEntityPort = remoteEntityPort;
        this.dicomCEcho.setRemoteEntityPort(remoteEntityPort);
        this.dicomCFind.setRemoteEntityPort(remoteEntityPort);
        this.dicomCStore.setRemoteEntityPort(remoteEntityPort);
    }

    public String getCalledAETitle() {
        return calledAETitle;
    }

    public void setCalledAETitle(String calledAETitle) {
        this.calledAETitle = calledAETitle;
        this.dicomCEcho.setCalledAETitle(calledAETitle);
        this.dicomCFind.setCalledAETitle(calledAETitle);
        this.dicomCStore.setCalledAETitle(calledAETitle);
    }

    public String getCallingAETitle() {
        return callingAETitle;
    }

    public void setCallingAETitle(String callingAETitle) {
        this.callingAETitle = callingAETitle;
        this.dicomCEcho.setCallingAETitle(callingAETitle);
        this.dicomCFind.setCallingAETitle(callingAETitle);
        this.dicomCStore.setCallingAETitle(callingAETitle);
    }

    public boolean echo(){
        return dicomCEcho.verify();
    }

    public ArrayList<AttributeList> find(String findType, String modality){
        return find(findType, modality, null, "");
    }

    public ArrayList<AttributeList> find(String findType, String modality, AttributeTag tag, String value){
        return dicomCFind.find(findType, modality, tag, value);
    }

    public boolean store(String... dicomFilePaths){
        return dicomCStore.store(dicomFilePaths);
    }

    public void updateStatus(String patientName, String patientID, String modality, String studyID,String status) throws Exception {
        dicomMPPSSender.sendMPPS(patientName,patientID,modality,studyID,status);
    }

}
