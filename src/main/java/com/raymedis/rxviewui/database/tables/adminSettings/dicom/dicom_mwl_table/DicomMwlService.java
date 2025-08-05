package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mwl_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomMwlService {

    private static final DicomMwlService instance = new DicomMwlService();
    public static DicomMwlService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomMwlEntity, Integer> dicomMwlDao;

    public DicomMwlService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("name", "VARCHAR(255)");
        columns.put("aeTitle", "VARCHAR(255)");
        columns.put("ipAddress", "VARCHAR(255)");
        columns.put("port", "INT");
        columns.put("maxPdu", "INT");
        columns.put("verificationTimeout", "INT");
        columns.put("codeMapping", "TEXT");
        columns.put("refreshCycle", "VARCHAR(255)");
        columns.put("state", "VARCHAR(255)");
        columns.put("examDateTime", "DATETIME");
        columns.put("patientName", "VARCHAR(255)");
        columns.put("patientId", "VARCHAR(255)");
        columns.put("isSelected","BIT NOT NULL");

        dicomMwlDao = new GenericRepository<>("dicomMwlEntity",columns,"id",new DicomMwlRowMapper(),true,1);
    }

    public void save(DicomMwlEntity dicomMwlEntity) throws SQLException {
        dicomMwlDao.save(dicomMwlEntity);
    }

    public void update(int id,DicomMwlEntity dicomMwlEntity) throws SQLException {
        dicomMwlDao.update(id,dicomMwlEntity);
    }

    public void delete(int id) throws SQLException {
        dicomMwlDao.deleteByPrimaryKey(id);
    }

    private ArrayList<DicomMwlEntity> mwlList;
    public ArrayList<DicomMwlEntity> findAll() throws SQLException {
        mwlList = (ArrayList<DicomMwlEntity>) dicomMwlDao.findAll();
        return mwlList;
    }


    public void setAllItemsSelectionToFalse(int id) throws SQLException {
        for (DicomMwlEntity dicomMwlEntity:mwlList){
            dicomMwlEntity.setIsSelected(dicomMwlEntity.getId() == id);
            update(dicomMwlEntity.getId(),dicomMwlEntity);
        }
    }

    public DicomMwlEntity findSelectedMwl() throws SQLException {
        for(DicomMwlEntity dicomMwlEntity : dicomMwlDao.findAll()){
            if(dicomMwlEntity.getIsSelected()){
                return dicomMwlEntity;
            }
        }
        return null;
    }

}
