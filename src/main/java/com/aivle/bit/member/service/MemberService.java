package com.aivle.bit.member.service;

import static com.aivle.bit.global.exception.ErrorCode.EMAIL_DUPLICATION;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_REQUEST;

import com.aivle.bit.company.service.S3Service;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.smtp.VerificationStorage;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.FindPasswordRequest;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.dto.request.PasswordChangeRequest;
import com.aivle.bit.member.dto.request.ProfileUpdateRequest;
import com.aivle.bit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final VerificationStorage verificationStorage;
    private final S3Service s3Service;
    private final SendRandomPasswordService sendRandomPasswordService;

    @Transactional(readOnly = true)
    public void checkEmailDuplicated(String email) {
        if (memberRepository.existsByEmailAndIsDeletedFalse(email)) {
            throw new AivleException(EMAIL_DUPLICATION);
        }
    }

    @Transactional
    public Member signup(MemberCreateRequest memberCreateRequest) {
        checkEmailDuplicated(memberCreateRequest.email());

        verificationStorage.isEmailVerified(memberCreateRequest.email());

        Member member = Member.of(memberCreateRequest.email(), memberCreateRequest.password(),
            memberCreateRequest.name(), memberCreateRequest.address());

        return memberRepository.save(member);
    }

    @Transactional
    public void updatePassword(Member member, PasswordChangeRequest request) {
        if (!request.getNewPassword().equals(request.getRetype())) {
            throw new AivleException(INVALID_REQUEST);
        }
        member.changePassword(request.getNewPassword(), request.getCurrentPassword());
        memberRepository.save(member);
    }

    public void uploadProfilePicture(Member member, MultipartFile file) {
        String imageUrl = s3Service.uploadImage(file, "profile/images");
        member.updateImageUrl(imageUrl);
        memberRepository.save(member);
    }

    public void deleteProfilePicture(Member member) {
        s3Service.deleteImage(member.getImageUrl());
        member.deleteImageUrl();
        memberRepository.save(member);
    }

    public void updateMemberInfo(Member member, ProfileUpdateRequest request) {
        member.updateInfo(request.name(), request.address());
        memberRepository.save(member);
    }

    public void deleteMember(Member member) {
        member.delete();
        memberRepository.save(member);
    }

    public void findPassword(FindPasswordRequest request) {
        Member member = memberRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AivleException(INVALID_REQUEST));

        String newPassword = member.resetPassword();
        sendRandomPasswordService.send(member.getEmail(), newPassword);
        memberRepository.save(member);
    }
}
