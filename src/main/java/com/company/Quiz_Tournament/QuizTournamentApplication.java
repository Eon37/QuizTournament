package com.company.Quiz_Tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.company.Quiz_Tournament"})
public class QuizTournamentApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizTournamentApplication.class, args);
	}

}
