package com.demo.services;

import com.demo.dto.requests.UserCreationRequest;
import com.demo.dto.requests.UserUpdateRequest;
import com.demo.dto.response.AppException;
import com.demo.dto.response.ErrorCode;
import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import com.demo.enums.Roles;
import com.demo.mapper.UserMapper;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest request){

        if(userRepository.existsByName(request.getName())){
            throw new RuntimeException("USER_EXISTED");
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles  = new HashSet<>();
        roles.add(Roles.USER.name());

        // user.setRoles(roles);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        log.info("get all users method");
        return userRepository.findAll();
    }

    public UserResponse getUserById(String userId){
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.UNFOUND_USER)));
    }

    public UserResponse updateUserById(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.UNFOUND_USER));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(role));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUserById(String userId){
        userRepository.deleteById(userId);
    }
    
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByName(name).orElseThrow(() -> new AppException(ErrorCode.UNFOUND_USER));

        return userMapper.toUserResponse(user);
    }
}
