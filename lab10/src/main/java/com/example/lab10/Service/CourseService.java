package com.example.lab10.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.StudentCourse;
import com.example.lab10.Entity.StudentCourse.CourseStatus;
import com.example.lab10.Entity.Teacher;
import com.example.lab10.Repository.CourseRepository;
import com.example.lab10.Repository.StudentCourseRepository;
import com.example.lab10.Repository.StudentRepository;
import com.example.lab10.Repository.TeacherRepository;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Course enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Create a new StudentCourse entity
        StudentCourse studentCourse = new StudentCourse(student, course, CourseStatus.ACTIVE);
        studentCourseRepository.save(studentCourse);
        
        return course;
    }

    public Course removeStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Find and remove the StudentCourse entity
        Optional<StudentCourse> studentCourse = studentCourseRepository.findByStudentAndCourse(student, course);
        studentCourse.ifPresent(sc -> studentCourseRepository.delete(sc));
        
        return course;
    }

    public List<Student> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        
        return course.getStudentCourses().stream()
            .map(StudentCourse::getStudent)
            .collect(Collectors.toList());
    }

    // Additional methods needed based on the error message
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
    
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }
}