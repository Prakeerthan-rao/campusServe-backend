package com.campus.service.repository;

import com.campus.service.entity.Ticket;
import com.campus.service.entity.Ticket.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStudentId(Long studentId);
    List<Ticket> findByDepartmentId(Long departmentId);
    List<Ticket> findByAssignedStaffId(Long staffId);
    List<Ticket> findByStatus(TicketStatus status);
}
