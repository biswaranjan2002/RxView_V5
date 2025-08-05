package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_procedure_table;

import com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table.ProcedureDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcedureService {

    private final static ProcedureService instance = new ProcedureService();
    public static ProcedureService getInstance(){
        return instance;
    }

    private ProcedureDao procedureDao;

    public ProcedureService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("procedureCode", "VARCHAR(255)");
        columns.put("procedureName", "VARCHAR(255)");
        columns.put("procedureDescription", "VARCHAR(255)");

        procedureDao = new ProcedureDao("procedureEntity",columns,"procedureId",new ProcedureRowMapper(),true,1);
    }

    public void save(ProcedureEntity procedureEntity) throws SQLException {
        procedureDao.save(procedureEntity);
    }

    public ArrayList<ProcedureEntity> findAll() throws SQLException {
        return (ArrayList<ProcedureEntity>) procedureDao.findAll();
    }

    public void update(int procedureId,ProcedureEntity procedureEntity) throws SQLException {
        procedureDao.update(procedureId,procedureEntity);
    }

    public void delete(int procedureId) throws SQLException {
        procedureDao.deleteByPrimaryKey(procedureId);
    }



}
