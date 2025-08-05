package com.raymedis.rxviewui.database.tables.adminSettings.system.system_Info_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemInfoRowMapper implements GenericRepository.RowMapper<SystemInfoEntity, Integer> {

    @Override
    public SystemInfoEntity mapRow(ResultSet rs) throws SQLException {
        // Map ResultSet data to a SystemInfoEntity object
        return new SystemInfoEntity(
                rs.getInt("id"),
                rs.getString("institutionName"),
                rs.getString("institutionAddress"),
                rs.getString("department"),
                rs.getString("manufacturer"),
                rs.getString("modelName"),
                rs.getString("telephone"),
                rs.getString("email"),
                rs.getString("homePage"),
                rs.getString("softwareVersion"),
                rs.getString("serialNumber"),
                rs.getString("language")
        );
    }

    @Override
    public Object getValue(SystemInfoEntity entity, String columnName) {
        // Return the value of the specified column from the entity
        return switch (columnName) {
            case "institutionName" -> entity.getInstitutionName();
            case "institutionAddress" -> entity.getInstitutionAddress();
            case "department" -> entity.getDepartment();
            case "manufacturer" -> entity.getManufacturer();
            case "modelName" -> entity.getModelName();
            case "telephone" -> entity.getTelephone();
            case "email" -> entity.getEmail();
            case "homePage" -> entity.getHomePage();
            case "softwareVersion" -> entity.getSoftwareVersion();
            case "serialNumber" -> entity.getSerialNumber();
            case "language" -> entity.getLanguage();
            default -> throw new IllegalArgumentException("Unknown column: " + columnName);
        };
    }

    @Override
    public Object getPrimaryKeyValue(SystemInfoEntity entity, String primaryKeyName) {
        return entity.getId();
    }
}