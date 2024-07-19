package com.aivle.bit.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanySearchResponse {

    private Long id;
    private String name;
    private String businessType;
}
