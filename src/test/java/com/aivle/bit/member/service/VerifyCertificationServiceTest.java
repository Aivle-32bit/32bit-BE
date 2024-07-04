package com.aivle.bit.member.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;
import com.aivle.bit.global.smtp.VerificationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VerifyCertificationServiceTest {

    @Mock
    private VerificationStorage verificationStorage;

    @InjectMocks
    private VerifyCertificationService verifyCertificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[성공] 인증번호가 올바를 경우 인증을 완료한다.")
    public void testVerify() {
        String email = "test@example.com";
        String code = "123456";

        // Act
        verifyCertificationService.verify(email, code);

        // Assert
        verify(verificationStorage, times(1)).verifyCodeAndRetrieveEmail(email, code);
    }

    @Test
    @DisplayName("[예외] 인증번호가 틀릴 경우 예외가 발생한다.")
    public void testVerify_withException() {
        String email = "test@example.com";
        String code = "wrong_code";

        // Arrange
        doThrow(new AivleException(ErrorCode.INVALID_VERIFICATION_CODE)).when(verificationStorage)
            .verifyCodeAndRetrieveEmail(email, code);

        // Act & Assert
        assertThrows(AivleException.class, () -> {
            verifyCertificationService.verify(email, code);
        });

        verify(verificationStorage, times(1)).verifyCodeAndRetrieveEmail(email, code);
    }
}
