package com.aivle.bit.member.service;

import org.springframework.stereotype.Service;

@Service
public interface SendCertificationEmailService {

    void send(String email, String code);
}
