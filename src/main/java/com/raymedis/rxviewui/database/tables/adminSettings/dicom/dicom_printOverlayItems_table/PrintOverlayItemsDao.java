package com.raymedis.rxviewui.database.tables.adminSettings.dicom.dicom_printOverlayItems_table;

import com.raymedis.rxviewui.database.ConnectionManager;
import com.raymedis.rxviewui.database.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrintOverlayItemsDao extends GenericRepository<PrintOverlayItemsEntity,Integer> {

    public PrintOverlayItemsDao(String table_name, Map<String, String> columns, String primaryKey, RowMapper<PrintOverlayItemsEntity, Integer> row_mapper, boolean isAutoIncrement, Integer dummyprimaryKeyValue) {
        super(table_name, columns, primaryKey, row_mapper, isAutoIncrement, dummyprimaryKeyValue);
    }

    private ArrayList<PrintOverlayItemsEntity> list(String query, Object... params) {
        ArrayList<PrintOverlayItemsEntity> resultList = new ArrayList<>();
        PrintOverlayItemsRowMapper rowMapper = PrintOverlayItemsRowMapper.getInstance();

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


    public PrintOverlayItemsEntity findByItem(String item) {
        String query = "SELECT * FROM printOverlayItemsEntity WHERE LOWER(Item) = LOWER(?)";
        List<PrintOverlayItemsEntity> resultList = list(query, item);

        return resultList.isEmpty() ? null : resultList.getFirst();
    }


}
