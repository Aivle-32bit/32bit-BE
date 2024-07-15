package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.request.EmailSendAllRequest;
import com.aivle.bit.admin.dto.request.EmailSendRequest;
import com.aivle.bit.admin.service.SendAdminMailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/mails")
@Slf4j
public class AdminMailController {

    private final SendAdminMailService sendAdminMailService;

    @PostMapping("/send-all")
    public void sendMailToAll(@RequestBody @Valid EmailSendRequest emailSendRequest) {
        log.info("Sending mail to all members");
        sendAdminMailService.sendAll(emailSendRequest);
    }

    @PostMapping("/send")
    public void sendMailToSpecific(@RequestBody @Valid EmailSendAllRequest emailSendAllRequest) {
        log.info("Sending mail to : {}", emailSendAllRequest.memberEmails());
        sendAdminMailService.sendMembers(emailSendAllRequest);
    }
}
