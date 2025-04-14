package com.demo.exception;

import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.AppException;
import com.demo.dto.response.ErrorCode;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();
        String enumKey = exception.getMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appExceptionHandler(AppException exception){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = exception.getErrorCode();

        apiResponse.setCode(exception.getErrorCode().getCode());
        apiResponse.setMessage(exception.getErrorCode().getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedExceptionHandler(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){

        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        try{
            errorCode = ErrorCode.valueOf(enumKey);

        } catch (IllegalArgumentException e){

        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = UnsatisfiedDependencyException.class)
    ResponseEntity<ApiResponse> unsatisfiedDependencyExceptionHandler(UnsatisfiedDependencyException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
