package com.aivle.bit.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record EmailSendAllRequest(
    @NotEmpty(message = "유저 이메일이 비어있습니다.")
    List<String> memberEmails,
    @NotEmpty(message = "내용이 비어있습니다.")
    String content
) {

}
