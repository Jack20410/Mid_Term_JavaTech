package com.tdtu.ui;

import com.tdtu.DAO.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginScreen extends JPanel {
    private AppFrame parentFrame;
    private JTextField userText;
    private JPasswordField passwordText;

    public LoginScreen(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout()); // Change layout to BorderLayout

        // Add title at the top
        JLabel titleLabel = new JLabel("Welcome to Student Management!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel for login fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        userText = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordText, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER); // Add form panel to the center

        // Login button functionality
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            try {
                UserDAO userDAO = new UserDAO();
                boolean isAuthenticated = userDAO.authenticateUser(username, password);

                if (isAuthenticated) {
                    String role = userDAO.getUserRole(username);
                    JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);

                    switch (role) {
                        case "Admin":
                            parentFrame.showScreen("AdminDashboard");
                            break;
                        case "Manager":
                            parentFrame.showScreen("ManagerDashboard");
                            break;
                        case "Employee":
                            parentFrame.showScreen("EmployeeDashboard");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void resetFields() {
        userText.setText("");
        passwordText.setText("");
    }
}
