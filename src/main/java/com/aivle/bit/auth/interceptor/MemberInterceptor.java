package com.aivle.bit.auth.interceptor;

import com.aivle.bit.auth.jwt.JwtTokenExtractor;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

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
        jwtTokenProvider.extractEmailFromAccessToken(accessToken);
        return true;
    }

    private boolean isOptionRequest(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }
}