package com.aivle.bit.global.smtp;

import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_DORMANT;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_VERIFICATION;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_APPROVE;
import static com.aivle.bit.global.smtp.HtmlEmailTemplate.MailType.EMAIL_REJECT;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class Mail {

    private static final String FROM_EMAIL = "no-reply@aivle.site";

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

    public SendEmailRequest toSes() {
        return new SendEmailRequest()
            .withSource(FROM_EMAIL)
            .withDestination(generateDestination())
            .withMessage(generateMessage());
    }

    private Destination generateDestination() {
        return new Destination().withToAddresses(to);
    }

    private Message generateMessage() {
        return new Message()
            .withSubject(generateContent(subject))
            .withBody(new Body().withHtml(generateContent(content)));
    }

    private Content generateContent(String content) {
        return new Content()
            .withCharset("UTF-8")
            .withData(content);
    }
}