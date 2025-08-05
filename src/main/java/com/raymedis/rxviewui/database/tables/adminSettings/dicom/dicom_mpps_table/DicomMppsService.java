package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_mpps_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomMppsService {

    private static final DicomMppsService instance = new DicomMppsService();
    public static DicomMppsService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomMppsEntity, Integer> dicomMppsDao;

    public DicomMppsService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("name", "VARCHAR(255)");
        columns.put("aeTitle", "VARCHAR(255)");
        columns.put("ipAddress", "VARCHAR(255)");
        columns.put("port", "INT");
        columns.put("maxPdu", "INT");
        columns.put("verificationTimeout", "INT");
        columns.put("language", "VARCHAR(255)");
        columns.put("isSelected","BIT NOT NULL");

        dicomMppsDao = new GenericRepository<>("dicomMppsEntity",columns,"id",new DicomMppsRowMapper(),true,1);
    }

    public void save(DicomMppsEntity dicomMppsEntity) throws SQLException {
        dicomMppsDao.save(dicomMppsEntity);
    }

    public void update(int id,DicomMppsEntity dicomMppsEntity) throws SQLException {
        dicomMppsDao.update(id,dicomMppsEntity);
    }

    public void delete(int id) throws SQLException {
        dicomMppsDao.deleteByPrimaryKey(id);
    }

    private ArrayList<DicomMppsEntity> mppsList;
    public ArrayList<DicomMppsEntity> findAll() throws SQLException {
        mppsList = (ArrayList<DicomMppsEntity>) dicomMppsDao.findAll();
        return mppsList;
    }


    public void setAllItemsSelectionToFalse(int id) throws SQLException {
        for (DicomMppsEntity dicomMppsEntity:mppsList){
            dicomMppsEntity.setIsSelected(dicomMppsEntity.getId() == id);
            update(dicomMppsEntity.getId(),dicomMppsEntity);
        }
    }


}
