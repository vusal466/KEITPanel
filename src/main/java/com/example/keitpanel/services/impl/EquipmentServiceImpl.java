package com.example.keitpanel.services.impl;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.common.exception.DepartmentNotFoundException;
import com.example.keitpanel.common.exception.EquipmentNotFoundException;
import com.example.keitpanel.dto.request.EquipmentCreateRequest;
import com.example.keitpanel.dto.request.EquipmentUpdateRequest;
import com.example.keitpanel.dto.response.EquipmentResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.equipment.Equipment;
import com.example.keitpanel.entities.equipment.EquipmentStatus;
import com.example.keitpanel.mapper.EquipmentMapper;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.repositories.DepartmentRepository;
import com.example.keitpanel.repositories.EquipmentRepository;
import com.example.keitpanel.services.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;

    @Override
    public List<EquipmentResponse> findAll(Pageable pageable) {
        return equipmentRepository.findAll().stream().map(equipmentMapper::toResponse).toList();
    }

    @Override
    public EquipmentResponse findById(Long id) {
        return equipmentRepository.findById(id).map(equipmentMapper::toResponse).orElseThrow(()->new EquipmentNotFoundException(id));
    }

    @Override
    public EquipmentResponse create(EquipmentCreateRequest request) {
        if(request.getInventoryNo()!=null && equipmentRepository.existsByInventoryNo(request.getInventoryNo())){
            throw new RuntimeException("Bu inventar nömrəsi ilə avadanlıq artıq mövcuddur: \" + request.getInventoryNo()");
        }

        Equipment equipment = equipmentMapper.toEntity(request);
        equipment.setBranch(branchRepository.findById(request.getBranchId()).orElseThrow(()->new BranchNotFoundException(request.getBranchId())));

        if(request.getDepartmentId()!=null) {
            equipment.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentId())));
        }
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponse(savedEquipment);
    }

    @Override
    public EquipmentResponse update(Long id, EquipmentUpdateRequest request) {
        Equipment equipment = getEntity(id);

        equipmentMapper.updateEntityFromDto(request,equipment);

        if(request.getBranchId() != null){
           equipment.setBranch(branchRepository.findById(request.getBranchId()).orElseThrow(()->new BranchNotFoundException(request.getBranchId())));
        }

        if (request.getDepartmentId() != null){
            equipment.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElseThrow(()->new DepartmentNotFoundException(request.getDepartmentId())));
        }
        Equipment updateEquipment = equipmentRepository.save(equipment);

        return equipmentMapper.toResponse(updateEquipment);
    }

    @Override
    public void delete(Long id) {
        Equipment equipment = getEntity(id);
        equipment.setStatus(EquipmentStatus.WRITTEN_OFF);
    }

    public Equipment getEntity(Long id){
        return equipmentRepository.findById(id).orElseThrow(()->new EquipmentNotFoundException(id));
    }
}
