package com.aivle.bit.global.config;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_TOKEN_EXTRACTOR;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;

import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
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

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtLogin.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String jwtToken = Arrays.stream(
                Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)).getCookies())
            .filter(cookie -> cookie.getName().equals(HttpHeaders.AUTHORIZATION))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new AivleException(INVALID_TOKEN_EXTRACTOR));

        String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new AivleException(NO_SEARCH_MEMBER));
    }
}