package com.aivle.bit.admin.service;

public interface SendRejectEmailService {

    void send(String email, String name, String reason);
}
