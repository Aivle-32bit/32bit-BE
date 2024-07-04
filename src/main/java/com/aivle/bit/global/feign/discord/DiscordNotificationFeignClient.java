package com.aivle.bit.global.feign.discord;

import static com.aivle.bit.global.config.OpenFeignConfig.DEFINED_ON_RUNTIME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord-send-notification", url = DEFINED_ON_RUNTIME)
public interface DiscordNotificationFeignClient {

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    void send(URI webHookUri, @RequestBody String message);
}