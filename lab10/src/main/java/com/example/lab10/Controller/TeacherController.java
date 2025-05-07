package com.example.lab10.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab10.Entity.StudentCourse;
import com.example.lab10.Service.TeacherService;
import com.example.lab10.dto.CourseDTO;
import com.example.lab10.dto.EnrollmentDTO;
import com.example.lab10.dto.StudentDTO;
import com.example.lab10.dto.TeacherDTO;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/profile")
    public ResponseEntity<TeacherDTO> getTeacherProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        TeacherDTO teacher = teacherService.getTeacherProfile(email);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<CourseDTO> courses = teacherService.getCoursesForTeacher(email);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/advisees")
    public ResponseEntity<List<StudentDTO>> getAdvisees() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<StudentDTO> advisees = teacherService.getAdviseesForTeacher(email);
        return ResponseEntity.ok(advisees);
    }

    @GetMapping("/courses/pending-enrollments")
    public ResponseEntity<?> getPendingEnrollments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<StudentCourse> pendingEnrollments = teacherService.getPendingCourseEnrollments(email);
        
        // Convert to DTOs
        List<EnrollmentDTO> result = pendingEnrollments.stream()
            .map(enrollment -> new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getStudent().getName() + " " + enrollment.getStudent().getLastName(),
                enrollment.getCourse().getName(),
                enrollment.getStatus().toString()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/courses/approve")
    public ResponseEntity<?> approveCourseEnrollment(@RequestParam Long studentCourseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        teacherService.approveCourseEnrollment(email, studentCourseId);
        return ResponseEntity.ok().build();
    }
}
