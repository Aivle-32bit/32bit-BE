package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.response.CompanyListResponse;
import com.aivle.bit.admin.service.CompanyManageService;
import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.dto.request.CompanyRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{id}/create-reports")
    @ResponseStatus(HttpStatus.OK)
    public void createReports(@PathVariable Long id,
                              @RequestParam @NotNull(message = "파일이 존재하지 않습니다.") MultipartFile file) {
        log.info("Creating reports for company with ID: {}", id);
        companyManageService.createReports(id, file);
    }

    @PostMapping
    public ResponseEntity<Company> addCompany(@ModelAttribute CompanyRequest request) {
        log.info("Adding company with name: {}", request.name());
        Company company = companyManageService.addCompany(request.name(), request.businessType(), request.image());
        return ResponseEntity.ok(company);
    }
}
