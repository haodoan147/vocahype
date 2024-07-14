package com.vocahype;

import com.vocahype.configuration.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableScheduling
public class VocahypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VocahypeApplication.class, args);
	}

}
