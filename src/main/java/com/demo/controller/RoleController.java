package com.demo.controller;

import com.demo.dto.requests.RoleRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.RoleResponse;
import com.demo.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<?> delete(@PathVariable String name){
        roleService.deleteRole(name);
        return ApiResponse.builder()
                .message("Permission deleted")
                .build();
    }
}
