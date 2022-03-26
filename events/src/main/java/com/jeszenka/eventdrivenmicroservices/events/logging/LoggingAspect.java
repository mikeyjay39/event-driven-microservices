package com.jeszenka.eventdrivenmicroservices.events.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


@Aspect
@Slf4j
public class LoggingAspect {

	@Around("@annotation(org.axonframework.eventhandling.EventHandler)")
	public Object logExecutionTimeForEvents(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object[] args = joinPoint.getArgs();
		Object proceed = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - start;

		log.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
		return proceed;
	}

	@Around("execution(* org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter.createKafkaMessage(..))")
	public Object setKafkaHeaders(ProceedingJoinPoint joinPoint) throws Throwable {


		Object[] args = joinPoint.getArgs();
		Object proceed = joinPoint.proceed();
		ProducerRecord record = (ProducerRecord) proceed;
		record.headers().add("TestHeader", "TestHeader".getBytes());
		return proceed;
	}

}
