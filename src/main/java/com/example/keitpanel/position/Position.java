package com.example.keitpanel.position;

import com.example.keitpanel.branch.Branch;
import com.example.keitpanel.common.BaseEntity;
import com.example.keitpanel.department.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "position")
public class Position extends BaseEntity {

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 30)
    private String code;

    @Column(nullable = false)
    private short headcount = 1;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reports_to_id")
    private Position reportsTo;

}
