package com.campus.service.repository;

import com.campus.service.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    List<Staff> findByDepartmentId(Long departmentId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
