package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.CompanyInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    Optional<CompanyInfo> findByCompanyIdAndYear(Long companyId, int now);

    Optional<CompanyInfo> findByCompanyAndYear(Company company, int year);
}
