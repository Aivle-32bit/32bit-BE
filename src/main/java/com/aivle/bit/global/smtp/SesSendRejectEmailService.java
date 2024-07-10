package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendRejectEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendRejectEmailService implements SendRejectEmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public void send(String email, String name, String reason) {
        Mail mail = Mail.reject(email, name, reason);
        amazonSimpleEmailService.sendEmail(mail.toSes());
        log.info("[SES] Rejection Sent to {}", email);
    }
}
