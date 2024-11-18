package com.tdtu.DAO;

import com.tdtu.models.Certificate;
import com.tdtu.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAO {

    // Fetch certificates for a specific student
    public List<Certificate> getCertificatesByStudentId(int studentId) throws SQLException {
        String sql = "SELECT * FROM Certificates WHERE StudentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Certificate> certificates = new ArrayList<>();
                while (rs.next()) {
                    certificates.add(new Certificate(
                            rs.getInt("CertificateID"),
                            rs.getString("CertificateName"),
                            rs.getDate("IssuedDate")
                    ));
                }
                return certificates;
            }
        }
    }

    // Add a new certificate
    public void addCertificate(int studentId, String certificateName, Date issuedDate) throws SQLException {
        String sql = "INSERT INTO Certificates (StudentID, CertificateName, IssuedDate) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setString(2, certificateName);
            stmt.setDate(3, issuedDate);
            stmt.executeUpdate();
        }
    }

    // Delete a certificate
    public void deleteCertificate(int certificateId) throws SQLException {
        String sql = "DELETE FROM Certificates WHERE CertificateID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, certificateId);
            stmt.executeUpdate();
        }
    }

    // Update a certificate
    public void updateCertificate(int certificateId, String certificateName, Date issuedDate) throws SQLException {
        String sql = "UPDATE Certificates SET CertificateName = ?, IssuedDate = ? WHERE CertificateID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, certificateName);
            stmt.setDate(2, issuedDate);
            stmt.setInt(3, certificateId);
            stmt.executeUpdate();
        }
    }
}
