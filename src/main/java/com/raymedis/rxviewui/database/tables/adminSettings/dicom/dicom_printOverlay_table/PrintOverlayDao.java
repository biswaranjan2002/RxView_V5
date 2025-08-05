package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlay_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class PrintOverlayDao extends GenericRepository<PrintOverlayEntity,Integer> {

    public PrintOverlayDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<PrintOverlayEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<PrintOverlayEntity> list(String query, Object... params) {
        ArrayList<PrintOverlayEntity> resultList = new ArrayList<>();
        PrintOverlayRowMapper rowMapper = PrintOverlayEntity.getInstance();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultList.add(rowMapper.mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database query execution failed: " + e.getMessage());
        }

        return resultList;
    }


    public ArrayList<PrintOverlayEntity> findAllByOverlayId(String overlayId) {
        String query = "SELECT * FROM printoverlayentity WHERE overlayId = ?";
        return list(query,overlayId );
    }


    public void deleteAllByOverlayId(String overlayId) {
        String query = "DELETE FROM printoverlayentity WHERE overlayId = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, overlayId);
            int rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting records by overlayId: " + e.getMessage());
        }
    }

    public ArrayList<PrintOverlayEntity> findAllSelected() {
        String query = "SELECT * FROM printoverlayentity WHERE isSelected = ?";
        return list(query, true);
    }

}
