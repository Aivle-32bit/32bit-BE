package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.response.CompanyListResponse;
import com.aivle.bit.admin.service.CompanyManageService;
import com.aivle.bit.company.domain.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/company")
@Slf4j
public class CompanyManageController {

    private final CompanyManageService companyManageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompanyListResponse getAllCompanies(Pageable pageable) {
        log.info("Fetching all companies");
        PagedModel<Company> allCompanies = companyManageService.getAllCompanies(pageable);
        return CompanyListResponse.from(allCompanies);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompany(@PathVariable Long id) {
        log.info("Deleting company with ID: {}", id);
        companyManageService.deleteCompany(id);
    }
}
