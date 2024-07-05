package com.aivle.bit.member.domain;

import lombok.Getter;

@Getter
public enum MemberState {
    UNVERIFIED(0),
    VERIFIED(1),
    USER_DORMANT(2);

    private final int code;

    MemberState(int code) {
        this.code = code;
    }
}
