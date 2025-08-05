package com.raymedis.rxviewui.database.tables.adminSettings.study.study_rejectedReason_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RejectedReasonService {

    private final static RejectedReasonService instance = new RejectedReasonService();

    public static RejectedReasonService getInstance(){
        return instance;
    }
    private final GenericRepository<RejectedReasonEntity, String> rejectedReasonDao;

    public RejectedReasonService(){
        Map<String, String> columns = new HashMap<>();
        columns.put("id","INT");
        rejectedReasonDao = new GenericRepository<>("RejectedReasonEntity",columns,"RejectedReason",new RejectedReasonRowMapper(),false,"");
    }

    public void save(RejectedReasonEntity rejectedReasonEntity) throws SQLException {
        rejectedReasonDao.save(rejectedReasonEntity);
    }

    public void delete(String reason) throws SQLException {
        rejectedReasonDao.deleteByPrimaryKey(reason);
    }

    public void update(String reason,RejectedReasonEntity rejectedReasonEntity) throws SQLException {
        rejectedReasonDao.update(reason,rejectedReasonEntity);
    }

    public ArrayList<RejectedReasonEntity> findAll() throws SQLException {
        return (ArrayList<RejectedReasonEntity>) rejectedReasonDao.findAll();
    }

}
