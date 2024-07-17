package com.aivle.bit.notice.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record NoticeUpdateRequest(
    @NotBlank(message = "제목이 비어있습니다.")
    String title,
    @NotBlank(message = "내용이 비어있습니다.")
    String content,
    LocalDateTime modifiedAt
) {

}