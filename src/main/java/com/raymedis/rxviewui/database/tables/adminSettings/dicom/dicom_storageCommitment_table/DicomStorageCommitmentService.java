package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_storageCommitment_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomStorageCommitmentService {

    private static DicomStorageCommitmentService instance = new DicomStorageCommitmentService();
    public static DicomStorageCommitmentService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomStorageCommitmentEntity,Integer> dicomStorageCommitmentDao;

    public DicomStorageCommitmentService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("name", "VARCHAR(255)");
        columns.put("aeTitle", "VARCHAR(255)");
        columns.put("ipAddress", "VARCHAR(255)");
        columns.put("port", "INT");
        columns.put("maxPdu", "INT");
        columns.put("verificationTimeout", "INT");
        columns.put("isSelected", "BIT NOT NULL");

        dicomStorageCommitmentDao = new GenericRepository<>("dicomStorageCommitmentEntity", columns, "id", new DicomStorageCommitmentRowMapper(), true, 1);
    }

    public void save(DicomStorageCommitmentEntity dicomStorageCommitmentEntity) throws SQLException {
        dicomStorageCommitmentDao.save(dicomStorageCommitmentEntity);
    }

    public void delete(int id) throws SQLException {
        dicomStorageCommitmentDao.deleteByPrimaryKey(id);
    }

    public void update(int id,DicomStorageCommitmentEntity dicomStorageCommitmentEntity) throws SQLException {
        dicomStorageCommitmentDao.update(id,dicomStorageCommitmentEntity);
    }

    private ArrayList<DicomStorageCommitmentEntity> storageCommitmentList;
    public ArrayList<DicomStorageCommitmentEntity> findAll() throws SQLException {
        storageCommitmentList = (ArrayList<DicomStorageCommitmentEntity>) dicomStorageCommitmentDao.findAll();
        return storageCommitmentList;
    }

    public void setAllItemsSelectionToFalse(int id) throws SQLException {
        for (DicomStorageCommitmentEntity dicomStorageCommitmentEntity:storageCommitmentList){
            dicomStorageCommitmentEntity.setIsSelected(dicomStorageCommitmentEntity.getId() == id);
            update(dicomStorageCommitmentEntity.getId(),dicomStorageCommitmentEntity);
        }
    }




}
