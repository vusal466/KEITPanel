package com.example.keitpanel.entities.equipment;

import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.common.BaseEntity;
import com.example.keitpanel.entities.department.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "equipment")
public class Equipment extends BaseEntity {

    @Column(length = 50, unique = true)
    private String inventoryNo;

    @Column(length = 120)
    private String serialNo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private EquipmentStatus status = EquipmentStatus.IN_STOCK;

    @Column(length = 80)
    private String brand;

    @Column(length = 120)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(length = 50)
    private String room;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> specs = new HashMap<>();

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(length = 150)
    private String supplier;

    @Column(columnDefinition = "text")
    private String note;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private DataSource source = DataSource.MANUAL;

    @Column(nullable = false)
    private boolean manualLock = false;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;


}
