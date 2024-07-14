package com.aivle.bit.auth.dto.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }

    public static void expireAccessToken(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

    public void setAccessToken(HttpServletResponse response, int expirationTime) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, accessToken)
            .httpOnly(true)
            .secure(false) // 개발 환경에서는 false
            .sameSite("None") // 개발 환경에서는 None
            .path("/")
            .maxAge(expirationTime)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }
}