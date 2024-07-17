package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Metrics;
import com.aivle.bit.company.domain.MetricsSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricsSummaryRepository extends JpaRepository<MetricsSummary, Long> {

    Optional<MetricsSummary> findByCompanyIdAndMetrics(Long companyId, Metrics metric);
}
