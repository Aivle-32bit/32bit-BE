package com.aivle.bit.admin.dto.response;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String address;
    private final MemberState state;
    private final String companyName;

    private MemberResponse(Long id, String email, String name, String address, MemberState state, Company company) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.state = state;
        this.companyName = (company != null) ? company.getName() : "정보 없음";
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getAddress(),
            member.getState(), member.getCompany());
    }
}
