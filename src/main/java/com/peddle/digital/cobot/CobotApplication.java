package com.peddle.digital.cobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Srinivasa Reddy Challa
 *
 */

@SpringBootApplication
@EnableJpaAuditing
public class CobotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CobotApplication.class, args);
	}
}

