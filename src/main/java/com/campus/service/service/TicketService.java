package com.campus.service.service;

import com.campus.service.dto.TicketRequest;
import com.campus.service.dto.TicketResponse;
import com.campus.service.entity.*;
import com.campus.service.entity.Ticket.TicketStatus;
import com.campus.service.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository     ticketRepository;
    private final StudentRepository    studentRepository;
    private final StaffRepository      staffRepository;
    private final DepartmentRepository departmentRepository;

    public TicketService(TicketRepository ticketRepository,
                         StudentRepository studentRepository,
                         StaffRepository staffRepository,
                         DepartmentRepository departmentRepository) {
        this.ticketRepository     = ticketRepository;
        this.studentRepository    = studentRepository;
        this.staffRepository      = staffRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public TicketResponse createTicket(TicketRequest request, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + request.getDepartmentId()));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStudent(student);
        ticket.setDepartment(department);

        return toResponse(ticketRepository.save(ticket));
    }

    public TicketResponse getTicketById(Long id) {
        return toResponse(findById(id));
    }

    public List<TicketResponse> getAllTickets() {
        List<TicketResponse> list = new ArrayList<>();
        for (Ticket t : ticketRepository.findAll()) list.add(toResponse(t));
        return list;
    }

    public List<TicketResponse> getTicketsByStudent(Long studentId) {
        List<TicketResponse> list = new ArrayList<>();
        for (Ticket t : ticketRepository.findByStudentId(studentId)) list.add(toResponse(t));
        return list;
    }

    public List<TicketResponse> getTicketsByDepartment(Long departmentId) {
        List<TicketResponse> list = new ArrayList<>();
        for (Ticket t : ticketRepository.findByDepartmentId(departmentId)) list.add(toResponse(t));
        return list;
    }

    public List<TicketResponse> getTicketsByStaff(Long staffId) {
        List<TicketResponse> list = new ArrayList<>();
        for (Ticket t : ticketRepository.findByAssignedStaffId(staffId)) list.add(toResponse(t));
        return list;
    }

    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        List<TicketResponse> list = new ArrayList<>();
        for (Ticket t : ticketRepository.findByStatus(status)) list.add(toResponse(t));
        return list;
    }

    @Transactional
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = findById(id);

        if (request.getTitle() != null)       ticket.setTitle(request.getTitle());
        if (request.getDescription() != null) ticket.setDescription(request.getDescription());
        if (request.getStatus() != null)      ticket.setStatus(request.getStatus());
        if (request.getPriority() != null)    ticket.setPriority(request.getPriority());
        if (request.getResolution() != null)  ticket.setResolution(request.getResolution());

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            ticket.setDepartment(dept);
        }

        if (request.getAssignedStaffId() != null) {
            Staff staff = staffRepository.findById(request.getAssignedStaffId())
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found"));
            ticket.setAssignedStaff(staff);
            if (ticket.getStatus() == TicketStatus.OPEN) {
                ticket.setStatus(TicketStatus.IN_PROGRESS);
            }
        }

        return toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse closeTicket(Long id) {
        Ticket ticket = findById(id);
        ticket.setStatus(TicketStatus.CLOSED);
        return toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    private Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + id));
    }

    public TicketResponse toResponse(Ticket t) {
        TicketResponse r = new TicketResponse();
        r.setId(t.getId());
        r.setTitle(t.getTitle());
        r.setDescription(t.getDescription());
        r.setStatus(t.getStatus());
        r.setPriority(t.getPriority());
        r.setResolution(t.getResolution());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());

        r.setStudentId(t.getStudent().getId());
        r.setStudentName(t.getStudent().getFullName());
        r.setStudentRollNumber(t.getStudent().getRollNumber());

        r.setDepartmentId(t.getDepartment().getId());
        r.setDepartmentName(t.getDepartment().getName());

        if (t.getAssignedStaff() != null) {
            r.setAssignedStaffId(t.getAssignedStaff().getId());
            r.setAssignedStaffName(t.getAssignedStaff().getFullName());
        }
        return r;
    }
}
