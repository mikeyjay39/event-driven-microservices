package com.jeszenka.eventdrivenmicroservices.userservice.query.handler;


import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountUpdatedEvent;
import com.jeszenka.eventdrivenmicroservices.events.logging.LogExecutionTime;
import com.jeszenka.eventdrivenmicroservices.userservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindAllAccountsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("userservice")
@Slf4j
public class AccountEventHandler {

	private final Map<String, Account> accounts = new HashMap<>();

	@Autowired
	private Tracing tracing;
	@Autowired
	private Tracer tracer;


	@EventHandler
	@LogExecutionTime
	public void on(AccountCreatedEvent e) {
		log.info("Showing current span");
		//Span span = tracer.nextSpan();
		//TraceContext traceContext = tracer.currentSpan().context();
		/*Span span = tracer.spanBuilder()
				//.setParent(traceContext)
				.tag("spanId", "testSpan")
				.tag("traceId", "testTag")
								.start();
		SpanCustomizer spanCustomizer = tracer.currentSpanCustomizer()
				.tag("spanId", "testSpan")
				.tag("traceId", "testTag");
		tracer.withSpan(span);*/
		TraceContext currentContext = tracer.currentSpan().context();
		long traceId = currentContext.traceId();
		TraceContext traceContext = TraceContext.newBuilder()
				.traceId(traceId)
				.spanId(System.nanoTime())
				.build();
		Span span =
				this.tracing.tracer()
						.toSpan(traceContext)
						.name("application.name")
						.start();
		tracer.withSpanInScope(span);

		//Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(traceContext)).name("test").start();
		//brave.Span span = tracer.nextSpan().name("name").traceId(someLong);


		log.info("Received message from account-service: {}", e);
		AccountCreatedEvent event = (AccountCreatedEvent) e;

		accounts.put(event.getAccountNumber(), new Account(event.getAccountNumber(),
				event.getBalance(),
				event.getUserId()));

	}

	@EventHandler
	public void on(AccountUpdatedEvent event) {
		accounts.computeIfPresent(event.getAccountNumber(), (accountNo, account) -> {
			account.setAccountNumber(accountNo);
			account.setBalance(event.getBalance());
			return account;
		});
	}

	@QueryHandler
	public List<Account> handle(FindAllAccountsQuery query) {
		return List.copyOf(accounts.values());
	}

}
