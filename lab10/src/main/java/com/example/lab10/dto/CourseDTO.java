package com.example.lab10.dto;

public class CourseDTO {
    private Long id;
    private String name;
    private String courseNo;
    private TeacherDTO teacher;
    private String status; // ACTIVE or PENDING

    // Constructors, getters, and setters
    public CourseDTO() {
    }

    public CourseDTO(Long id, String name, String courseNo, TeacherDTO teacher, String status) {
        this.id = id;
        this.name = name;
        this.courseNo = courseNo;
        this.teacher = teacher;
        this.status = status;
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

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 