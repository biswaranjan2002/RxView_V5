package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.dicom.*;
import com.pixelmed.network.FindSOPClassSCU;

import java.util.ArrayList;
import java.util.List;

public class DicomCFind {

    private String remoteEntityHostName; //their hostname or IP address
    private int remoteEntityPort; //the port their entity is listening on
    private String calledAETitle; //their Application Entity Title
    private String callingAETitle; //our Application Entity Title

    private ArrayList<AttributeTag> allRetriveTags = new ArrayList<>();


    public DicomCFind(String remoteEntityHostName, int remoteEntityPort, String calledAETitle, String callingAETitle) {
        this.remoteEntityHostName = remoteEntityHostName;
        this.remoteEntityPort = remoteEntityPort;
        this.calledAETitle = calledAETitle;
        this.callingAETitle = callingAETitle;

        //add RequestedProcedureCode

        allRetriveTags.addAll(List.of(TagFromName.PatientName, TagFromName.PatientID, TagFromName.PatientBirthDate,
                TagFromName.PatientSex, TagFromName.ReferringPhysicianName, TagFromName.PatientWeight,
                TagFromName.AccessionNumber, TagFromName.StudyID, TagFromName.StudyDate,
                TagFromName.Modality, TagFromName.StudyDescription, TagFromName.ScheduledProcedureStepStartDate,
                TagFromName.ScheduledProcedureStepStartTime, TagFromName.RequestedProcedureID,
                TagFromName.RequestedProcedureDescription, TagFromName.ScheduledStationAETitle,
                TagFromName.ScheduledPerformingPhysicianName, TagFromName.ScheduledProcedureStepLocation,
                TagFromName.PreMedication, TagFromName.SpecialNeeds,
                TagFromName.SOPInstanceUID, TagFromName.SeriesInstanceUID, TagFromName.StudyInstanceUID, TagFromName.Status,TagFromName.ProcedureCodeSequence,TagFromName.BodyPartExamined,TagFromName.ViewPosition
        ));
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

    public ArrayList<AttributeList> find(String findType, String modality){
        return find(findType, modality, null, "");
    }

    public ArrayList<AttributeList> find(String findType, String modality, AttributeTag tag, String value){

        try{
            OurCustomFindIdentifierHandler.clearData();
            // use the default character set for VR encoding - override this as necessary
            SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet((String[])null);
            AttributeList identifier = new AttributeList();

            identifier.putNewAttribute(TagFromName.QueryRetrieveLevel).addValue(findType.toUpperCase());


            for(AttributeTag tags : allRetriveTags){
                if(tag!= null){
                    if(tags.equals(tag)){
                        identifier.putNewAttribute(tags).addValue(value);
                        continue;
                    }
                }
                if(tags.equals(TagFromName.Modality)){
                    identifier.putNewAttribute(tags).addValue(modality);
                }
                else{
                    identifier.putNewAttribute(tags);
                }
            }

            //System.out.println("\nInitiating C-FIND operation...");

            //System.out.println(identifier.toString());

            new FindSOPClassSCU(remoteEntityHostName,
                    remoteEntityPort,
                    calledAETitle,
                    callingAETitle,
                    SOPClass.StudyRootQueryRetrieveInformationModelFind,
                    identifier,
                    new OurCustomFindIdentifierHandler());

            return OurCustomFindIdentifierHandler.getFindResult();
        }
        catch (Exception e){
            e.printStackTrace(System.err);
            return null;
        }
    }
}