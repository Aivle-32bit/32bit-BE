package com.aivle.bit.board.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BoardUpdateRequest(
    @NotBlank(message = "제목이 비어있습니다.")
    String title,
    @NotBlank(message = "내용이 비어있습니다.")
    String content,
    @NotBlank(message = "비밀 상태가 비어있습니다.")
    Boolean isSecret
) {

}