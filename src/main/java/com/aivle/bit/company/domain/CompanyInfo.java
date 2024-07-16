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
@Table(name = "company_info",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"year", "company_id"})
    }
)
public class CompanyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("년도")
    @Column(nullable = false)
    private int year;

    @Comment("직원 수")
    @Column(nullable = false)
    private int numEmployees;

    @Comment("업력")
    @Column(nullable = false)
    private int experience;

    @Comment("입사자 수")
    @Column(nullable = false)
    private int numHires;

    @Comment("퇴사자 수")
    @Column(nullable = false)
    private int numResignations;

    @Comment("회사 정보")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Company company;

    private CompanyInfo(int year, int numEmployees, int experience, int numHires, int numResignations, Company company) {
        this.year = year;
        this.numEmployees = numEmployees;
        this.experience = experience;
        this.numHires = numHires;
        this.numResignations = numResignations;
        this.company = company;
    }

    public static CompanyInfo of(int year, int numEmployees, int experience, int numHires, int numResignations, Company company) {
        return new CompanyInfo(year, numEmployees, experience, numHires, numResignations, company);
    }
}