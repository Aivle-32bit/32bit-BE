package com.aivle.bit.auth.service;

import static com.aivle.bit.global.exception.ErrorCode.EXPIRED_TOKEN;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_JWT_TOKEN;

import com.aivle.bit.auth.repository.TokenRepository;
import com.aivle.bit.global.exception.AivleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository tokenRepository;

    public void validateRefreshToken(String email, String extractedRefreshToken) {
        String refreshTokenFromCash = tokenRepository.getValues(email)
            .orElseThrow(() -> new AivleException(EXPIRED_TOKEN));

        if (!refreshTokenFromCash.equals(extractedRefreshToken)) {
            tokenRepository.deleteValues(email);
            throw new AivleException(INVALID_JWT_TOKEN);
        }
    }
}