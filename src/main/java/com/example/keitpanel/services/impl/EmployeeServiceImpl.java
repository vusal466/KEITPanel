package com.example.keitpanel.services.impl;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.common.exception.DepartmentNotFoundException;
import com.example.keitpanel.common.exception.EmployeeNotFoundException;
import com.example.keitpanel.common.exception.PositionNotFoundException;
import com.example.keitpanel.dto.request.EmployeeCreateRequest;
import com.example.keitpanel.dto.request.EmployeeUpdateRequest;
import com.example.keitpanel.dto.response.EmployeeResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.department.Department;
import com.example.keitpanel.entities.employee.Employee;
import com.example.keitpanel.entities.employee.EmployeeStatus;
import com.example.keitpanel.entities.position.Position;
import com.example.keitpanel.mapper.EmployeeMapper;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.repositories.DepartmentRepository;
import com.example.keitpanel.repositories.EmployeeRepository;
import com.example.keitpanel.repositories.PositionRepository;
import com.example.keitpanel.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PositionRepository positionRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;


    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream().map(employeeMapper::toResponse).toList();
    }

    @Override
    public EmployeeResponse findById(Long id) {
        return employeeMapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeCreateRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        employee.setBranch(findBranch(request.getBranchId()));
        employee.setDepartment(findDepartmentOrNull(request.getDepartmentId()));
        employee.setPosition(findPosition(request.getPositionId()));
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long id, EmployeeUpdateRequest request) {
        Employee employee = getEntity(id);
        employeeMapper.updateEntityFromDto(request,employee);
        employee.setBranch(findBranch(request.getBranchId()));
        employee.setPosition(findPosition(request.getPositionId()));
        employee.setDepartment(findDepartmentOrNull(request.getDepartmentId()));
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = getEntity(id);
        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.setTerminatedAt(LocalDate.now());
    }

    private Employee getEntity(Long id) {
        return employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
    }

    private Branch findBranch(Long branchId){
        return branchRepository.findById(branchId).orElseThrow(()->new BranchNotFoundException(branchId));
    }

    private Position findPosition(Long positionId){
        return positionRepository.findById(positionId).orElseThrow(()->new PositionNotFoundException(positionId));
    }

    private Department findDepartmentOrNull(Long departmentId){
        if(departmentId==null) return null;
        return departmentRepository.findById(departmentId).orElseThrow(()->new DepartmentNotFoundException(departmentId));
    }
}
