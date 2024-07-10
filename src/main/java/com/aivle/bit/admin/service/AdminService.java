package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;

import com.aivle.bit.admin.dto.response.MemberResponse;
import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.repository.CompanyRegistrationRepository;
import com.aivle.bit.company.service.S3Service;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import com.aivle.bit.member.repository.MemberRepository;
import java.util.List;
import java.util.UUID;
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
}
