package com.example.lab10.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.lab10.Entity.Course;
import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.StudentCourse;
import com.example.lab10.Entity.Teacher;
import com.example.lab10.Repository.CourseRepository;
import com.example.lab10.Repository.StudentCourseRepository;
import com.example.lab10.Repository.StudentRepository;
import com.example.lab10.Repository.TeacherRepository;
import com.example.lab10.dto.CourseDTO;
import com.example.lab10.dto.StudentDTO;
import com.example.lab10.dto.TeacherDTO;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    public StudentService(StudentRepository studentRepository, TeacherRepository teacherRepository,
                          CourseRepository courseRepository, StudentCourseRepository studentCourseRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    public StudentDTO getStudentProfile(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return convertToDTO(student);
    }

    public List<CourseDTO> getActiveCoursesForStudent(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return studentCourseRepository.findByStudentAndStatus(student, StudentCourse.CourseStatus.ACTIVE)
                .stream()
                .map(sc -> convertToCourseDTO(sc.getCourse(), sc.getStatus().toString()))
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getPendingCoursesForStudent(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return studentCourseRepository.findByStudentAndStatus(student, StudentCourse.CourseStatus.PENDING)
                .stream()
                .map(sc -> convertToCourseDTO(sc.getCourse(), sc.getStatus().toString()))
                .collect(Collectors.toList());
    }

    public void requestCourse(String email, Long courseId) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        // Check if already enrolled
        Optional<StudentCourse> existingEnrollment = studentCourseRepository.findByStudentAndCourse(student, course);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Already enrolled in this course");
        }
        
        // Create new pending enrollment
        StudentCourse studentCourse = new StudentCourse(student, course, StudentCourse.CourseStatus.PENDING);
        studentCourseRepository.save(studentCourse);
    }

    public void changeAdviser(String email, Long adviserId) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Teacher adviser = teacherRepository.findById(adviserId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        student.setAdviser(adviser);
        studentRepository.save(student);
    }

    private StudentDTO convertToDTO(Student student) {
        TeacherDTO adviserDTO = null;
        if (student.getAdviser() != null) {
            adviserDTO = new TeacherDTO(
                student.getAdviser().getId(),
                student.getAdviser().getName(),
                student.getAdviser().getLastName(),
                student.getAdviser().getDepartment(),
                student.getAdviser().getEmail()
            );
        }
        
        return new StudentDTO(
            student.getId(),
            student.getName(),
            student.getLastName(),
            student.getEmail(),
            student.getStudentNo(),
            student.getEnrollmentDate(),
            adviserDTO
        );
    }

    private CourseDTO convertToCourseDTO(Course course, String status) {
        TeacherDTO teacherDTO = new TeacherDTO(
            course.getTeacher().getId(),
            course.getTeacher().getName(),
            course.getTeacher().getLastName(),
            course.getTeacher().getDepartment(),
            course.getTeacher().getEmail()
        );
        
        return new CourseDTO(
            course.getId(),
            course.getName(),
            course.getCourseNo(),
            teacherDTO,
            status
        );
    }
}