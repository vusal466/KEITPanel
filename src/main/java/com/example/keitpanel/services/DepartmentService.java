package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.DepartmentCreateRequest;
import com.example.keitpanel.dto.request.DepartmentUpdateRequest;
import com.example.keitpanel.dto.response.DepartmentResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.department.Department;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse> findAll();

    DepartmentResponse findById(Long id);

    DepartmentResponse create(DepartmentCreateRequest request);

    DepartmentResponse update(Long id, DepartmentUpdateRequest request);

    void delete(Long id);

}
