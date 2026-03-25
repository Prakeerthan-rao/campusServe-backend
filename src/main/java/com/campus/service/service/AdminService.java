package com.campus.service.service;

import com.campus.service.entity.Admin;
import com.campus.service.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + id));
    }

    @Transactional
    public Admin updateAdmin(Long id, java.util.Map<String, Object> updates) {
        Admin existing = getAdminById(id);
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(existing.getUsername())) {
                if (adminRepository.existsByUsername(newUsername)) {
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
        return adminRepository.save(existing);
    }
}
