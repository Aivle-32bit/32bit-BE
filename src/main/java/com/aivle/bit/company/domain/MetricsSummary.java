package com.aivle.bit.company.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "metrics_summary",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"year", "company_id", "metrics"})
    }
)
public class MetricsSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("AGR, ROA, PPE, DEBT, ATR")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Metrics metrics;

    @Column(nullable = false)
    private String summary;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Company company;

    @Column(nullable = false)
    private int year;

    private MetricsSummary(Metrics metrics, String summary, Company company, int year) {
        this.metrics = metrics;
        this.summary = summary;
        this.company = company;
        this.year = year;
    }

    public static MetricsSummary of(Metrics metrics, String summary, Company company, int year) {
        return new MetricsSummary(metrics, summary, company, year);
    }
}
