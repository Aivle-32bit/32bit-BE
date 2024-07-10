package com.aivle.bit.auth.logging.service;

import com.aivle.bit.auth.logging.domain.LoginRecord;
import com.aivle.bit.auth.logging.repository.LoginRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginRecordService {

    private final LoginRecordRepository loginRecordRepository;

    public void logLoginAttempt(String ipAddress, String userAgent, Long userId, boolean success) {
        LoginRecord loginRecord = LoginRecord.of(ipAddress, userAgent, userId, success);
        loginRecordRepository.save(loginRecord);
    }

    public long getTotalLoginAttempts() {
        return loginRecordRepository.count();
    }

    public long getSuccessfulLoginAttempts() {
        return loginRecordRepository.countBySuccess(true);
    }

    public long getFailedLoginAttempts() {
        return loginRecordRepository.countBySuccess(false);
    }
}
