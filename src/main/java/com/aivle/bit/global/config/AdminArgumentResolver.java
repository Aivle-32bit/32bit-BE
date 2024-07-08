package com.aivle.bit.global.config;

import static com.aivle.bit.global.exception.ErrorCode.ACCESS_DENIED;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_TOKEN_EXTRACTOR;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_MEMBER;

import com.aivle.bit.auth.jwt.Admin;
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
public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Admin.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String jwtToken = Arrays.stream(Objects.requireNonNull(request.getCookies()))
            .filter(cookie -> HttpHeaders.AUTHORIZATION.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new AivleException(INVALID_TOKEN_EXTRACTOR));

        String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new AivleException(NO_SEARCH_MEMBER));

        if (!member.getIsAdmin()) {
            throw new AivleException(ACCESS_DENIED);
        }

        return member;
    }
}
