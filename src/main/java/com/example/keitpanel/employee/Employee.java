package com.example.keitpanel.employee;

import com.example.keitpanel.branch.Branch;
import com.example.keitpanel.common.BaseEntity;
import com.example.keitpanel.department.Department;
import com.example.keitpanel.position.Position;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Column(length = 30, unique = true)
    private String personnelNo;

    @Column(length = 80, nullable = false)
    private String firstName;

    @Column(length = 80, nullable = false)
    private String lastName;

    @Column(length = 80)
    private String patronymic;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    private LocalDate hiredAt;
    private LocalDate terminatedAt;

    @Column(length = 30)
    private String phoneMobile;

    @Column(length = 20)
    private String phoneInternal;

    @Column(length = 20)
    private String anydeskId; // Məs: "987654321" (Uzaqdan dəstək üçün)

    @Column(length = 50)
    private String pcUsername; // Local Kompüter adı (Məs: "HP-DESK-01\User")

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
