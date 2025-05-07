package com.example.lab10.Controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab10.Service.StudentService;
import com.example.lab10.dto.CourseDTO;
import com.example.lab10.dto.StudentDTO;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    private static final Logger logger = Logger.getLogger(StudentController.class.getName());
    
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getStudentProfile() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            logger.info("Getting profile for student: " + email);
            StudentDTO student = studentService.getStudentProfile(email);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            logger.severe("Error getting student profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courses/active")
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            logger.info("Getting active courses for student: " + email);
            List<CourseDTO> courses = studentService.getActiveCoursesForStudent(email);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            logger.severe("Error getting active courses: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courses/pending")
    public ResponseEntity<List<CourseDTO>> getPendingCourses() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            logger.info("Getting pending courses for student: " + email);
            List<CourseDTO> courses = studentService.getPendingCoursesForStudent(email);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            logger.severe("Error getting pending courses: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/courses/request")
    public ResponseEntity<?> requestCourse(@RequestParam Long courseId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            logger.info("Student " + email + " requesting course: " + courseId);
            studentService.requestCourse(email, courseId);
            return ResponseEntity.ok("Enrolled course successfully");
        } catch (Exception e) {
            logger.severe("Error requesting course: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/adviser")
    public ResponseEntity<?> changeAdviser(@RequestParam Long adviserId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            logger.info("Student " + email + " changing adviser to: " + adviserId);
            studentService.changeAdviser(email, adviserId);
            return ResponseEntity.ok().body("Adviser changed successfully");
        } catch (Exception e) {
            logger.severe("Error changing adviser: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.severe("Unhandled exception in StudentController: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}
