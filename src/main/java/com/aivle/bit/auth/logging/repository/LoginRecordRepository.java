package com.aivle.bit.auth.logging.repository;

import com.aivle.bit.auth.logging.domain.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

}
