package com.aivle.bit.auth.jwt;

import static com.aivle.bit.global.exception.ErrorCode.EXPIRED_TOKEN;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_JWT_TOKEN;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_TOKEN_EXTRACTOR;
import static com.aivle.bit.global.exception.ErrorCode.PAYLOAD_EMAIL_MISSING;

import com.aivle.bit.global.exception.AivleException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String EMAIL_KEY = "email";

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;
    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;
    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    public String generateAccessToken(final String email) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);
        final SecretKey secretKey = new SecretKeySpec(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .claim(EMAIL_KEY, email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    public String extractEmailFromAccessToken(final String token) {
        validateAccessToken(token);
        final Jws<Claims> claimsJws = getAccessTokenParser().parseClaimsJws(token);
        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);
        if (extractedEmail == null) {
            throw new AivleException(PAYLOAD_EMAIL_MISSING);
        }
        return extractedEmail;
    }

    private JwtParser getAccessTokenParser() {
        return Jwts.parserBuilder()
            .setSigningKey(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    private void validateAccessToken(final String token) {
        try {
            getAccessTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new AivleException(INVALID_TOKEN_EXTRACTOR);
        } catch (ExpiredJwtException e) {
            throw new AivleException(EXPIRED_TOKEN);
        }
    }

    public String generateRefreshToken(final String email) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpirationInMs);
        final SecretKey secretKey = new SecretKeySpec(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .claim(EMAIL_KEY, email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    public String extractEmailFromRefreshToken(final String token) {
        validateRefreshToken(token);
        final Jws<Claims> claimsJws = getRefreshTokenParser().parseClaimsJws(token);
        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);
        if (extractedEmail == null) {
            throw new AivleException(PAYLOAD_EMAIL_MISSING);
        }
        return extractedEmail;
    }

    private JwtParser getRefreshTokenParser() {
        return Jwts.parserBuilder()
            .setSigningKey(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    private void validateRefreshToken(final String token) {
        try {
            getRefreshTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new AivleException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AivleException(EXPIRED_TOKEN);
        }
    }

    public Duration getRefreshTokenExpiryDurationFromNow() {
        return Duration.ofMillis(jwtRefreshTokenExpirationInMs);
    }
}