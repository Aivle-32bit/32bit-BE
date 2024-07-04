package com.aivle.bit.member.service;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aivle.bit.global.smtp.VerificationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SendVerificationEmailServiceTest {

    @Mock
    private SendCertificationEmailService sendCertificationEmailService;

    @Mock
    private VerificationStorage verificationStorage;

    @InjectMocks
    private SendVerificationEmailService sendVerificationEmailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[성공] 인증 이메일을 전송한다.")
    public void testSendVerification() {
        String email = "test@example.com";
        String code = "123456";

        mockStatic(RandomCodeGenerator.class);
        when(RandomCodeGenerator.generate()).thenReturn(code);

        sendVerificationEmailService.sendVerification(email);

        verify(sendCertificationEmailService, times(1)).send(email, code);
        verify(verificationStorage, times(1)).storeVerificationCode(email, code);
    }
}
