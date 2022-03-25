package com.jeszenka.eventdrivenmicroservices.userservice.core.logging;

import com.jeszenka.eventdrivenmicroservices.events.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

	@Bean
	public LoggingAspect loggingAspect() {
		return new LoggingAspect();
	}
}
