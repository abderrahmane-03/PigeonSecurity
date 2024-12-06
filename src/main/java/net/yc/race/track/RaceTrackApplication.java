package net.yc.race.track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class RaceTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaceTrackApplication.class, args);

	}

}
