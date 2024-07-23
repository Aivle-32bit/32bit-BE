package com.aivle.bit.company.controller;

import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.company.domain.CompanyInfo;
import com.aivle.bit.company.dto.response.CompanyInfoResponse;
import com.aivle.bit.company.dto.response.CompanyReportResponse;
import com.aivle.bit.company.dto.response.MetricsResponse;
import com.aivle.bit.company.dto.response.SwotResponse;
import com.aivle.bit.admin.service.CompanyInfoService;
import com.aivle.bit.company.service.CompanyReportService;
import com.aivle.bit.company.service.SwotService;
import com.aivle.bit.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company-report")
public class CompanyReportController {

    private final CompanyReportService companyReportService;
    private final SwotService swotService;
    private final CompanyInfoService companyInfoService;

    @GetMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyReportResponse getCompanyReport(@PathVariable Long companyId, @JwtLogin Member member) {
        return companyReportService.getCompanyReport(companyId);
    }

    @GetMapping("/{companyId}/{metrics}")
    @ResponseStatus(HttpStatus.OK)
    public MetricsResponse getCompanyReport(@PathVariable Long companyId, @PathVariable String metrics,
                                            @JwtLogin Member member) {
        return companyReportService.getMetricsReport(companyId, metrics);
    }

    @GetMapping("/{companyId}/swot")
    @ResponseStatus(HttpStatus.OK)
    public SwotResponse getSwotReport(@PathVariable Long companyId, @JwtLogin Member member) {
        return swotService.getSwotReport(companyId);
    }

    @GetMapping("/{companyId}/info")
    @ResponseStatus(HttpStatus.OK)
    public CompanyInfoResponse getCompanyInfo(@PathVariable Long companyId, @JwtLogin Member member) {
        CompanyInfo companyInfo = companyInfoService.getCompanyInfo(companyId);

        return CompanyInfoResponse.from(companyInfo);
    }
}
