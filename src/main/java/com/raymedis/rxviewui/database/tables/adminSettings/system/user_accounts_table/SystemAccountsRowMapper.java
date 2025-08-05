package com.raymedis.rxviewui.database.tables.adminSettings.system.user_accounts_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemAccountsRowMapper implements GenericRepository.RowMapper<SystemAccountsEntity, String> {
    @Override
    public SystemAccountsEntity mapRow(ResultSet rs) throws SQLException {
        return new SystemAccountsEntity(
                rs.getString("userName"),
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("userGroup")
        );
    }

    @Override
    public Object getValue(SystemAccountsEntity entity, String columnName) {
        return switch (columnName) {
            case "userName" -> entity.getUserName();
            case "password" -> entity.getPassword();
            case "userGroup" -> entity.getUserGroup();
            default -> throw new IllegalArgumentException("Unknown column: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(SystemAccountsEntity entity, String primaryKeyName) {
        return entity.getUserId();
    }
}
