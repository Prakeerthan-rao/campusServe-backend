package com.campus.service.controller;

import com.campus.service.config.JwtUtil;
import com.campus.service.dto.TicketRequest;
import com.campus.service.dto.TicketResponse;
import com.campus.service.entity.Ticket.TicketStatus;
import com.campus.service.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final JwtUtil       jwtUtil;

    public TicketController(TicketService ticketService, JwtUtil jwtUtil) {
        this.ticketService = ticketService;
        this.jwtUtil       = jwtUtil;
    }

    // Student: create ticket
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody TicketRequest request,
            @RequestHeader("Authorization") String authHeader) {
        Long studentId = extractUserId(authHeader);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(request, studentId));
    }

    // Admin: get all tickets
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAllTickets(
            @RequestParam(required = false) TicketStatus status) {
        List<TicketResponse> tickets = (status != null)
                ? ticketService.getTicketsByStatus(status)
                : ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // Any authenticated user: get single ticket
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    // Student: get my own tickets
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<TicketResponse>> getMyTickets(
            @RequestHeader("Authorization") String authHeader) {
        Long studentId = extractUserId(authHeader);
        return ResponseEntity.ok(ticketService.getTicketsByStudent(studentId));
    }

    // Staff: get tickets assigned to me
    @GetMapping("/assigned")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<TicketResponse>> getAssignedTickets(
            @RequestHeader("Authorization") String authHeader) {
        Long staffId = extractUserId(authHeader);
        return ResponseEntity.ok(ticketService.getTicketsByStaff(staffId));
    }

    // Staff/Admin: update ticket
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

    // Student/Admin: close ticket
    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<TicketResponse> closeTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.closeTicket(id));
    }

    // Admin: delete ticket
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(String authHeader) {
        return jwtUtil.getUserIdFromToken(authHeader.substring(7));
    }
}
