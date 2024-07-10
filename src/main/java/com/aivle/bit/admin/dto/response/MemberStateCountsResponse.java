package com.aivle.bit.admin.dto.response;

import lombok.Getter;

@Getter
public class MemberStateCountsResponse {

    private final long active;
    private final long dormant;
    private final long rejected;

    private MemberStateCountsResponse(long active, long dormant, long rejected) {
        this.active = active;
        this.dormant = dormant;
        this.rejected = rejected;
    }

    public static MemberStateCountsResponse of(long active, long dormant, long rejected) {
        return new MemberStateCountsResponse(active, dormant, rejected);
    }
}