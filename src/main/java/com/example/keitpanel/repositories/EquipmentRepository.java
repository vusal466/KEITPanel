package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    boolean existsByInventoryNo(String inventoryNo);
}
