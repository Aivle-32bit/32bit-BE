package com.aivle.bit.company.dto.response;

import com.aivle.bit.company.domain.CompanyInfo;
import lombok.Getter;

@Getter
public class CompanyInfoResponse {

    private final int year;
    private final int numEmployees;
    private final int experience;
    private final int numHires;
    private final int numResignations;

    private CompanyInfoResponse(int year, int numEmployees, int experience, int numHires, int numResignations) {
        this.year = year;
        this.numEmployees = numEmployees;
        this.experience = experience;
        this.numHires = numHires;
        this.numResignations = numResignations;
    }

    public static CompanyInfoResponse from(CompanyInfo companyInfo) {
        return new CompanyInfoResponse(companyInfo.getYear(), companyInfo.getNumEmployees(),
            companyInfo.getExperience(),
            companyInfo.getNumHires(), companyInfo.getNumResignations());
    }
}
