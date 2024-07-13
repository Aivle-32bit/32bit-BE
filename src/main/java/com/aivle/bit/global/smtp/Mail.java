package com.aivle.bit.global.smtp;

import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_APPROVE;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_DORMANT;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_REJECT;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_VERIFICATION;

import lombok.Getter;

@Getter
public class Mail {

    private final String to;
    private final String subject;
    private final String content;

    private Mail(final String to, final String subject, final String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public static Mail verification(String to, String code) {
        return new Mail(to,
            EMAIL_VERIFICATION.subject(),
            EMAIL_VERIFICATION.content(code));
    }

    public static Mail approve(String to, String name) {
        return new Mail(to,
            EMAIL_APPROVE.subject(),
            EMAIL_APPROVE.content(name));
    }

    public static Mail reject(String to, String name, String reason) {
        return new Mail(to,
            EMAIL_REJECT.subject(),
            EMAIL_REJECT.content(name, reason));
    }

    public static Mail dormant(String to, String name) {
        return new Mail(to,
            EMAIL_DORMANT.subject(),
            EMAIL_DORMANT.content(name));
    }
}