package com.example.keitpanel.services;

import com.example.keitpanel.dto.request.EquipmentCreateRequest;
import com.example.keitpanel.dto.request.EquipmentUpdateRequest;
import com.example.keitpanel.dto.response.EquipmentResponse;
import org.hibernate.query.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface EquipmentService {

    List<EquipmentResponse> findAll(Pageable pageable);

    EquipmentResponse findById(Long id);

    EquipmentResponse create(EquipmentCreateRequest request);

    EquipmentResponse update(Long id, EquipmentUpdateRequest request);

    void delete(Long id);
}
