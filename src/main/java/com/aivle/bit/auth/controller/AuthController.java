package com.aivle.bit.auth.controller;

import com.aivle.bit.auth.dto.request.FindEmailRequest;
import com.aivle.bit.auth.dto.request.FindPasswordRequest;
import com.aivle.bit.auth.dto.request.SignInRequest;
import com.aivle.bit.auth.dto.response.TokenResponse;
import com.aivle.bit.auth.service.SignInService;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.EmailVerificationRequest;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.dto.request.MemberVerificationRequest;
import com.aivle.bit.member.dto.response.MemberCreateResponse;
import com.aivle.bit.member.service.MemberService;
import com.aivle.bit.member.service.SendVerificationEmailService;
import com.aivle.bit.member.service.VerifyCertificationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.access.expiration}")
    private int EXPIRATION_TIME;

    private final SignInService signInService;
    private final MemberService memberService;
    private final SendVerificationEmailService sendVerificationEmailService;
    private final VerifyCertificationService verifyCertificationService;

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public String signIn(@RequestBody @Valid SignInRequest signInRequest,
                         HttpServletResponse response) {
        TokenResponse tokenResponse = signInService.signInUser(signInRequest);
        tokenResponse.setAccessToken(response, EXPIRATION_TIME);

        return tokenResponse.refreshToken();
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberCreateResponse signup(@RequestBody @Valid final MemberCreateRequest memberCreateRequest) {
        Member member = memberService.signup(memberCreateRequest);

        return MemberCreateResponse.from(member);
    }

    @PostMapping("/send-verification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendVerification(@Valid @RequestBody final MemberVerificationRequest memberVerificationRequest) {
        memberService.checkEmailDuplicated(memberVerificationRequest.email());
        sendVerificationEmailService.sendVerification(memberVerificationRequest.email());
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@Valid @RequestBody final EmailVerificationRequest emailVerificationRequest) {
        verifyCertificationService.verify(emailVerificationRequest.email(), emailVerificationRequest.code());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        TokenResponse.expireAccessToken(response);
    }

    @PostMapping("/find-password")
    @ResponseStatus(HttpStatus.OK)
    public void findPassword(@RequestBody @Valid FindPasswordRequest request) {
        memberService.findPassword(request);
    }

    @GetMapping("/find-email")
    @ResponseStatus(HttpStatus.OK)
    public String findEmail(@ModelAttribute @Valid FindEmailRequest request) {
        return memberService.findEmail(request);
    }
}