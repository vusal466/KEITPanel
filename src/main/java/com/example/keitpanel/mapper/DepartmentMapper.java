package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.DepartmentCreateRequest;
import com.example.keitpanel.dto.request.DepartmentUpdateRequest;
import com.example.keitpanel.dto.response.DepartmentResponse;
import com.example.keitpanel.entities.department.Department;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    DepartmentResponse toResponse(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "branch", ignore = true)
    Department toEntity(DepartmentCreateRequest response);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DepartmentUpdateRequest request, @MappingTarget Department department);
}
