package com.demo.services;

import com.demo.dto.requests.PermissionRequest;
import com.demo.dto.response.AppException;
import com.demo.dto.response.ErrorCode;
import com.demo.dto.response.PermissionResponse;
import com.demo.entity.Permission;
import com.demo.mapper.PermissionMapper;
import com.demo.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        return permissionRepository.findAll().stream().map(permission -> permissionMapper.toPermissionResponse(permission)).collect(Collectors.toList());
    }

    public void delete(String name){
        permissionRepository.deleteById(name);
    }
}
