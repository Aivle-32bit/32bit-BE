package com.aivle.bit.global.config;

import com.aivle.bit.auth.interceptor.AdminInterceptor;
import com.aivle.bit.auth.interceptor.MemberInterceptor;
import com.aivle.bit.auth.interceptor.VisitorInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorAuthConfig implements WebMvcConfigurer {

    private final MemberInterceptor memberInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;
    private final AdminInterceptor adminInterceptor;
    private final AdminArgumentResolver adminArgumentResolver;
    private final AllowUnverifiedUserArgumentResolver allowUnverifiedUserArgumentResolver;
    private final VisitorInterceptor visitorInterceptor;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        resolvers.add(adminArgumentResolver);
        resolvers.add(allowUnverifiedUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorInterceptor)
            .order(0)
            .addPathPatterns("/**")
            .excludePathPatterns("/error");

        registry.addInterceptor(memberInterceptor)
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/error", "/api/auth/**, /api/notice");

        registry.addInterceptor(adminInterceptor)
            .order(2)
            .addPathPatterns("/api/admin/**");
    }
}
