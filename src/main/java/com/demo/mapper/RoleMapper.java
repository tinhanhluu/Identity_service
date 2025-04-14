package com.demo.mapper;

import com.demo.dto.requests.RoleRequest;
import com.demo.dto.response.RoleResponse;
import com.demo.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
