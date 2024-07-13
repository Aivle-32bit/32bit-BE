package com.aivle.bit.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateRequest(
    @NotBlank(message = "이름을 입력해주세요.")
    String name,
    @NotBlank(message = "주소를 입력해주세요.")
    String address
) {

}
