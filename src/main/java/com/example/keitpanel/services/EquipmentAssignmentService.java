package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.AssignmentCreateRequest;
import com.example.keitpanel.dto.request.AssignmentReturnRequest;
import com.example.keitpanel.dto.response.AssignmentResponse;

import java.util.List;

public interface EquipmentAssignmentService {
     AssignmentResponse assign(AssignmentCreateRequest request);

     AssignmentResponse returnEquipment(Long assignmentId, AssignmentReturnRequest request);

     List<AssignmentResponse> getHistoryByEquipment(Long equipmentId);
}
