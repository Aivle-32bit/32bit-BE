package com.aivle.bit.company.service;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_METRICS;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY_REGISTRATION;
import static java.lang.Math.abs;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.FinancialSummary;
import com.aivle.bit.company.domain.Metrics;
import com.aivle.bit.company.domain.MetricsSummary;
import com.aivle.bit.company.dto.response.CompanyReportResponse;
import com.aivle.bit.company.dto.response.MetricsResponse;
import com.aivle.bit.company.dto.response.MetricsValue;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.company.repository.FinancialSummaryRepository;
import com.aivle.bit.company.repository.MetricsSummaryRepository;
import com.aivle.bit.global.exception.AivleException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyReportService {

    private final CompanyRepository companyRepository;
    private final FinancialSummaryRepository financialSummaryRepository;
    private final MetricsSummaryRepository metricsSummaryRepository;

    public CompanyReportResponse getCompanyReport(Long companyId) {
        Company company = getCompanyById(companyId);
        int currentYear = LocalDate.now().getYear();
        FinancialSummary currentYearSummary = getFinancialSummary(companyId, currentYear);
        FinancialSummary previousYearSummary = getFinancialSummary(companyId, currentYear - 1);

        return buildCompanyReportResponse(company, currentYearSummary, previousYearSummary);
    }

    public MetricsResponse getMetricsReport(Long companyId, String metricsString) {
        Metrics metrics = Metrics.fromString(metricsString);
        MetricsSummary metricsSummary = getMetricsSummary(companyId, metrics);
        List<MetricsValue> metricsValues = getMetricsValues(companyId, metrics);

        return MetricsResponse.of(metricsValues, metricsSummary.getSummary());
    }

    private Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY));
    }

    private FinancialSummary getFinancialSummary(Long companyId, int year) {
        return financialSummaryRepository.findByCompanyIdAndYear(companyId, year)
            .orElseThrow(() -> new AivleException(NO_SEARCH_COMPANY_REGISTRATION));
    }

    private MetricsSummary getMetricsSummary(Long companyId, Metrics metrics) {
        return metricsSummaryRepository.findByCompanyIdAndMetrics(companyId, metrics)
            .orElseThrow(() -> new AivleException(INVALID_METRICS));
    }

    private List<MetricsValue> getMetricsValues(Long companyId, Metrics metrics) {
        List<FinancialSummary> financialSummaries = financialSummaryRepository.findByCompanyIdOrderByYearAsc(companyId);

        return financialSummaries.stream()
            .map(summary -> MetricsValue.of(String.valueOf(summary.getYear()), metrics.name(),
                getMetricValue(summary, metrics)))
            .collect(Collectors.toList());
    }

    private double getMetricValue(FinancialSummary summary, Metrics metric) {
        return switch (metric) {
            case DEBT -> summary.getDEBT();
            case ATR -> summary.getATR();
            case AGR -> summary.getAGR();
            case ROA -> summary.getROA();
            case PPE -> summary.getPPE();
        };
    }

    private String calculateStatus(double currentValue, double previousValue) {
        double changePercentage = ((currentValue - previousValue) / abs(previousValue)) * 100;
        if (changePercentage > 5) {
            return "GOOD";
        } else if (changePercentage < -5) {
            return "BAD";
        } else {
            return "NORMAL";
        }
    }

    private CompanyReportResponse buildCompanyReportResponse(Company company, FinancialSummary currentYearSummary,
                                                             FinancialSummary previousYearSummary) {
        return CompanyReportResponse.of(
            company.getName(),
            company.getImageUrl(),
            calculateStatus(currentYearSummary.getSalesAmount(), previousYearSummary.getSalesAmount()),
            calculateStatus(currentYearSummary.getNetIncome(), previousYearSummary.getNetIncome()),
            calculateStatus(currentYearSummary.getTotalAssets(), previousYearSummary.getTotalAssets()),
            calculateStatus(currentYearSummary.getTotalLiabilities(), previousYearSummary.getTotalLiabilities()),
            currentYearSummary.getDEBT(),
            currentYearSummary.getATR(),
            currentYearSummary.getROA(),
            currentYearSummary.getAGR(),
            currentYearSummary.getPPE(),
            previousYearSummary.getDEBT(),
            previousYearSummary.getATR(),
            previousYearSummary.getROA(),
            previousYearSummary.getAGR(),
            previousYearSummary.getPPE()
        );
    }
}