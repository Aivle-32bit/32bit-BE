package com.aivle.bit.global.config;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_TOKEN_EXTRACTOR;
import static com.aivle.bit.global.exception.ErrorCode.NOT_AUTHORIZED;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;
import static com.aivle.bit.global.exception.ErrorCode.USER_DORMANT;

import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.domain.MemberState;
import com.aivle.bit.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTH_COOKIE_NAME = HttpHeaders.AUTHORIZATION;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtLogin.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String jwtToken = extractJwtTokenFromCookies(webRequest);
        String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
        Member member = findMemberByEmail(email);
        validateMemberState(member);

        return member;
    }

    private String extractJwtTokenFromCookies(NativeWebRequest webRequest) {
        return Arrays.stream(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)).getCookies())
            .filter(cookie -> AUTH_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new AivleException(INVALID_TOKEN_EXTRACTOR));
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow(() -> new AivleException(NO_SEARCH_MEMBER));
    }

    private void validateMemberState(Member member) {
        if (member.getState() == MemberState.UNVERIFIED) {
            throw new AivleException(NOT_AUTHORIZED);
        }
        if (member.getState() == MemberState.USER_DORMANT) {
            throw new AivleException(USER_DORMANT);
        }
    }
}
