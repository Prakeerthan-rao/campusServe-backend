package com.campus.service.controller;

import com.campus.service.dto.StaffRegisterRequest;
import com.campus.service.entity.Staff;
import com.campus.service.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Staff>> getStaffByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(staffService.getStaffByDepartment(departmentId));
    }

    @PostMapping("/register")
    public ResponseEntity<Staff> registerStaff(@Valid @RequestBody StaffRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(staffService.registerStaff(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id,
                                              @RequestBody java.util.Map<String, Object> updates) {
        return ResponseEntity.ok(staffService.updateStaff(id, updates));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}