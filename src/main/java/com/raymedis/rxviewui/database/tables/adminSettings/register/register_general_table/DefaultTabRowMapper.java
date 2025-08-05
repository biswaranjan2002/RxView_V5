package com.raymedis.rxviewui.database.tables.adminSettings.register.register_general_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultTabRowMapper implements GenericRepository.RowMapper<DefaultTabEntity, Integer>{

    @Override
    public DefaultTabEntity mapRow(ResultSet rs) throws SQLException {
        DefaultTabEntity defaultTab = new DefaultTabEntity();

        defaultTab.setId(rs.getInt("id"));
        defaultTab.setManual(rs.getBoolean("isManual"));
        defaultTab.setWorkList(rs.getBoolean("isWorkList"));
        defaultTab.setLocalList(rs.getBoolean("isLocalList"));

        return defaultTab;
    }

    @Override
    public Object getValue(DefaultTabEntity entity, String columnName) {
        return switch (columnName) {
            case "isManual" -> entity.getManual();
            case "isWorkList" -> entity.getWorkList();
            case "isLocalList" -> entity.getLocalList();
            default -> null;
        };
    }

    @Override
    public Object getPrimaryKeyValue(DefaultTabEntity entity, String primaryKeyName) {
        return entity.getId();
    }


}
