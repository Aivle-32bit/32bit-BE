package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.Swot;
import com.aivle.bit.company.domain.SwotCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwotRepository extends JpaRepository<Swot, Long> {

    List<Swot> findByCompanyIdAndYear(Long companyId, int year);

    Optional<Swot> findByCompanyAndYearAndCategoryAndDescription(Company company, int year, SwotCategory category,
                                                                 String description);
}
