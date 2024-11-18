package com.tdtu.ui;

import com.tdtu.DAO.StudentDAO;
import com.tdtu.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ViewStudentsUI extends JPanel {
    private AppFrame parentFrame;
    private DefaultTableModel tableModel;

    public ViewStudentsUI(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());

        // Top Panel for Sort and Search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sortLabel = new JLabel("Sort By:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"None", "Name", "Age", "Status"});
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        topPanel.add(sortLabel);
        topPanel.add(sortComboBox);
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Phone", "Status"}, 0);
        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> parentFrame.showScreen("EmployeeDashboard"));
        add(backButton, BorderLayout.SOUTH);

        // Load data into table
        loadStudentData(null, null);

        // Sort Functionality
        sortComboBox.addActionListener(e -> {
            String criteria = (String) sortComboBox.getSelectedItem();
            if ("None".equals(criteria)) {
                loadStudentData(null, null); // Reset to unsorted data
            } else {
                loadSortedData(criteria);
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
            ex.printStackTrace();
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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sorting student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
