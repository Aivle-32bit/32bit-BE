package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendDormantEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SesSendDormantEmailService implements SendDormantEmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public void send(String email, String name) {
        Mail mail = Mail.dormant(email, name);
        amazonSimpleEmailService.sendEmail(mail.toSes());
        log.info("[SES] Dormant Sent to {}", email);
    }
}
