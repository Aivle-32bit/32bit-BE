package com.aivle.bit.auth.dto;

import jakarta.validation.constraints.NotNull;

public record SignInRequest(
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    String email,
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    String password) {

}