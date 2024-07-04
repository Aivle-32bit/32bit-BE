package com.aivle.bit.member.service;

import com.aivle.bit.global.smtp.VerificationStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendVerificationEmailService {

    private final SendCertificationEmailService sendCertificationEmailService;
    private final VerificationStorage verificationStorage;

    public void sendVerification(String email) {
        String code = RandomCodeGenerator.generate();
        sendCertificationEmailService.send(email, code);
        verificationStorage.storeVerificationCode(email, code);
    }
}
