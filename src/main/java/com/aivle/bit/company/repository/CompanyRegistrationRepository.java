package com.aivle.bit.company.repository;

import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistration, Long> {

    List<CompanyRegistration> findAllByMember(Member member);

    List<CompanyRegistration> findByMemberId(Long id);

    Optional<CompanyRegistration> findByRegistrationId(UUID uuid);

    Optional<CompanyRegistration> findFirstByMemberIdOrderByModifiedAtDesc(Long id);
}
