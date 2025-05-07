package com.example.lab10.Service;

import java.util.ArrayList;
import java.util.List;
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
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;

    public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository,
                           StudentRepository studentRepository, StudentCourseRepository studentCourseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    public TeacherDTO getTeacherProfile(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return convertToDTO(teacher);
    }

    public List<CourseDTO> getCoursesForTeacher(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        return courseRepository.findByTeacher(teacher)
                .stream()
                .map(course -> convertToCourseDTO(course, null))
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getAdviseesForTeacher(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        return teacher.getAdvisees()
                .stream()
                .map(this::convertToStudentDTO)
                .collect(Collectors.toList());
    }

    public List<StudentCourse> getPendingCourseEnrollments(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // Collect all pending enrollments where teacher is the adviser of the student
        List<StudentCourse> pendingEnrollments = new ArrayList<>();
        
        // Get enrollments for students where this teacher is the adviser
        List<Student> advisees = teacher.getAdvisees();
        for (Student advisee : advisees) {
            for (StudentCourse enrollment : advisee.getStudentCourses()) {
                // Add if status is PENDING
                if (enrollment.getStatus() == StudentCourse.CourseStatus.PENDING) {
                    pendingEnrollments.add(enrollment);
                }
            }
        }
        
        return pendingEnrollments;
    }

    public void approveCourseEnrollment(String email, Long studentCourseId) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        StudentCourse studentCourse = studentCourseRepository.findById(studentCourseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        
        // Check if the teacher is the student's adviser
        boolean isAdviserOfStudent = studentCourse.getStudent().getAdviser() != null && 
                                    studentCourse.getStudent().getAdviser().getId().equals(teacher.getId());
        
        if (!isAdviserOfStudent) {
            throw new RuntimeException("Not authorized to approve this course. You can only approve courses for your advisees.");
        }
        
        // Change status to ACTIVE
        studentCourse.setStatus(StudentCourse.CourseStatus.ACTIVE);
        studentCourseRepository.save(studentCourse);
    }

    private TeacherDTO convertToDTO(Teacher teacher) {
        return new TeacherDTO(
            teacher.getId(),
            teacher.getName(),
            teacher.getLastName(),
            teacher.getDepartment(),
            teacher.getEmail()
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
            status != null ? status : "ACTIVE" // Default to ACTIVE for teacher's own courses
        );
    }

    private StudentDTO convertToStudentDTO(Student student) {
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
}