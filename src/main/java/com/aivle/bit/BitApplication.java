package com.aivle.bit;

import com.aivle.bit.discord.DiscordProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties(value = {
	DiscordProperties.class
})
@EnableJpaAuditing
@EnableRetry
@SpringBootApplication
public class BitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitApplication.class, args);
	}

}
