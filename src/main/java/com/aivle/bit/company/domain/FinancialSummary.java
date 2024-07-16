package com.aivle.bit.company.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    @Column(nullable = false)
    private int salesAmount;

    @Comment("당기순이익")
    @Column(nullable = false)
    private int netIncome;

    @Comment("부채총액")
    @Column(nullable = false)
    private int totalLiabilities;

    @Comment("자산총액")
    @Column(nullable = false)
    private int totalAssets;

    private FinancialSummary(int year, Company company, double DEBT, double ATR, double AGR, double ROA, double PPE,
                             int salesAmount, int netIncome, int totalLiabilities, int totalAssets) {
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
    }

    public static FinancialSummary of(int year, Company company, double DEBT, double ATR, double AGR, double ROA,
                                      double PPE,
                                      int salesAmount, int netIncome, int totalLiabilities,
                                      int totalAssets) {
        return new FinancialSummary(year, company, DEBT, ATR, AGR, ROA, PPE, salesAmount, netIncome, totalLiabilities,
            totalAssets);
    }
}
