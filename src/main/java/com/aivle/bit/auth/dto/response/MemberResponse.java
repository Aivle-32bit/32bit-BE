package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String name;
    private final Long companyId;
    private final MemberState state;
    private final Boolean isAdmin;

    private MemberResponse(Long id, String name, Long companyId, MemberState state, Boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.state = state;
        this.isAdmin = isAdmin;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getCompany() != null ? member.getCompany().getId() : null,
            member.getState(),
            member.getIsAdmin()
        );
    }
}
