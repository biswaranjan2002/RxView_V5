package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintOverlayItemsRowMapper implements GenericRepository.RowMapper<PrintOverlayItemsEntity, Integer> {
    private static PrintOverlayItemsRowMapper instance = new PrintOverlayItemsRowMapper();
    public static PrintOverlayItemsRowMapper getInstance() {
        return instance;
    }

    @Override
    public PrintOverlayItemsEntity mapRow(ResultSet rs) throws SQLException {
        PrintOverlayItemsEntity entity = new PrintOverlayItemsEntity();
        entity.setId(rs.getInt("id"));
        entity.setItem(rs.getString("item"));
        return entity;
    }

    @Override
    public Object getValue(PrintOverlayItemsEntity entity, String columnName) {
        if (columnName.equals("item")) {
            return entity.getItem();
        } else {
            return null;
        }
    }


    @Override
    public Object getPrimaryKeyValue(PrintOverlayItemsEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
