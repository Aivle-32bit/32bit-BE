package com.aivle.bit.notice.repository;

import com.aivle.bit.notice.domain.Notice;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByIsDeletedFalse(Pageable pageable);

    Optional<Notice> findByIdAndIsDeletedFalse(Long id);
}
