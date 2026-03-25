package com.campus.service.service;

import com.campus.service.entity.Student;
import com.campus.service.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder   passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + username));
    }

    @Transactional
    public Student registerStudent(Student student) {
        if (studentRepository.existsByUsername(student.getUsername()))
            throw new IllegalStateException("Username already taken");
        if (studentRepository.existsByEmail(student.getEmail()))
            throw new IllegalStateException("Email already registered");
        if (student.getRollNumber() != null && studentRepository.existsByRollNumber(student.getRollNumber()))
            throw new IllegalStateException("Roll number already exists");

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, java.util.Map<String, Object> updates) {
        Student existing = getStudentById(id);
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(existing.getUsername())) {
                if (studentRepository.existsByUsername(newUsername)) {
                    throw new IllegalStateException("Username already taken");
                }
                existing.setUsername(newUsername);
            }
        }
        if (updates.containsKey("password")) {
            String newPass = (String) updates.get("password");
            if (newPass != null && !newPass.trim().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(newPass));
            }
        }
        if (updates.containsKey("fullName") && updates.get("fullName") != null) {
            existing.setFullName((String) updates.get("fullName"));
        }
        if (updates.containsKey("email") && updates.get("email") != null) {
            existing.setEmail((String) updates.get("email"));
        }
        return studentRepository.save(existing);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            throw new EntityNotFoundException("Student not found: " + id);
        studentRepository.deleteById(id);
    }
}