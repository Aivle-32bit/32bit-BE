package com.aivle.bit.company.domain;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;

public enum Metrics {
    DEBT, ATR, AGR, ROA, PPE;

    public static Metrics fromString(String text) {
        System.out.println("text = " + text);
        for (Metrics b : Metrics.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new AivleException(ErrorCode.INVALID_METRICS);
    }
}
