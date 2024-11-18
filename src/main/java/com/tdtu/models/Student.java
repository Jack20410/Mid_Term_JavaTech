package com.tdtu.models;

public class Student {
    private int studentId;
    private String name;
    private int age;
    private String phoneNumber;
    private String status;

    public Student() {}

    public Student(int studentId, String name, int age, String phoneNumber, String status) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

