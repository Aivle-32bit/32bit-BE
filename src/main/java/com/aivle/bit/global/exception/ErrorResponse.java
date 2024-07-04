package com.aivle.bit.global.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus httpStatus,
    String errorName,
    String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getHttpStatus(),
            errorCode.name(),
            errorCode.getMessage()
        );
    }
}
