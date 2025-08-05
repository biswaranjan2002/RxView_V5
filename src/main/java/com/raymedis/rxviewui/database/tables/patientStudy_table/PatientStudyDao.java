package com.raymedis.rxviewui.database.tables.patientStudy_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PatientStudyDao extends GenericRepository<PatientStudyEntity,String> {

    public PatientStudyDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<PatientStudyEntity, String> row_mapper, String dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, false,dummyprimaryKeyValue);
    }

    public ArrayList<PatientStudyEntity> findLastDay() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime previousDateTime = currentDateTime.minusDays(1);

        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDateTime, currentDateTime);
    }

    public ArrayList<PatientStudyEntity> findLastWeek() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(7);

        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }

    public ArrayList<PatientStudyEntity> findLastMonth() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(30);

        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }

    public ArrayList<PatientStudyEntity> findByRegisterDateBetween(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, from, to);
    }

    public ArrayList<PatientStudyEntity> findByPatientID(String patientId) {
        String query = "SELECT * FROM PatientStudyEntity WHERE patientId LIKE ?";
        String pattern = "%" + patientId + "%";
        return list(query, pattern);
    }

    public ArrayList<PatientStudyEntity> findByAccessionId(String accessionNumber) {
        String query = "SELECT * FROM PatientStudyEntity WHERE accessionNumber LIKE ?";
        String pattern = "%" + accessionNumber + "%";
        return list(query, pattern);
    }

    public ArrayList<PatientStudyEntity> findByPatientName(String patientName) {
        String query = "SELECT * FROM PatientStudyEntity WHERE patientName LIKE ?";
        String pattern = "%" + patientName + "%";
        return list(query, pattern);
    }

    public ArrayList<PatientStudyEntity> findAllPatientsFilterByPatientName(LocalDateTime from, LocalDateTime to, String patientName) {
        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ? AND patientName LIKE ?";
        String pattern = "%" + patientName + "%";
        return list(query, from, to, pattern);
    }

    public ArrayList<PatientStudyEntity> findAllPatientsFilterByPatientId(LocalDateTime from, LocalDateTime to, String patientId) {
        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ? AND patientId LIKE ?";
        String pattern = "%" + patientId + "%";
        return list(query, from, to, pattern);
    }

    public ArrayList<PatientStudyEntity> findAllPatientsFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String accessionNumber) {
        String query = "SELECT * FROM PatientStudyEntity WHERE registerDateTime BETWEEN ? AND ? AND accessionNumber LIKE ?";
        String pattern = "%" + accessionNumber + "%";
        return list(query, from, to, pattern);
    }

    public int countByStudyId(String studyId) {
        String query = "SELECT COUNT(*) FROM PatientStudyEntity WHERE studyId = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int findTodayStudies(String studyIdPrefix) {
        String query = "SELECT COUNT(*) FROM PatientStudyEntity WHERE studyId LIKE ? AND DATE(studyDateTime) = CURDATE()";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, studyIdPrefix + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private ArrayList<PatientStudyEntity> list(String query, Object... params) {
        ArrayList<PatientStudyEntity> resultList = new ArrayList<>();
        PatientStudyRowMapper rowMapper = PatientStudyRowMapper.getInstance();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resultList.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    public PatientStudyEntity findByPrimaryKey(String studyId) {
        String query = "SELECT * FROM PatientStudyEntity WHERE studyId = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new PatientStudyRowMapper().mapRow(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<PatientStudyEntity> findAllRejectedStudies() {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1";
        return list(query);
    }



    public List<PatientStudyEntity> findLastDayRejectedStudies() {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ?";
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime previousDateTime = currentDateTime.minusDays(1);

        return list(query, previousDateTime, currentDateTime);
    }

    public List<PatientStudyEntity> findLastWeekRejectedStudies() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(7);

        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }

    public List<PatientStudyEntity> findLastMonthRejectedStudies() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(30);

        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesFilterByPatientName(LocalDateTime from, LocalDateTime to, String patientName) {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ? AND patientName LIKE ?";
        String pattern = "%" + patientName + "%";
        return list(query, from, to, pattern);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesFilterByPatientId(LocalDateTime from, LocalDateTime to, String patientId) {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ? AND patientId LIKE ?";
        String pattern = "%" + patientId + "%";
        return list(query, from, to, pattern);
    }


    public List<PatientStudyEntity> findAllRejectedStudiesFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String accessionNumber) {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ? AND accessionNumber LIKE ?";
        String pattern = "%" + accessionNumber + "%";
        return list(query, from, to, pattern);
    }

    public List<PatientStudyEntity> findAllRejectedStudiesCustomDateRange(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM PatientStudyEntity WHERE isRejected = 1 AND registerDateTime BETWEEN ? AND ?";
        return list(query, from, to);
    }

    public List<PatientStudyEntity> findAllBackedUpStudies() {
        String query = "SELECT * FROM PatientStudyEntity WHERE isBackedUp = 1 AND isCleaned = 0";
        return list(query);
    }

    public List<PatientStudyEntity> findAllNonBackedUpStudies() {
        String query = "SELECT * FROM PatientStudyEntity WHERE isBackedUp = 0 AND isCleaned = 0";
        return list(query);
    }

    public List<PatientStudyEntity> findAllRestorableStudies() {
        String query = "SELECT * FROM PatientStudyEntity WHERE isBackedUp = 1 AND isCleaned = 1";
        return list(query);
    }

    public List<PatientStudyEntity> findAllDatabaseStudies(){
        String query = "SELECT * FROM PatientStudyEntity WHERE isCleaned = 0";
        return list(query);
    }

}
