package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendDormantEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmtpSendDormantEmailService implements SendDormantEmailService {

    private final SmtpEmailSender smtpEmailSender;

    @Override
    public void send(String email, String name) {
        Mail mail = Mail.dormant(email, name);
        smtpEmailSender.send(mail);
        log.info("[SMTP] Dormant Sent to {}", email);
    }
}
