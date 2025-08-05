package com.raymedis.rxviewui.modules.dicom;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.network.IdentifierHandler;

import java.util.ArrayList;

public class OurCustomFindIdentifierHandler extends IdentifierHandler {


    private static ArrayList<AttributeList> findResult  = new ArrayList<>();;

    public static ArrayList<AttributeList> getFindResult() {
        return findResult;
    }

    @Override
    public void doSomethingWithIdentifier(AttributeList attributeListForFindResult) throws DicomException {
        //System.out.println("Matched result:" + attributeListForFindResult);

//        String studyInstanceUID = attributeListForFindResult.get(TagFromName.StudyInstanceUID).getSingleStringValueOrEmptyString();
//        String patientID = attributeListForFindResult.get(TagFromName.PatientID).getSingleStringValueOrEmptyString();
//        String seriesInstanceUID = attributeListForFindResult.get(TagFromName.StudyInstanceUID).getSingleStringValueOrEmptyString();
//        String sOPInstanceUID = attributeListForFindResult.get(TagFromName.SOPInstanceUID).getSingleStringValueOrEmptyString();
//        System.out.println("PatientID:  " + patientID);
//        System.out.println("StudyInstanceUID:   " + studyInstanceUID);
//        System.out.println("SeriesInstanceUID:  " + seriesInstanceUID);
//        System.out.println("SOPInstanceUID:     " + sOPInstanceUID);

        //System.out.println(attributeListForFindResult.get(TagFromName.ScheduledProcedureStepDescription).getSingleStringValueOrEmptyString());


        //System.out.println("\n=================================================================");

        findResult.add((AttributeList) attributeListForFindResult.clone());

        //do other things you need to do with the matched results
    }

    public static void clearData(){
        findResult.clear();
    }

//    public static void printData(){
//        Collections.sort(data);
//        for(String d : data){
//            System.out.println(d);
//        }
//
//        System.out.println("\n Number of Study : " + data.size());
//    }


}
