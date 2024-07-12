package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.MemberState;

public record SignInResult(
    String refreshToken,
    Long memberId,
    String memberName,
    MemberState state,
    boolean isAdmin
) {

    public static SignInResult from(SignInResponse signInResponse) {
        return new SignInResult(signInResponse.tokenResponse().refreshToken(), signInResponse.memberId(),
            signInResponse.memberName(), signInResponse.state(),
            signInResponse.isAdmin());
    }
}