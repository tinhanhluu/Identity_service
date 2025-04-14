package com.demo.controller;

import com.demo.dto.requests.UserCreationRequest;
import com.demo.dto.requests.UserUpdateRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import com.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Create new user successfully");
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<User>> getAllUsers(){
        var authenticaiton = SecurityContextHolder.getContext().getAuthentication();

        log.info("username: {}", authenticaiton.getName());
        authenticaiton.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Get all users successfully");
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/myInfo")
    ApiResponse<Object> getMyInfo(){
        return ApiResponse.builder()
                .data(userService.getMyInfo())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Get user successfully");
        apiResponse.setData(userService.getUserById(userId));

        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUserById(@PathVariable String userId,
                        @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Update user successfully");
        apiResponse.setData(userService.updateUserById(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    String deleteUserById(@PathVariable String userId){
        userService.deleteUserById(userId);
        return "User deleted";
    }
}
