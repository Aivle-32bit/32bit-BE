package com.aivle.bit.admin.service;

import com.aivle.bit.admin.dto.request.EmailSendAllRequest;
import com.aivle.bit.admin.dto.request.EmailSendRequest;

public interface SendAdminMailService {

    void sendMembers(EmailSendAllRequest emailSendAllRequest);

    void sendAll(EmailSendRequest emailSendRequest);
}
