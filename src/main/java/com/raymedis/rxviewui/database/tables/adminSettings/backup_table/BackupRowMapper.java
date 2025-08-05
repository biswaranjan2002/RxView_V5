package com.raymedis.rxviewui.database.tables.adminSettings.backup_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BackupRowMapper implements GenericRepository.RowMapper<BackupEntity, String>{

    private final static BackupRowMapper instance = new BackupRowMapper();
    public static BackupRowMapper getInstance() {
        return instance;
    }

    @Override
    public BackupEntity mapRow(ResultSet rs) throws SQLException {
        BackupEntity backupEntity = new BackupEntity();

        backupEntity.setStudyId(rs.getString("StudyId"));
        backupEntity.setBackupPath(rs.getString("backupPath"));

        return backupEntity;
    }

    @Override
    public Object getValue(BackupEntity entity, String columnName) {
        return switch (columnName) {
            case "backupPath" -> entity.getBackupPath();
            default -> {
                System.out.println("unknown column name");
                yield null;
            }
        };
    }

    @Override
    public Object getPrimaryKeyValue(BackupEntity entity, String primaryKeyName) {
        return entity.getStudyId();
    }
}
