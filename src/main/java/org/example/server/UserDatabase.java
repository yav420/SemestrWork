package org.example.server;

import java.sql.*;

public class UserDatabase {
    private static final String DB_URL = "jdbc:sqlite:chat.db";

    public UserDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String createTableQuery = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Добавление нового пользователя
    public boolean addUser(String username, String password) throws CustomExceptions.UserAlreadyExistsException, SQLException {
        if (isUserExists(username)) {
            throw new CustomExceptions.UserAlreadyExistsException("User " + username + " already exists.");
        }

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        }
    }

    // Проверка, существует ли уже пользователь в базе
    private boolean isUserExists(String username) throws SQLException {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // Если пользователь найден, возвращаем true
            }
        }
    }

    // Аутентификация пользователя
    public boolean authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password); // Сравниваем пароли
                }
            }
        }
        return false; // Если пользователь не найден или пароль неверный
    }
}