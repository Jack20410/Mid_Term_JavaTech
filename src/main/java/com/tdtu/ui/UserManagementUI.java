package com.tdtu.ui;

import com.tdtu.DAO.UserDAO;
import com.tdtu.utils.PasswordUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementUI extends JPanel {
    private AppFrame parentFrame;

    public UserManagementUI(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());

        // Table for displaying users
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Role", "Active"}, 0);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons for CRUD operations
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add User");
        JButton deleteButton = new JButton("Delete User");
        JButton updateButton = new JButton("Update User");
        JButton backButton = new JButton("Back");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        loadUserData(tableModel);

        // Add User
        addButton.addActionListener(e -> {
            JTextField userNameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Manager", "Employee"});
            JCheckBox activeCheckBox = new JCheckBox("Active");

            JPanel inputPanel = new JPanel(new GridLayout(4, 2));
            inputPanel.add(new JLabel("Username:"));
            inputPanel.add(userNameField);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);
            inputPanel.add(new JLabel("Role:"));
            inputPanel.add(roleCombo);
            inputPanel.add(new JLabel("Active:"));
            inputPanel.add(activeCheckBox);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String userName = userNameField.getText();
                    String password = new String(passwordField.getPassword());
                    String hashedPassword = PasswordUtil.hashPassword(password); // Hash the password
                    String role = (String) roleCombo.getSelectedItem();
                    boolean isActive = activeCheckBox.isSelected();

                    new UserDAO().addUser(userName, hashedPassword, role, isActive); // Pass hashed password
                    loadUserData(tableModel);
                    JOptionPane.showMessageDialog(this, "User added successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Update User
        updateButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update.");
                return;
            }

            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            JTextField userNameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Manager", "Employee"});
            roleCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            JCheckBox activeCheckBox = new JCheckBox("Active", Boolean.parseBoolean(tableModel.getValueAt(selectedRow, 3).toString()));

            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("Username:"));
            inputPanel.add(userNameField);
            inputPanel.add(new JLabel("Role:"));
            inputPanel.add(roleCombo);
            inputPanel.add(new JLabel("Active:"));
            inputPanel.add(activeCheckBox);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    new UserDAO().updateUser(
                            userId,
                            userNameField.getText(),
                            (String) roleCombo.getSelectedItem(),
                            activeCheckBox.isSelected()
                    );
                    JOptionPane.showMessageDialog(this, "User updated successfully!");
                    loadUserData(tableModel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete User  Functionality
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                return;
            }

            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    new UserDAO().deleteUser(userId);
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    loadUserData(tableModel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back button
        backButton.addActionListener(e -> parentFrame.showScreen("AdminDashboard"));
    }

    private void loadUserData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try {
            List<String[]> users = new UserDAO().getAllUsers();
            for (String[] user : users) {
                tableModel.addRow(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
