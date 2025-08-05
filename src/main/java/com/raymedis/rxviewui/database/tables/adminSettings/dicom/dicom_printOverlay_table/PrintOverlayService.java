package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrintOverlayService {

    public static PrintOverlayService instance =new PrintOverlayService();
    public static PrintOverlayService getInstance(){
        return instance;
    }

    private final PrintOverlayDao printOverlayDao;

    public PrintOverlayService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("overlayId", "VARCHAR(255)");
        columns.put("overlayName", "VARCHAR(255)");
        columns.put("logoPath", "VARCHAR(255)");
        columns.put("positionType", "VARCHAR(50)");
        columns.put("itemContent", "VARCHAR(255)");
        columns.put("isSelected","BIT");

        printOverlayDao = new PrintOverlayDao("printOverlayEntity",columns,"id", new PrintOverlayRowMapper(),true, 1);
    }


    public void save(PrintOverlayEntity printOverlayEntity) throws SQLException {
        printOverlayDao.save(printOverlayEntity);
    }

    public void delete(int id) throws SQLException {
        printOverlayDao.deleteByPrimaryKey(id);
    }

    public void update(int id,PrintOverlayEntity printOverlayEntity) throws SQLException {
        printOverlayDao.update(id,printOverlayEntity);
    }

    public ArrayList<PrintOverlayEntity> findAll() throws SQLException {
        return (ArrayList<PrintOverlayEntity>) printOverlayDao.findAll();
    }

    public ArrayList<PrintOverlayEntity> findAllByOverlayId(String overlayId){
        return printOverlayDao.findAllByOverlayId(overlayId);
    }

    public void deleteAllByOverlayId(String overlayId) {
        printOverlayDao.deleteAllByOverlayId(overlayId);
    }

    public ArrayList<PrintOverlayEntity> findAllSelected(){
        return printOverlayDao.findAllSelected();
    }


}
