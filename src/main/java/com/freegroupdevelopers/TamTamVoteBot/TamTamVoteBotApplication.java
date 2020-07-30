package com.freegroupdevelopers.TamTamVoteBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TamTamVoteBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TamTamVoteBotApplication.class, args);
	}

}
