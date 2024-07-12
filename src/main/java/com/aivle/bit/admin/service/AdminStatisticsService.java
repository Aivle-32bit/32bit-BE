package com.aivle.bit.admin.service;

import com.aivle.bit.admin.dto.response.LoginStatisticsResponse;
import com.aivle.bit.admin.dto.response.MemberStateCountsResponse;
import com.aivle.bit.admin.dto.response.RegistrationStatisticsResponse;
import com.aivle.bit.admin.dto.response.VisitCountResponse;
import com.aivle.bit.admin.dto.response.VisitorStatisticsResponse;
import com.aivle.bit.auth.logging.domain.VisitorCount;
import com.aivle.bit.auth.logging.repository.VisitorCountRepository;
import com.aivle.bit.auth.logging.service.LoginRecordService;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import com.aivle.bit.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final MemberRepository memberRepository;
    private final LoginRecordService loginRecordService;
    private final VisitorCountRepository visitorCountRepository;


    @Transactional(readOnly = true)
    public MemberStateCountsResponse getMemberStateCounts() {
        List<MemberState> states = memberRepository.findAll().stream()
            .map(Member::getState)
            .toList();

        long unverifiedCount = states.stream().filter(state -> state == MemberState.UNVERIFIED).count();
        long verifiedCount = states.stream().filter(state -> state == MemberState.VERIFIED).count();
        long dormantCount = states.stream().filter(state -> state == MemberState.USER_DORMANT).count();

        return MemberStateCountsResponse.of(unverifiedCount, verifiedCount, dormantCount);
    }

    public LoginStatisticsResponse getLoginStatistics() {
        long totalLoginAttempts = loginRecordService.getTotalLoginAttempts();
        long successfulLoginAttempts = loginRecordService.getSuccessfulLoginAttempts();
        long failedLoginAttempts = loginRecordService.getFailedLoginAttempts();

        return LoginStatisticsResponse.of(totalLoginAttempts, successfulLoginAttempts, failedLoginAttempts);
    }

    @Transactional(readOnly = true)
    public RegistrationStatisticsResponse getRegistrationStatistics() {
        long dailyRegistrations = memberRepository.countByCreatedAtAfter(LocalDate.now().minusDays(1).atStartOfDay());
        long weeklyRegistrations = memberRepository.countByCreatedAtAfter(LocalDate.now().minusWeeks(1).atStartOfDay());
        long monthlyRegistrations = memberRepository.countByCreatedAtAfter(
            LocalDate.now().minusMonths(1).atStartOfDay());
        return RegistrationStatisticsResponse.of(dailyRegistrations, weeklyRegistrations, monthlyRegistrations);
    }

    @Transactional(readOnly = true)
    public VisitorStatisticsResponse getVisitorStatistics(LocalDate startDate, LocalDate endDate) {
        List<VisitorCount> visitorCounts = visitorCountRepository.findByVisitDateBetween(startDate, endDate);

        List<VisitCountResponse> visitorCountDTOs = visitorCounts.stream()
            .map(vc -> VisitCountResponse.of(vc.getVisitDate(), vc.getVisitCount()))
            .toList();

        List<VisitCountResponse> completeVisitorCounts = Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1)
            .map(date -> visitorCountDTOs.stream()
                .filter(dto -> dto.getVisitDate().equals(date))
                .findFirst()
                .orElse(VisitCountResponse.of(date, 0L)))
            .collect(Collectors.toList());

        return VisitorStatisticsResponse.of(completeVisitorCounts);
    }
}
