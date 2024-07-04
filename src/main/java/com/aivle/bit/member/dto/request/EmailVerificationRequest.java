package com.aivle.bit.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerificationRequest(
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,

    @Pattern(regexp = "^[0-9]{6}$", message = "인증 코드는 6자리 숫자입니다.")
    @NotBlank(message = "인증 코드를 입력해주세요.")
    String code
) {

}
