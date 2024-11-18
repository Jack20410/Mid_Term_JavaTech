package com.tdtu.DAO;

import com.tdtu.models.User;
import com.tdtu.utils.DatabaseUtil;
import com.tdtu.utils.PasswordUtil;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void addUser(String userName, String passwordHash, String role, boolean isActive) throws SQLException {
        String sql = "INSERT INTO Users (UserName, PasswordHash, Role, IsActive) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, passwordHash);
            stmt.setString(3, role);
            stmt.setBoolean(4, isActive);
            stmt.executeUpdate();
        }
    }

    public boolean authenticateUser(String userName, String password) throws SQLException {
        String sql = "SELECT PasswordHash, IsActive FROM Users WHERE UserName = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean isActive = rs.getBoolean("IsActive");
                    if (!isActive) {
                        throw new SQLException("Account is inactive. Please contact the administrator.");
                    }
                    String storedHashedPassword = rs.getString("PasswordHash");
                    return PasswordUtil.verifyPassword(password, storedHashedPassword);
                }
            }
        }
        return false;
    }

    public String getUserRole(String userName) throws SQLException {
        String sql = "SELECT Role FROM Users WHERE UserName = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Role");
                }
            }
        }
        return null; // Return null if no user is found
    }

    public List<String[]> getAllUsers() throws Exception {
        String sql = "SELECT UserID, UserName, Role, IsActive FROM Users";
        List<String[]> users = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] user = {
                        String.valueOf(rs.getInt("UserID")),
                        rs.getString("UserName"),
                        rs.getString("Role"),
                        String.valueOf(rs.getBoolean("IsActive"))
                };
                users.add(user);
            }
        }
        return users;
    }

    public String validateUser(String username, String password) throws Exception {
        String sql = "SELECT Role FROM Users WHERE UserName = ? AND PasswordHash = MD5(?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role"); // Return the role of the user
            }
        }
        return null; // Return null if user not found
    }

    public byte[] getProfilePicture(String userName) throws SQLException {
        String sql = "SELECT ProfilePicture FROM Users WHERE UserName = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("ProfilePicture");
                }
            }
        }
        return null; // Default avatar if not found
    }

    public void updateProfilePicture(String userName, byte[] profilePicture) throws SQLException {
        String sql = "UPDATE Users SET ProfilePicture = ? WHERE UserName = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, profilePicture);
            stmt.setString(2, userName);
            stmt.executeUpdate();
        }
    }


    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void updateUser(int userId, String userName, String role, boolean isActive) throws SQLException {
        String sql = "UPDATE Users SET UserName = ?, Role = ?, IsActive = ? WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, role);
            stmt.setBoolean(3, isActive);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("Role"),
                            rs.getString("PasswordHash"),
                            rs.getBlob("ProfilePicture"),
                            rs.getBoolean("IsActive"),
                            rs.getTimestamp("DateCreated").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }


}

