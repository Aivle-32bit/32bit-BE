package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.ALREADY_REGISTERED_COMPANY;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;

import com.aivle.bit.admin.dto.response.MemberInfoResponse;
import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.repository.CompanyRegistrationRepository;
import com.aivle.bit.company.repository.CompanyRepository;
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
public class MemberManageService {

    private final MemberRepository memberRepository;
    private final CompanyRegistrationRepository companyRegistrationRepository;
    private final S3Service s3Service;
    private final SendApproveEmailService sendApproveEmailService;
    private final SendRejectEmailService sendRejectEmailService;
    private final SendDormantEmailService sendDormantEmailService;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> findAllMember(MemberState state) {
        List<Member> members = (state == null) ?
            memberRepository.findByIsAdminFalseAndIsDeletedFalse() :
            memberRepository.findByIsAdminFalseAndStateAndIsDeletedFalse(state);

        return members.stream()
            .map(MemberInfoResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CompanyRegistrationResponse> findCompanyRegistrations(Long id) {
        List<CompanyRegistration> registrations = companyRegistrationRepository.findByMemberId(id);
        return registrations.stream()
            .map(registration -> CompanyRegistrationResponse.from(registration, s3Service::generatePresignedUrl))
            .toList();
    }

    @Transactional
    public void approveUser(UUID uuid) {
        CompanyRegistration registration = findRegistrationById(uuid);
        approveRegistration(registration);

        Company company = createAndSaveCompany(registration);

        updateMemberWithCompany(registration.getMember().getId(), company);

        saveRegistration(registration);

        sendApprovalEmail(registration.getMember().getEmail(), registration.getMember().getName());
    }

    @Transactional
    public void rejectUser(UUID uuid, String reason) {
        CompanyRegistration registration = findRegistrationById(uuid);
        rejectRegistration(registration, reason);
    }

    @Transactional
    public void updateDormant(Long id) {
        Member member = findMemberById(id);
        setMemberDormant(member);
    }

    @Transactional
    public void deleteUser(Long id) {
        Member member = findMemberById(id);
        deleteMember(member);
    }

    @Transactional(readOnly = true)
    public CompanyRegistrationResponse findLatestByMember(Long id) {
        CompanyRegistration registration = findLatestRegistrationByMemberId(id);
        return CompanyRegistrationResponse.from(registration, s3Service::generatePresignedUrl);
    }

    private CompanyRegistration findRegistrationById(UUID uuid) {
        return companyRegistrationRepository.findByRegistrationId(uuid)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
    }

    private void approveRegistration(CompanyRegistration registration) {
        registration.approve();
    }

    private void rejectRegistration(CompanyRegistration registration, String reason) {
        registration.reject();
        memberRepository.findByIdAndIsDeletedFalse(registration.getMember().getId()).ifPresent(member -> {
            member.reject();
            memberRepository.save(member);
        });

        companyRegistrationRepository.save(registration);
        sendRejectEmailService.send(registration.getMember().getEmail(), registration.getMember().getName(), reason);
    }

    private Company createAndSaveCompany(CompanyRegistration registration) {
        if (companyRepository.existsByName(registration.getCompanyName())) {
            throw new AivleException(ALREADY_REGISTERED_COMPANY);
        }
        Company company = Company.of(registration.getCompanyName(), registration.getBusinessType());
        return companyRepository.save(company);
    }

    private void updateMemberWithCompany(Long memberId, Company company) {
        memberRepository.findByIdAndIsDeletedFalse(memberId).ifPresent(member -> {
            member.approve();
            member.updateCompany(company);
            memberRepository.save(member);
        });
    }

    private void saveRegistration(CompanyRegistration registration) {
        companyRegistrationRepository.save(registration);
    }

    private void sendApprovalEmail(String email, String name) {
        sendApproveEmailService.send(email, name);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AivleException(NO_SEARCH_MEMBER));
    }

    private void setMemberDormant(Member member) {
        member.dormant();
        memberRepository.save(member);
        sendDormantEmailService.send(member.getEmail(), member.getName());
    }

    private void deleteMember(Member member) {
        member.delete();
        memberRepository.save(member);
    }

    private CompanyRegistration findLatestRegistrationByMemberId(Long id) {
        return companyRegistrationRepository.findFirstByMemberIdOrderByModifiedAtDesc(id)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
    }
}