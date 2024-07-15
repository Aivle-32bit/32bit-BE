package com.aivle.bit.admin.dto.response;

import lombok.Getter;

@Getter
public class MemberStateCountsResponse {

    private final long unverified;
    private final long verified;
    private final long dormant;

    private MemberStateCountsResponse(long unverified, long verified, long dormant) {
        this.unverified = unverified;
        this.verified = verified;
        this.dormant = dormant;
    }

    public static MemberStateCountsResponse of(long unverified, long verified, long dormant) {
        return new MemberStateCountsResponse(unverified, verified, dormant);
    }
}