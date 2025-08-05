package com.raymedis.rxviewui.modules.dicom;

import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralEntity;
import com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table.DicomGeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class DicomTest {


    private static final Logger logger = LoggerFactory.getLogger(DicomTest.class);
    private static DicomGeneralService dicomGeneralService = DicomGeneralService.getInstance();
    private static ArrayList<DicomGeneralEntity> dicomGeneralList = new ArrayList<>();

    public static void main(String[] args){

        try {
            dicomGeneralList = dicomGeneralService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (dicomGeneralList != null && !dicomGeneralList.isEmpty()) {
            DicomGeneralEntity firstEntity = dicomGeneralList.getFirst();

            DicomMain dicomMain = new DicomMain(firstEntity.getStationName(), Integer.parseInt(firstEntity.getStationPort()), "Orthanc", firstEntity.getStationAETitle());
            boolean status = dicomMain.echo();
            logger.info("Echo : {}" , (status?"Success":"Fail"));
        }

    }



}
