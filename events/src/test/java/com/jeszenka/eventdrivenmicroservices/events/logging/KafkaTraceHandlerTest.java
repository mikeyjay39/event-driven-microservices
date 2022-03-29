package com.jeszenka.eventdrivenmicroservices.events.logging;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaTraceHandlerTest {

	private KafkaTraceHandler kafkaTraceHandler;

	@BeforeEach
	void setUp() {
		this.kafkaTraceHandler = null;
	}

	@AfterEach
	void tearDown() {
		this.kafkaTraceHandler = null;
	}

	@Test
	@DisplayName("Tests a mock trace value stays the same during serialization and deserialization")
	void serializeAndDeserialize() {
		serializeAndDeserializeTest();
	}

	private void serializeAndDeserializeTest() {
		long traceValue = System.nanoTime();
		initKafkaTraceHandler(traceValue);
		byte[] traceBytes = this.kafkaTraceHandler.getTraceBytes();
		long deserializedValue = this.kafkaTraceHandler.bytesToLong(traceBytes);
		assertEquals(traceValue, deserializedValue);
	}

	private void initKafkaTraceHandler(long traceValue) {
		Tracer mockTracer = Mockito.mock(Tracer.class);
		Span mockSpan = Mockito.mock(Span.class);

		TraceContext mockTraceContext = TraceContext.newBuilder()
				.traceId(traceValue)
				.spanId(traceValue)
				.build();

		Tracing mockTracing = Mockito.mock(Tracing.class);
		Mockito.doReturn(mockTracer).when(mockTracing).tracer();
		Mockito.doReturn(mockSpan).when(mockTracer).currentSpan();
		Mockito.doReturn(mockTraceContext).when(mockSpan).context();
		Mockito.doReturn(mockTracing.tracer().currentSpan()).when(mockTracer).newTrace();
		Mockito.doReturn(mockTracing.tracer().currentSpan()).when(mockTracer).currentSpan();
		this.kafkaTraceHandler = new KafkaTraceHandler(mockTracing.tracer(), mockTracing);
	}
}