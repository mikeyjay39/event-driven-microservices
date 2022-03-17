package com.jeszenka.eventdrivenmicroservices.userservice.core.controller;

import com.jeszenka.eventdrivenmicroservices.userservice.command.commands.CreateUserCommand;
import com.jeszenka.eventdrivenmicroservices.userservice.query.model.UserDto;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindUserQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@GetMapping("/{user}")
	public CompletableFuture<UserDto> getUsers(@PathVariable("user") String user) {
		return queryGateway.query(new FindUserQuery(user), ResponseTypes.instanceOf(UserDto.class));
	}

	@PostMapping("/{userName}/{firstName}/{lastName}")
	public CompletableFuture<String> createUser(@PathVariable("userName") String username,
												@PathVariable("firstName") String firstName,
												@PathVariable("lastName") String lastName) {
		return commandGateway.send(new CreateUserCommand(username, firstName, lastName));
	}
}
