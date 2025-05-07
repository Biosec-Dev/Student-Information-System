package com.example.lab10.Controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/create-pending-enrollment")
    public ResponseEntity<?> createPendingEnrollment() {
        try {
            // Read the SQL script
            InputStream is = getClass().getResourceAsStream("/test-data.sql");
            if (is == null) {
                return ResponseEntity.badRequest().body("SQL script not found");
            }
            
            String sql = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
            
            // Execute the SQL script
            jdbcTemplate.execute(sql);
            
            return ResponseEntity.ok("Pending enrollment created for testing");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating test data: " + e.getMessage());
        }
    }
    
    @PostMapping("/create-direct-enrollment")
    public ResponseEntity<?> createDirectEnrollment() {
        try {
            // Execute direct SQL queries
            
            // Bob (ID 2) is advised by Jane (ID 2)
            jdbcTemplate.update("INSERT INTO student_courses (student_id, course_id, status) VALUES (?, ?, ?)", 
                2, 2, "PENDING");
            
            // Alice (ID 1) is advised by John (ID 1)
            jdbcTemplate.update("INSERT INTO student_courses (student_id, course_id, status) VALUES (?, ?, ?)", 
                1, 3, "PENDING");
            
            return ResponseEntity.ok("Pending enrollments created directly");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating direct enrollments: " + e.getMessage());
        }
    }
} 