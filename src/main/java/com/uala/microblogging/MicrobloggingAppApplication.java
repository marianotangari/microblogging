package com.uala.microblogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MicrobloggingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrobloggingAppApplication.class, args);
	}
}