package com.demo.dto.requests;

import com.demo.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String name;

    @Size(min = 8, message = "UNDEMAND_USER")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate birthday;

}
