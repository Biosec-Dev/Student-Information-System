package com.example.lab10.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.StudentCourse;
import com.example.lab10.Entity.StudentCourse.CourseStatus;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudentAndStatus(Student student, CourseStatus status);
    List<StudentCourse> findByStudent(Student student);
    Optional<StudentCourse> findByStudentAndCourse(Student student, Course course);
    Optional<StudentCourse> findByCourseIdAndStudentId(Long courseId, Long studentId);
} 