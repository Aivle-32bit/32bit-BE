package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.response.LoginStatisticsResponse;
import com.aivle.bit.admin.dto.response.MemberStateCountsResponse;
import com.aivle.bit.admin.dto.response.RegistrationStatisticsResponse;
import com.aivle.bit.admin.dto.response.VisitorStatisticsResponse;
import com.aivle.bit.admin.service.AdminStatisticsService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/statistics")
@Slf4j
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @GetMapping("/member-states")
    @ResponseStatus(HttpStatus.OK)
    public MemberStateCountsResponse getMemberStateCounts() {
        log.info("Fetching member state counts");
        return adminStatisticsService.getMemberStateCounts();
    }

    @GetMapping("/login-statistics")
    @ResponseStatus(HttpStatus.OK)
    public LoginStatisticsResponse getLoginStatistics() {
        log.info("Fetching login statistics");
        return adminStatisticsService.getLoginStatistics();
    }

    @GetMapping("/registration-statistics")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationStatisticsResponse getRegistrationStatistics() {
        log.info("Fetching registration statistics");
        return adminStatisticsService.getRegistrationStatistics();
    }


    @GetMapping("/visitor-statistics")
    @ResponseStatus(HttpStatus.OK)
    public VisitorStatisticsResponse getVisitorStatistics(
        @RequestParam(value = "startDate", required = false, defaultValue = "#{T(java.time.LocalDate).now().minusDays(7)}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate", required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("Fetching visitor statistics from {} to {}", startDate, endDate);
        return adminStatisticsService.getVisitorStatistics(startDate, endDate);
    }
}
