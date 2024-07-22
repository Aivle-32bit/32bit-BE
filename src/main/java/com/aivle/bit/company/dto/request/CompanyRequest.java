package com.aivle.bit.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CompanyRequest(
    @NotBlank(message = "회사명을 입력해주세요.")
    String name,

    @NotBlank(message = "사업 종류를 입력해주세요.")
    String businessType,

    @NotNull(message = "사업자 등록증 이미지를 등록해주세요.")
    MultipartFile image
) {

}
