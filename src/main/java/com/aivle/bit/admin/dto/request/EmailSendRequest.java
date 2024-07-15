package com.aivle.bit.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record EmailSendRequest(
    @NotEmpty(message = "내용이 비어있습니다.")
    String content
) {

}
