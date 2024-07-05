package com.aivle.bit.global.exception;

import com.aivle.bit.global.event.DiscordEvent;

public class UnknownException extends DiscordEvent {

    public UnknownException(Throwable throwable) {
        super("@everyone " + throwable.getMessage());
    }
}