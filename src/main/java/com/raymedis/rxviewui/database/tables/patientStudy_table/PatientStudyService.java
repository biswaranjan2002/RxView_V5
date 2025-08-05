package com.raymedis.rxviewui.database.tables.patientStudy_table;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class PatientStudyService {

    private static PatientStudyService instance = new PatientStudyService();
    public static PatientStudyService getInstance(){
        return instance;
    }

    private final PatientStudyDao patientStudyDao;

    public PatientStudyService() {
        Map<String, String> columns = new HashMap<>();

        // Define column mappings for PatientEntity with all fields NOT NULL
        columns.put("patientId", "VARCHAR(255) NOT NULL");
        columns.put("patientName", "VARCHAR(255) NOT NULL");
        columns.put("sex", "VARCHAR(10) NOT NULL");
        columns.put("accessionNumber", "VARCHAR(255) NOT NULL");
        columns.put("studyDescription", "TEXT NOT NULL");
        columns.put("modality", "VARCHAR(50) NOT NULL");
        columns.put("patientSize", "VARCHAR(50) NOT NULL");
        columns.put("readingPhysician", "VARCHAR(255) NOT NULL");
        columns.put("performingPhysician", "VARCHAR(255) NOT NULL");
        columns.put("referringPhysician", "VARCHAR(255) NOT NULL");
        columns.put("patientInstituteResidence", "VARCHAR(255) NOT NULL");
        columns.put("requestProcedurePriority", "VARCHAR(50) NOT NULL");
        columns.put("additionalPatientHistory", "TEXT NOT NULL");
        columns.put("admittingDiagnosisDescription", "TEXT NOT NULL");
        columns.put("studyProcedure", "TEXT NOT NULL");
        columns.put("patientComments", "TEXT NOT NULL");
        columns.put("studyUid", "VARCHAR(255) NOT NULL");
        columns.put("institution", "VARCHAR(255) NOT NULL");
        columns.put("age", "INT NOT NULL");
        columns.put("height", "DOUBLE NOT NULL");
        columns.put("weight", "DOUBLE NOT NULL");
        columns.put("registerDateTime", "DATETIME NOT NULL");
        columns.put("exposureDateTime", "DATETIME NOT NULL");
        columns.put("studyDateTime", "DATETIME NOT NULL");
        columns.put("scheduledDateTime", "DATETIME NOT NULL");
        columns.put("dob", "DATE NOT NULL");
        columns.put("seriesId", "VARCHAR(255) NOT NULL");
        columns.put("instanceId", "VARCHAR(255) NOT NULL");
        columns.put("seriesUid", "VARCHAR(255) NOT NULL");
        columns.put("instanceUid", "VARCHAR(255) NOT NULL");
        columns.put("isRejected","BIT NOT NULL");
        columns.put("isPrinted","BIT NOT NULL");
        columns.put("isDicomUploaded","BIT NOT NULL");
        columns.put("isBackedUp","BIT NOT NULL");
        columns.put("isCleaned" , "BIT NOT NULL");

        patientStudyDao = new PatientStudyDao("PatientStudyEntity", columns, "studyId", new PatientStudyRowMapper(), "");
    }


    public void savePatient(PatientStudyEntity patientStudyEntity)throws SQLException {
        patientStudyDao.save(patientStudyEntity);
    }

    public void deletePatient(String studyId)throws SQLException {
        patientStudyDao.deleteByPrimaryKey(studyId);
    }

    public void updatePatient(String studyId, PatientStudyEntity patientStudyEntity)throws SQLException {
        patientStudyDao.update(studyId, patientStudyEntity);
    }

    public PatientStudyEntity findPatientByStudyId(String studyId){
        return patientStudyDao.findByPrimaryKey(studyId);
    }

    public List<PatientStudyEntity> findAllPatients() throws SQLException {
        return patientStudyDao.findAll();
    }

    public List<PatientStudyEntity> findLastDay(){
        return patientStudyDao.findLastDay();
    }

    public ArrayList<PatientStudyEntity> findLastWeek() {
        return patientStudyDao.findLastWeek();
    }

    public ArrayList<PatientStudyEntity> findLastMonth() {
        return patientStudyDao.findLastMonth();
    }

    public ArrayList<PatientStudyEntity> findCustomDateRange(LocalDateTime from, LocalDateTime to) {
        return patientStudyDao.findByRegisterDateBetween(from, to);
    }

    public ArrayList<PatientStudyEntity> findByPatientId(String  patientId) {
        return  patientStudyDao.findByPatientID(patientId);
    }

    public ArrayList<PatientStudyEntity> findByAccessionNumber(String accessionNumber) {
        return  patientStudyDao.findByAccessionId(accessionNumber);
    }

    public ArrayList<PatientStudyEntity> findByPatientName(String patientName) {
        return patientStudyDao.findByPatientName(patientName);
    }


    public ArrayList<PatientStudyEntity> findAllPatientsFilterByPatientName(LocalDateTime from, LocalDateTime to, String patientName) {
        return patientStudyDao.findAllPatientsFilterByPatientName(from,to,patientName);
    }

    public ArrayList<PatientStudyEntity> findAllPatientsFilterByPatientId(LocalDateTime from, LocalDateTime to, String patientId) {
        return patientStudyDao.findAllPatientsFilterByPatientId(from,to,patientId);
    }

    public ArrayList<PatientStudyEntity> findAllPatientsFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String accessionNumber) {
        return patientStudyDao.findAllPatientsFilterByAccessionNumber(from,to,accessionNumber);
    }

    public int findStudyCount(String studyId) {
        return patientStudyDao.countByStudyId(studyId);
    }

    public int findTodayStudies(String studyIdPrefix) {
        return patientStudyDao.findTodayStudies(studyIdPrefix);
    }

    public ArrayList<PatientStudyEntity> findAllRejectedStudies(){
        return patientStudyDao.findAllRejectedStudies();
    }


    public List<PatientStudyEntity> findLastDayRejectedStudies() {
       return patientStudyDao.findLastDayRejectedStudies();
    }


    public List<PatientStudyEntity> findLastWeekRejectedStudies() {
        return patientStudyDao.findLastWeekRejectedStudies();
    }

    public List<PatientStudyEntity> findLastMonthRejectedStudies() {
        return patientStudyDao.findLastMonthRejectedStudies();
    }

    public List<PatientStudyEntity> findAllRejectedStudiesFilterByPatientName(LocalDateTime from, LocalDateTime to, String patientName) {
        return patientStudyDao.findAllRejectedStudiesFilterByPatientName(from,to,patientName);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesFilterByPatientId(LocalDateTime from, LocalDateTime to, String patientId) {
        return patientStudyDao.findAllRejectedStudiesFilterByPatientId(from,to,patientId);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String accessionNumber) {
        return patientStudyDao.findAllRejectedStudiesFilterByAccessionNumber(from,to,accessionNumber);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesCustomDateRange(LocalDateTime from, LocalDateTime to) {
        return patientStudyDao.findAllRejectedStudiesCustomDateRange(from,to);
    }

    public List<PatientStudyEntity> findAllBackedUpStudies(){
        return patientStudyDao.findAllBackedUpStudies();
    }

    public List<PatientStudyEntity> findAllNonBackedUpStudies(){
        return patientStudyDao.findAllNonBackedUpStudies();
    }

    public List<PatientStudyEntity> findAllRestorableStudies(){
        return patientStudyDao.findAllRestorableStudies();
    }

    public List<PatientStudyEntity> findAllDatabaseStudies(){
        return patientStudyDao.findAllDatabaseStudies();
    }

    public PatientStudyEntity findByStudyId(String studyId) {
        return patientStudyDao.findByPrimaryKey(studyId);
    }



}
