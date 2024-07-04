package com.aivle.bit.global.encode;

import static com.aivle.bit.global.exception.ErrorCode.PASSWORD_ENCRYPTION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aivle.bit.global.exception.AivleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordEncoderSHA256Test {

    @Test
    @DisplayName("[성공] 비밀번호를 SHA-256으로 인코딩하면 올바른 해시 값을 생성한다.")
    void encode_Success() {
        // given
        CharSequence rawPassword = "ValidPassword1!";

        // when
        String encodedPassword = PasswordEncoderSHA256.encode(rawPassword);

        // then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).hasSize(64);
    }

    @Test
    @DisplayName("[성공] 인코딩된 비밀번호와 원본 비밀번호가 일치하면 true를 반환한다.")
    void matches_Success() {
        // given
        CharSequence rawPassword = "ValidPassword1!";
        String encodedPassword = PasswordEncoderSHA256.encode(rawPassword);

        // when
        boolean matches = PasswordEncoderSHA256.matches(rawPassword, encodedPassword);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("[실패] 인코딩된 비밀번호와 원본 비밀번호가 일치하지 않으면 false를 반환한다.")
    void matches_Fail() {
        // given
        CharSequence rawPassword = "ValidPassword1!";
        String differentPassword = "InvalidPassword1!";
        String encodedPassword = PasswordEncoderSHA256.encode(rawPassword);

        // when
        boolean matches = PasswordEncoderSHA256.matches(differentPassword, encodedPassword);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    @DisplayName("[예외] 지원하지 않는 알고리즘을 사용할 때 AivleException이 발생한다.")
    void encode_UnsupportedAlgorithm() {
        // given
        String originalAlgorithm = "SHA-256";
        String unsupportedAlgorithm = "UNSUPPORTED";

        try {
            // Reflection을 사용하여 알고리즘을 변경
            java.lang.reflect.Field algorithmField = PasswordEncoderSHA256.class.getDeclaredField("ALGORITHM");
            algorithmField.setAccessible(true);
            algorithmField.set(null, unsupportedAlgorithm);

            // when & then
            AivleException exception = assertThrows(AivleException.class,
                () -> PasswordEncoderSHA256.encode("password"));
            assertThat(exception.getErrorCode()).isEqualTo(PASSWORD_ENCRYPTION_ERROR);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                java.lang.reflect.Field algorithmField = PasswordEncoderSHA256.class.getDeclaredField("ALGORITHM");
                algorithmField.setAccessible(true);
                algorithmField.set(null, originalAlgorithm);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
