package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendRejectEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpSendRejectEmailService implements SendRejectEmailService {

    private final SmtpEmailSender smtpEmailSender;

    @Override
    public void send(String email, String name, String reason) {
        Mail mail = Mail.reject(email, name, reason);
        smtpEmailSender.send(mail);
        log.info("[SMTP] Rejection Sent to {}", email);
    }
}
