package com.aivle.bit.global.exception;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_REQUEST;
import static com.aivle.bit.global.exception.ErrorCode.SERVER_ERROR;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AivleException.class)
    public ResponseEntity<ErrorResponse> handleAivleException(AivleException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        log.warn("{} : {}", errorCode.name(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        log.warn("ConstraintViolationException: {}", exception.getConstraintViolations());
        return ErrorResponse.from(INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException: {}",
            exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ErrorResponse.from(INVALID_INPUT_VALUE);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestMappingException(RuntimeException exception) {
        log.warn("Request Mapping Fail : {}", exception.getMessage());
        return ErrorResponse.from(INVALID_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException exception) {
        log.error("UnExpectedException: {}", exception.getMessage());
        return ErrorResponse.from(SERVER_ERROR);
    }

    @ExceptionHandler(ImageSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleImageSizeExceededException(ImageSizeExceededException exception) {
        log.warn("ImageSizeExceededException: {}", exception.getMessage());
        return ErrorResponse.from(ErrorCode.IMAGE_SIZE_EXCEEDED);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.warn("MaxUploadSizeExceededException: {}", exception.getMessage());
        return ErrorResponse.from(ErrorCode.FILE_SIZE_EXCEEDED);
    }
}