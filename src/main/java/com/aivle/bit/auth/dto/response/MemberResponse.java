package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String name;
    private final MemberState state;
    private final Boolean isAdmin;

    private MemberResponse(Long id, String name, MemberState state, Boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.isAdmin = isAdmin;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getState(), member.getIsAdmin());
    }
}
