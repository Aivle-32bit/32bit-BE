package com.aivle.bit.admin.dto.response;

import lombok.Getter;

@Getter
public class RegistrationStatisticsResponse {

    private final long dailyRegistrations;
    private final long weeklyRegistrations;
    private final long monthlyRegistrations;

    private RegistrationStatisticsResponse(long dailyRegistrations, long weeklyRegistrations,
                                           long monthlyRegistrations) {
        this.dailyRegistrations = dailyRegistrations;
        this.weeklyRegistrations = weeklyRegistrations;
        this.monthlyRegistrations = monthlyRegistrations;
    }

    public static RegistrationStatisticsResponse of(long dailyRegistrations, long weeklyRegistrations,
                                                    long monthlyRegistrations) {
        return new RegistrationStatisticsResponse(dailyRegistrations, weeklyRegistrations, monthlyRegistrations);
    }
}
