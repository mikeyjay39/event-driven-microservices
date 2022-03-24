package com.jeszenka.eventdrivenmicroservices.userservice.query.handler;

import com.jeszenka.eventdrivenmicroservices.events.events.AccountCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountUpdatedEvent;
import com.jeszenka.eventdrivenmicroservices.userservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindAllAccountsQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("userservice")
@Slf4j
public class AccountEventHandler {

	private final Map<String, Account> accounts = new HashMap<>();

	@EventHandler(payloadType = AccountCreatedEvent.class)
	public void on(AccountCreatedEvent e) {
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
