package com.aivle.bit.auth.logging.aspect;

import com.aivle.bit.auth.dto.request.SignInRequest;
import com.aivle.bit.auth.dto.response.SignInResponse;
import com.aivle.bit.auth.jwt.JwtTokenProvider;
import com.aivle.bit.auth.logging.service.LoginRecordService;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginRecordAspect {

    private final LoginRecordService loginRecordService;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Pointcut("execution(* com.aivle.bit.auth.service.SignInService.signInUser(..)) && args(signInRequest)")
    public void signInPointcut(SignInRequest signInRequest) {
    }

    @AfterReturning(pointcut = "signInPointcut(signInRequest)", returning = "result", argNames = "signInRequest,result")
    public void logLoginAttempt(SignInRequest signInRequest, Object result) {
        log.info("[Login Log] {} 로그인 시도", request.getRemoteAddr());
        if (result instanceof SignInResponse signInResponse) {
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            Long userId = extractUserIdFromToken(signInResponse.tokenResponse().accessToken());
            boolean success = true;

            loginRecordService.logLoginAttempt(ipAddress, userAgent, userId, success);
        }
    }

    @AfterThrowing(pointcut = "signInPointcut(signInRequest)", throwing = "ex", argNames = "signInRequest,ex")
    public void logLoginFailure(SignInRequest signInRequest, Exception ex) {
        log.info("[Login Log] {} 로그인 실패", request.getRemoteAddr());
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Long userId = memberRepository.findByEmailAndIsDeletedFalse(signInRequest.email())
            .map(Member::getId)
            .orElse(null);
        boolean success = false;

        loginRecordService.logLoginAttempt(ipAddress, userAgent, userId, success);
    }

    private Long extractUserIdFromToken(String token) {
        return jwtTokenProvider.extractIdFromAccessToken(token);
    }
}
