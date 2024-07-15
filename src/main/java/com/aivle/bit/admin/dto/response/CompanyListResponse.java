package com.aivle.bit.admin.dto.response;

import com.aivle.bit.company.domain.Company;
import lombok.Getter;
import org.springframework.data.web.PagedModel;

@Getter
public class CompanyListResponse {

    private final PagedModel<Company> companies;

    private CompanyListResponse(PagedModel<Company> companies) {
        this.companies = companies;
    }

    public static CompanyListResponse from(PagedModel<Company> companies) {
        return new CompanyListResponse(companies);
    }
}
