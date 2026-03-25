package com.campus.service.service;

import com.campus.service.dto.StaffRegisterRequest;
import com.campus.service.entity.Department;
import com.campus.service.entity.Staff;
import com.campus.service.repository.DepartmentRepository;
import com.campus.service.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StaffService {

    private final StaffRepository      staffRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder      passwordEncoder;

    public StaffService(StaffRepository staffRepository,
                        DepartmentRepository departmentRepository,
                        PasswordEncoder passwordEncoder) {
        this.staffRepository      = staffRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder      = passwordEncoder;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found: " + id));
    }

    public Staff getStaffByUsername(String username) {
        return staffRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found: " + username));
    }

    public List<Staff> getStaffByDepartment(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Staff registerStaff(StaffRegisterRequest req) {
        if (staffRepository.existsByUsername(req.getUsername()))
            throw new IllegalStateException("Username already taken");
        if (staffRepository.existsByEmail(req.getEmail()))
            throw new IllegalStateException("Email already registered");

        Staff staff = new Staff();
        staff.setUsername(req.getUsername());
        staff.setEmail(req.getEmail());
        staff.setFullName(req.getFullName());
        staff.setPassword(passwordEncoder.encode(req.getPassword()));

        if (req.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(req.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found: " + req.getDepartmentId()));
            staff.setDepartment(dept);
        }

        return staffRepository.save(staff);
    }

    @Transactional
    public Staff updateStaff(Long id, java.util.Map<String, Object> updates) {
        Staff existing = getStaffById(id);
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(existing.getUsername())) {
                if (staffRepository.existsByUsername(newUsername)) {
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
        return staffRepository.save(existing);
    }

    @Transactional
    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id))
            throw new EntityNotFoundException("Staff not found: " + id);
        staffRepository.deleteById(id);
    }
}