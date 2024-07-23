package com.aivle.bit.company.dto.response;

import com.aivle.bit.company.domain.FinancialSummary;
import lombok.Getter;

@Getter
public class FinancialSummaryResponse {

    private final int year;
    private final Double DEBT;
    private final Double ATR;
    private final Double AGR;
    private final Double ROA;
    private final Double PPE;
    private final Integer salesAmount;
    private final Integer netIncome;
    private final Integer totalLiabilities;
    private final Integer totalAssets;
    private final Integer operatingIncome;
    private final Integer capitalStock;
    private final Integer cashFlowFromOperatingActivities;
    private final Double ROE;

    private FinancialSummaryResponse(int year, Double DEBT, Double ATR, Double AGR, Double ROA, Double PPE,
                                     Integer salesAmount,
                                     Integer netIncome, Integer totalLiabilities, Integer totalAssets,
                                     Integer operatingIncome, Integer capitalStock,
                                     Integer cashFlowFromOperatingActivities, Double ROE) {
        this.year = year;
        this.DEBT = DEBT;
        this.ATR = ATR;
        this.AGR = AGR;
        this.ROA = ROA;
        this.PPE = PPE;
        this.salesAmount = salesAmount;
        this.netIncome = netIncome;
        this.totalLiabilities = totalLiabilities;
        this.totalAssets = totalAssets;
        this.operatingIncome = operatingIncome;
        this.capitalStock = capitalStock;
        this.cashFlowFromOperatingActivities = cashFlowFromOperatingActivities;
        this.ROE = ROE;
    }

    public static FinancialSummaryResponse from(FinancialSummary financialSummary) {
        return new FinancialSummaryResponse(
            financialSummary.getYear(),
            financialSummary.getDEBT(),
            financialSummary.getATR(),
            financialSummary.getAGR(),
            financialSummary.getROA(),
            financialSummary.getPPE(),
            financialSummary.getSalesAmount(),
            financialSummary.getNetIncome(),
            financialSummary.getTotalLiabilities(),
            financialSummary.getTotalAssets(),
            financialSummary.getOperatingIncome(),
            financialSummary.getCapitalStock(),
            financialSummary.getCashFlowFromOperatingActivities(),
            financialSummary.getROE()
        );
    }
}
