package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storage_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomStorageService {

    private static DicomStorageService instance = new DicomStorageService();
    public static DicomStorageService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomStorageEntity,Integer> dicomStorageDao ;

    public DicomStorageService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("name", "VARCHAR(255)");
        columns.put("aeTitle", "VARCHAR(255)");
        columns.put("ipAddress", "VARCHAR(255)");
        columns.put("port", "INT");
        columns.put("maxPdu", "INT");
        columns.put("timeout", "INT");
        columns.put("verificationTimeout", "INT");
        columns.put("isSelected","BIT NOT NULL");
        columns.put("burnedInAnnotation","BIT");
        columns.put("burnedInInformation","BIT");
        columns.put("BurnedWithCrop","BIT");
        columns.put("lut","INT");
        columns.put("compression","INT");
        columns.put("transferSyntax","INT");
        columns.put("dapUnitType","INT");
        columns.put("softwareCollimation","INT");

        dicomStorageDao = new GenericRepository<>("dicomStorageEntity",columns,"id",new DicomStorageRowMapper(),true,1);
    }


    public void save(DicomStorageEntity dicomStorageEntity) throws SQLException {
        dicomStorageDao.save(dicomStorageEntity);
    }

    public void update(int id,DicomStorageEntity dicomStorageEntity) throws SQLException {
        dicomStorageDao.update(id,dicomStorageEntity);
    }

    public void delete(int id) throws SQLException {
        dicomStorageDao.deleteByPrimaryKey(id);
    }

    private ArrayList<DicomStorageEntity> storageList;
    public ArrayList<DicomStorageEntity> findAll() throws SQLException {
        storageList = (ArrayList<DicomStorageEntity>) dicomStorageDao.findAll();
        return storageList;
    }

    public DicomStorageEntity findSelected(){
        for(DicomStorageEntity dicomStorageEntity:storageList){
            if(dicomStorageEntity.getIsSelected()){
                return dicomStorageEntity;
            }
        }
        return null;
    }



    public void setAllItemsSelectionToFalse(int id) throws SQLException {
        for (DicomStorageEntity dicomStorageEntity:storageList){
            dicomStorageEntity.setIsSelected(dicomStorageEntity.getId() == id);
            update(dicomStorageEntity.getId(),dicomStorageEntity);
        }
    }



}
