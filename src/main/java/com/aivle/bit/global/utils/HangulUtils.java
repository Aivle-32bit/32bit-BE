package com.aivle.bit.global.utils;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;

public class HangulUtils {

    public static final String HANGUL_CONSONANTS = "[ㄱ-ㅎ]";

    private static final char HANGUL_BASE = '가';
    private static final char HANGUL_END = '힣';
    private static final char[] INITIAL_CONSONANTS =
        {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    private HangulUtils() {
        throw new AivleException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    public static char getInitialConsonant(char c) {
        if (c >= HANGUL_BASE && c <= HANGUL_END) {
            int index = (c - HANGUL_BASE) / 588;
            return INITIAL_CONSONANTS[index];
        }
        return c;
    }

    public static boolean containsInitialConsonant(String text, char consonant) {
        for (char c : text.toCharArray()) {
            if (getInitialConsonant(c) == consonant) {
                return true;
            }
        }
        return false;
    }
}
