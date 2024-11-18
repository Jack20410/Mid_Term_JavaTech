package com.tdtu.ui;

import com.tdtu.ui.dashboard.AdminDashboard;
import com.tdtu.ui.dashboard.EmployeeDashboard;
import com.tdtu.ui.dashboard.ManagerDashboard;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUserRole;

    public AppFrame() {
        setTitle("Management Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        LoginScreen loginScreen = new LoginScreen(this);
        AdminDashboard adminDashboard = new AdminDashboard(this);
        ManagerDashboard managerDashboard = new ManagerDashboard(this);
        EmployeeDashboard employeeDashboard = new EmployeeDashboard(this);
        StudentManagementUI studentManagementUI = new StudentManagementUI(this);
        UserManagementUI userManagementUI = new UserManagementUI(this);
        ViewStudentsUI viewStudentsUI = new ViewStudentsUI(this);
        StudentDetailsUI studentDetailsUI = new StudentDetailsUI(this);

        // Set names for screens and add them to mainPanel
        loginScreen.setName("LoginScreen");
        adminDashboard.setName("AdminDashboard");
        managerDashboard.setName("ManagerDashboard");
        employeeDashboard.setName("EmployeeDashboard");
        studentManagementUI.setName("StudentManagementUI");
        userManagementUI.setName("UserManagementUI");
        viewStudentsUI.setName("ViewStudentsUI");
        studentDetailsUI.setName("StudentDetailsUI");

        mainPanel.add(loginScreen, "LoginScreen");
        mainPanel.add(adminDashboard, "AdminDashboard");
        mainPanel.add(managerDashboard, "ManagerDashboard");
        mainPanel.add(employeeDashboard, "EmployeeDashboard");
        mainPanel.add(studentManagementUI, "StudentManagementUI");
        mainPanel.add(userManagementUI, "UserManagementUI");
        mainPanel.add(viewStudentsUI, "ViewStudentsUI");
        mainPanel.add(studentDetailsUI, "StudentDetailsUI");

        add(mainPanel);
    }

    // Show a specific screen based on its name
    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }


    public void showScreen(String screenName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, screenName);
    }

    // Retrieve a screen by name for interactions
    public JPanel getScreen(String screenName) {
        for (Component component : mainPanel.getComponents()) {
            if (component.getName() != null && component.getName().equals(screenName)) {
                return (JPanel) component;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppFrame app = new AppFrame();
            app.setVisible(true);
        });
    }
}
