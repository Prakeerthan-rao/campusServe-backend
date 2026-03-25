package com.campus.service.controller;

import com.campus.service.dto.TicketResponse;
import com.campus.service.entity.Department;
import com.campus.service.entity.Ticket.TicketStatus;
import com.campus.service.repository.DepartmentRepository;
import com.campus.service.service.StaffService;
import com.campus.service.service.StudentService;
import com.campus.service.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final TicketService        ticketService;
    private final StudentService       studentService;
    private final StaffService         staffService;
    private final DepartmentRepository departmentRepository;
    private final com.campus.service.service.AdminService adminService;

    public AdminController(TicketService ticketService,
                           StudentService studentService,
                           StaffService staffService,
                           DepartmentRepository departmentRepository,
                           com.campus.service.service.AdminService adminService) {
        this.ticketService        = ticketService;
        this.studentService       = studentService;
        this.staffService         = staffService;
        this.departmentRepository = departmentRepository;
        this.adminService         = adminService;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<com.campus.service.entity.Admin> updateAdmin(@PathVariable Long id, @RequestBody java.util.Map<String, Object> updates) {
        return ResponseEntity.ok(adminService.updateAdmin(id, updates));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTickets",      ticketService.getAllTickets().size());
        stats.put("openTickets",       ticketService.getTicketsByStatus(TicketStatus.OPEN).size());
        stats.put("inProgressTickets", ticketService.getTicketsByStatus(TicketStatus.IN_PROGRESS).size());
        stats.put("resolvedTickets",   ticketService.getTicketsByStatus(TicketStatus.RESOLVED).size());
        stats.put("closedTickets",     ticketService.getTicketsByStatus(TicketStatus.CLOSED).size());
        stats.put("totalStudents",     studentService.getAllStudents().size());
        stats.put("totalStaff",        staffService.getAllStaff().size());
        stats.put("totalDepartments",  departmentRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentRepository.save(department));
    }

    @PutMapping("/departments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                        @RequestBody Department updates) {
        Optional<Department> opt = departmentRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        Department d = opt.get();
        if (updates.getName() != null)        d.setName(updates.getName());
        if (updates.getDescription() != null) d.setDescription(updates.getDescription());
        return ResponseEntity.ok(departmentRepository.save(d));
    }

    @DeleteMapping("/departments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tickets/department/{deptId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponse>> getTicketsByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(ticketService.getTicketsByDepartment(deptId));
    }
}