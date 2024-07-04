package com.aivle.bit.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password,

    @NotBlank(message = "이름을 입력해주세요.")
    String name,

    @NotBlank(message = "주소를 입력해주세요.")
    String address
) {

}
