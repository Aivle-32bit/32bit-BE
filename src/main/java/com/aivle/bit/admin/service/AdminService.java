package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;

import com.aivle.bit.admin.dto.response.LoginStatisticsResponse;
import com.aivle.bit.admin.dto.response.MemberResponse;
import com.aivle.bit.admin.dto.response.MemberStateCountsResponse;
import com.aivle.bit.admin.dto.response.RegistrationStatisticsResponse;
import com.aivle.bit.admin.dto.response.VisitCountResponse;
import com.aivle.bit.admin.dto.response.VisitorStatisticsResponse;
import com.aivle.bit.auth.logging.domain.VisitorCount;
import com.aivle.bit.auth.logging.repository.VisitorCountRepository;
import com.aivle.bit.auth.logging.service.LoginRecordService;
import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.repository.CompanyRegistrationRepository;
import com.aivle.bit.company.service.S3Service;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import com.aivle.bit.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final CompanyRegistrationRepository companyRegistrationRepository;
    private final S3Service s3Service;
    private final SendApproveEmailService sendApproveEmailService;
    private final SendRejectEmailService sendRejectEmailService;
    private final SendDormantEmailService sendDormantEmailService;
    private final LoginRecordService loginRecordService;
    private final VisitorCountRepository visitorCountRepository;


    public List<MemberResponse> findAllMember(MemberState state) {
        List<Member> members = (state == null) ?
            memberRepository.findByIsAdminFalseAndIsDeletedFalse() :
            memberRepository.findByIsAdminFalseAndStateAndIsDeletedFalse(state);

        return members.stream()
            .map(MemberResponse::from)
            .toList();
    }

    public List<CompanyRegistrationResponse> findCompanyRegistrations(Long id) {
        List<CompanyRegistration> registrations = companyRegistrationRepository.findByMemberId(id);
        return registrations.stream()
            .map((CompanyRegistration registration) -> CompanyRegistrationResponse.from(registration,
                s3Service::generatePresignedUrl))
            .toList();
    }

    @Transactional
    public void approveUser(UUID uuid) {
        CompanyRegistration registration = companyRegistrationRepository.findByRegistrationId(uuid)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
        registration.approve();

        memberRepository.findByIdAndIsDeletedFalse(registration.getMember().getId()).ifPresent(member -> {
            member.approve();
            memberRepository.save(member);
        });

        companyRegistrationRepository.save(registration);

        sendApproveEmailService.send(registration.getMember().getEmail(), registration.getMember().getName());
    }

    @Transactional
    public void rejectUser(UUID uuid, String reason) {
        CompanyRegistration registration = companyRegistrationRepository.findByRegistrationId(uuid)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
        registration.reject();

        memberRepository.findByIdAndIsDeletedFalse(registration.getMember().getId()).ifPresent(member -> {
            member.reject();
            memberRepository.save(member);
        });

        companyRegistrationRepository.save(registration);
        sendRejectEmailService.send(registration.getMember().getEmail(), registration.getMember().getName(),
            reason);
    }

    public void updateDormant(Long id) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
            () -> new AivleException(NO_SEARCH_MEMBER)
        );

        member.dormant();
        memberRepository.save(member);

        sendDormantEmailService.send(member.getEmail(), member.getName());
    }

    public void deleteUser(Long id) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
            () -> new AivleException(NO_SEARCH_MEMBER)
        );
        member.delete();
        memberRepository.save(member);
    }


    public MemberStateCountsResponse getMemberStateCounts() {
        List<MemberState> states = memberRepository.findAll().stream()
            .map(Member::getState)
            .toList();

        long activeCount = states.stream().filter(state -> state == MemberState.UNVERIFIED).count();
        long dormantCount = states.stream().filter(state -> state == MemberState.VERIFIED).count();
        long rejectedCount = states.stream().filter(state -> state == MemberState.USER_DORMANT).count();

        return MemberStateCountsResponse.of(activeCount, dormantCount, rejectedCount);
    }

    public LoginStatisticsResponse getLoginStatistics() {
        long totalLoginAttempts = loginRecordService.getTotalLoginAttempts();
        long successfulLoginAttempts = loginRecordService.getSuccessfulLoginAttempts();
        long failedLoginAttempts = loginRecordService.getFailedLoginAttempts();

        return LoginStatisticsResponse.of(totalLoginAttempts, successfulLoginAttempts, failedLoginAttempts);
    }

    public RegistrationStatisticsResponse getRegistrationStatistics() {
        long dailyRegistrations = memberRepository.countByCreatedAtAfter(LocalDate.now().minusDays(1).atStartOfDay());
        long weeklyRegistrations = memberRepository.countByCreatedAtAfter(LocalDate.now().minusWeeks(1).atStartOfDay());
        long monthlyRegistrations = memberRepository.countByCreatedAtAfter(
            LocalDate.now().minusMonths(1).atStartOfDay());
        return RegistrationStatisticsResponse.of(dailyRegistrations, weeklyRegistrations, monthlyRegistrations);
    }

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
