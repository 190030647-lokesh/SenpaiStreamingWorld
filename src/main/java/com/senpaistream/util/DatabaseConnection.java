package com.senpaistream.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Use environment variables for Railway, fallback to localhost for development
    private static final String HOST = getEnvOrDefault("MYSQL_HOST", "localhost");
    private static final String PORT = getEnvOrDefault("MYSQL_PORT", "3306");
    private static final String DATABASE = getEnvOrDefault("MYSQL_DATABASE", "senpai_streaming_world");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";
    private static final String USER = getEnvOrDefault("MYSQL_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("MYSQL_PASSWORD", "Tiger@123");
    
    private static boolean tablesCreated = false;
    private static boolean driverLoaded = false;
    
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            if (!driverLoaded) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverLoaded = true;
                System.out.println("MySQL Driver loaded.");
            }
            
            System.out.println("Connecting to: " + HOST + ":" + PORT + "/" + DATABASE);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            
            // Auto-create tables on first connection
            if (!tablesCreated) {
                createTables(connection);
                tablesCreated = true;
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to database failed: " + e.getMessage());
            System.err.println("Host: " + HOST + ", Port: " + PORT + ", DB: " + DATABASE + ", User: " + USER);
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
