package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_print_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DicomPrintService {

    private static DicomPrintService instance =  new DicomPrintService();
    public static DicomPrintService getInstance(){
        return instance;
    }

    private final GenericRepository<DicomPrintEntity,Integer> printDao;

    public DicomPrintService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("name", "VARCHAR(255)");
        columns.put("aeTitle", "VARCHAR(255)");
        columns.put("ipAddress", "VARCHAR(255)");
        columns.put("port", "INT");
        columns.put("maxPdu", "INT");
        columns.put("timeout","INT");
        columns.put("verificationTimeout", "INT");
        columns.put("printLayout", "VARCHAR(255)");
        columns.put("isSelected", "BIT NOT NULL");

        printDao =  new GenericRepository<>("dicomPrintEntity", columns, "id", new DicomPrintRowMapper(), true, 1);

    }

    public void save(DicomPrintEntity dicomPrintEntity) throws SQLException {
        printDao.save(dicomPrintEntity);
    }

    public void update(int id, DicomPrintEntity dicomPrintEntity) throws SQLException {
        printDao.update(id,dicomPrintEntity);
    }

    public void delete(int id) throws SQLException {
        printDao.deleteByPrimaryKey(id);
    }

    private ArrayList<DicomPrintEntity> printList;
    public ArrayList<DicomPrintEntity> findAll() throws SQLException {
        printList = (ArrayList<DicomPrintEntity>) printDao.findAll();
        return printList;
    }

    public void setAllItemsSelectionToFalse(int id) throws SQLException {
        for (DicomPrintEntity dicomPrintEntity: printList){
            dicomPrintEntity.setIsSelected(dicomPrintEntity.getId() == id);
            update(dicomPrintEntity.getId(),dicomPrintEntity);
        }
    }

    public DicomPrintEntity findSelectedPrinter() throws SQLException {
        for(DicomPrintEntity dicomPrintEntity : printDao.findAll()){
            if(dicomPrintEntity.getIsSelected()){
                return dicomPrintEntity;
            }
        }
        return null;
    }


    public DicomPrintEntity findByAeTitle(String selectedAeTitle) throws SQLException {
        for(DicomPrintEntity dicomPrintEntity : printDao.findAll()){
            if(dicomPrintEntity.getAeTitle().equals(selectedAeTitle)){
                return dicomPrintEntity;
            }
        }
        return null;
    }
}
