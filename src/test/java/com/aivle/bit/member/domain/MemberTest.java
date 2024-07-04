package com.aivle.bit.member.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aivle.bit.global.exception.AivleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    private static final String TEST_EMAIL = "Fighting32@aivle.site";
    private static final String VALID_PASSWORD = "Woanxkawjd1!";
    private static final String VALID_NAME = "최정원";
    private static final String VALID_ADDRESS = "창원시 정원구 다이노스동 123-45";

    @ParameterizedTest
    @ValueSource(strings = {"dkwk123@aivle.site", "tkatlqdl@google.com", "xptmxmtpxm@naver.com"})
    @DisplayName("[성공] 유효한 이메일로 유저를 생성하면 예외가 발생하지 않는다.")
    void create_Success_With_Valid_Email(String email) {
        // given, when & then
        assertThatNoException().isThrownBy(() -> Member.of(email, VALID_PASSWORD, VALID_NAME, VALID_ADDRESS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"아", "a", "a@b", ""})
    @DisplayName("[예외] 유효하지 않은 이메일로 유저를 생성하면 예외가 발생한다.")
    void create_Fail_With_Invalid_Email(String email) {
        // given, when & then
        assertThrows(AivleException.class, () -> Member.of(email, VALID_PASSWORD, VALID_NAME, VALID_ADDRESS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Woanxka1!", "vkdlxlD1!", "Qlrvmfhwpr!!%1"})
    @DisplayName("[성공] 유효한 비밀번호로 유저를 생성하면 예외가 발생하지 않는다.")
    void create_Success_With_Valid_Password(String password) {
        // given, when & then
        assertThatNoException().isThrownBy(() -> Member.of(TEST_EMAIL, password, VALID_NAME, VALID_ADDRESS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Woanxkawjd", "Woanxkawjd1", "woanxkawjd1!", "WOANXKAWJD1!"})
    @DisplayName("[예외] 유효하지 않은 비밀번호로 유저를 생성하면 예외가 발생한다.")
    void create_Fail_With_Invalid_Password(String password) {
        // given, when & then
        assertThrows(AivleException.class, () -> Member.of(TEST_EMAIL, password, VALID_NAME, VALID_ADDRESS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"최정원", "김정원", "박정원"})
    @DisplayName("[성공] 유효한 이름으로 유저를 생성하면 예외가 발생하지 않는다.")
    void create_Success_With_Valid_Name(String name) {
        // given, when & then
        assertThatNoException().isThrownBy(() -> Member.of(TEST_EMAIL, VALID_PASSWORD, name, VALID_ADDRESS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"아", "a", "a@b", "아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아아"})
    @DisplayName("[예외] 유효하지 않은 이름으로 유저를 생성하면 예외가 발생한다.")
    void create_Fail_With_Invalid_Name(String name) {
        // given, when & then
        assertThrows(AivleException.class, () -> Member.of(TEST_EMAIL, VALID_PASSWORD, name, VALID_ADDRESS));
    }

    @Test
    @DisplayName("[성공] 유효한 주소로 유저를 생성하면 예외가 발생하지 않는다.")
    void create_Success_With_Valid_Address() {
        // given, when & then
        assertThatNoException().isThrownBy(() -> Member.of(TEST_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_ADDRESS));
    }
}
