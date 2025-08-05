package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printLayout_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintLayoutRowMapper implements GenericRepository.RowMapper<PrintLayoutEntity, Integer> {

    @Override
    public PrintLayoutEntity mapRow(ResultSet rs) throws SQLException {
        PrintLayoutEntity printLayoutEntity = new PrintLayoutEntity();
        printLayoutEntity.setId(rs.getInt("id"));
        printLayoutEntity.setLayoutName(rs.getString("layoutName"));
        return printLayoutEntity;
    }


    @Override
    public Object getValue(PrintLayoutEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "layoutName" -> entity.getLayoutName();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(PrintLayoutEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
