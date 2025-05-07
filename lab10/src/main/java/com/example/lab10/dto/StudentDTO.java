package com.example.lab10.dto;

import java.time.LocalDate;

public class StudentDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String studentNo;
    private LocalDate enrollmentDate;
    private TeacherDTO adviser;

    // Constructors, getters, and setters
    public StudentDTO() {
    }

    public StudentDTO(Long id, String name, String lastName, String email, String studentNo, LocalDate enrollmentDate, TeacherDTO adviser) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studentNo = studentNo;
        this.enrollmentDate = enrollmentDate;
        this.adviser = adviser;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public TeacherDTO getAdviser() {
        return adviser;
    }

    public void setAdviser(TeacherDTO adviser) {
        this.adviser = adviser;
    }
} 