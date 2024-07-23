package com.aivle.bit.company.controller;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.dto.response.FinancialSummaryResponse;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.company.repository.FinancialSummaryRepository;
import com.aivle.bit.global.exception.AivleException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialSummaryService {

    private final FinancialSummaryRepository financialSummaryRepository;
    private final CompanyRepository companyRepository;

    public List<FinancialSummaryResponse> getFinancialStatement(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));

        return financialSummaryRepository.findByCompany(company).stream()
            .map(FinancialSummaryResponse::from)
            .toList();
    }
}
