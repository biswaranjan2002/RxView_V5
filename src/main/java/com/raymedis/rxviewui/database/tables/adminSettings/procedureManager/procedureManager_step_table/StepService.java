package com.raymedis.rxviewui.database.tables.adminSettings.procedureManager.procedureManager_step_table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StepService {
    private final static StepService instance = new StepService();
    public static StepService getInstance(){
        return instance;
    }

    private StepDao stepDao;

    public StepService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("stepName", "VARCHAR(255)");
        columns.put("patientSize", "VARCHAR(255)");
        columns.put("text", "VARCHAR(255)");
        columns.put("textPos", "VARCHAR(255)");
        columns.put("generatorP1", "VARCHAR(255)");
        columns.put("generatorP2", "VARCHAR(255)");
        columns.put("lrLabel", "VARCHAR(255)");


        columns.put("autoCrop", "BIT");
        columns.put("flip", "BIT");
        columns.put("mirror", "BIT");
        columns.put("rotation", "BIT");
        columns.put("imageRotation", "BIT");
        columns.put("consoleAec", "BIT");
        columns.put("consoleAecLeft", "BIT");
        columns.put("consoleAecRight", "BIT");
        columns.put("consoleAecCenter", "BIT");

        columns.put("detectorNo", "INTEGER");
        columns.put("consoleDen", "INTEGER");
        columns.put("imageProcType", "INTEGER");
        columns.put("imageParamType", "INTEGER");
        columns.put("procedureId","INTEGER");

        columns.put("imageProcessParamKey", "DOUBLE");
        columns.put("lrPos", "DOUBLE");
        columns.put("consoleKv", "DOUBLE");
        columns.put("consoleMas", "DOUBLE");
        columns.put("consoleMa", "DOUBLE");
        columns.put("consoleMs", "DOUBLE");
        columns.put("sid", "DOUBLE");
        columns.put("armAngle", "DOUBLE");
        columns.put("detectorAngle", "DOUBLE");
        columns.put("tubeTiltAngle", "DOUBLE");
        columns.put("tubeRotateAngle", "DOUBLE");
        columns.put("ceilingPosition", "DOUBLE");
        columns.put("detectorArmPosition", "DOUBLE");
        columns.put("tubeArmPosition", "DOUBLE");

        stepDao = new StepDao("stepEntity", columns, "stepId", new StepRowMapper(), true, 1);
    }

    public void save(StepEntity stepEntity) throws SQLException {
        stepDao.save(stepEntity);
    }

    public void delete(int id) throws SQLException {
        stepDao.deleteByPrimaryKey(id);
    }

    public void deleteByProjection(String projection){
        stepDao.deleteByProjection(projection);
    }

    public void update(int id,StepEntity stepEntity) throws SQLException {
        stepDao.update(id,stepEntity);
    }


    public ArrayList<StepEntity> findAll() throws SQLException {
        return (ArrayList<StepEntity>) stepDao.findAll();
    }


    public ArrayList<StepEntity> findByStepName(String searchText){
        return stepDao.findByStepName(searchText);
    }

    public ArrayList<StepEntity> findByProcedureId(int selectedProcedureId) {
        return stepDao.findByProcedureId(selectedProcedureId);
    }

    public ArrayList<StepEntity> findStepByPatientSize(String patientSize, String selectedBodyPart) {
        return stepDao.findStepByPatientSize(patientSize,selectedBodyPart);
    }
}
