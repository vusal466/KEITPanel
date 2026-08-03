package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean existsByCode(String code);
}
