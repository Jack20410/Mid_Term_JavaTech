package com.tdtu.ui;

import com.tdtu.DAO.StudentDAO;
import com.tdtu.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentManagementUI extends JPanel {
    private AppFrame parentFrame;
    private DefaultTableModel tableModel;

    public StudentManagementUI(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());

        // Top Panel with Search and Sort
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sortLabel = new JLabel("Sort By:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"None", "Name", "Age", "Status"});
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton sortButton = new JButton("Sort");

        topPanel.add(sortLabel);
        topPanel.add(sortComboBox);
        topPanel.add(sortButton);
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Phone", "Status"}, 0);
        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");
        JButton detailButton = new JButton("Detail");

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(detailButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load data into the table
        loadStudentData(null, null);

        // Action Listeners
        addButton.addActionListener(e -> addStudent());
        deleteButton.addActionListener(e -> deleteStudent(studentTable));
        updateButton.addActionListener(e -> updateStudent(studentTable));
        backButton.addActionListener(e -> {
            if ("Admin".equals(parentFrame.getCurrentUserRole())) {
                parentFrame.showScreen("AdminDashboard");
            } else if ("Manager".equals(parentFrame.getCurrentUserRole())) {
                parentFrame.showScreen("ManagerDashboard");
            }
        });
        detailButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if ("Admin".equals(parentFrame.getCurrentUserRole()) && selectedRow != -1) {
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                parentFrame.showScreen("StudentDetailsUI");
                ((StudentDetailsUI) parentFrame.getScreen("StudentDetailsUI")).loadStudentDetails(studentId);

            } else if ("Manager".equals(parentFrame.getCurrentUserRole()) && selectedRow != -1) {
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                parentFrame.showScreen("StudentDetailsUI");
                ((StudentDetailsUI) parentFrame.getScreen("StudentDetailsUI")).loadStudentDetails(studentId);

            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to view details.");
            }
        });

        // Sort Functionality
        sortButton.addActionListener(e -> {
            String selectedCriteria = (String) sortComboBox.getSelectedItem();
            if ("None".equals(selectedCriteria)) {
                loadStudentData(null, null); // Reset to unsorted data
            } else {
                loadSortedData(selectedCriteria);
            }
        });

        // Search Functionality
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText();
            loadStudentData(searchQuery, "All"); // Default search with no status filter
        });
    }

    private void loadStudentData(String searchQuery, String statusFilter) {
        tableModel.setRowCount(0); // Clear existing data
        try {
            StudentDAO studentDAO = new StudentDAO();
            List<Student> students;

            if (searchQuery != null) {
                students = studentDAO.searchStudents(searchQuery, statusFilter);
            } else {
                students = studentDAO.getAllStudents();
            }

            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getStudentId(),
                        student.getName(),
                        student.getAge(),
                        student.getPhoneNumber(),
                        student.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSortedData(String criteria) {
        tableModel.setRowCount(0); // Clear existing data
        try {
            StudentDAO studentDAO = new StudentDAO();
            List<Student> students = studentDAO.getSortedStudents(criteria);

            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getStudentId(),
                        student.getName(),
                        student.getAge(),
                        student.getPhoneNumber(),
                        student.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Normal", "Locked"});

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.addStudent(nameField.getText(), Integer.parseInt(ageField.getText()), phoneField.getText(), (String) statusCombo.getSelectedItem());
                loadStudentData(null, null); // Reload data
                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteStudent(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new StudentDAO().deleteStudent(studentId);
                loadStudentData(null, null); // Reload data
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStudent(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField ageField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        JTextField phoneField = new JTextField((String) tableModel.getValueAt(selectedRow, 3));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Normal", "Locked"});
        statusCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 4));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.updateStudent(studentId, nameField.getText(), Integer.parseInt(ageField.getText()), phoneField.getText(), (String) statusCombo.getSelectedItem());
                loadStudentData(null, null); // Reload data
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
