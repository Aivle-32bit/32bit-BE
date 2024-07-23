package com.aivle.bit.company.service;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_ANALYSIS;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.FinancialSummary;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.company.repository.FinancialSummaryRepository;
import com.aivle.bit.global.exception.AivleException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final FinancialSummaryRepository financialSummaryRepository;
    private final CompanyRepository companyRepository;

    public FinancialSummary getAnalysis(Long id) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));

        return financialSummaryRepository.findByCompanyAndYear(company, LocalDate.now().getYear()).orElseThrow(
            () -> new AivleException(NO_SEARCH_COMPANY_ANALYSIS)
        );

    }
}
