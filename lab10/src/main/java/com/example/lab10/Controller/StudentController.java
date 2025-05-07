package com.example.lab10.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getStudentProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        StudentDTO student = studentService.getStudentProfile(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/courses/active")
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<CourseDTO> courses = studentService.getActiveCoursesForStudent(email);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/pending")
    public ResponseEntity<List<CourseDTO>> getPendingCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<CourseDTO> courses = studentService.getPendingCoursesForStudent(email);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses/request")
    public ResponseEntity<?> requestCourse(@RequestParam Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        studentService.requestCourse(email, courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/adviser")
    public ResponseEntity<?> changeAdviser(@RequestParam Long adviserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        studentService.changeAdviser(email, adviserId);
        return ResponseEntity.ok().build();
    }
}
