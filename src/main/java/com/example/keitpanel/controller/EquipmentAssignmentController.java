package com.example.keitpanel.controller;

import com.example.keitpanel.dto.request.AssignmentCreateRequest;
import com.example.keitpanel.dto.request.AssignmentReturnRequest;
import com.example.keitpanel.dto.response.AssignmentResponse;
import com.example.keitpanel.services.EquipmentAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Tag(name = "Equipment Assignment", description = "Avadanlıqların təhvil-təslim idarəetməsi")
public class EquipmentAssignmentController {

    private final EquipmentAssignmentService assignmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse assign(@Valid @RequestBody AssignmentCreateRequest request) {
        return assignmentService.assign(request);
    }

    @PostMapping("/{id}/return")
    public AssignmentResponse returnEquipment(@PathVariable Long id, @RequestBody AssignmentReturnRequest request) {
        return assignmentService.returnEquipment(id, request);
    }

    @GetMapping("/equipment/{equipmentId}")
    public List<AssignmentResponse> getHistoryByEquipment(@PathVariable Long equipmentId) {
        return assignmentService.getHistoryByEquipment(equipmentId);
    }
}