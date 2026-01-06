package com.senpaistream.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Use environment variables for Railway, fallback to localhost for development
    private static final String HOST = getEnvOrDefault("MYSQL_HOST", "localhost");
    private static final String PORT = getEnvOrDefault("MYSQL_PORT", "3306");
    private static final String DATABASE = getEnvOrDefault("MYSQL_DATABASE", "senpai_streaming_world");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    private static final String USER = getEnvOrDefault("MYSQL_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("MYSQL_PASSWORD", "Tiger@123");
    
    private static Connection connection = null;
    
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Connecting to: " + URL);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to database failed: " + e.getMessage());
            System.err.println("URL: " + URL + ", User: " + USER);
            // Return null - servlets should handle this gracefully
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
