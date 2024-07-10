package com.aivle.bit.admin.dto.response;

import lombok.Getter;

@Getter
public class LoginStatisticsResponse {

    private final long totalLoginAttempts;
    private final long successfulLoginAttempts;
    private final long failedLoginAttempts;

    private LoginStatisticsResponse(long totalLoginAttempts, long successfulLoginAttempts, long failedLoginAttempts) {
        this.totalLoginAttempts = totalLoginAttempts;
        this.successfulLoginAttempts = successfulLoginAttempts;
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public static LoginStatisticsResponse of(long totalLoginAttempts, long successfulLoginAttempts,
                                             long failedLoginAttempts) {
        return new LoginStatisticsResponse(totalLoginAttempts, successfulLoginAttempts, failedLoginAttempts);
    }
}