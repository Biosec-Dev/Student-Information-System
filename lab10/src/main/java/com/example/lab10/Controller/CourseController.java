package com.example.lab10.Controller;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course);
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public Course enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        return courseService.enrollStudent(courseId, studentId);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public Course removeStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        return courseService.removeStudent(courseId, studentId);
    }

    @GetMapping("/{courseId}/students")
    public List<Student> getEnrolledStudents(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    public Course assignTeacher(@PathVariable Long courseId, @PathVariable Long teacherId) {
        return courseService.assignTeacherToCourse(courseId, teacherId);
    }
}

