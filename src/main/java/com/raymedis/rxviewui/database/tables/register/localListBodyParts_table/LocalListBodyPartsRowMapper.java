package com.raymedis.rxviewui.database.tables.register.localListBodyParts_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalListBodyPartsRowMapper implements GenericRepository.RowMapper<LocalListBodyPartsEntity,Integer> {


    private static LocalListBodyPartsRowMapper instance = new LocalListBodyPartsRowMapper();
    public static LocalListBodyPartsRowMapper getInstance() {
        return instance;
    }

    @Override
    public LocalListBodyPartsEntity mapRow(ResultSet rs) throws SQLException {
        LocalListBodyPartsEntity localListBodyPartsEntity = new LocalListBodyPartsEntity();
        localListBodyPartsEntity.setId(rs.getInt("id"));
        localListBodyPartsEntity.setBodyPartName(rs.getString("bodyPartName"));
        localListBodyPartsEntity.setPosition(rs.getString("position"));
        localListBodyPartsEntity.setLocalListId(rs.getInt("localListId"));
        return localListBodyPartsEntity;
    }

    @Override
    public Object getValue(LocalListBodyPartsEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "bodyPartName" -> entity.getBodyPartName();
            case "position" -> entity.getPosition();
            case "localListId" -> entity.getLocalListId();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(LocalListBodyPartsEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}
