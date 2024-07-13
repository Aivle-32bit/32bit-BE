package com.aivle.bit.member.dto.response;

import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MemberFindResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String address;
    private final Boolean isAdmin;
    private final MemberState state;
    private final String companyName;
    private final LocalDate createdAt;
    private final String imageUrl;

    private MemberFindResponse(Long id, String email, String name, String address, Boolean isAdmin, MemberState state,
                               String companyName, LocalDate createdAt, String imageUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.isAdmin = isAdmin;
        this.state = state;
        this.companyName = companyName;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public static MemberFindResponse from(Member member) {
        return new MemberFindResponse(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getAddress(),
            member.getIsAdmin(),
            member.getState(),
            member.getCompany() != null ? member.getCompany().getName() : "소속 없음",
            member.getCreatedAt().toLocalDate(),
            member.getImageUrl() != null ? member.getImageUrl() : ""
        );
    }
}