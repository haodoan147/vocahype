package com.vocahype;

import com.vocahype.configuration.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class VocahypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VocahypeApplication.class, args);
	}

}
