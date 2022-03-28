package com.jeszenka.eventdrivenmicroservices.accountservice.core.logging;

import brave.Tracer;
import brave.Tracing;
import com.jeszenka.eventdrivenmicroservices.events.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

	@Bean
	public LoggingAspect loggingAspect(Tracer tracer, Tracing tracing) {
		return new LoggingAspect(tracer, tracing);
	}
}
