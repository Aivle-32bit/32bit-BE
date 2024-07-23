package com.aivle.bit.company.controller;


import com.aivle.bit.company.domain.FinancialSummary;
import com.aivle.bit.company.dto.response.AnalysisResponse;
import com.aivle.bit.company.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/{id}/analysis")
    public AnalysisResponse getAnalysis(@PathVariable Long id) {
        FinancialSummary analysis = analysisService.getAnalysis(id);
        return AnalysisResponse.from(analysis);
    }

}
