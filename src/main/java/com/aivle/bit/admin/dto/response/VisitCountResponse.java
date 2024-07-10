package com.aivle.bit.admin.dto.response;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class VisitCountResponse {

    private LocalDate visitDate;
    private Long visitCount;

    private VisitCountResponse(LocalDate visitDate, Long visitCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
    }

    public static VisitCountResponse of(LocalDate visitDate, Long visitCount) {
        return new VisitCountResponse(visitDate, visitCount);
    }
}
