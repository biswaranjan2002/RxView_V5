package com.raymedis.rxviewui.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
    // Default relative path to your SQLite DB file
    private static String dbPath = "data/rxview_database2.db";
    private static String url = "jdbc:sqlite:" + dbPath;

    // Ensures the parent directory of the DB file exists
    private static void ensureDirectoryExists() {
        File dbFile = new File(dbPath);
        File parentDir = dbFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("Created directory: " + parentDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        ensureDirectoryExists();  // Make sure directory exists before connecting
        return DriverManager.getConnection(url);
    }

    public static String getDbPath() {
        return dbPath;
    }

    public static void setDbPath(String newPath) {
        dbPath = newPath;
        url = "jdbc:sqlite:" + dbPath;
    }

    public static String getUrl() {
        return url;
    }
}

