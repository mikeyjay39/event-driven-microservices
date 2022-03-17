package com.jeszenka.eventdrivenmicroservices.accountservice.query.handler;

import com.jeszenka.eventdrivenmicroservices.accountservice.core.events.AccountCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.accountservice.core.events.AccountUpdatedEvent;
import com.jeszenka.eventdrivenmicroservices.accountservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.accountservice.query.queries.FindAllAccountsQuery;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("accountEventHandler")
public class AccountEventHandler {

	private final Map<String, Account> accounts = new HashMap<>();

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
