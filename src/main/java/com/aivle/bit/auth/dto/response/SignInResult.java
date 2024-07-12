package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.MemberState;

public record SignInResult(
    String refreshToken,
    MemberState state,
    boolean isAdmin
) {

    public static SignInResult from(SignInResponse signInResponse) {
        return new SignInResult(signInResponse.tokenResponse().refreshToken(), signInResponse.state(),
            signInResponse.isAdmin());
    }
}