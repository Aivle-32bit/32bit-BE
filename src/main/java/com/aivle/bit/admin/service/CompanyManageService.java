package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_REQUEST;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyManageService {

    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;

    public PagedModel<Company> getAllCompanies(Pageable pageable) {
        return new PagedModel<>(companyRepository.findAllByIsDeletedFalse(pageable));
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
            () -> new AivleException(NO_SEARCH_COMPANY)
        );
        company.delete();
        memberRepository.findByCompanyId(id).orElseThrow(
            () -> new AivleException(INVALID_REQUEST)
        ).updateCompany(null);
    }
}
