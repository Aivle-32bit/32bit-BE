package com.aivle.bit.member.service;

import static com.aivle.bit.global.exception.ErrorCode.EMAIL_DUPLICATION;
import static com.aivle.bit.global.exception.ErrorCode.EMAIL_NOT_VERIFIED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.smtp.VerificationStorage;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("MemberService 테스트")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private VerificationStorage verificationStorage;

    @InjectMocks
    private MemberService memberService;

    private MemberCreateRequest memberCreateRequest;

    private String email = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberCreateRequest = new MemberCreateRequest(email, "ValidPassword1!", "홍길동", "서울시 강남구");
    }

    @Nested
    @DisplayName("signup 메서드 테스트")
    class SignupTests {

        @Test
        @DisplayName("[성공] 유효한 요청으로 회원 가입을 시도하면 회원이 정상적으로 생성된다.")
        void signup_Success() {
            // given
            when(memberRepository.existsByEmail(memberCreateRequest.email())).thenReturn(false);
            when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(verificationStorage).isEmailVerified(memberCreateRequest.email());

            // when
            Member result = memberService.signup(memberCreateRequest);

            // then
            assertNotNull(result);
            verify(memberRepository, times(1)).existsByEmail(memberCreateRequest.email());
            verify(memberRepository, times(1)).save(any(Member.class));
            verify(verificationStorage, times(1)).isEmailVerified(memberCreateRequest.email());
        }

        @Test
        @DisplayName("[예외] 중복된 이메일로 회원 가입을 시도하면 EMAIL_DUPLICATION 예외가 발생한다.")
        void signup_Fail_DuplicateEmail() {
            // given
            when(memberRepository.existsByEmail(memberCreateRequest.email())).thenReturn(true);

            // when & then
            AivleException exception = assertThrows(AivleException.class,
                () -> memberService.signup(memberCreateRequest));

            assertEquals(EMAIL_DUPLICATION, exception.getErrorCode());

            // verify
            verify(memberRepository, times(1)).existsByEmail(memberCreateRequest.email());
            verify(memberRepository, never()).save(any(Member.class));
            verify(verificationStorage, never()).isEmailVerified(anyString());
            verifyNoMoreInteractions(memberRepository, verificationStorage);
        }

        @Test
        @DisplayName("[예외] 이메일 인증이 되지 않은 상태에서 회원 가입을 시도하면 EMAIL_NOT_VERIFIED 예외가 발생한다.")
        void signup_Fail_EmailNotVerified() {
            // given
            doThrow(new AivleException(EMAIL_NOT_VERIFIED))
                .when(verificationStorage).isEmailVerified(memberCreateRequest.email());
            when(memberRepository.existsByEmail(memberCreateRequest.email())).thenReturn(false);

            // when & then
            AivleException exception = assertThrows(AivleException.class,
                () -> memberService.signup(memberCreateRequest));

            assertEquals(EMAIL_NOT_VERIFIED, exception.getErrorCode());

            // verify
            verify(verificationStorage, times(1)).isEmailVerified(memberCreateRequest.email());
            verify(memberRepository, never()).save(any(Member.class));
            verify(memberRepository, times(1)).existsByEmail(memberCreateRequest.email());
            verifyNoMoreInteractions(memberRepository, verificationStorage);
        }
    }

    @Nested
    @DisplayName("checkEmailDuplicated 메서드 테스트")
    class CheckEmailDuplicatedTests {

        @Test
        @DisplayName("[성공] 이메일 중복 체크를 통과")
        void checkEmailDuplicated_Success() {
            // given
            when(memberRepository.existsByEmail(email)).thenReturn(false);

            // when & then
            assertDoesNotThrow(() -> memberService.checkEmailDuplicated(email));

            // verify
            verify(memberRepository, times(1)).existsByEmail(email);
            verifyNoMoreInteractions(memberRepository);
        }

        @Test
        @DisplayName("[예외] 중복된 이메일로 회원 가입을 시도하면 EMAIL_DUPLICATION 예외가 발생한다.")
        void checkEmailDuplicated_Fail() {
            // given
            when(memberRepository.existsByEmail(email)).thenReturn(true);

            // when & then
            AivleException exception = assertThrows(AivleException.class,
                () -> memberService.checkEmailDuplicated(email));

            assertEquals(EMAIL_DUPLICATION, exception.getErrorCode());

            // verify
            verify(memberRepository, times(1)).existsByEmail(email);
            verifyNoMoreInteractions(memberRepository);
        }
    }
}
