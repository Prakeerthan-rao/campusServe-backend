package com.campus.service.repository;

import com.campus.service.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    boolean existsByUsername(String username);
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
}
