package com.aivle.bit.admin.service;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyManageService {

    private final CompanyRepository companyRepository;

    public PagedModel<Company> getAllCompanies(Pageable pageable) {
        return new PagedModel<>(companyRepository.findAll(pageable));
    }
}
