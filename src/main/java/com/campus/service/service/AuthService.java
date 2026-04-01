package com.campus.service.service;

import com.campus.service.config.JwtUtil;
import com.campus.service.dto.LoginRequest;
import com.campus.service.entity.Admin;
import com.campus.service.entity.Staff;
import com.campus.service.entity.Student;
import com.campus.service.repository.AdminRepository;
import com.campus.service.repository.StaffRepository;
import com.campus.service.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepository studentRepository;
    private final StaffRepository   staffRepository;
    private final AdminRepository   adminRepository;
    private final JwtUtil           jwtUtil;
    private final PasswordEncoder   passwordEncoder;

    public AuthService(StudentRepository studentRepository,
                       StaffRepository staffRepository,
                       AdminRepository adminRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.staffRepository   = staffRepository;
        this.adminRepository   = adminRepository;
        this.jwtUtil           = jwtUtil;
        this.passwordEncoder   = passwordEncoder;
    }

    public LoginRequest.Response login(LoginRequest.Request request) {
        String role = request.getRole().toUpperCase();
        switch (role) {
            case "STUDENT": return authenticateStudent(request);
            case "STAFF":   return authenticateStaff(request);
            case "ADMIN":   return authenticateAdmin(request);
            default: throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    private LoginRequest.Response authenticateStudent(LoginRequest.Request req) {
        Student student = studentRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        validatePassword(req.getPassword(), student.getPassword());
        String token = jwtUtil.generateToken(student.getUsername(), "STUDENT", student.getId());
        return new LoginRequest.Response(token, "STUDENT", student.getId(),
                student.getUsername(), student.getFullName(), student.getEmail());
    }

    private LoginRequest.Response authenticateStaff(LoginRequest.Request req) {
        Staff staff = staffRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Staff not found"));
        validatePassword(req.getPassword(), staff.getPassword());
        String token = jwtUtil.generateToken(staff.getUsername(), "STAFF", staff.getId());
        return new LoginRequest.Response(token, "STAFF", staff.getId(),
                staff.getUsername(), staff.getFullName(), staff.getEmail());
    }

    private LoginRequest.Response authenticateAdmin(LoginRequest.Request req) {
        Admin admin = adminRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        validatePassword(req.getPassword(), admin.getPassword());
        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN", admin.getId());
        return new LoginRequest.Response(token, "ADMIN", admin.getId(),
                admin.getUsername(), admin.getFullName(), admin.getEmail());
    }

    private void validatePassword(String raw, String encoded) {
        System.out.println("=== PASSWORD DEBUG ===");
        System.out.println("Raw: " + raw);
        System.out.println("Encoded: " + encoded);
        System.out.println("Match: " + passwordEncoder.matches(raw, encoded));
        System.out.println("=====================");
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
    private LoginRequest.Response authenticateAdmin(LoginRequest.Request req) {
    System.out.println("FRESH HASH: " + passwordEncoder.encode("admin123"));
    // ... rest of code
}
}
