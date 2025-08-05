package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrintOverlayItemsService {

    private final static PrintOverlayItemsService instance = new PrintOverlayItemsService();
    public static PrintOverlayItemsService getInstance(){
        return instance;
    }

    private final PrintOverlayItemsDao printOverlayItemsDao;


    public PrintOverlayItemsService() {
        Map<String, String> columns = new HashMap<>();
        columns.put("item", "VARCHAR(255)");

        printOverlayItemsDao = new PrintOverlayItemsDao("printOverlayItemsEntity", columns, "id", new PrintOverlayItemsRowMapper(), true, 1);
    }

    public ArrayList<PrintOverlayItemsEntity> findAll() throws SQLException {
        return (ArrayList<PrintOverlayItemsEntity>) printOverlayItemsDao.findAll();
    }


    public PrintOverlayItemsEntity findByItem(String item) {
        return printOverlayItemsDao.findByItem(item);
    }
}
