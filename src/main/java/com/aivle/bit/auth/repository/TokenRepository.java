package com.aivle.bit.auth.repository;

import java.time.Duration;
import java.util.Optional;

public interface TokenRepository {

    void setValues(String email, String refreshToken, Duration expiryDuration);

    Optional<String> getValues(String email);

    void deleteValues(String email);

}