package com.jeszenka.eventdrivenmicroservices.events.logging;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LoggingConfig.class)
@EnableAutoConfiguration
class KafkaTraceHandlerTestIT {

	@Autowired
	private LoggingAspect loggingAspect;

	private KafkaTraceHandler kafkaTraceHandler;
	private Long traceValue;

	@BeforeEach
	void setUp() {
		this.kafkaTraceHandler =
				(KafkaTraceHandler) ReflectionTestUtils.getField(loggingAspect, "kafkaTraceHandler");
		traceValue = System.nanoTime();
		Tracing tracing = (Tracing) ReflectionTestUtils.getField(kafkaTraceHandler, "tracing");
		Tracer tracer = tracing.tracer();
		TraceContext traceContext = TraceContext.newBuilder()
				.traceId(traceValue)
				.spanId(traceValue)
				.build();
		Span span = tracing
				.tracer()
				.toSpan(traceContext)
				.name("application.name")
				.start();

		tracer.withSpanInScope(span);
	}

	@AfterEach
	void tearDown() {
		this.kafkaTraceHandler = null;
		traceValue = null;
	}

	@Test
	@DisplayName("Tests a real trace value stays the same during serialization and deserialization")
	void serializeAndDeserialize() {
		serializeAndDeserializeTest();
	}

	private void serializeAndDeserializeTest() {
		byte[] traceBytes = this.kafkaTraceHandler.getTraceBytes();
		long deserializedValue = this.kafkaTraceHandler.bytesToLong(traceBytes);
		assertEquals(traceValue, deserializedValue);
	}

}
