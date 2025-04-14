package com.demo.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(2002, "The user you just created already exists in database", HttpStatus.BAD_GATEWAY),
    UNFOUND_USER(2003, "User not found", HttpStatus.NOT_FOUND),
    UNDEMAND_USER(2004, "User not match requirements", HttpStatus.BAD_REQUEST),
    UNEXISTING_USER(2005, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2006, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2007, "unauthenticated", HttpStatus.FORBIDDEN),
    INVALID_DOB(2008, "Invalid date", HttpStatus.BAD_REQUEST),;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
