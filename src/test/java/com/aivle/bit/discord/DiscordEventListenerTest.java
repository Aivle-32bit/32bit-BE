package com.aivle.bit.discord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import com.aivle.bit.global.event.DiscordEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
class DiscordEventListenerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ApplicationEvents events; // IDE 에서는 빨간줄이지만 정상 동작함

    @MockBean
    private DiscordEventListener discordEventListener;

    @Test
    @DisplayName("[성공] DiscordEvent 발생 시 onDiscordEvent 호출")
    void onDiscordEvent_Success() {
        // given

        // when
        eventPublisher.publishEvent(new DiscordEvent("test"));

        // then
        long count = events.stream(DiscordEvent.class).count();
        assertThat(count).isOne();

        then(discordEventListener).should(times(1)).onDiscordEvent(any());
    }
}