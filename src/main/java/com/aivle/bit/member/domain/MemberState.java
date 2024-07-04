package com.aivle.bit.member.domain;

import lombok.Getter;

@Getter
public enum MemberState {
    UNVERIFIED(0),
    VERIFIED(1),
    ADMIN_VERIFIED(2),
    USER_DORMANT(3),
    ADMIN_DORMANT(4);

    private final int code;

    MemberState(int code) {
        this.code = code;
    }
}
