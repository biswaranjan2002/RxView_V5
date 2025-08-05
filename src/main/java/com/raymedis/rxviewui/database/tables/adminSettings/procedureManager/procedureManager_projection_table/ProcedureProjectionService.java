package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_projection_table;

import com.raymedis.rxviewui.database.GenericRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcedureProjectionService {

    private final static ProcedureProjectionService instance = new ProcedureProjectionService();
    public static ProcedureProjectionService getInstance(){
        return instance;
    }

    private GenericRepository<ProcedureProjectionEntity, Integer> procedureProjectionDao;

    public ProcedureProjectionService(){
        Map<String, String> columns = new HashMap<>();

        columns.put("meaning", "VARCHAR(255)");
        columns.put("alias", "VARCHAR(255)");
        columns.put("hide", "BIT");
        columns.put("codeValue", "VARCHAR(255)");
        columns.put("designator", "VARCHAR(255)");
        columns.put("version", "VARCHAR(255)");
        columns.put("viewPosition", "VARCHAR(255)");

        procedureProjectionDao = new GenericRepository<>("procedureProjectionEntity", columns, "id", new ProcedureProjectionRowMapper(), true, 1);
    }

    public void save(ProcedureProjectionEntity procedureBodyPart) throws SQLException {
        procedureProjectionDao.save(procedureBodyPart);
    }

    public void delete(int id) throws SQLException {
        procedureProjectionDao.deleteByPrimaryKey(id);
    }

    public void update(int id,ProcedureProjectionEntity procedureBodyPart) throws SQLException {
        procedureProjectionDao.update(id,procedureBodyPart);
    }


    public ArrayList<ProcedureProjectionEntity> findAll() throws SQLException {
        return (ArrayList<ProcedureProjectionEntity>) procedureProjectionDao.findAll();
    }



}
