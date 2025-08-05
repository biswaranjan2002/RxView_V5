package com.raymedis.rxviewui.database.tables.adminSettings.backup_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupService {

    public static BackupService instance =new BackupService();
    public static BackupService getInstance(){
        return instance;
    }

    private final BackupDao backupDao;

    public BackupService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("backupPath", "VARCHAR(255) NOT NULL");
        backupDao = new BackupDao("BackupEntity",columns,"StudyId", new BackupRowMapper(),false, "");
    }

    public void save(BackupEntity backupEntity) throws SQLException {
        backupDao.save(backupEntity);
    }

    public void delete(String studyId) throws SQLException {
        backupDao.deleteByPrimaryKey(studyId);
    }

    public List<BackupEntity> findAll() throws SQLException {
        return backupDao.findAll();
    }

    public ArrayList<BackupEntity> findByStudyId(String studyId){
        return backupDao.findByStudyId(studyId);
    }


}
