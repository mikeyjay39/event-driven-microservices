package com.jeszenka.eventdrivenmicroservices.userservice.core.controller;

import com.jeszenka.eventdrivenmicroservices.userservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindAllAccountsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final QueryGateway queryGateway;

	@GetMapping("/all")
	public CompletableFuture<List<Account>> getAllAccounts() {
		return queryGateway.query(new FindAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
	}

}
