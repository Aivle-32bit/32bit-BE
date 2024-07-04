package com.aivle.bit.member.service;

import static com.aivle.bit.global.exception.ErrorCode.EMAIL_DUPLICATION;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.smtp.VerificationStorage;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final VerificationStorage verificationStorage;

    @Transactional
    public Member signup(MemberCreateRequest memberCreateRequest) {
        verificationStorage.isEmailVerified(memberCreateRequest.email());

        memberRepository.findByEmail(memberCreateRequest.email())
            .ifPresent(member -> {
                throw new AivleException(EMAIL_DUPLICATION);
            });

        Member member = Member.of(memberCreateRequest.email(), memberCreateRequest.password(),
            memberCreateRequest.name(), memberCreateRequest.address());

        return memberRepository.save(member);
    }
}
