package com.aivle.bit.member.domain;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_MEMBER_STATE;

import com.aivle.bit.global.exception.AivleException;
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

    public static MemberState of(int code) {
        for (MemberState state : values()) {
            if (state.code == code) {
                return state;
            }
        }
        throw new AivleException(INVALID_MEMBER_STATE);
    }
}
