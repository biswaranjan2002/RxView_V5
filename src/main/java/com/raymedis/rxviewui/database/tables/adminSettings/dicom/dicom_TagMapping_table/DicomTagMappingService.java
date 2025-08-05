package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_TagMapping_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomTagMappingService {

    private static  DicomTagMappingService instance = new DicomTagMappingService();
    public static DicomTagMappingService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomTagMappingEntity,Integer> dicomTagMappingDao;

    public DicomTagMappingService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("originalTagNo", "VARCHAR(255)");
        columns.put("inputTagNo", "VARCHAR(255)");
        columns.put("outputTagNo", "VARCHAR(255)");
        columns.put("originalTagName", "VARCHAR(255)");
        columns.put("inputTagName", "VARCHAR(255)");
        columns.put("outputTagName", "VARCHAR(255)");

        dicomTagMappingDao = new GenericRepository<>("dicomTagMappingEntity",columns,"id",new DicomTagMappingRowMapper(),true,1);
    }

    public void save(DicomTagMappingEntity dicomTagMappingEntity) throws SQLException {
        dicomTagMappingDao.save(dicomTagMappingEntity);
    }

    public void update(int id,DicomTagMappingEntity dicomTagMappingEntity) throws SQLException {
        dicomTagMappingDao.update(id,dicomTagMappingEntity);
    }

    public void delete(int id) throws SQLException {
        dicomTagMappingDao.deleteByPrimaryKey(id);
    }

    public ArrayList<DicomTagMappingEntity> findAll() throws SQLException {
        return (ArrayList<DicomTagMappingEntity>) dicomTagMappingDao.findAll();
    }


}
