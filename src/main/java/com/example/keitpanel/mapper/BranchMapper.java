package com.example.keitpanel.mapper;

import com.example.keitpanel.dto.request.BranchCreateRequest;
import com.example.keitpanel.dto.request.BranchUpdateRequest;
import com.example.keitpanel.dto.response.BranchResponse;
import com.example.keitpanel.entities.branch.Branch;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface BranchMapper {
    // 1. Entity -> Response DTO çevirməsi
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    BranchResponse toResponse(Branch branch);

    // 2. CreateRequest -> Entity çevirməsi
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "parent", ignore = true) // Parent-i service-də tapıb qoyacağıq
    @Mapping(target = "children", ignore = true)
    Branch toEntity(BranchCreateRequest request);

    // 3. UpdateRequest-dən gələn məlumatları mövcud Entity-nin üzərinə yazmaq (Null olanları toxunmur)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BranchUpdateRequest request, @MappingTarget Branch branch);
}

