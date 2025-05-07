package com.example.lab10.Controller;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.Teacher;
import com.example.lab10.Service.CourseService;
import com.example.lab10.dto.CourseDTO;
import com.example.lab10.dto.TeacherDTO;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private static final Logger logger = Logger.getLogger(CourseController.class.getName());
    
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        logger.info("Fetching all courses");
        List<Course> courses = courseService.getAllCourses();
        logger.info("Found " + courses.size() + " courses");
        
        // Log some details about each course
        for (Course course : courses) {
            logger.info("Course: " + course.getId() + " - " + course.getCourseNo() + " - " + course.getName() + 
                    " - Teacher: " + (course.getTeacher() != null ? course.getTeacher().getName() : "None"));
        }
        
        // Convert to DTOs
        return courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CourseDTO addCourse(@RequestBody Course course) {
        return convertToDTO(courseService.addCourse(course));
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public CourseDTO enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        return convertToDTO(courseService.enrollStudent(courseId, studentId));
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public CourseDTO removeStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        return convertToDTO(courseService.removeStudent(courseId, studentId));
    }

    @GetMapping("/{courseId}/students")
    public List<Student> getEnrolledStudents(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    public CourseDTO assignTeacher(@PathVariable Long courseId, @PathVariable Long teacherId) {
        return convertToDTO(courseService.assignTeacherToCourse(courseId, teacherId));
    }
    
    // Helper method to convert Course entity to CourseDTO
    private CourseDTO convertToDTO(Course course) {
        if (course == null) {
            return null;
        }
        
        TeacherDTO teacherDTO = null;
        if (course.getTeacher() != null) {
            Teacher teacher = course.getTeacher();
            teacherDTO = new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getLastName(),
                teacher.getDepartment(),
                teacher.getEmail()
            );
        }
        
        return new CourseDTO(
            course.getId(),
            course.getName(),
            course.getCourseNo(),
            teacherDTO
        );
    }
}

