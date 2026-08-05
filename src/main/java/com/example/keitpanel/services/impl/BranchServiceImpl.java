package com.example.keitpanel.services.impl;

import com.example.keitpanel.dto.request.BranchCreateRequest;
import com.example.keitpanel.dto.request.BranchUpdateRequest;
import com.example.keitpanel.dto.response.BranchResponse;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.mapper.BranchMapper;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.services.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;
    private final BranchRepository branchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> findAll() {
        return branchRepository.findAll()
                .stream()
                .map(branchMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public BranchResponse findById(Long id) {
        return branchRepository.findById(id)
                .map(branchMapper::toResponse) // ✅ branchMapper.toResponse
                .orElseThrow(() -> new RuntimeException("Filial tapılmadı. ID: " + id));
    }

    @Override
    @Transactional
    public BranchResponse create(BranchCreateRequest request) {
        if(branchRepository.existsByCode(request.getCode())){
            throw new RuntimeException("Bu kodla filial artıq mövcuddur: " + request.getCode());
        }
        Branch branch = branchMapper.toEntity(request);
        branch.setActive(true);

        if (request.getParentId()!=null){
            Branch parent = getBranchEntity(request.getParentId());
            branch.setParent(parent);
        }

        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toResponse(savedBranch) ;
    }

    @Override
    public BranchResponse update(Long id, BranchUpdateRequest request) {

        Branch branch = getBranchEntity(id);

        branchMapper.updateEntityFromDto(request,branch);

        if (request.getParentId()!= null){
            if(id.equals(request.getParentId())){
                throw new RuntimeException("Filial özü-özünə parent ola bilməz!");
            }
            Branch parent = getBranchEntity(request.getParentId());
            branch.setParent(parent);
        }else {
            branch.setParent(null);
        }

        Branch updatedBranch = branchRepository.save(branch);
        return branchMapper.toResponse(updatedBranch);

    }

    @Override
    public void delete(Long id) {

        Branch branch = getBranchEntity(id);
        if(!branch.getChildren().isEmpty()){
            throw new RuntimeException("Alt filialları olan filialı silmək olmaz.");
        }
        branchRepository.delete(branch);
    }

    private Branch getBranchEntity(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filial tapılmadı. ID: " + id));
    }
}
