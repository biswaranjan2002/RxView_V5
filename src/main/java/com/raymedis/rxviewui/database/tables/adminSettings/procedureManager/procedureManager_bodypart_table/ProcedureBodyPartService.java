package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_bodypart_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcedureBodyPartService {

    private final static ProcedureBodyPartService instance = new ProcedureBodyPartService();
    public static ProcedureBodyPartService getInstance(){
        return instance;
    }

    private final GenericRepository<ProcedureBodyPartEntity, Integer> procedureBodyPartDao;

    public ProcedureBodyPartService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("meaning", "VARCHAR(255)");
        columns.put("alias", "VARCHAR(255)");
        columns.put("hide", "BIT");
        columns.put("codeValue", "VARCHAR(255)");
        columns.put("designator", "VARCHAR(255)");
        columns.put("version", "VARCHAR(255)");
        columns.put("bodyPartExamination", "VARCHAR(255)");

        procedureBodyPartDao = new GenericRepository<>("ProcedureBodyPartsEntity", columns, "id", new ProcedureBodyPartRowMapper(), true, 1);
    }


    public void save(ProcedureBodyPartEntity procedureBodyPart) throws SQLException {
        procedureBodyPartDao.save(procedureBodyPart);
    }

    public void delete(int id) throws SQLException {
        procedureBodyPartDao.deleteByPrimaryKey(id);
    }

    public void update(int id,ProcedureBodyPartEntity procedureBodyPart) throws SQLException {
        procedureBodyPartDao.update(id,procedureBodyPart);
    }



    public ArrayList<ProcedureBodyPartEntity> findAll() throws SQLException {
        return (ArrayList<ProcedureBodyPartEntity>) procedureBodyPartDao.findAll();
    }
}
