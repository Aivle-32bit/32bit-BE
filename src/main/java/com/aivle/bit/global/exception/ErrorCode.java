package com.aivle.bit.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
    AUTH_INVALID_PASSWORD(BAD_REQUEST, "유효하지 않은 비밀번호입니다."),
    INVALID_NAME_FORMAT(BAD_REQUEST, "이름 형식이 올바르지 않습니다."),
    EMAIL_DUPLICATION(BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    EXPIRED_TOKEN(BAD_REQUEST, "토큰이 만료되었습니다"),
    PAYLOAD_EMAIL_MISSING(BAD_REQUEST, "페이로드에 이메일이 없습니다."),
    NO_SEARCH_MEMBER(BAD_REQUEST, "유저를 찾을 수 없습니다."),
    NO_SEARCH_COMPANY_REGISTRATION(BAD_REQUEST, "회사 등록 정보를 찾을 수 없습니다."),
    NO_SEARCH_COMPANY(BAD_REQUEST, "회사를 찾을 수 없습니다."),
    INVALID_JWT_TOKEN(BAD_REQUEST, "JWT 유효성 검증에 실패하였습니다."),
    DUPLICATE_PASSWORD(BAD_REQUEST, "중복된 비밀번호 요청입니다."),
    INVALID_FILE_FORMAT(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "파일 크기는 5MB를 초과할 수 없습니다."),
    INVALID_BUSINESS_REGISTRATION_NUMBER_FORMAT(BAD_REQUEST, "사업자 등록 번호 형식이 올바르지 않습니다."),
    INVALID_PHONE_NUMBER_FORMAT(BAD_REQUEST, "전화번호 형식이 올바르지 않습니다."),
    TITLE_REQUIRED(BAD_REQUEST, "제목을 입력하세요."),
    CONTENT_REQUIRED(BAD_REQUEST, "내용을 입력하세요."),
    POST_NOTFOUND(BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    POST_FORBIDDEN(BAD_REQUEST, "비밀글을 열람할 권한이 없습니다."),
    POST_CANNOT_EDIT(BAD_REQUEST, "답변이 달린 게시글은 수정할 수 없습니다."),
    BOARD_AUTHOR_ONLY_EXCEPTION(BAD_REQUEST, "수정, 삭제 권한이 없습니다."),
    MEMBER_ALREADY_DELETED(BAD_REQUEST, "이미 탈퇴한 회원입니다."),
    INVALID_MEMBER_STATE(BAD_REQUEST, "유효하지 않은 회원 상태입니다."),
    ALREADY_REGISTERED_COMPANY(BAD_REQUEST, "이미 등록된 회사입니다."),
    // 401 Unauthorized
    INVALID_TOKEN_EXTRACTOR(UNAUTHORIZED, "토큰 추출에 실패했습니다."),
    NOT_AUTHORIZED(UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    // 403 Forbidden
    ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    USER_DORMANT(FORBIDDEN, "휴면 계정입니다. 다시 인증해주세요."),
    // 404 Not Found
    NOT_SUPPORT_API(BAD_REQUEST, "지원하지 않는 API입니다."),

    // 500 Internal Server Error
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    PASSWORD_ENCRYPTION_ERROR(INTERNAL_SERVER_ERROR, "암호화에 문제가 발생했습니다. 관리자에게 문의하세요."),
    SHA256AlgorithmNotFoundException(INTERNAL_SERVER_ERROR, "SHA-256 알고리즘을 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
    FILE_DELETE_ERROR(INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
