package com.aivle.bit.global.smtp;

import com.aivle.bit.admin.dto.request.EmailSendAllRequest;
import com.aivle.bit.admin.dto.request.EmailSendRequest;
import com.aivle.bit.admin.service.SendAdminMailService;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpSendAdminMailService implements SendAdminMailService {

    private final SmtpEmailSender smtpEmailSender;
    private final MemberRepository memberRepository;

    @Override
    public void sendMembers(EmailSendAllRequest emailSendAllRequest) {
        List<String> memberEmails = emailSendAllRequest.memberEmails();
        String content = emailSendAllRequest.content();

        for (String email : memberEmails) {
            Mail mail = Mail.notice(email, content);
            smtpEmailSender.send(mail);
            log.info("[SMTP] Notification Sent to {}", email);
        }
    }

    @Override
    public void sendAll(EmailSendRequest emailSendRequest) {
        List<String> allEmails = getAllEmails();
        for (String email : allEmails) {
            Mail mail = Mail.notice(email, emailSendRequest.content());
            smtpEmailSender.send(mail);
            log.info("[SMTP] Notification Sent to {}", email);
        }
    }

    private List<String> getAllEmails() {
        return memberRepository.findByIsAdminFalseAndIsDeletedFalse()
            .stream()
            .map(Member::getEmail)
            .toList();
    }
}
