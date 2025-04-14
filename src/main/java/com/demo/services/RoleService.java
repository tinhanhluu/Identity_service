package com.demo.services;

import com.demo.dto.requests.PermissionRequest;
import com.demo.dto.requests.RoleRequest;
import com.demo.dto.response.RoleResponse;
import com.demo.entity.Role;
import com.demo.mapper.RoleMapper;
import com.demo.repository.PermissionRepository;
import com.demo.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permission));

        roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles(){
        return roleRepository.findAll().stream().map(role -> roleMapper.toRoleResponse(role)).collect(Collectors.toList());
    }

    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }

}
