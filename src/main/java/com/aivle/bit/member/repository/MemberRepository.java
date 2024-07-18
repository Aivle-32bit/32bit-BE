package com.aivle.bit.member.repository;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.company WHERE m.email = :email AND m.isDeleted = false")
    Optional<Member> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    List<Member> findByIsAdminFalseAndIsDeletedFalse();

    List<Member> findByIsAdminFalseAndStateAndIsDeletedFalse(MemberState state);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);

    long countByCreatedAtAfter(LocalDateTime createdAt);

    Optional<Member> findByCompanyId(Long id);

    Optional<Member> findByNameAndAddressAndIsDeletedFalse(String name, String address);
}
