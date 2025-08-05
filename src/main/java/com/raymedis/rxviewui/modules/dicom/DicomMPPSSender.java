package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.dicom.*;

import java.util.Collections;
import java.util.Date;

public class DicomMPPSSender {

    private String remoteEntityHostName;
    private int remoteEntityPort;
    private String calledAETitle;
    private String callingAETitle;

    public DicomMPPSSender(String remoteEntityHostName, int remoteEntityPort, String calledAETitle, String callingAETitle) {
        this.remoteEntityHostName = remoteEntityHostName;
        this.remoteEntityPort = remoteEntityPort;
        this.calledAETitle = calledAETitle;
        this.callingAETitle = callingAETitle;
    }



    public void sendMPPS(String patientName, String patientID, String modality, String studyID,String status) throws Exception {

        //Create AttributeList
        AttributeList attributeList = new AttributeList();
        String studyInstanceUID = "";
        try {
            SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet(Collections.singleton("ISO_IR 100"));

            attributeList.putNewAttribute(TagFromName.PatientName, specificCharacterSet).addValue(patientName);
            attributeList.put(new LongStringAttribute(TagFromName.PatientID)).addValue(patientID);

            DateAttribute performedProcedureStepStartDate = new DateAttribute(TagFromName.PerformedProcedureStepStartDate);
            performedProcedureStepStartDate.addValue(String.valueOf(new Date()));
            attributeList.put(performedProcedureStepStartDate);

            TimeAttribute performedProcedureStepStartTime = new TimeAttribute(TagFromName.PerformedProcedureStepStartTime);
            performedProcedureStepStartTime.addValue(String.valueOf(new Date()));
            attributeList.put(performedProcedureStepStartTime);

            attributeList.put(new CodeStringAttribute(TagFromName.Modality)).addValue(modality);
            attributeList.put(new ShortStringAttribute(TagFromName.StudyID)).addValue(studyID);


            studyInstanceUID = new UIDGenerator().getNewUID();
            attributeList.put(new UniqueIdentifierAttribute(TagFromName.StudyInstanceUID)).addValue(studyInstanceUID);

            attributeList.put(new CodeStringAttribute(TagFromName.Status)).addValue(status);

        } catch (DicomException e) {
            e.printStackTrace();
        }

        // CREATE message
        //MPPSSCU mppsSCU = new MPPSSCU(remoteEntityHostName, remoteEntityPort, calledAETitle, callingAETitle, studyInstanceUID, attributeList);
        //boolean success = mppsSCU.sendNCreateMessage();


        // Send Message
//        if (success) {
//            success = mppsSCU.sendNSetMessage(studyInstanceUID, mppsNSetAttributes);
//        }
//
//        if (!success) {
//            throw new Exception("Failed to send MPPS message");
//        }
    }
}