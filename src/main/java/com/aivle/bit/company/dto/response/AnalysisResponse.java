package com.aivle.bit.company.dto.response;

import com.aivle.bit.company.domain.FinancialSummary;
import lombok.Getter;

@Getter
public class AnalysisResponse {

    private final Double DEBT;
    private final Double ATR;
    private final Double AGR;
    private final Double ROA;
    private final Double PPE;

    private AnalysisResponse(Double DEBT, Double ATR, Double AGR, Double ROA, Double PPE) {
        this.DEBT = DEBT;
        this.ATR = ATR;
        this.AGR = AGR;
        this.ROA = ROA;
        this.PPE = PPE;
    }

    public static AnalysisResponse from(FinancialSummary financialSummary) {
        return new AnalysisResponse(
            financialSummary.getDEBT(),
            financialSummary.getATR(),
            financialSummary.getAGR(),
            financialSummary.getROA(),
            financialSummary.getPPE());
    }
}
