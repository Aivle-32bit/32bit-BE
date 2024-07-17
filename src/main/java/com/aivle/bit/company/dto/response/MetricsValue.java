package com.aivle.bit.company.dto.response;

import lombok.Getter;

@Getter
public class MetricsValue {

    private final String year;
    private final String metric;
    private final double value;

    private MetricsValue(String year, String metric, double value) {
        this.year = year;
        this.metric = metric;
        this.value = value;
    }

    public static MetricsValue of(String year, String metric, double value) {
        return new MetricsValue(year, metric, value);
    }
}
