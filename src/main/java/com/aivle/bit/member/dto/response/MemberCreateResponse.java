package com.aivle.bit.member.dto.response;

import com.aivle.bit.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberCreateResponse {

    private final Long id;

    private final String email;

    private final int state;

    private final String name;

    private MemberCreateResponse(final Long id, final String email, final int state, final String name) {
        this.id = id;
        this.email = email;
        this.state = state;
        this.name = name;
    }

    public static MemberCreateResponse from(final Member member) {
        return new MemberCreateResponse(member.getId(), member.getEmail(), member.getState().getCode(),
            member.getName());
    }
}
