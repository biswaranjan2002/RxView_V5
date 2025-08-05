package com.raymedis.rxviewui.database.tables.patientStudy_table;

import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientStudyRowMapper implements GenericRepository.RowMapper<PatientStudyEntity, String> {

    private final static PatientStudyRowMapper instance = new PatientStudyRowMapper();
    public static PatientStudyRowMapper getInstance(){
        return instance;
    }

    @Override
    public PatientStudyEntity mapRow(ResultSet rs) throws SQLException {
        PatientStudyEntity patientStudyEntity = new PatientStudyEntity();

        patientStudyEntity.setPatientId(rs.getString("patientId"));
        patientStudyEntity.setPatientName(rs.getString("patientName"));
        patientStudyEntity.setSex(rs.getString("sex"));
        patientStudyEntity.setAge(rs.getInt("age"));
        patientStudyEntity.setDob(rs.getObject("dob", LocalDate.class));
        patientStudyEntity.setHeight(rs.getDouble("height"));
        patientStudyEntity.setWeight(rs.getDouble("weight"));
        patientStudyEntity.setPatientSize(rs.getString("patientSize"));
        patientStudyEntity.setPatientInstituteResidence(rs.getString("patientInstituteResidence"));

        patientStudyEntity.setAccessionNumber(rs.getString("accessionNumber"));
        patientStudyEntity.setModality(rs.getString("modality"));
        patientStudyEntity.setRequestProcedurePriority(rs.getString("requestProcedurePriority"));
        patientStudyEntity.setAdditionalPatientHistory(rs.getString("additionalPatientHistory"));
        patientStudyEntity.setAdmittingDiagnosisDescription(rs.getString("admittingDiagnosisDescription"));
        patientStudyEntity.setStudyDescription(rs.getString("studyDescription"));
        patientStudyEntity.setStudyProcedure(rs.getString("studyProcedure"));
        patientStudyEntity.setStudyDateTime(rs.getObject("studyDateTime", LocalDateTime.class));
        patientStudyEntity.setRegisterDateTime(rs.getObject("registerDateTime", LocalDateTime.class));
        patientStudyEntity.setScheduledDateTime(rs.getObject("scheduledDateTime", LocalDateTime.class));
        patientStudyEntity.setExposureDateTime(rs.getObject("exposureDateTime", LocalDateTime.class));
        patientStudyEntity.setPatientComments(rs.getString("patientComments"));
        patientStudyEntity.setStudyId(rs.getString("studyId"));
        patientStudyEntity.setReadingPhysician(rs.getString("readingPhysician"));
        patientStudyEntity.setReferringPhysician(rs.getString("referringPhysician"));
        patientStudyEntity.setPerformingPhysician(rs.getString("performingPhysician"));
        patientStudyEntity.setInstitution(rs.getString("institution"));
        patientStudyEntity.setStudyUid(rs.getString("studyUid"));

        patientStudyEntity.setSeriesUid(rs.getString("seriesUid"));
        patientStudyEntity.setInstanceUid(rs.getString("instanceUid"));
        patientStudyEntity.setSeriesId(rs.getString("seriesId"));
        patientStudyEntity.setInstanceId(rs.getString("instanceId"));
        patientStudyEntity.setIsRejected(rs.getBoolean("isRejected"));
        patientStudyEntity.setIsPrinted(rs.getBoolean("isPrinted"));
        patientStudyEntity.setIsDicomUploaded(rs.getBoolean("isDicomUploaded"));
        patientStudyEntity.setIsBackedUp(rs.getBoolean("isBackedUp"));
        patientStudyEntity.setIsBackedUp(rs.getBoolean("isCleaned"));


        return patientStudyEntity;
    }


    @Override
    public Object getValue(PatientStudyEntity entity, String columnName) {
        return switch (columnName) {
            case "patientId" -> entity.getPatientId();
            case "patientName" -> entity.getPatientName();
            case "sex" -> entity.getSex();
            case "age" -> entity.getAge();
            case "dob" -> entity.getDob();
            case "height" -> entity.getHeight();
            case "weight" -> entity.getWeight();
            case "patientSize" -> entity.getPatientSize();
            case "patientInstituteResidence" -> entity.getPatientInstituteResidence();
            case "accessionNumber" -> entity.getAccessionNumber();
            case "modality" -> entity.getModality();
            case "requestProcedurePriority" -> entity.getRequestProcedurePriority();
            case "additionalPatientHistory" -> entity.getAdditionalPatientHistory();
            case "admittingDiagnosisDescription" -> entity.getAdmittingDiagnosisDescription();
            case "studyDescription" -> entity.getStudyDescription();
            case "studyProcedure" -> entity.getStudyProcedure();
            case "studyDateTime" -> entity.getStudyDateTime();
            case "registerDateTime" -> entity.getRegisterDateTime();
            case "scheduledDateTime" -> entity.getScheduledDateTime();
            case "exposureDateTime" -> entity.getExposureDateTime();
            case "patientComments" -> entity.getPatientComments();
            case "studyId" -> entity.getStudyId();
            case "readingPhysician" -> entity.getReadingPhysician();
            case "referringPhysician" -> entity.getReferringPhysician();
            case "performingPhysician" -> entity.getPerformingPhysician();
            case "institution" -> entity.getInstitution();
            case "studyUid" -> entity.getStudyUid();
            case "seriesUid" -> entity.getSeriesUid();
            case "instanceUid" -> entity.getInstanceUid();
            case "seriesId" -> entity.getSeriesId();
            case "instanceId" -> entity.getInstanceId();
            case "isRejected" -> entity.getIsRejected();
            case "isPrinted" -> entity.getIsPrinted();
            case "isDicomUploaded" -> entity.getIsDicomUploaded();
            case "isBackedUp" -> entity.getIsBackedUp();
            case "isCleaned" -> entity.getIsCleaned();
            default -> null;
        };
    }


    @Override
    public Object getPrimaryKeyValue(PatientStudyEntity entity, String primaryKeyName) {
        return entity.getStudyId() == null ? "" : entity.getStudyId();
    }

}
