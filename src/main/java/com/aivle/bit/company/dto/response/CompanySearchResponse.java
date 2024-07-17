package com.aivle.bit.company.dto.response;

import lombok.Getter;

@Getter
public class CompanySearchResponse {

    private final Long companyId;
    private final String companyName;
    private final String businessType;

    private CompanySearchResponse(Long companyId, String companyName, String businessType) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.businessType = businessType;
    }

    public static CompanySearchResponse of(Long companyId, String companyName, String businessType) {
        return new CompanySearchResponse(companyId, companyName, businessType);
    }
}
