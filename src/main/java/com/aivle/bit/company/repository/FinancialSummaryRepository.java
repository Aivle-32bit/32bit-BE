package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.FinancialSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialSummaryRepository extends JpaRepository<FinancialSummary, Long> {

    Optional<FinancialSummary> findByCompanyIdAndYear(Long companyId, int year);

    List<FinancialSummary> findByCompanyIdOrderByYearAsc(Long companyId);

    void deleteByCompanyAndYear(Company company, int currentYear);

    Optional<FinancialSummary> findByCompanyAndYear(Company company, int year);
}
