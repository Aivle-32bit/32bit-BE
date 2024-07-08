package com.aivle.bit.auth.interceptor;

import static com.aivle.bit.global.exception.ErrorCode.ACCESS_DENIED;

import com.aivle.bit.auth.jwt.JwtTokenExtractor;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final Object handler
    ) {
        if (isOptionRequest(request)) {
            return true;
        }
        final String accessToken = jwtTokenExtractor.extractAccessToken(request);
        String email = jwtTokenProvider.extractEmailFromAccessToken(accessToken);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new AivleException(ACCESS_DENIED));

        if (!member.getIsAdmin()) {
            throw new AivleException(ACCESS_DENIED);
        }

        return true;
    }

    private boolean isOptionRequest(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }
}
