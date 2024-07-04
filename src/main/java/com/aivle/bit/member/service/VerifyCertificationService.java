package com.aivle.bit.member.service;

import com.aivle.bit.global.smtp.VerificationStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyCertificationService {

    private final VerificationStorage verificationStorage;

    public void verify(String email, String code) {
        verificationStorage.verifyCodeAndRetrieveEmail(email, code);
    }
}
