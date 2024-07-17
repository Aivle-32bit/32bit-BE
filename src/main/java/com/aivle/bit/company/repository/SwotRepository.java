package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Swot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwotRepository extends JpaRepository<Swot, Long> {

    List<Swot> findByCompanyIdAndYear(Long companyId, int year);
}
