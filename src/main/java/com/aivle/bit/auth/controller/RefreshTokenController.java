package com.aivle.bit.auth.controller;

import com.aivle.bit.auth.dto.RefreshTokenRequest;
import com.aivle.bit.auth.dto.TokenResponse;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import com.aivle.bit.auth.service.RefreshTokenService;
import com.aivle.bit.member.domain.MemberState;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth/refresh")
@RestController
public class RefreshTokenController {

    @Value("${jwt.access.expiration}")
    private int EXPIRATION_TIME;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void refreshAccessToken(
        @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest,
        HttpServletResponse response
    ) {
        String extractedRefreshToken = refreshTokenRequest.refreshToken();
        String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshTokenRequest.refreshToken());
        Long userId = jwtTokenProvider.extractIdFromRefreshToken(refreshTokenRequest.refreshToken());
        MemberState state = jwtTokenProvider.extractStateFromRefreshToken(refreshTokenRequest.refreshToken());

        refreshTokenService.validateRefreshToken(email, extractedRefreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(email, userId, state);

        TokenResponse.of(newAccessToken, refreshTokenRequest.refreshToken())
            .setAccessToken(response, EXPIRATION_TIME);
    }
}