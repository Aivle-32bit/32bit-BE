package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.request.CompanyInfoRequest;
import com.aivle.bit.admin.service.CompanyInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/company")
public class CompanyInfoController {

    private final CompanyInfoService companyInfoService;

    @PutMapping("/{id}/info")
    public void updateCompanyInfo(@RequestBody @Valid CompanyInfoRequest companyInfoRequest, @PathVariable Long id) {
        companyInfoService.save(id, companyInfoRequest);
    }
}
