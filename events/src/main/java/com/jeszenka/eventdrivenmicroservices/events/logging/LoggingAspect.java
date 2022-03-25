package com.jeszenka.eventdrivenmicroservices.events.logging;

import lombok.extern.slf4j.Slf4j;
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

}
