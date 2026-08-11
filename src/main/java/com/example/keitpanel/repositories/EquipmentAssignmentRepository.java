package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.equipment.EquipmentAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentAssignmentRepository extends JpaRepository<EquipmentAssignment,Long> {

    // Konkret avadanlığın bütün təhvil-təslim tarixçəsini (ən yenidən köhnəyə) gətirir
    List<EquipmentAssignment> findAllByEquipmentIdOrderByAssignedAtDesc(Long equipmentId);

    // Konkret işçiyə verilmiş və hələ geri qaytarılmamış (aktiv) avadanlıqları tapır
    List<EquipmentAssignment> findAllByEmployeeIdAndReturnedAtIsNull(Long employeeId);
}
