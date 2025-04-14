package com.demo.controller;

import com.demo.dto.requests.PermissionRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.PermissionResponse;
import com.demo.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<?> delete(@PathVariable String name){
        permissionService.delete(name);
        return ApiResponse.builder()
                .message("Permission deleted")
                .build();
    }
}
