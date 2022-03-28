package com.jeszenka.eventdrivenmicroservices.events.logging;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;


@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {

	private final Tracer tracer;
	private final Tracing tracing;
	private static final String KAFKA_SPAN_HEADER = "spanId";

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
		Object proceed = joinPoint.proceed();
		ProducerRecord<String, byte[]> producerRecord = (ProducerRecord<String, byte[]>) proceed;
		producerRecord.headers().add(KAFKA_SPAN_HEADER, getTraceBytes());
		return proceed;
	}

	@Around("execution(* org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter.readKafkaMessage(..))")
	public Object setSpanFromKafka(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		ConsumerRecord<String, byte[]> consumerRecord = (ConsumerRecord<String, byte[]>) args[0];
		Long trace = extractTraceFromHeader(consumerRecord);
		setTrace(trace);
		return joinPoint.proceed();
	}

	private long getTraceId() {
		TraceContext currentContext = tracer.currentSpan().context();
		log.info("Is trace set? {}", Long.toHexString(currentContext.traceId()));
		return currentContext.traceId();
	}

	private long getSpanId() {
		Span span = tracer.currentSpan();
		TraceContext currentContext= null;

		if (span == null) {
			currentContext = tracer.newTrace().context();
		} else {
			currentContext = tracer.currentSpan().context();
		}
		return currentContext.spanId();
	}

	private byte[] getTraceBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(this.getTraceId());
		return buffer.array();
	}

	private long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getLong();
	}

	private Long extractTraceFromHeader(ConsumerRecord<String, byte[]> consumerRecord) {
		Headers headers = consumerRecord.headers();
		Long trace = null;

		if (headers != null) {
			Header header = headers.lastHeader(KAFKA_SPAN_HEADER);

			if (header != null) {
				trace = bytesToLong(header.value());
				log.info("Header = {}", Long.toHexString(trace));
			}
		}

		return trace;
	}

	private void setTrace(Long traceId) {

		Span span = null;

		if (traceId == null) {
			span = tracer.newTrace();
		} else {
			log.info("Setting trace to {}", Long.toHexString(traceId));
			TraceContext traceContext = TraceContext.newBuilder()
					.traceId(traceId)
					.spanId(getSpanId())
					.build();
			span =
					this.tracing.tracer()
							.toSpan(traceContext)
							.name("application.name")
							.start();

			tracer.withSpanInScope(span);
		}

		log.info("traceId = {}", Long.toHexString(traceId));

	}

}
