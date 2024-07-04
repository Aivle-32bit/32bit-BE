package com.aivle.bit.global.smtp;

import com.aivle.bit.member.service.SendCertificationEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendCertificationService implements SendCertificationEmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Async
    @Override
    public void send(String email, String code) {
        Mail mail = Mail.verification(email, code);
        amazonSimpleEmailService.sendEmail(mail.toSes());
        log.info("[SES] Verification Sent to {}", email);
    }
}
