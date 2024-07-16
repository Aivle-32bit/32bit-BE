package com.aivle.bit.member.controller;

import com.aivle.bit.auth.dto.response.MemberResponse;
import com.aivle.bit.auth.jwt.AllowUnverifiedUser;
import com.aivle.bit.company.service.S3Service;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.PasswordChangeRequest;
import com.aivle.bit.member.dto.request.ProfileUpdateRequest;
import com.aivle.bit.member.dto.response.MemberFindResponse;
import com.aivle.bit.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse findMember(@AllowUnverifiedUser Member member) {
        return MemberResponse.from(member);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public MemberFindResponse myInfo(@AllowUnverifiedUser Member member) {
        return MemberFindResponse.from(member, s3Service::generatePresignedUrl);
    }

    @PostMapping("/my/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid PasswordChangeRequest request, @AllowUnverifiedUser Member member) {
        memberService.updatePassword(member, request);
    }

    @PutMapping("/my/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public void uploadProfilePicture(@RequestParam @NotNull(message = "파일을 업로드 해주세요.") MultipartFile file,
                                     @AllowUnverifiedUser Member member) {
        memberService.uploadProfilePicture(member, file);
    }

    @DeleteMapping("/my/profile-picture")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfilePicture(@AllowUnverifiedUser Member member) {
        memberService.deleteProfilePicture(member);
    }

    @PutMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberInfo(@RequestBody @Valid ProfileUpdateRequest request, @AllowUnverifiedUser Member member) {
        memberService.updateMemberInfo(member, request);
    }

    @DeleteMapping("/my")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@AllowUnverifiedUser Member member) {
        memberService.deleteMember(member);
    }
}
