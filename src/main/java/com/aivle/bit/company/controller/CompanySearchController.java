package com.aivle.bit.company.controller;

import com.aivle.bit.company.dto.response.CompanySearchResponse;
import com.aivle.bit.company.service.CompanySearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/company-search")
@RequiredArgsConstructor
public class CompanySearchController {

    private final CompanySearchService companySearchService;

    @GetMapping("/search")
    public List<CompanySearchResponse> searchCompanies(@RequestParam String keyword) {
        return companySearchService.searchCompanies(keyword);
    }


    @GetMapping("/autocomplete")
    public List<CompanySearchResponse> autocompleteCompanies(@RequestParam String keyword) {
        return companySearchService.autocompleteCompanies(keyword);
    }
}