package com.aivle.bit.member.controller;

import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.PasswordForm;
import com.aivle.bit.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/my/password")
    @ResponseStatus(HttpStatus.OK)
    public void passwordEdit(@RequestBody @Valid PasswordForm form, @JwtLogin Member currentMember) {

        memberService.updatePassword(currentMember, form);
    }
}