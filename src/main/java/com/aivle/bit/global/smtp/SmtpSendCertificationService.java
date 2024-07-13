package com.aivle.bit.global.smtp;

import com.aivle.bit.member.service.SendCertificationEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpSendCertificationService implements SendCertificationEmailService {

    private final SmtpEmailSender smtpEmailSender;

    @Override
    public void send(String email, String code) {
        Mail mail = Mail.verification(email, code);
        smtpEmailSender.send(mail);
        log.info("[SMTP] Verification Sent to {}", email);
    }
}
