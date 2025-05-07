package com.example.lab10.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.Teacher;

public class repos {
public interface StudentRepository extends JpaRepository<Student, Long> {}
public interface CourseRepository extends JpaRepository<Course, Long> {}
public interface TeacherRepository extends JpaRepository<Teacher, Long> {}

    
}
