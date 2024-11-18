package com.tdtu.ui.dashboard;

import com.tdtu.DAO.UserDAO;
import com.tdtu.ui.AppFrame;
import com.tdtu.ui.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class EmployeeDashboard extends JPanel {
    private AppFrame parentFrame;
    private JLabel avatarLabel;
    private String currentUserName = "Employee";

    public EmployeeDashboard(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout()); // Center content using GridBagLayout

        // Create a panel for the avatar and button
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the avatar label
        avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(100, 100)); // Fixed size for avatar
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        avatarPanel.add(avatarLabel);

        // Add some spacing between avatar and button
        avatarPanel.add(Box.createVerticalStrut(10));

        // Update avatar button
        JButton updateAvatarButton = new JButton("Change Image");
        updateAvatarButton.setPreferredSize(new Dimension(200, 30)); // Adjusted size
        //updateAvatarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateAvatarButton.addActionListener(e -> updateAvatar());
        avatarPanel.add(updateAvatarButton);

        // Add the avatar panel to the top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10)); // Add spacing
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        topPanel.add(avatarPanel, BorderLayout.WEST);

        // Add a welcome message in the center
        JLabel welcomeLabel = new JLabel("Welcome, Employee!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add the top panel to the main frame
        add(topPanel, BorderLayout.NORTH);

        // Center panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewStudentsButton = new JButton("View Students");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(viewStudentsButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Button actions
        viewStudentsButton.addActionListener(e -> parentFrame.showScreen("ViewStudentsUI"));

        logoutButton.addActionListener(e -> {
            // Reset the LoginScreen fields and navigate back to the login screen
            LoginScreen loginScreen = (LoginScreen) parentFrame.getScreen("LoginScreen");
            if (loginScreen != null) {
                loginScreen.resetFields();
            }
            parentFrame.showScreen("LoginScreen");
        });

        loadAvatar();
    }

    private void loadAvatar() {
        try {
            byte[] avatarData = new UserDAO().getProfilePicture(currentUserName);
            if (avatarData != null) {
                ImageIcon avatarIcon = new ImageIcon(avatarData);
                Image scaledAvatar = avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(scaledAvatar));
            } else {
                // Load default avatar from resources
                ImageIcon defaultAvatarIcon = new ImageIcon(getClass().getResource("/default-avatar.png"));
                Image scaledDefaultAvatar = defaultAvatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(scaledDefaultAvatar));
            }
        } catch (Exception ex) {
            avatarLabel.setText("Error loading avatar");
        }
    }

    private void updateAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());
                new UserDAO().updateProfilePicture(currentUserName, imageData);
                JOptionPane.showMessageDialog(this, "Avatar updated successfully!");
                loadAvatar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating avatar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
