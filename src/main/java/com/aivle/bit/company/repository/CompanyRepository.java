package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.Company;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Page<Company> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Company> findByIdAndIsDeletedFalse(Long id);

    Optional<Company> findByNameAndIsDeletedFalse(String companyName);

    Optional<Company> findByName(String name);
}
