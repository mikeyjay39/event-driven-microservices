package com.jeszenka.eventdrivenmicroservices.events.logging;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
@Slf4j
public class KafkaTraceHandler {

	private final Tracer tracer;
	private final Tracing tracing;
	public static final String KAFKA_SPAN_HEADER = "spanId";

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

	byte[] getTraceBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(this.getTraceId());
		return buffer.array();
	}

	long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getLong();
	}

	Long extractTraceFromHeader(ConsumerRecord<String, byte[]> consumerRecord) {
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

	void setTrace(Long traceId) {

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
