package com.aivle.bit.board.dto.request;

import com.aivle.bit.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

public record BoardCreateRequest(
    @NotBlank(message = "제목이 비어있습니다.")
    String title,
    @NotBlank(message = "내용이 비어있습니다.")
    String content,
    @NotNull(message = "비밀 상태가 비어있습니다.")
    Boolean isSecret,
    Long id,
    Member member,
    Boolean isDeleted,
    LocalDateTime createdAt
) {
    @Builder
    public BoardCreateRequest {
        if (isSecret == null) {
            isSecret = false;
        }
    }
}
