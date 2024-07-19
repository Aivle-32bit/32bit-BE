package com.aivle.bit.company.service;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.dto.response.CompanySearchResponse;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;
import com.aivle.bit.global.utils.HangulUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanySearchService {

    private final CompanyRepository companyRepository;

    public CompanySearchService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<CompanySearchResponse> searchCompanies(String keyword) {
        Optional<Company> company = companyRepository.findByNameAndIsDeletedFalse(keyword);
        if (company.isEmpty()) {
            throw new AivleException(ErrorCode.NO_COMPANY);
        }
        Company foundCompany = company.get();
        return List.of(
            new CompanySearchResponse(foundCompany.getId(), foundCompany.getName(), foundCompany.getBusinessType()));
    }

    @Transactional(readOnly = true)
    public List<CompanySearchResponse> autocompleteCompanies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        List<Company> startsWithKeyword;
        List<Company> containsKeyword;

        if (isKorean(keyword)) {
            List<Company> allCompanies = companyRepository.findAllByIsDeletedFalse(Pageable.unpaged()).getContent();
            if (keyword.matches(HangulUtils.HANGUL_CONSONANTS)) {
                startsWithKeyword = allCompanies.stream()
                    .filter(
                        company -> HangulUtils.getInitialConsonant(company.getName().charAt(0)) == keyword.charAt(0))
                    .sorted(Comparator.comparing(company -> company.getName().substring(1)))
                    .toList();

                containsKeyword = allCompanies.stream()
                    .filter(company -> HangulUtils.containsInitialConsonant(company.getName(), keyword.charAt(0))
                        && !startsWithKeyword.contains(company))
                    .sorted(Comparator.comparing(Company::getName))
                    .toList();
            } else {
                startsWithKeyword = allCompanies.stream()
                    .filter(company -> company.getName().startsWith(keyword))
                    .sorted(Comparator.comparing(company -> company.getName().substring(keyword.length())))
                    .toList();

                containsKeyword = allCompanies.stream()
                    .filter(company -> company.getName().contains(keyword) && !startsWithKeyword.contains(company))
                    .sorted(Comparator.comparing(Company::getName))
                    .toList();
            }
        } else {
            List<Company> allCompanies = companyRepository.findAllByIsDeletedFalse(Pageable.unpaged()).getContent();
            startsWithKeyword = allCompanies.stream()
                .filter(company -> company.getName().toLowerCase().startsWith(keyword.toLowerCase()))
                .sorted(Comparator.comparing(company -> company.getName().substring(keyword.length())))
                .toList();

            containsKeyword = allCompanies.stream()
                .filter(company -> company.getName().toLowerCase().contains(keyword.toLowerCase())
                    && !startsWithKeyword.contains(company))
                .sorted(Comparator.comparing(Company::getName))
                .toList();
        }

        List<Company> sortedCompanies = new ArrayList<>();
        sortedCompanies.addAll(startsWithKeyword);
        sortedCompanies.addAll(containsKeyword);

        return sortedCompanies.stream()
            .map(company -> new CompanySearchResponse(company.getId(), company.getName(), company.getBusinessType()))
            .toList();
    }

    private boolean isKorean(String keyword) {
        return keyword.chars()
            .anyMatch(ch -> (ch >= '가' && ch <= '힣') || (ch >= 'ㄱ' && ch <= 'ㅎ') || (ch >= 'ㅏ' && ch <= 'ㅣ'));
    }
}
