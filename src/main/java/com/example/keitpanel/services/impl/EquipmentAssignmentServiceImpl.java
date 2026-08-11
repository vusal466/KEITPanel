package com.example.keitpanel.services.impl;

import com.example.keitpanel.dto.request.AssignmentCreateRequest;
import com.example.keitpanel.dto.request.AssignmentReturnRequest;
import com.example.keitpanel.dto.response.AssignmentResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.employee.Employee;
import com.example.keitpanel.entities.equipment.Equipment;
import com.example.keitpanel.entities.equipment.EquipmentAssignment;
import com.example.keitpanel.entities.equipment.EquipmentStatus;
import com.example.keitpanel.entities.position.Position;
import com.example.keitpanel.mapper.AssignmentMapper;
import com.example.keitpanel.repositories.*;
import com.example.keitpanel.services.EquipmentAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentAssignmentServiceImpl implements EquipmentAssignmentService {

    private final EquipmentAssignmentRepository assignmentRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final BranchRepository branchRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    @Transactional
    public AssignmentResponse assign(AssignmentCreateRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Avadanlıq tapılmadı"));

        // Biznes qaydası: Əgər avadanlıq artıq kimdəsa varsa, yenidən təhvil verilə bilməz!
        if (equipment.getStatus() == EquipmentStatus.ASSIGNED) {
            throw new RuntimeException("Bu avadanlıq artıq başqa bir işçidə/vəzifədə təhkim olunub!");
        }

        EquipmentAssignment assignment = assignmentMapper.toEntity(request);
        assignment.setEquipment(equipment);

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("İşçi tapılmadı"));
            assignment.setEmployee(employee);
        }

        if (request.getPositionId() != null) {
            Position position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Vəzifə tapılmadı"));
            assignment.setPosition(position);
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Filial tapılmadı"));
        assignment.setBranch(branch);

        assignment.setAssignedAt(Instant.now());

        // 🟢 Avadanlığın statusunu ASSIGNED edirik!
        equipment.setStatus(EquipmentStatus.ASSIGNED);
        equipmentRepository.save(equipment);

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional
    public AssignmentResponse returnEquipment(Long assignmentId, AssignmentReturnRequest request) {
        EquipmentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Təhvil recordu tapılmadı"));

        if (assignment.getReturnedAt() != null) {
            throw new RuntimeException("Bu avadanlıq artıq geri təhvil alınıb!");
        }

        assignment.setReturnedAt(Instant.now());
        if (request.getNote() != null) {
            assignment.setNote(assignment.getNote() + " | Qaytarılma qeydi: " + request.getNote());
        }

        // 🔵 Avadanlığı geri anbara qaytarırıq (IN_STOCK)
        Equipment equipment = assignment.getEquipment();
        equipment.setStatus(EquipmentStatus.IN_STOCK);
        equipmentRepository.save(equipment);

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getHistoryByEquipment(Long equipmentId) {
        return assignmentRepository.findAllByEquipmentIdOrderByAssignedAtDesc(equipmentId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }
}