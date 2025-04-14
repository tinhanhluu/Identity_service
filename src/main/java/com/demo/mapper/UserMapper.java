package com.demo.mapper;

import com.demo.dto.requests.UserCreationRequest;
import com.demo.dto.requests.UserUpdateRequest;
import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
