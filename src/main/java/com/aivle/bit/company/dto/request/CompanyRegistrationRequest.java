package com.aivle.bit.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CompanyRegistrationRequest(
    @NotBlank(message = "회사명을 입력해주세요.")
    String companyName,

    @NotBlank(message = "대표자명을 입력해주세요.")
    String representativeName,

    @NotBlank(message = "사업자 등록번호를 입력해주세요.")
    String companyRegistrationNumber,

    @NotBlank(message = "회사 주소를 입력해주세요.")
    String companyAddress,

    @NotBlank(message = "회사 전화번호를 입력해주세요.")
    String companyPhoneNumber,

    @NotBlank(message = "사업 종류를 입력해주세요.")
    String businessType,

    @NotNull(message = "사업자 등록증 이미지를 등록해주세요.")
    MultipartFile image
) {

}
