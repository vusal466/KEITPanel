package com.example.keitpanel.entities.equipment;


import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.common.BaseEntity;
import com.example.keitpanel.entities.employee.Employee;
import com.example.keitpanel.entities.position.Position;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "equipment_assignment")
public class EquipmentAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt = Instant.now();
    private Instant returnedAt;

    @Column(length = 100)
    private String assignedBy;

    @Column(length = 50)
    private String handoverDocNo;

    @Column(columnDefinition = "text")
    private String note;
}
