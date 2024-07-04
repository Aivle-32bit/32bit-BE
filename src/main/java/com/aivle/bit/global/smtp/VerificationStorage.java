package com.aivle.bit.global.smtp;

public interface VerificationStorage {

    void storeVerificationCode(String email, String code);

    void verifyCodeAndRetrieveEmail(String email, String code);

    void isEmailVerified(String email);
}
