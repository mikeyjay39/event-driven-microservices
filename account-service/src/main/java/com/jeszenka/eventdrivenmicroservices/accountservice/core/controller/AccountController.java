package com.jeszenka.eventdrivenmicroservices.accountservice.core.controller;

import com.jeszenka.eventdrivenmicroservices.accountservice.command.commands.CreateAccountCommand;
import com.jeszenka.eventdrivenmicroservices.accountservice.command.commands.UpdateAccountCommand;
import com.jeszenka.eventdrivenmicroservices.accountservice.query.model.Account;
import com.jeszenka.eventdrivenmicroservices.accountservice.query.queries.FindAllAccountsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	private final EventProcessingConfiguration eventProcessingConfiguration;

	@GetMapping("/all")
	public CompletableFuture<List<Account>> getAllAccounts() {
		log.info("Request for accounts.");
		return queryGateway.query(new FindAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
	}

	@PostMapping("/{userId}")
	public CompletableFuture<String> createAccount(@PathVariable("userId") String userId) {
		log.info("Creating account.");
		return commandGateway.send(new CreateAccountCommand(UUID.randomUUID().toString(), BigDecimal.ZERO, userId));
	}

	@PutMapping("/{accountNo}/{balance}")
	public CompletableFuture<String> updateAccount(@PathVariable("accountNo") String accountNo,
												   @PathVariable("balance") String balance) {
		return commandGateway.send(new UpdateAccountCommand(accountNo, new BigDecimal(balance)));
	}

}
