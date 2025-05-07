package com.example.lab10.Service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.Teacher;
import com.example.lab10.Repository.StudentRepository;
import com.example.lab10.Repository.TeacherRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserDetailsServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find a teacher with the given email
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            return new User(
                    teacher.get().getEmail(),
                    teacher.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER"))
            );
        }

        // Try to find a student with the given email
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return new User(
                    student.get().getEmail(),
                    student.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
    
    public String getUserRole(String email) {
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            return "ROLE_TEACHER";
        }
        
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return "ROLE_STUDENT";
        }
        
        return null;
    }
} 