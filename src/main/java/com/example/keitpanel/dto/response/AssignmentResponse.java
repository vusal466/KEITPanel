package com.example.keitpanel.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AssignmentResponse {

    private Long id;

    private Long equipmentId;
    private String equipmentInventoryNo;
    private String equipmentModel;

    private Long employeeId;
    private String employeeFullName;

    private Long positionId;
    private String positionTitle;

    private Long branchId;
    private String branchName;

    private Instant assignedAt;
    private Instant returnedAt;

    private String assignedBy;
    private String handoverDocNo;
    private String note;

    private boolean active;


}
