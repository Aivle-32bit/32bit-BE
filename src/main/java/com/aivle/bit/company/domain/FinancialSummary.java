package com.aivle.bit.company.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "financial_summary",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"year", "company_id"})
    }
)
public class FinancialSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("년도")
    @Column(nullable = false)
    private int year;

    @Comment("회사 정보")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Company company;

    @Comment("부채비율")
    @Column(nullable = false)
    private double DEBT;

    @Comment("자기자본비율")
    @Column(nullable = false)
    private double ATR;

    @Comment("자산증가율")
    @Column(nullable = false)
    private double AGR;

    @Comment("ROA")
    @Column(nullable = false)
    private double ROA;

    @Comment("유동자산")
    @Column(nullable = false)
    private double PPE;

    @Comment("매출액")
    @Column(nullable = true)
    private Integer salesAmount;

    @Comment("당기순이익")
    @Column(nullable = true)
    private Integer netIncome;

    @Comment("부채총액")
    @Column(nullable = false)
    private Integer totalLiabilities;

    @Comment("자산총액")
    @Column(nullable = false)
    private Integer totalAssets;

    @Comment("영업이익")
    @Column(nullable = true)
    private Integer operatingIncome;

    @Comment("자본총계")
    @Column(nullable = true)
    private Integer capitalStock;

    @Comment("영업 활동으로 인한 현금흐름")
    @Column(nullable = true)
    private Integer cashFlowFromOperatingActivities;

    @Comment("자기자본이익률")
    @Column(nullable = true)
    private Double ROE;

    private FinancialSummary(int year, Company company, double DEBT, double ATR, double AGR, double ROA, double PPE,
                             Integer salesAmount, Integer netIncome, Integer totalLiabilities, Integer totalAssets, Integer operatingIncome,
                             Integer capitalStock, Integer cashFlowFromOperatingActivities, Double ROE) {
        this.year = year;
        this.company = company;
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

    public static FinancialSummary of(int year, Company company, double DEBT, double ATR, double AGR, double ROA, double PPE,
                                      Integer salesAmount, Integer netIncome, Integer totalLiabilities, Integer totalAssets, Integer operatingIncome,
                                      Integer capitalStock, Integer cashFlowFromOperatingActivities, Double ROE) {
        return new FinancialSummary(year, company, DEBT, ATR, AGR, ROA, PPE, salesAmount, netIncome, totalLiabilities, totalAssets,
            operatingIncome, capitalStock, cashFlowFromOperatingActivities, ROE);
    }

    public void UpdateData(Double debt, Double atr, Double agr, Double roa, Double ppe,
                           Integer salesAmount, Integer netIncome, Integer totalLiabilities, Integer totalAssets,
                           Integer operatingIncome, Integer capitalStock, Integer cashFlowFromOperatingActivities,
                           Double roe) {
        this.DEBT = debt;
        this.ATR = atr;
        this.AGR = agr;
        this.ROA = roa;
        this.PPE = ppe;
        this.salesAmount = salesAmount;
        this.netIncome = netIncome;
        this.totalLiabilities = totalLiabilities;
        this.totalAssets = totalAssets;
        this.operatingIncome = operatingIncome;
        this.capitalStock = capitalStock;
        this.cashFlowFromOperatingActivities = cashFlowFromOperatingActivities;
        this.ROE = roe;
    }
}