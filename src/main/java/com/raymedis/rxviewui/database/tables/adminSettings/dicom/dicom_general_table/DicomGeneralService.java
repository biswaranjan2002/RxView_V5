package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_general_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomGeneralService {

    private final static DicomGeneralService instance = new DicomGeneralService();
    public static DicomGeneralService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomGeneralEntity, Integer> dicomGeneralDao;

    public DicomGeneralService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("stationName", "VARCHAR(255)");
        columns.put("stationAETitle", "VARCHAR(255)");
        columns.put("stationPort", "VARCHAR(255)");

        dicomGeneralDao = new GenericRepository<>("DicomGeneralEntity",columns,"id",new DicomGeneralRowMapper(),true,1);
    }


    public void save(DicomGeneralEntity dicomGeneralEntity) throws SQLException {
        dicomGeneralDao.save(dicomGeneralEntity);
    }

    public void update(int id,DicomGeneralEntity dicomGeneralEntity) throws SQLException {
        dicomGeneralDao.update(id,dicomGeneralEntity);
    }

    public void delete(int id) throws SQLException {
        dicomGeneralDao.deleteByPrimaryKey(id);
    }

    public ArrayList<DicomGeneralEntity> findAll() throws SQLException {
        return (ArrayList<DicomGeneralEntity>) dicomGeneralDao.findAll();
    }



}
