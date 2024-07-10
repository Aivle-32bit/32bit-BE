package com.aivle.bit.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectRequest(

    @NotBlank(message = "거절 사유를 입력해주세요.")
    String reason
) {

}
