package com.example.keitpanel.services.impl;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.common.exception.DepartmentNotFoundException;
import com.example.keitpanel.dto.request.DepartmentCreateRequest;
import com.example.keitpanel.dto.request.DepartmentUpdateRequest;
import com.example.keitpanel.dto.response.DepartmentResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.department.Department;
import com.example.keitpanel.mapper.DepartmentMapper;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.repositories.DepartmentRepository;
import com.example.keitpanel.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;


    @Override
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream().map(departmentMapper::toResponse).toList();
    }

    @Override
    public DepartmentResponse findById(Long id) {
        return departmentMapper.toResponse(getEntity(id));
    }

    @Override
    public DepartmentResponse create(DepartmentCreateRequest request) {
        Department department = departmentMapper.toEntity(request);
        department.setActive(true);
        department.setBranch(findBranch(request.getBranchId()));
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentUpdateRequest request) {
        Department department = getEntity(id);
        departmentMapper.updateEntityFromDto(request,department);
        department.setBranch(findBranch(request.getBranchId()));
        return departmentMapper.toResponse(department);
    }

    @Override
    public void delete(Long id) {
        Department department = getEntity(id);
        department.setActive(false);
    }

    @Override
    public Department getEntity(Long id) {
        return departmentRepository.findById(id).orElseThrow(()-> new DepartmentNotFoundException(id));
    }

    @Override
    public Branch findBranch(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(()->new BranchNotFoundException(branchId));
    }
}
