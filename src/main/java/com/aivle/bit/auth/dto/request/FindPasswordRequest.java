package com.aivle.bit.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record FindPasswordRequest(
    @NotEmpty(message = "이메일을 입력해주세요.")
    String email
) {

}
