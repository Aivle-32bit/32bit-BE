package com.aivle.bit.member.controller;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.EmailVerificationRequest;
import com.aivle.bit.member.dto.request.MemberVerificationRequest;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.dto.response.MemberCreateResponse;
import com.aivle.bit.member.service.MemberService;
import com.aivle.bit.member.service.SendVerificationEmailService;
import com.aivle.bit.member.service.VerifyCertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;
    private final SendVerificationEmailService sendVerificationEmailService;
    private final VerifyCertificationService verifyCertificationService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberCreateResponse signup(@RequestBody @Valid final MemberCreateRequest memberCreateRequest) {
        Member member = memberService.signup(memberCreateRequest);

        return MemberCreateResponse.from(member);
    }

    @PostMapping("/send-verification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendVerification(@Valid @RequestBody final MemberVerificationRequest memberVerificationRequest) {
        sendVerificationEmailService.sendVerification(memberVerificationRequest.email());
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@Valid @RequestBody final EmailVerificationRequest emailVerificationRequest) {
        verifyCertificationService.verify(emailVerificationRequest.email(), emailVerificationRequest.code());
    }
}
