package com.aivle.bit.global.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_VERIFICATION_CODE(BAD_REQUEST, "인증 코드가 유효하지 않습니다."),
    EMAIL_NOT_VERIFIED(BAD_REQUEST, "이메일 인증이 필요합니다."),
    EXPIRED_VERIFICATION_CODE(BAD_REQUEST, "인증 코드가 만료되었습니다."),
    INVALID_EMAIL_FORMAT(BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_FORMAT(BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_NAME_FORMAT(BAD_REQUEST, "이름 형식이 올바르지 않습니다."),
    EMAIL_DUPLICATION(BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    // 404 Not Found
    NOT_SUPPORT_API(BAD_REQUEST, "지원하지 않는 API입니다."),

    // 500 Internal Server Error
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    PASSWORD_ENCRYPTION_ERROR(INTERNAL_SERVER_ERROR, "암호화에 문제가 발생했습니다. 관리자에게 문의하세요.");
    private final HttpStatus httpStatus;
    private final String message;
}
