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

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "swot",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"year", "company_id"})
    }
)
public class Swot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int year;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwotCategory category;

    @Column(nullable = false)
    private String description;

    private Swot(int year, Company company, SwotCategory category, String description) {
        this.year = year;
        this.company = company;
        this.category = category;
        this.description = description;
    }

    public static Swot of(int year, Company company, SwotCategory category, String description) {
        return new Swot(year, company, category, description);
    }
}
