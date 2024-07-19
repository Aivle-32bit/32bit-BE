package com.aivle.bit.global.smtp;

import com.aivle.bit.member.service.SendRandomPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpSendRandomPasswordService implements SendRandomPasswordService {

    private final SmtpEmailSender smtpEmailSender;

    @Override
    public void send(String email, String password) {
        Mail mail = Mail.randomPassword(email, password);
        smtpEmailSender.send(mail);
        log.info("[SMTP] Random Password Sent to {}", email);
    }
}
