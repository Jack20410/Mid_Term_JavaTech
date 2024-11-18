CREATE DATABASE StudentInfoManagement;

USE StudentInfoManagement;

-- Users Table
CREATE TABLE Users (
                       UserID INT AUTO_INCREMENT PRIMARY KEY,
                       UserName VARCHAR(100),
                       Role ENUM('Admin', 'Manager', 'Employee'),
                       PasswordHash VARCHAR(255),
                       ProfilePicture BLOB,
                       IsActive BOOLEAN DEFAULT TRUE,
                       DateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students Table
CREATE TABLE Students (
                          StudentID INT AUTO_INCREMENT PRIMARY KEY,
                          Name VARCHAR(100),
                          Age INT,
                          PhoneNumber VARCHAR(15),
                          Status ENUM('Normal', 'Locked'),
                          DateAdded TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Certificates Table
CREATE TABLE Certificates (
                              CertificateID INT AUTO_INCREMENT PRIMARY KEY,
                              StudentID INT,
                              CertificateName VARCHAR(255),
                              IssuedDate DATE,
                              FOREIGN KEY (StudentID) REFERENCES Students(StudentID) ON DELETE CASCADE
);

-- Login History Table
CREATE TABLE LoginHistory (
                              LogID INT AUTO_INCREMENT PRIMARY KEY,
                              UserID INT,
                              LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Insert a default Admin user
INSERT INTO Users (UserName, Role, PasswordHash, IsActive)
VALUES ('Admin', 'Admin', MD5('admin123'), TRUE);
