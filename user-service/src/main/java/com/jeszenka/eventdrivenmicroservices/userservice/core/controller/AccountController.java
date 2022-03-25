package com.jeszenka.eventdrivenmicroservices.userservice.core.controller;

import com.jeszenka.eventdrivenmicroservices.userservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindAllAccountsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

	private final QueryGateway queryGateway;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/all")
	public CompletableFuture<List<Account>> getAllAccounts() {
		log.info("Get all accounts called.");
		String response = restTemplate.getForEntity("http://localhost:8080/account/all", String.class).getBody();
		log.info("Response from account-service: {}", response);
		return queryGateway.query(new FindAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
	}

}
