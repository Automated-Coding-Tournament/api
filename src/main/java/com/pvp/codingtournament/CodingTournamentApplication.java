package com.pvp.codingtournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CodingTournamentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingTournamentApplication.class, args);
	}
}
