package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.PositionCreateRequest;
import com.example.keitpanel.dto.request.PositionUpdateRequest;
import com.example.keitpanel.dto.response.PositionResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.department.Department;
import com.example.keitpanel.entities.position.Position;

import java.util.List;

public interface PositionService {
    List<PositionResponse> findAll();

    PositionResponse findById(Long id);

    PositionResponse create(PositionCreateRequest request);

    PositionResponse update(Long id, PositionUpdateRequest request);

    void delete(Long id);


}
