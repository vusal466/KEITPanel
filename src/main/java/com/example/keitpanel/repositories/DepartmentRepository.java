package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
