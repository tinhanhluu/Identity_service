package com.demo.dto.response;

import com.demo.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String id;
    String name;
    String firstName;
    String lastName;
    LocalDate birthday;
    Set<Role> roles;
}
