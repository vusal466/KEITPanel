package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.BranchCreateRequest;
import com.example.keitpanel.dto.request.BranchUpdateRequest;
import com.example.keitpanel.dto.response.BranchResponse;

import java.util.List;


public interface BranchService {
    List<BranchResponse> findAll();

    BranchResponse findById(Long id);

    BranchResponse create(BranchCreateRequest request);

    BranchResponse update(Long id, BranchUpdateRequest request);

    void delete(Long id);

}
