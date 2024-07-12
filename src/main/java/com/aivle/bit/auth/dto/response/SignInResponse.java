package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.MemberState;

public record SignInResponse(
    TokenResponse tokenResponse,
    Long memberId,
    String memberName,
    MemberState state,
    boolean isAdmin
) {

    public static SignInResponse of(TokenResponse tokenResponse, Long memberId, String memberName, MemberState state,
                                    boolean isAdmin) {
        return new SignInResponse(tokenResponse, memberId, memberName, state, isAdmin);
    }
}