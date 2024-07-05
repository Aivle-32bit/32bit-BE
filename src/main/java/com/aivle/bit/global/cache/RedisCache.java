package com.aivle.bit.global.cache;

import static com.aivle.bit.global.exception.ErrorCode.EMAIL_NOT_VERIFIED;
import static com.aivle.bit.global.exception.ErrorCode.EXPIRED_VERIFICATION_CODE;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_VERIFICATION_CODE;

import com.aivle.bit.auth.repository.TokenRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.smtp.VerificationStorage;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RedisCache implements VerificationStorage, TokenRepository {

    private static final long VERIFICATION_EMAIL_EXPIRE = 10 * 60 * 1000L; // 10분
    private static final String VERIFIED_STATUS = "verified"; // 인증 완료 상태 표시

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void storeVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, Duration.ofMillis(VERIFICATION_EMAIL_EXPIRE));
    }

    @Override
    public void verifyCodeAndRetrieveEmail(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(email);
        compareCodes(storedCode, inputCode);
        markEmailAsVerified(email);
    }

    @Override
    public void isEmailVerified(String email) {
        String status = redisTemplate.opsForValue().get(email + ":verified");
        if (status == null) {
            throw new AivleException(EMAIL_NOT_VERIFIED);
        }
    }

    private void markEmailAsVerified(String email) {
        redisTemplate.opsForValue()
            .set(email + ":verified", VERIFIED_STATUS, Duration.ofMillis(VERIFICATION_EMAIL_EXPIRE));
    }

    private void compareCodes(String storedCode, String inputCode) {
        if (storedCode == null) {
            throw new AivleException(EXPIRED_VERIFICATION_CODE);
        }
        if (!storedCode.equals(inputCode)) {
            throw new AivleException(INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return Optional.ofNullable(values.get(key));
    }

    @Override
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
