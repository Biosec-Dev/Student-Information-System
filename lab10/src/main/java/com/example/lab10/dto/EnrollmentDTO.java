package com.example.lab10.dto;

public class EnrollmentDTO {
    private Long id;
    private String studentName;
    private String courseName;
    private String status;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long id, String studentName, String courseName, String status) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 