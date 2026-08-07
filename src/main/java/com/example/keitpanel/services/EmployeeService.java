package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.EmployeeCreateRequest;
import com.example.keitpanel.dto.request.EmployeeUpdateRequest;
import com.example.keitpanel.dto.response.EmployeeResponse;
import com.example.keitpanel.entities.employee.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> findAll();

    EmployeeResponse findById(Long id);

    EmployeeResponse create(EmployeeCreateRequest request);

    EmployeeResponse update(Long id, EmployeeUpdateRequest request);

    void delete(Long id);



}
