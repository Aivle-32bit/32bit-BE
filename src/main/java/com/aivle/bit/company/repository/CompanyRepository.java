package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Boolean existsByName(String name);
}
