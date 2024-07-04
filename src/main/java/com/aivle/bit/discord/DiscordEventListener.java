package com.aivle.bit.discord;

import com.aivle.bit.global.event.DiscordEvent;
import com.aivle.bit.global.feign.discord.DiscordNotificationFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordEventListener {

    private final DiscordNotificationFeignClient discordNotificationFeignClient;
    private final DiscordProperties discordProperties;
    private final ObjectMapper mapper;

    @Async
    @EventListener
    public void onDiscordEvent(DiscordEvent discordEvent) {
        discordNotificationFeignClient.send(discordProperties.webHookUri(), toJson(discordEvent.getMessage()));
    }

    private String toJson(String rawMessage) {
        try {
            return mapper.writeValueAsString(Map.of("content", rawMessage));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }
    }
}
