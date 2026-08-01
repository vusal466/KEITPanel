package com.example.keitpanel.services;

import com.example.keitpanel.common.exception.BranchNotFoundException;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.repositories.BranchRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepository;

    public List<Branch> findAll(){
        return branchRepository.findAll();
    }

    public Branch findById(Long id){
        return branchRepository.findById(id).orElseThrow(()->new BranchNotFoundException(id));
    }

    @Transactional
    public Branch create(Branch branch){
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch update(Long id, Branch updated) {
        Branch existing = findById(id);
        existing.setName(updated.getName());
        existing.setCode(updated.getCode());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setPhone(updated.getPhone());
        existing.setActive(updated.isActive());
        return existing;
    }

    @Transactional
    public void delete(Long id) {
        Branch branch = findById(id);
        branch.setActive(false);
    }

}
