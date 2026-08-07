package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.EmployeeCreateRequest;
import com.example.keitpanel.dto.request.EmployeeUpdateRequest;
import com.example.keitpanel.dto.response.EmployeeResponse;
import com.example.keitpanel.entities.employee.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName" ,source = "branch.name")
    @Mapping(target = "departmentId" ,source = "department.id" )
    @Mapping(target = "departmentName" ,source = "department.name" )
    @Mapping(target = "positionId",source = "position.id")
    @Mapping(target = "positionName",source = "position.title")
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "branch",ignore = true)
    @Mapping(target = "department",ignore = true)
    @Mapping(target = "position",ignore = true)
    Employee toEntity(EmployeeCreateRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "branch",ignore = true)
    @Mapping(target = "department",ignore = true)
    @Mapping(target = "position",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EmployeeUpdateRequest request,  @MappingTarget Employee employee);
}
