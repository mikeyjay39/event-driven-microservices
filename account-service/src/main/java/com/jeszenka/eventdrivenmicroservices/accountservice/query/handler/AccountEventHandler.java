package com.jeszenka.eventdrivenmicroservices.accountservice.query.handler;

import com.jeszenka.eventdrivenmicroservices.accountservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.accountservice.query.queries.FindAllAccountsQuery;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaEventPublisher;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("accountservice")
public class AccountEventHandler {

	private final Map<String, Account> accounts = new HashMap<>();

	@Autowired
	private EventBus eventBus;

	@EventHandler
	public void on(AccountCreatedEvent event) {
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
