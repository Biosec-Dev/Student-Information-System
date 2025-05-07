package com.example.lab10.dto;

public class TeacherDTO {
    private Long id;
    private String name;
    private String lastName;
    private String department;
    private String email;

    // Constructors, getters, and setters
    public TeacherDTO() {
    }

    public TeacherDTO(Long id, String name, String lastName, String department, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 