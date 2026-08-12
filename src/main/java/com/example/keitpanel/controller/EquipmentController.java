package com.example.keitpanel.controller;

import com.example.keitpanel.dto.request.EquipmentCreateRequest;
import com.example.keitpanel.dto.request.EquipmentUpdateRequest;
import com.example.keitpanel.dto.response.EquipmentResponse;
import com.example.keitpanel.services.EquipmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/equipments")
@Tag(name = "Equipment", description = "Avadanlıqların idarəetməsi")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public List<EquipmentResponse> findAll(Pageable pageable){
        return equipmentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public EquipmentResponse findById(@PathVariable Long id){
       return equipmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentResponse create(@Valid @RequestBody EquipmentCreateRequest request){
        return equipmentService.create(request);
    }

    @PutMapping("/{id}")
    public EquipmentResponse update(@PathVariable Long id, @Valid @RequestBody EquipmentUpdateRequest request){
        return equipmentService.update(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        equipmentService.delete(id);
    }
}
