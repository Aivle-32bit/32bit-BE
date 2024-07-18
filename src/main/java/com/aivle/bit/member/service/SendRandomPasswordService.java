package com.aivle.bit.member.service;

import org.springframework.stereotype.Service;

@Service
public interface SendRandomPasswordService {

    void send(String email, String password);
}
