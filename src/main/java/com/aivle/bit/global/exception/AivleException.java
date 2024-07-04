package com.aivle.bit.global.exception;

import lombok.Getter;

@Getter
public class AivleException extends RuntimeException {

    private final ErrorCode errorCode;

    public AivleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus().value();
    }
}
