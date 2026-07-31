package com.example.keitpanel.entities.department;

import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "department")
public class Department extends BaseEntity {

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 20)
    private String code;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Department> departments = new ArrayList<>();

}
