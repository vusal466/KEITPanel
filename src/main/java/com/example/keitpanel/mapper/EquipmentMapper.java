package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.EquipmentCreateRequest;
import com.example.keitpanel.dto.request.EquipmentUpdateRequest;
import com.example.keitpanel.dto.response.EquipmentResponse;
import com.example.keitpanel.entities.equipment.Equipment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "departmentId",source = "department.id")
    @Mapping(target = "departmentName",source = "department.name")
    EquipmentResponse toResponse(Equipment equipment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "source",ignore = true)
    @Mapping(target = "manualLock",ignore = true)
    @Mapping(target = "lastSeenAt",ignore = true)
    Equipment toEntity(EquipmentCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EquipmentUpdateRequest request, @MappingTarget Equipment equipment);
}
