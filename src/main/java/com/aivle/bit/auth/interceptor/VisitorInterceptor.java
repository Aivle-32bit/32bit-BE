package com.aivle.bit.auth.interceptor;

import com.aivle.bit.auth.logging.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorService visitorService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (isNoticePath(request)) { // 수정된 부분
            return true; // 수정된 부분
        }
        String sessionId = request.getSession().getId();
        visitorService.recordVisit(sessionId);
        return true;
    }
    private boolean isNoticePath(HttpServletRequest request) { return request.getRequestURI().startsWith("/API/notice"); } // 수정된 부분
}
