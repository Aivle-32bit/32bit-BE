package com.aivle.bit.company.controller;

import com.aivle.bit.company.dto.response.FinancialSummaryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class FinancialSummaryController {

    private final FinancialSummaryService financialStatementService;

    @GetMapping("/{id}/financial-summary")
    public List<FinancialSummaryResponse> getFinancialStatement(@PathVariable Long id) {
        return financialStatementService.getFinancialStatement(id);
    }
}
