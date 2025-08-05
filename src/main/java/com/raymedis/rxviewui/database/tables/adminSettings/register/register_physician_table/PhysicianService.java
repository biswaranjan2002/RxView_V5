package com.raymedis.rxviewui.database.tables.adminSettings.register.register_physician_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhysicianService {

    private final static PhysicianService instance = new PhysicianService();

    public static PhysicianService getInstance(){
        return instance;
    }

    private final GenericRepository<PhysicianEntity, String> physicianDao;


    public PhysicianService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("physicianName", "VARCHAR(100) NOT NULL");
        columns.put("physicianGroup", "VARCHAR(100) NOT NULL");

        physicianDao = new GenericRepository<>("PhysiciansEntity", columns, "physicianId", new PhysicianRowMapper(), false,"");
    }

    public void save(PhysicianEntity physician) throws SQLException {
        physicianDao.save(physician);
    }

    public void delete(String id) throws SQLException {
        physicianDao.deleteByPrimaryKey(id);
    }

    public void update(String id,PhysicianEntity physician) throws SQLException {
        physicianDao.update(id,physician);
    }

    public ArrayList<PhysicianEntity> findAll() throws SQLException {
        return (ArrayList<PhysicianEntity>) physicianDao.findAll();
    }



}
