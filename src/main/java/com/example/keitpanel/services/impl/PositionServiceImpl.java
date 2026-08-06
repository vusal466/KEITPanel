package com.example.keitpanel.services.impl;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.common.exception.DepartmentNotFoundException;
import com.example.keitpanel.common.exception.PositionNotFoundException;
import com.example.keitpanel.dto.request.PositionCreateRequest;
import com.example.keitpanel.dto.request.PositionUpdateRequest;
import com.example.keitpanel.dto.response.PositionResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.department.Department;
import com.example.keitpanel.entities.position.Position;
import com.example.keitpanel.mapper.PositionMapper;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.repositories.DepartmentRepository;
import com.example.keitpanel.repositories.PositionRepository;
import com.example.keitpanel.services.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<PositionResponse> findAll() {
        return positionRepository.findAll().stream().map(positionMapper::toResponse).toList();
    }

    @Override
    public PositionResponse findById(Long id) {
        return positionMapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional
    public PositionResponse create(PositionCreateRequest request) {
        Position position = positionMapper.toEntity(request);
        position.setBranch(findBranch(request.getBranchId()));
        position.setDepartment(findDepartmentOrNull(request.getDepartmentId()));
        position.setReportsTo(findPositionByOptionalId(request.getReportsToId()));
        return positionMapper.toResponse(positionRepository.save(position));
    }

    @Override
    @Transactional
    public PositionResponse update(Long id, PositionUpdateRequest request) {
        Position position = getEntity(id);
        positionMapper.updateEntityFromDto(request,position);
        position.setBranch(findBranch(request.getBranchId()));
        position.setDepartment(findDepartmentOrNull(request.getDepartmentId()));
        position.setReportsTo(findPositionByOptionalId(request.getReportsToId()));
        return positionMapper.toResponse(position);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Position position = getEntity(id);
        position.setActive(false);
    }


    private Position getEntity(Long id) {
        return positionRepository.findById(id).orElseThrow(()->new PositionNotFoundException(id));
    }


    private Branch findBranch(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(()->new BranchNotFoundException(branchId));
    }


    private Department findDepartmentOrNull(Long departmentId) {
        if(departmentId==null) return null;
        return departmentRepository.findById(departmentId).orElseThrow(()->new DepartmentNotFoundException(departmentId));
    }


    private Position findPositionByOptionalId(Long positionId) {
        if(positionId==null) return  null;
        return positionRepository.findById(positionId).orElseThrow(()->new PositionNotFoundException(positionId));
    }
}
