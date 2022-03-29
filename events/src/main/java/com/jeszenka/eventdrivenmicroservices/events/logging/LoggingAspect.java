package com.jeszenka.eventdrivenmicroservices.events.logging;

import brave.Tracer;
import brave.Tracing;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.jeszenka.eventdrivenmicroservices.events.logging.KafkaTraceHandler.KAFKA_SPAN_HEADER;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

	private final KafkaTraceHandler kafkaTraceHandler;

	public LoggingAspect(Tracer tracer, Tracing tracing) {
		this.kafkaTraceHandler = new KafkaTraceHandler(tracer, tracing);
	}

	@Around("execution(* org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter.createKafkaMessage(..))")
	public Object setKafkaHeaders(ProceedingJoinPoint joinPoint) throws Throwable {
		Object proceed = joinPoint.proceed();
		ProducerRecord<String, byte[]> producerRecord = (ProducerRecord<String, byte[]>) proceed;
		producerRecord.headers().add(KAFKA_SPAN_HEADER, kafkaTraceHandler.getTraceBytes());
		return proceed;
	}

	@Around("execution(* org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter.readKafkaMessage(..))")
	public Object setSpanFromKafka(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		ConsumerRecord<String, byte[]> consumerRecord = (ConsumerRecord<String, byte[]>) args[0];
		Long trace = kafkaTraceHandler.extractTraceFromHeader(consumerRecord);
		kafkaTraceHandler.setTrace(trace);
		return joinPoint.proceed();
	}



}
