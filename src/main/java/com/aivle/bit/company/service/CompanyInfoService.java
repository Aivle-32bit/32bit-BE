package com.aivle.bit.company.service;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;

import com.aivle.bit.company.domain.CompanyInfo;
import com.aivle.bit.company.repository.CompanyInfoRepository;
import com.aivle.bit.global.exception.AivleException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;

    public CompanyInfo getCompanyInfo(Long companyId) {
        int now = LocalDate.now().getYear();
        return companyInfoRepository.findByCompanyIdAndYear(companyId, now)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
    }
}
