package com.raymedis.rxviewui.database.tables.register.patientLocalList_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientLocalListRowMapper implements GenericRepository.RowMapper<PatientLocalListEntity,Integer> {

    private static PatientLocalListRowMapper instance = new PatientLocalListRowMapper();
    public static PatientLocalListRowMapper getInstance() {
        return instance;
    }

    @Override
    public PatientLocalListEntity mapRow(ResultSet rs) throws SQLException {
        PatientLocalListEntity patientLocalListEntity = new PatientLocalListEntity();

        patientLocalListEntity.setId(rs.getInt("id"));
        patientLocalListEntity.setAccessionNumber(rs.getString("accessionNumber"));
        patientLocalListEntity.setAdditionalPatientHistory(rs.getString("additionalPatientHistory"));
        patientLocalListEntity.setAge(rs.getInt("age"));
        patientLocalListEntity.setAdmittingDiagnosisDescription(rs.getString("admittingDiagnosisDescription"));
        patientLocalListEntity.setDob(rs.getObject("dob", LocalDate.class));
        patientLocalListEntity.setHeight(rs.getDouble("height"));
        patientLocalListEntity.setInstitution(rs.getString("institution"));
        patientLocalListEntity.setModality(rs.getString("modality"));
        patientLocalListEntity.setPatientComments(rs.getString("patientComments"));
        patientLocalListEntity.setPatientId(rs.getString("patientId"));
        patientLocalListEntity.setPatientInstituteResidence(rs.getString("patientInstituteResidence"));
        patientLocalListEntity.setPatientName(rs.getString("patientName"));
        patientLocalListEntity.setPatientSize(rs.getString("patientSize"));
        patientLocalListEntity.setPerformingPhysician(rs.getString("performingPhysician"));
        patientLocalListEntity.setReadingPhysician(rs.getString("readingPhysician"));
        patientLocalListEntity.setRegisterDateTime(rs.getObject("registerDateTime", LocalDateTime.class));
        patientLocalListEntity.setReferringPhysician(rs.getString("referringPhysician"));
        patientLocalListEntity.setRequestProcedurePriority(rs.getString("requestProcedurePriority"));
        patientLocalListEntity.setScheduledDateTime(rs.getObject("scheduledDateTime", LocalDateTime.class));
        patientLocalListEntity.setSex(rs.getString("sex"));
        patientLocalListEntity.setStudyDescription(rs.getString("studyDescription"));
        patientLocalListEntity.setWeight(rs.getDouble("weight"));
        patientLocalListEntity.setStudyId(rs.getString("studyId"));
        patientLocalListEntity.setStudySessionId(rs.getString("studySessionId"));

        return patientLocalListEntity;
    }


    @Override
    public Object getValue(PatientLocalListEntity entity, String columnName) {
        if (entity == null || columnName == null) {
            return null;
        }

        return switch (columnName) {
            case "accessionNumber" -> entity.getAccessionNumber();
            case "additionalPatientHistory" -> entity.getAdditionalPatientHistory();
            case "age" -> entity.getAge();
            case "admittingDiagnosisDescription" -> entity.getAdmittingDiagnosisDescription();
            case "dob" -> entity.getDob();
            case "height" -> entity.getHeight();
            case "institution" -> entity.getInstitution();
            case "modality" -> entity.getModality();
            case "patientComments" -> entity.getPatientComments();
            case "patientId" -> entity.getPatientId();
            case "patientInstituteResidence" -> entity.getPatientInstituteResidence();
            case "patientName" -> entity.getPatientName();
            case "patientSize" -> entity.getPatientSize();
            case "performingPhysician" -> entity.getPerformingPhysician();
            case "readingPhysician" -> entity.getReadingPhysician();
            case "registerDateTime" -> entity.getRegisterDateTime();
            case "referringPhysician" -> entity.getReferringPhysician();
            case "requestProcedurePriority" -> entity.getRequestProcedurePriority();
            case "scheduledDateTime" -> entity.getScheduledDateTime();
            case "sex" -> entity.getSex();
            case "studyDescription" -> entity.getStudyDescription();
            case "weight" -> entity.getWeight();
            case "studyId" -> entity.getStudyId();
            case "studySessionId" ->entity.getStudySessionId();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(PatientLocalListEntity entity, String primaryKeyName) {
        return entity.getId();
    }



}
