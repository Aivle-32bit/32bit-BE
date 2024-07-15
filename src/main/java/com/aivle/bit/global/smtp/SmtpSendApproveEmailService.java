package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.service.SendApproveEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpSendApproveEmailService implements SendApproveEmailService {

    private final SmtpEmailSender smtpEmailSender;

    @Override
    public void send(String email, String name) {
        Mail mail = Mail.approve(email, name);
        smtpEmailSender.send(mail);
        log.info("[SMTP] Approval Sent to {}", email);
    }
}
