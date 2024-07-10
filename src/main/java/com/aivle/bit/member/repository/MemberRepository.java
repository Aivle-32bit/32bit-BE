package com.aivle.bit.member.repository;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    List<Member> findByIsAdminFalseAndIsDeletedFalse();

    List<Member> findByIsAdminFalseAndStateAndIsDeletedFalse(MemberState state);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
