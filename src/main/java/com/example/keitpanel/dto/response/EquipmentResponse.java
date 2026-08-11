package com.example.keitpanel.dto.response;

import com.example.keitpanel.entities.equipment.DataSource;
import com.example.keitpanel.entities.equipment.EquipmentStatus;
import com.example.keitpanel.entities.equipment.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class EquipmentResponse {

    private Long id;

    private String inventoryNo;

    private String serialNo;

    private EquipmentType type;

    private EquipmentStatus status;

    private String brand;

    private String model;

    private String room;

    private Map<String, Object> specs;

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    private BigDecimal price;
    private String supplier;

    private String note;

    private DataSource source;

    private Boolean manualLock;

    private Long branchId;
    private String branchName;

    private Long departmentId;
    private String departmentName;
}
