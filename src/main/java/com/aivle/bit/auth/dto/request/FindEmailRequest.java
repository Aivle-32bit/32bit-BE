package com.aivle.bit.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record FindEmailRequest(
    @NotEmpty(message = "이름을 입력해주세요.")
    String name,
    @NotEmpty(message = "주소를 입력해주세요.")
    String address
) {

}
