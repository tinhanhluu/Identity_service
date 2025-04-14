package com.demo.controller;

import com.demo.dto.requests.AuthenticationRequest;
import com.demo.dto.requests.IntrospectRequest;
import com.demo.dto.requests.LogoutRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.AuthenticationResponse;
import com.demo.dto.response.IntrospectResponse;
import com.demo.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> loginProcess(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();

        apiResponse.setData(result);

        return apiResponse;

//        return ApiResponse.<AuthenticationResponse>builder()
//                .data(AuthenticationResponse.builder()
//                        .loginSuccess(result)
//                        .build())
//                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> abc(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();

        apiResponse.setData(result);

        return apiResponse;
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

}
