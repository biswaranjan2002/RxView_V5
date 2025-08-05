package com.raymedis.rxviewui.database.tables.register.patientLocalList_table;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientLocalListService {

    private static PatientLocalListService instance = new PatientLocalListService();
    public static PatientLocalListService getInstance(){
        return instance;
    }

    private final PatientLocalListDao patientLocalListDao;

    public PatientLocalListService() {
        Map<String, String> columns = new HashMap<>();

        columns.put("accessionNumber", "VARCHAR(255)");
        columns.put("additionalPatientHistory", "VARCHAR(255)");
        columns.put("age", "INT");
        columns.put("admittingDiagnosisDescription", "VARCHAR(255)");
        columns.put("dob", "DATE");
        columns.put("height", "DOUBLE");
        columns.put("institution", "VARCHAR(255)");
        columns.put("modality", "VARCHAR(50)");
        columns.put("patientComments", "VARCHAR(255)");
        columns.put("patientId", "VARCHAR(255)");
        columns.put("patientInstituteResidence", "VARCHAR(255)");
        columns.put("patientName", "VARCHAR(255)");
        columns.put("patientSize", "VARCHAR(50)");
        columns.put("performingPhysician", "VARCHAR(255)");
        columns.put("readingPhysician", "VARCHAR(255)");
        columns.put("registerDateTime", "DATETIME");
        columns.put("referringPhysician", "VARCHAR(255)");
        columns.put("requestProcedurePriority", "VARCHAR(50)");
        columns.put("scheduledDateTime", "DATETIME");
        columns.put("sex", "VARCHAR(10)");
        columns.put("studyDescription", "VARCHAR(255)");
        columns.put("weight", "DOUBLE");
        columns.put("studyId","VARCHAR(50)");
        columns.put("studySessionId","VARCHAR(50)");  

        patientLocalListDao = new PatientLocalListDao(
                "patientLocalListEntity",
                columns,
                "id",
                new PatientLocalListRowMapper(),
                true,
                1
        );
    }

    public void save(PatientLocalListEntity patientLocalListEntity) throws SQLException {
        patientLocalListDao.save(patientLocalListEntity);
    }

    public void delete(int id) throws SQLException {
        patientLocalListDao.deleteByPrimaryKey(id);
    }

    public void update(int id, PatientLocalListEntity patientLocalListEntity) throws SQLException {
        patientLocalListDao.update(id, patientLocalListEntity);
    }


    public ArrayList<PatientLocalListEntity> findAll() throws SQLException {
        return (ArrayList<PatientLocalListEntity>) patientLocalListDao.findAll();
    }


    public ArrayList<PatientLocalListEntity> findLastDay() {
        return patientLocalListDao.findLastDay();
    }

    public ArrayList<PatientLocalListEntity> findLastWeek() {
        return patientLocalListDao.findLastWeek();
    }

    public ArrayList<PatientLocalListEntity> findLastMonth() {
        return patientLocalListDao.findLastMonth();
    }


    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByPatientName(LocalDateTime from, LocalDateTime to, String detail) {
        return patientLocalListDao.findAllPatientsFilterByPatientName(from,to,detail);
    }

    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByPatientId(LocalDateTime from, LocalDateTime to, String detail) {
        return patientLocalListDao.findAllPatientsFilterByPatientId(from,to,detail);
    }

    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String detail) {
        return patientLocalListDao.findAllPatientsFilterByAccessionNumber(from,to,detail);
    }

    public ArrayList<PatientLocalListEntity> findCustomDateRange(LocalDateTime from, LocalDateTime to) {
        return patientLocalListDao.findCustomDateRange(from,to);
    }


    public PatientLocalListEntity findByStudySessionId(String studySessionId) {
        return patientLocalListDao.findByStudySessionId(studySessionId);
    }
}
