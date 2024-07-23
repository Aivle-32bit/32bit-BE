package com.aivle.bit.company.dto.response;

import java.util.function.Function;
import lombok.Getter;

@Getter
public class CompanyReportResponse {

    private final String companyName;
    private final String companyImage;
    private final String salesAmountStatus;
    private final String netIncomeStatus;
    private final String totalAssetStatus;
    private final String totalLiabilityStatus;
    private final double DEBT;
    private final double ATR;
    private final double ROA;
    private final double AGR;
    private final double PPE;
    private final double previousDEBT;
    private final double previousATR;
    private final double previousROA;
    private final double previousAGR;
    private final double previousPPE;

    // private 생성자
    private CompanyReportResponse(final String companyName, final String companyImage,
                                  final String salesAmountStatus, final String netIncomeStatus,
                                  final String totalAssetStatus, final String totalLiabilityStatus,
                                  final double DEBT, final double ATR, final double ROA, final double AGR,
                                  final double PPE, final double previousDEBT, final double previousATR,
                                  final double previousROA, final double previousAGR, final double previousPPE) {
        this.companyName = companyName;
        this.companyImage = companyImage;
        this.salesAmountStatus = salesAmountStatus;
        this.netIncomeStatus = netIncomeStatus;
        this.totalAssetStatus = totalAssetStatus;
        this.totalLiabilityStatus = totalLiabilityStatus;
        this.DEBT = DEBT;
        this.ATR = ATR;
        this.ROA = ROA;
        this.AGR = AGR;
        this.PPE = PPE;
        this.previousDEBT = previousDEBT;
        this.previousATR = previousATR;
        this.previousROA = previousROA;
        this.previousAGR = previousAGR;
        this.previousPPE = previousPPE;
    }

    public static CompanyReportResponse of(final String companyName, final String companyImage,
                                           final String salesAmountStatus, final String netIncomeStatus,
                                           final String totalAssetStatus, final String totalLiabilityStatus,
                                           final double DEBT, final double ATR, final double ROA, final double AGR,
                                           final double PPE, final double previousDEBT, final double previousATR,
                                           final double previousROA, final double previousAGR,
                                           final double previousPPE, Function<String, String> urlGenerator) {
        return new CompanyReportResponse(companyName, urlGenerator.apply(companyImage), salesAmountStatus, netIncomeStatus,
            totalAssetStatus, totalLiabilityStatus, DEBT, ATR, ROA, AGR, PPE, previousDEBT, previousATR,
            previousROA, previousAGR, previousPPE);
    }
}