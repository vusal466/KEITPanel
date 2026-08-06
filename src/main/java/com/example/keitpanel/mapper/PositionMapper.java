package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.PositionCreateRequest;
import com.example.keitpanel.dto.request.PositionUpdateRequest;
import com.example.keitpanel.dto.response.PositionResponse;
import com.example.keitpanel.entities.position.Position;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName" ,source = "branch.name")
    @Mapping(target = "departmentId" ,source = "department.id" )
    @Mapping(target = "departmentName" ,source = "department.name" )
    @Mapping(target = "reportsToId" ,source = "reportsTo.id" )
    @Mapping(target = "reportsToTitle" ,source = "reportsTo.title" )
    PositionResponse toResponse(Position position);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "branch" , ignore = true)
    @Mapping(target = "department" , ignore = true)
    @Mapping(target = "reportsTo" , ignore = true)
    Position toEntity(PositionCreateRequest request);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "branch" , ignore = true)
    @Mapping(target = "department" , ignore = true)
    @Mapping(target = "reportsTo" , ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PositionUpdateRequest request, @MappingTarget Position position);
}
