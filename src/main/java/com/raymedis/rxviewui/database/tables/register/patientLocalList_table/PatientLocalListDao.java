package com.raymedis.rxviewui.database.tables.register.patientLocalList_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class PatientLocalListDao extends GenericRepository<PatientLocalListEntity,Integer>  {

    public PatientLocalListDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<PatientLocalListEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<PatientLocalListEntity> list(String query, Object... params) {
        ArrayList<PatientLocalListEntity> resultList = new ArrayList<>();
        PatientLocalListRowMapper rowMapper = PatientLocalListRowMapper.getInstance();
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


    public ArrayList<PatientLocalListEntity> findLastDay() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime previousDateTime = currentDateTime.minusDays(1);

        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDateTime, currentDateTime);
    }


    public ArrayList<PatientLocalListEntity> findLastWeek() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(7);

        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }


    public ArrayList<PatientLocalListEntity> findLastMonth() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(30);

        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, previousDate, currentDate);
    }

    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByPatientName(LocalDateTime from, LocalDateTime to, String patientName) {
        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ? AND patientName LIKE ?";
        String pattern = "%" + patientName + "%";
        return list(query, from, to, pattern);
    }

    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByPatientId(LocalDateTime from, LocalDateTime to, String patientId) {
        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ? AND patientId LIKE ?";
        String pattern = "%" + patientId + "%";
        return list(query, from, to, pattern);
    }

    public ArrayList<PatientLocalListEntity> findAllPatientsFilterByAccessionNumber(LocalDateTime from, LocalDateTime to, String accessionNumber) {
        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ? AND accessionNumber LIKE ?";
        String pattern = "%" + accessionNumber + "%";
        return list(query, from, to, pattern);
    }

    public ArrayList<PatientLocalListEntity> findCustomDateRange(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM patientLocalListEntity WHERE registerDateTime BETWEEN ? AND ?";
        return list(query, from, to);
    }

    public PatientLocalListEntity findByStudySessionId(String studySessionId) {
        String query = "SELECT * FROM patientLocalListEntity WHERE studySessionId = ?";
        ArrayList<PatientLocalListEntity> resultList = list(query, studySessionId);
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

}
