package com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table;

import com.raymedis.rxviewui.database.GenericRepository;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhysicianRowMapper implements GenericRepository.RowMapper<PhysicianEntity, String>{


    @Override
    public PhysicianEntity mapRow(ResultSet rs) throws SQLException {
        PhysicianEntity physicianEntity = new PhysicianEntity();

        physicianEntity.setPhysicianId(rs.getString("physicianId"));
        physicianEntity.setPhysicianName(rs.getString("physicianName"));
        physicianEntity.setPhysicianGroup(rs.getString("physicianGroup"));

        return physicianEntity;
    }


    @Override
    public Object getValue(PhysicianEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "physicianName" -> entity.getPhysicianName();
            case "physicianGroup" -> entity.getPhysicianGroup();
            default -> throw new IllegalArgumentException("Invalid column name: " + columnName);
        };
    }


    @Override
    public Object getPrimaryKeyValue(PhysicianEntity entity, String primaryKeyName) {
        return entity.getPhysicianId();
    }
}
