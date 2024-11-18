package com.tdtu.DAO;

import com.tdtu.models.Student;
import com.tdtu.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Fetch all students
    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM Students";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("StudentID"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Status")
                ));
            }
            return students;
        }
    }

    // Add a new student
    public void addStudent(String name, int age, String phone, String status) throws SQLException {
        String sql = "INSERT INTO Students (Name, Age, PhoneNumber, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, phone);
            stmt.setString(4, status);
            stmt.executeUpdate();
        }
    }

    // Delete a student by ID
    public void deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }
    }

    // Update a student
    public void updateStudent(int studentId, String name, int age, String phone, String status) throws SQLException {
        String sql = "UPDATE Students SET Name = ?, Age = ?, PhoneNumber = ?, Status = ? WHERE StudentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, phone);
            stmt.setString(4, status);
            stmt.setInt(5, studentId);
            stmt.executeUpdate();
        }
    }

    // Fetch a single student by ID
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM Students WHERE StudentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("StudentID"),
                            rs.getString("Name"),
                            rs.getInt("Age"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Status")
                    );
                } else {
                    throw new SQLException("Student not found with ID: " + studentId);
                }
            }
        }
    }

    // Add the following methods to the StudentDAO class

    // Sort students by a given criterion
    public List<Student> getSortedStudents(String criteria) throws SQLException {
        String sql = "SELECT * FROM Students ORDER BY ";
        switch (criteria) {
            case "Name":
                sql += "Name";
                break;
            case "Age":
                sql += "Age";
                break;
            case "Status":
                sql += "Status";
                break;
            default:
                sql += "StudentID";
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("StudentID"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Status")
                ));
            }
            return students;
        }
    }

    // Search students by name and status
    public List<Student> searchStudents(String name, String status) throws SQLException {
        String sql = "SELECT * FROM Students WHERE Name LIKE ?";
        if (!status.equals("All")) {
            sql += " AND Status = ?";
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            if (!status.equals("All")) {
                stmt.setString(2, status);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (rs.next()) {
                    students.add(new Student(
                            rs.getInt("StudentID"),
                            rs.getString("Name"),
                            rs.getInt("Age"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Status")
                    ));
                }
                return students;
            }
        }
    }

}
