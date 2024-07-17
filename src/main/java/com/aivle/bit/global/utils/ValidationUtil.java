package com.aivle.bit.global.utils;

import static com.aivle.bit.global.exception.ErrorCode.CONTENT_REQUIRED;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.aivle.bit.global.exception.ErrorCode.TITLE_REQUIRED;

import com.aivle.bit.global.exception.AivleException;

public class ValidationUtil {

    public static void validateTitleAndContent(String title, String content) {
        if ((title == null || title.isBlank()) && (content == null || content.isBlank())) {
            throw new AivleException(INVALID_INPUT_VALUE);
        }
        if (title == null || title.isBlank()) {
            throw new AivleException(TITLE_REQUIRED);
        }
        if (content == null || content.isBlank()) {
            throw new AivleException(CONTENT_REQUIRED);
        }
    }
}