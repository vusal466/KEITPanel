package com.example.keitpanel.dto.request;

import com.example.keitpanel.entities.equipment.EquipmentStatus;
import com.example.keitpanel.entities.equipment.EquipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class EquipmentCreateRequest {

    @Size(max = 50)
    private String inventoryNo;

    @Size(max = 50)
    private String serialNo;

    @NotNull
    private EquipmentType type;

    @NotBlank(message = "marka boş olmamalıdır.")
    @Size(max = 50)
    private String brand;

    @NotBlank(message = "model boş olmamalıdır.")
    @Size(max = 50)
    private String model;

    @Size(max = 50)
    private String room;

    private Map<String, Object> specs;

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    private BigDecimal price;

    @Size(max = 150)
    private String supplier;

    private String note;

    @NotNull(message = "Filial seçilməlidir")
    private Long branchId;

    private Long departmentId;

}
