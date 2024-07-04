package com.aivle.bit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitApplication.class, args);
	}

}
