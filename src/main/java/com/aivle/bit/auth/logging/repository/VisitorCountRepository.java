package com.aivle.bit.auth.logging.repository;

import com.aivle.bit.auth.logging.domain.VisitorCount;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {

    Optional<VisitorCount> findByVisitDate(LocalDate visitDate);

    List<VisitorCount> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);
}
