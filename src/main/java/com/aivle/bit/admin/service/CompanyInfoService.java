package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;

import com.aivle.bit.admin.dto.request.CompanyInfoRequest;
import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.CompanyInfo;
import com.aivle.bit.company.repository.CompanyInfoRepository;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.global.exception.AivleException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyRepository companyRepository;

    public CompanyInfo getCompanyInfo(Long companyId) {
        int now = LocalDate.now().getYear();
        return companyInfoRepository.findByCompanyIdAndYear(companyId, now)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
    }

    public CompanyInfo save(Long companyId, CompanyInfoRequest companyInfoRequest) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));

        companyInfoRepository.findByCompanyAndYear(company, LocalDate.now().getYear()).ifPresent(
            companyInfoRepository::delete
        );

        CompanyInfo companyInfo = CompanyInfo.of(LocalDate.now().getYear(), companyInfoRequest.numEmployees(),
            companyInfoRequest.experience(),
            companyInfoRequest.numHires(), companyInfoRequest.numResignations(), company);

        return companyInfoRepository.save(companyInfo);
    }
}
