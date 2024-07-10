package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendApproveEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendApproveEmailService implements SendApproveEmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public void send(String email, String name) {
        Mail mail = Mail.approve(email, name);
        amazonSimpleEmailService.sendEmail(mail.toSes());
        log.info("[SES] Approval Sent to {}", email);
    }
}
