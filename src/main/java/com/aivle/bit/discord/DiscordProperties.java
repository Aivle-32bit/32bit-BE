package com.aivle.bit.discord;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.discord")
public class DiscordProperties {
    private static URI webHookUri;

    private final String webHook;

    URI webHookUri() {
        if (webHookUri == null) {
            webHookUri = URI.create(webHook);
        }

        return webHookUri;
    }
}