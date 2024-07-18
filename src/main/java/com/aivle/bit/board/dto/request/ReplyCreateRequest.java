package com.aivle.bit.board.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyCreateRequest(
    @NotBlank(message = "내용이 비어있습니다.")
    String content
) {

}
