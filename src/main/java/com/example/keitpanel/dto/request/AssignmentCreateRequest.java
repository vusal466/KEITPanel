package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentCreateRequest {

    @NotNull(message = "Avadanlıq seçilməlidir.")
    private Long equipmentId;

    private Long employeeId;

    private Long positionId;

    @NotNull(message = "Filial seçilməlidir.")
    private Long branchId;

    @Size(max = 100)
    private String assignedBy;

    @Size(max = 50)
    private String handoverDocNo;

    private String note;


}
