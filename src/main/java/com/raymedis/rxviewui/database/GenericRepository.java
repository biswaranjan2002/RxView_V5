package com.raymedis.rxviewui.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericRepository<T, K> {

    private final String table_name;
    private final Map<String, String> columns;
    private final RowMapper<T, K> row_mapper;
    private final String primaryKey;
    private boolean isAutoIncrement;
    K obj;

    public GenericRepository(String table_name, Map<String, String> columns, String primaryKey, RowMapper<T, K> row_mapper, boolean isAutoIncrement, K dummyprimaryKeyValue) {
        this.table_name = table_name;
        this.columns = columns;
        this.row_mapper = row_mapper;
        this.primaryKey = primaryKey;
        this.isAutoIncrement = isAutoIncrement;
        this.obj = dummyprimaryKeyValue;
        createTableIfNotExists();
    }

    public void createTableIfNotExists() {
        StringBuilder columnsSQL = new StringBuilder();

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            columnsSQL.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        if (columnsSQL.length() > 0) {
            columnsSQL.setLength(columnsSQL.length() - 2);
        }

        String create_table_sql;

        if (obj instanceof Integer) {
            if (isAutoIncrement) {
                // SQLite requires INTEGER PRIMARY KEY AUTOINCREMENT (no other name will work for auto-increment)
                create_table_sql = "CREATE TABLE IF NOT EXISTS " + table_name + " ("
                        + primaryKey + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + columnsSQL.toString()
                        + ")";
            } else {
                create_table_sql = "CREATE TABLE IF NOT EXISTS " + table_name + " ("
                        + primaryKey + " INTEGER PRIMARY KEY, "
                        + columnsSQL.toString()
                        + ")";
            }
        } else if (obj instanceof String) {
            create_table_sql = "CREATE TABLE IF NOT EXISTS " + table_name + " ("
                    + primaryKey + " TEXT PRIMARY KEY, "
                    + columnsSQL.toString()
                    + ")";
        } else {
            throw new IllegalArgumentException("Unsupported primary key type: " + obj.getClass().getName());
        }

        try (Connection connection = ConnectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(create_table_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(T entity) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = createInsertStatement(connection, entity)) {
            stmt.executeUpdate();
        }
    }

    public Optional<T> findById(int id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE " + primaryKey + " = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(row_mapper.mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<T> findAll() throws SQLException {
        List<T> resultList = new ArrayList<>();
        String query = "SELECT * FROM " + table_name;
        try (Connection connection = ConnectionManager.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                resultList.add(row_mapper.mapRow(rs));
            }
        }
        return resultList;
    }

    public void update(K key, T entity) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = createUpdateStatement(connection, key, entity)) {
            stmt.executeUpdate();
        }
    }

    public void deleteByPrimaryKey(K key) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE " + primaryKey + " = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            if (key instanceof Integer val) {
                stmt.setInt(1, val);
            } else if (key instanceof String val) {
                stmt.setString(1, val);
            }
            stmt.executeUpdate();
        }
    }

    private PreparedStatement createInsertStatement(Connection connection, T entity) throws SQLException {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        int index = 1;

        for (String column : columns.keySet()) {
            columnNames.append(column).append(", ");
            placeholders.append("?").append(", ");
        }

        if (!(isAutoIncrement && obj instanceof Integer)) {
            columnNames.append(primaryKey).append(", ");
            placeholders.append("?").append(", ");
        }

        if (!columnNames.isEmpty()) columnNames.setLength(columnNames.length() - 2);
        if (!placeholders.isEmpty()) placeholders.setLength(placeholders.length() - 2);

        String query = "INSERT INTO " + table_name + " (" + columnNames + ") VALUES (" + placeholders + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            Object value = row_mapper.getValue(entity, entry.getKey());
            stmt.setObject(index++, value);
        }

        if (!(isAutoIncrement && obj instanceof Integer)) {
            stmt.setObject(index, row_mapper.getPrimaryKeyValue(entity, primaryKey));
        }

        return stmt;
    }

    private PreparedStatement createUpdateStatement(Connection connection, K key, T entity) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        int index = 1;

        for (String column : columns.keySet()) {
            setClause.append(column).append(" = ?, ");
        }

        if (!setClause.isEmpty()) setClause.setLength(setClause.length() - 2);

        String query = "UPDATE " + table_name + " SET " + setClause + " WHERE " + primaryKey + " = ?";
        PreparedStatement stmt = connection.prepareStatement(query);

        for (String column : columns.keySet()) {
            Object value = row_mapper.getValue(entity, column);
            stmt.setObject(index++, value);
        }

        stmt.setObject(index, key);
        return stmt;
    }

    public interface RowMapper<T, K> {
        T mapRow(ResultSet rs) throws SQLException;
        Object getValue(T entity, String columnName);
        Object getPrimaryKeyValue(T entity, String primaryKeyName);
    }
}

