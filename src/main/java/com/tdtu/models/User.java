package com.tdtu.models;

import java.sql.Blob;
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String userName;
    private String role;
    private String passwordHash;
    private Blob profilePicture;
    private boolean isActive;
    private LocalDateTime dateCreated;

    // Constructors
    public User() {}

    public User(int userId, String userName, String role, String passwordHash, Blob profilePicture, boolean isActive, LocalDateTime dateCreated) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.passwordHash = passwordHash;
        this.profilePicture = profilePicture;
        this.isActive = isActive;
        this.dateCreated = dateCreated;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Blob getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Blob profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
