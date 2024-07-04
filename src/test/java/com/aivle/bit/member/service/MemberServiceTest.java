package com.aivle.bit.member.service;

import static com.aivle.bit.global.exception.ErrorCode.EMAIL_NOT_VERIFIED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.smtp.VerificationStorage;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.dto.request.MemberCreateRequest;
import com.aivle.bit.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private VerificationStorage verificationStorage;

    @InjectMocks
    private MemberService memberService;

    private MemberCreateRequest memberCreateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberCreateRequest = new MemberCreateRequest("test@example.com", "ValidPassword1!", "홍길동", "서울시 강남구");
    }

    @Test
    @DisplayName("[성공] 유효한 요청으로 회원 가입을 시도하면 회원이 정상적으로 생성된다.")
    void signup_Success() {
        // given
        when(memberRepository.findByEmail(memberCreateRequest.email())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(verificationStorage).isEmailVerified(memberCreateRequest.email());

        // when
        Member result = memberService.signup(memberCreateRequest);

        // then
        verify(memberRepository, times(1)).findByEmail(memberCreateRequest.email());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(verificationStorage, times(1)).isEmailVerified(memberCreateRequest.email());
    }

    @Test
    @DisplayName("[예외] 중복된 이메일로 회원 가입을 시도하면 EMAIL_DUPLICATION 예외가 발생한다.")
    void signup_Fail_DuplicateEmail() {
        // given
        when(memberRepository.findByEmail(memberCreateRequest.email())).thenReturn(Optional.of(mock(Member.class)));

        // when & then
        AivleException exception = assertThrows(AivleException.class, () -> memberService.signup(memberCreateRequest));

        // then
        verify(memberRepository, times(1)).findByEmail(memberCreateRequest.email());
        verify(memberRepository, never()).save(any(Member.class));
        verify(verificationStorage, times(1)).isEmailVerified(memberCreateRequest.email());
    }

    @Test
    @DisplayName("[예외] 이메일 인증이 되지 않은 상태에서 회원 가입을 시도하면 EMAIL_NOT_VERIFIED 예외가 발생한다.")
    void signup_Fail_EmailNotVerified() {
        // given
        doThrow(new AivleException(EMAIL_NOT_VERIFIED)).when(verificationStorage)
            .isEmailVerified(memberCreateRequest.email());
        when(memberRepository.findByEmail(memberCreateRequest.email())).thenReturn(Optional.empty());

        // when & then
        assertThrows(AivleException.class, () -> memberService.signup(memberCreateRequest));

        // then
        verify(verificationStorage, times(1)).isEmailVerified(memberCreateRequest.email());
        verify(memberRepository, never()).save(any(Member.class));
    }
}
