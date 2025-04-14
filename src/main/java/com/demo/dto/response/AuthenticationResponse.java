package com.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AuthenticationResponse {
    String token;
    boolean loginSuccess;
}
