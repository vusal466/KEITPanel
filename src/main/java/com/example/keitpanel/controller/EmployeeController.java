package com.example.keitpanel.controller;

import com.example.keitpanel.dto.request.EmployeeCreateRequest;
import com.example.keitpanel.dto.request.EmployeeUpdateRequest;
import com.example.keitpanel.dto.response.EmployeeResponse;
import com.example.keitpanel.services.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee",description = "İşçi idarəetməsi")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeResponse> findAll(){
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeResponse findById(@PathVariable Long id){
        return employeeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest request){
       return employeeService.create(request);
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest request){
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        employeeService.delete(id);
    }
}
