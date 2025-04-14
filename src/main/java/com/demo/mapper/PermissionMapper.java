package com.demo.mapper;

import com.demo.dto.requests.PermissionRequest;
import com.demo.dto.response.PermissionResponse;
import com.demo.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
