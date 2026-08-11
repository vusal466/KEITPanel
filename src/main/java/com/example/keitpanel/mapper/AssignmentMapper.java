package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.AssignmentCreateRequest;
import com.example.keitpanel.dto.response.AssignmentResponse;
import com.example.keitpanel.entities.equipment.EquipmentAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    @Mapping(target = "equipmentId", source = "equipment.id")
    @Mapping(target = "equipmentInventoryNo", source = "equipment.inventoryNo")
    @Mapping(target = "equipmentModel", source = "equipment.model")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeFullName", expression = "java(assignment.getEmployee() != null ? assignment.getEmployee().getFirstName() + ' ' + assignment.getEmployee().getLastName() : null)")
    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "positionTitle", source = "position.title")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "active", expression = "java(assignment.getReturnedAt() == null)")
    AssignmentResponse toResponse(EquipmentAssignment assignment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipment", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "returnedAt", ignore = true)
    EquipmentAssignment toEntity(AssignmentCreateRequest request);

}
