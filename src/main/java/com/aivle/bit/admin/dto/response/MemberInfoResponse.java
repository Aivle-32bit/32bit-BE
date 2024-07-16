package com.aivle.bit.auth.dto.response;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import lombok.Getter;

@Getter
public class MemberInfoResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final MemberState state;
    private final String companyName;
    private final Boolean isAdmin;

    private MemberInfoResponse(Long id, String email, String name, MemberState state, String companyName,
                               Boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.state = state;
        this.companyName = companyName;
        this.isAdmin = isAdmin;
    }

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getState(),
            member.getCompany().getName(), member.getIsAdmin());
    }
}
