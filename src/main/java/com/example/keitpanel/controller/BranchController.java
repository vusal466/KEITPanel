package com.example.keitpanel.controller;

import com.example.keitpanel.dto.request.BranchCreateRequest;
import com.example.keitpanel.dto.response.BranchResponse;
import com.example.keitpanel.dto.response.BranchUpdateRequest;
import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.services.BranchService;
import com.example.keitpanel.services.impl.BranchServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branch", description = "Filial idarə etməsi")
public class BranchController {
    private final BranchServiceImpl branchService;

    @GetMapping
    public List<BranchResponse> findAll(){
        return branchService.findAll();
    }

    @GetMapping("/{id}")
    public BranchResponse findById(@PathVariable Long id){
        return branchService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse create(@Valid @RequestBody BranchCreateRequest branch){
        return branchService.create(branch);
    }

    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable Long id, @Valid @RequestBody BranchUpdateRequest branch){
        return branchService.update(id,branch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        branchService.delete(id);
    }
}
