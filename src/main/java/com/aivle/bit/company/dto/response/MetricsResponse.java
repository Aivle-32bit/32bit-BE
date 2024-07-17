package com.aivle.bit.company.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class MetricsResponse {

    private final List<MetricsValue> metricsValues;
    private final String summary;

    private MetricsResponse(List<MetricsValue> metricsValues, String summary) {
        this.metricsValues = metricsValues;
        this.summary = summary;
    }

    public static MetricsResponse of(List<MetricsValue> metricsValues, String summary) {
        return new MetricsResponse(metricsValues, summary);
    }
}
