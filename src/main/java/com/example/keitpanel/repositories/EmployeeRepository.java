package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
