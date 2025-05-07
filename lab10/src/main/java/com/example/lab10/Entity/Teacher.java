package com.example.lab10.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String lastName;
    private String department;
    private String email;
    
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Course> courses = new ArrayList<>();
    
    @OneToMany(mappedBy = "adviser", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Student> advisees = new ArrayList<>();

    // Constructors
    public Teacher() {}

    public Teacher(String name, String lastName, String department, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<Student> getAdvisees() { return advisees; }
    public void setAdvisees(List<Student> advisees) { this.advisees = advisees; }
}
