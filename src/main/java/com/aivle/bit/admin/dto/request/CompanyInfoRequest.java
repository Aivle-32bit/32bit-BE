package com.aivle.bit.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record CompanyInfoRequest(
    @NotNull(message = "회사명을 입력해주세요.")
    int experience,
    @NotNull(message = "직원 수를 입력해주세요.")
    int numEmployees,
    @NotNull(message = "입사자 수를 입력해주세요.")
    int numHires,
    @NotNull(message = "퇴사자 수를 입력해주세요.")
    int numResignations
) {

}
