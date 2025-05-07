package com.example.lab10.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab10.Entity.Student;
import com.example.lab10.Entity.Teacher;
import com.example.lab10.Repository.StudentRepository;
import com.example.lab10.Repository.TeacherRepository;
import com.example.lab10.Service.UserDetailsServiceImpl;
import com.example.lab10.dto.AuthRequest;
import com.example.lab10.dto.AuthResponse;
import com.example.lab10.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, 
                         UserDetailsServiceImpl userDetailsService, 
                         JwtUtil jwtUtil,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository,
                         PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Login attempt with email: " + authRequest.getEmail());
            System.out.println("Password provided: " + authRequest.getPassword());
            
            // Check if user exists in the database
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
                System.out.println("User found in database: " + userDetails.getUsername());
                System.out.println("Stored password hash: " + userDetails.getPassword());
                
                // Hash the provided password for comparison
                String providedPasswordHash = passwordEncoder.encode(authRequest.getPassword());
                System.out.println("Provided password hash: " + providedPasswordHash);
                
                // Check if passwords match
                boolean passwordsMatch = passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword());
                System.out.println("Passwords match: " + passwordsMatch);
                
                if (!passwordsMatch) {
                    System.out.println("Password mismatch for user: " + authRequest.getEmail());
                    return ResponseEntity.status(401).body("Incorrect username or password");
                }
                
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
                );
                
                final String role = userDetailsService.getUserRole(authRequest.getEmail());
                final String jwt = jwtUtil.generateToken(userDetails, role);
                
                System.out.println("Authentication successful for: " + authRequest.getEmail() + " with role: " + role);
                return ResponseEntity.ok(new AuthResponse(jwt, role));
            } catch (UsernameNotFoundException e) {
                System.out.println("User not found in database: " + authRequest.getEmail());
                return ResponseEntity.status(401).body("Incorrect username or password");
            }
        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed: BadCredentialsException - " + e.getMessage());
            return ResponseEntity.status(401).body("Incorrect username or password");
        } catch (Exception e) {
            System.out.println("Authentication failed: Unexpected error - " + e.getClass().getName() + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Authentication error: " + e.getMessage());
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> testAuth() {
        return ResponseEntity.ok("Authentication endpoint is working");
    }
    
    @GetMapping("/diagnostics")
    public ResponseEntity<?> diagnostics() {
        Map<String, Object> response = new HashMap<>();
        
        // Check if users exist in database
        long teacherCount = teacherRepository.count();
        long studentCount = studentRepository.count();
        
        response.put("teacherCount", teacherCount);
        response.put("studentCount", studentCount);
        
        // Check specific test users
        Optional<Teacher> johnDoe = teacherRepository.findByEmail("john.doe@example.com");
        Optional<Student> alice = studentRepository.findByEmail("alice@example.com");
        
        Map<String, Object> testUsers = new HashMap<>();
        
        if (johnDoe.isPresent()) {
            Teacher teacher = johnDoe.get();
            Map<String, Object> teacherData = new HashMap<>();
            teacherData.put("id", teacher.getId());
            teacherData.put("name", teacher.getName());
            teacherData.put("lastName", teacher.getLastName());
            teacherData.put("email", teacher.getEmail());
            teacherData.put("passwordHash", teacher.getPassword());
            teacherData.put("isValidPassword", passwordEncoder.matches("password", teacher.getPassword()));
            testUsers.put("johnDoe", teacherData);
        } else {
            testUsers.put("johnDoe", "Not found");
        }
        
        if (alice.isPresent()) {
            Student student = alice.get();
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("id", student.getId());
            studentData.put("name", student.getName());
            studentData.put("lastName", student.getLastName());
            studentData.put("email", student.getEmail());
            studentData.put("passwordHash", student.getPassword());
            studentData.put("isValidPassword", passwordEncoder.matches("password", student.getPassword()));
            testUsers.put("alice", studentData);
        } else {
            testUsers.put("alice", "Not found");
        }
        
        response.put("testUsers", testUsers);
        
        // Add JWT info
        Map<String, Object> jwtInfo = new HashMap<>();
        jwtInfo.put("algorithm", "HS256");
        jwtInfo.put("expiration", jwtUtil.getJwtExpiration());
        response.put("jwtConfig", jwtInfo);
        
        return ResponseEntity.ok(response);
    }
} 