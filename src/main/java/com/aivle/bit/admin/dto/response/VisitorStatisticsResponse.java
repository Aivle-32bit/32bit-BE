package com.aivle.bit.admin.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class VisitorStatisticsResponse {

    private final List<VisitCountResponse> visitCountResponses;

    private VisitorStatisticsResponse(List<VisitCountResponse> visitCountResponses) {
        this.visitCountResponses = visitCountResponses;
    }

    public static VisitorStatisticsResponse of(List<VisitCountResponse> visitCountResponses) {
        return new VisitorStatisticsResponse(visitCountResponses);
    }

}
