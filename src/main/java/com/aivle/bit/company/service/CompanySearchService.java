package com.aivle.bit.company.service;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.dto.response.CompanySearchResponse;
import com.aivle.bit.company.repository.CompanyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanySearchService {

    private final CompanyRepository companyRepository;
    private final StringSimilarityService stringSimilarityService;

    public List<CompanySearchResponse> getCompaniesByName(String name) {
        List<Company> companies = companyRepository.findByNameContainingIgnoreCase(name);
        return companies.stream()
            .sorted((c1, c2) -> Double.compare(
                stringSimilarityService.jaccardSimilarity(name, c2.getName()),
                stringSimilarityService.jaccardSimilarity(name, c1.getName())
            ))
            .map(company -> CompanySearchResponse.of(company.getId(), company.getName(), company.getBusinessType()))
            .toList();
    }
}