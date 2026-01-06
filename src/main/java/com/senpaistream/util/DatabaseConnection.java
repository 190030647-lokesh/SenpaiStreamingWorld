package com.senpaistream.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    
    private static boolean tablesCreated = false;
    private static boolean driverLoaded = false;
    
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    private static String buildConnectionUrl() {
        // First check for MYSQL_URL (complete URL from Railway)
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
            // Convert mysql:// to jdbc:mysql://
            if (mysqlUrl.startsWith("mysql://")) {
                mysqlUrl = "jdbc:" + mysqlUrl;
            }
            System.out.println("Using MYSQL_URL");
            return mysqlUrl + "?allowPublicKeyRetrieval=true&useSSL=false";
        }
        
        // Fallback to individual variables
        String host = getEnvOrDefault("MYSQL_HOST", "localhost");
        String port = getEnvOrDefault("MYSQL_PORT", "3306");
        String database = getEnvOrDefault("MYSQL_DATABASE", "senpai_streaming_world");
        
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + 
                     "?allowPublicKeyRetrieval=true&useSSL=false";
        System.out.println("Built URL: " + host + ":" + port + "/" + database);
        return url;
    }
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            if (!driverLoaded) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverLoaded = true;
                System.out.println("MySQL Driver loaded.");
            }
            
            String url = buildConnectionUrl();
            String user = getEnvOrDefault("MYSQL_USER", "root");
            String password = getEnvOrDefault("MYSQL_PASSWORD", "Tiger@123");
            
            System.out.println("Attempting database connection...");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
            
            // Auto-create tables on first connection
            if (!tablesCreated && connection != null) {
                createTables(connection);
                tablesCreated = true;
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to database failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    private static void createTables(Connection conn) {
        String createUsersTable = 
            "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(50) NOT NULL UNIQUE, " +
            "email VARCHAR(100) NOT NULL UNIQUE, " +
            "password VARCHAR(255) NOT NULL, " +
            "profile_picture VARCHAR(255) DEFAULT 'default.png', " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "last_login TIMESTAMP NULL" +
            ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
