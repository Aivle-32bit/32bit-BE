package com.aivle.bit.global.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.aivle.bit.global.feign")
public class OpenFeignConfig {
    public static final String DEFINED_ON_RUNTIME = "aivle.site";

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Retryer 설정
     * Retry 3번, 1초 간격으로 재시도, 최대 2초 대기
     */
    @Bean
    Retryer retryer() {
        return new Retryer.Default(1000L, 2000L, 3);
    }
}
