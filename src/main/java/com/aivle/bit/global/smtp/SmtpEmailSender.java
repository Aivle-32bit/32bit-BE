package com.aivle.bit.global.smtp;

import static com.aivle.bit.global.exception.ErrorCode.SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.aivle.bit.global.exception.AivleException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailSender {

    private static final String FROM_EMAIL = "no-reply@aivle.site";

    private final JavaMailSender javaMailSender;

    @Async
    public void send(Mail mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, UTF_8.toString());
        setContent(messageHelper, mail);
        javaMailSender.send(message);
    }

    private void setContent(MimeMessageHelper messageHelper, Mail mail) {
        try {
            messageHelper.setTo(mail.getTo());
            messageHelper.setFrom(FROM_EMAIL);
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getContent(), true);
        } catch (MessagingException exception) {
            throw new AivleException(SERVER_ERROR);
        }
    }
}
