package com.example.keitpanel.services;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.dto.request.BranchCreateRequest;
import com.example.keitpanel.dto.response.BranchResponse;
import com.example.keitpanel.dto.response.BranchUpdateRequest;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.repositories.BranchRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BranchService {
    List<BranchResponse> findAll();

    BranchResponse findById(Long id);

    BranchResponse create(BranchCreateRequest request);

    BranchResponse update(Long id, BranchUpdateRequest request);

    void delete(Long id);

}
