// Corrected StudentDetailsUI.java

package com.tdtu.ui;

import com.tdtu.DAO.CertificateDAO;
import com.tdtu.DAO.StudentDAO;
import com.tdtu.models.Certificate;
import com.tdtu.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentDetailsUI extends JPanel {
    private AppFrame parentFrame;
    private JLabel studentNameLabel;
    private JLabel studentAgeLabel;
    private JLabel studentPhoneLabel;
    private JLabel studentStatusLabel;
    private DefaultTableModel certificateTableModel;

    private int currentStudentId;

    public StudentDetailsUI(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());

        // Student Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        studentNameLabel = new JLabel();
        studentAgeLabel = new JLabel();
        studentPhoneLabel = new JLabel();
        studentStatusLabel = new JLabel();

        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(studentNameLabel);
        detailsPanel.add(new JLabel("Age:"));
        detailsPanel.add(studentAgeLabel);
        detailsPanel.add(new JLabel("Phone:"));
        detailsPanel.add(studentPhoneLabel);
        detailsPanel.add(new JLabel("Status:"));
        detailsPanel.add(studentStatusLabel);

        add(detailsPanel, BorderLayout.NORTH);

        // Certificate Table
        certificateTableModel = new DefaultTableModel(new String[]{"Certificate ID", "Certificate Name", "Issued Date"}, 0);
        JTable certificateTable = new JTable(certificateTableModel);
        JScrollPane scrollPane = new JScrollPane(certificateTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Certificates"));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addCertificateButton = new JButton("Add Certificate");
        JButton deleteCertificateButton = new JButton("Delete Certificate");
        JButton backButton = new JButton("Back");
        buttonPanel.add(addCertificateButton);
        buttonPanel.add(deleteCertificateButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addCertificateButton.addActionListener(e -> addCertificate());
        deleteCertificateButton.addActionListener(e -> deleteCertificate(certificateTable));
        backButton.addActionListener(e -> {
            if ("Admin".equals(parentFrame.getCurrentUserRole())) {

                parentFrame.showScreen("StudentManagementUI");
            } else if ("Manager".equals(parentFrame.getCurrentUserRole())) {

                parentFrame.showScreen("StudentManagementUI");
            }
        });
    }

    public void loadStudentDetails(int studentId) {
        try {
            this.currentStudentId = studentId;

            // Load student details
            Student student = new StudentDAO().getStudentById(studentId);
            studentNameLabel.setText(student.getName());
            studentAgeLabel.setText(String.valueOf(student.getAge()));
            studentPhoneLabel.setText(student.getPhoneNumber());
            studentStatusLabel.setText(student.getStatus());

            // Load certificates
            loadCertificates(studentId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCertificates(int studentId) {
        certificateTableModel.setRowCount(0);
        try {
            List<Certificate> certificates = new CertificateDAO().getCertificatesByStudentId(studentId);
            for (Certificate certificate : certificates) {
                certificateTableModel.addRow(new Object[]{
                        certificate.getCertificateId(),
                        certificate.getCertificateName(),
                        certificate.getIssuedDate()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading certificates: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCertificate() {
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Certificate Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Issued Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Certificate", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Convert date string to java.sql.Date
                String dateText = dateField.getText();
                java.sql.Date issuedDate;
                try {
                    issuedDate = java.sql.Date.valueOf(dateText); // Ensure correct date format (YYYY-MM-DD)
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call DAO to add certificate
                new CertificateDAO().addCertificate(currentStudentId, nameField.getText(), issuedDate);
                loadCertificates(currentStudentId); // Reload certificates
                JOptionPane.showMessageDialog(this, "Certificate added successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding certificate: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCertificate(JTable certificateTable) {
        int selectedRow = certificateTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a certificate to delete.");
            return;
        }

        int certificateId = (int) certificateTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this certificate?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new CertificateDAO().deleteCertificate(certificateId);
                loadCertificates(currentStudentId); // Reload certificates
                JOptionPane.showMessageDialog(this, "Certificate deleted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting certificate: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
